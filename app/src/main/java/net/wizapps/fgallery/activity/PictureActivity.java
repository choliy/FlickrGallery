package net.wizapps.fgallery.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.ToxicBakery.viewpager.transforms.AccordionTransformer;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import net.wizapps.fgallery.R;
import net.wizapps.fgallery.adapter.PictureAdapter;
import net.wizapps.fgallery.async.DownloadService;
import net.wizapps.fgallery.base.BaseActivity;
import net.wizapps.fgallery.data.FlickrLab;
import net.wizapps.fgallery.fragment.GalleryFragment;
import net.wizapps.fgallery.fragment.SavedFragment;
import net.wizapps.fgallery.model.GalleryItem;
import net.wizapps.fgallery.tool.Constants;
import net.wizapps.fgallery.tool.Events;
import net.wizapps.fgallery.util.FabUtils;
import net.wizapps.fgallery.util.InfoUtils;

import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.OnClick;

public class PictureActivity extends BaseActivity {

    @BindView(R.id.fab_web) FloatingActionButton mFabWeb;
    @BindView(R.id.fab_share) FloatingActionButton mFabShare;
    @BindView(R.id.fab_copy) FloatingActionButton mFabCopy;
    @BindView(R.id.fab_save) FloatingActionButton mFabSave;
    @BindView(R.id.fab_download) FloatingActionButton mFabDownload;
    @BindView(R.id.fab_menu_pic) FloatingActionMenu mFabMenu;
    @BindView(R.id.fence_picture_view) View mFenceView;
    @BindView(R.id.picture_pager) ViewPager mPicturePager;

    public static final int REQUEST_CODE = 111;
    private static final int PAGE_LIMIT = 3;

    private GalleryItem mItem;
    private int mItemPosition;
    private boolean mPictureSaved;
    private boolean mPictureDownloaded;
    private boolean mIsFabOpened;
    private boolean mSavedLibrary;

    public static Intent newInstance(Context context, int position, boolean savedPicture) {
        Intent intent = new Intent(context, PictureActivity.class);
        intent.putExtra(Constants.POSITION_KEY, position);
        intent.putExtra(Constants.SAVE_KEY, savedPicture);
        return intent;
    }

    @Override
    public int layoutRes() {
        return R.layout.activity_picture;
    }

    @Override
    public void setUi(Bundle savedInstanceState) {
        restoreData(savedInstanceState);
        updateItemByPosition();
        checkOrientationSize();
        setupViewPager();
        onMenuClick();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mIsFabOpened) enablePictureScreen(Boolean.FALSE);
    }

    @Override
    public void onBackPressed() {
        if (mFabMenu.isOpened()) {
            enablePictureScreen(Boolean.TRUE);
        } else {
            setResultAndFinish();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(Constants.PICTURE_SAVED_KEY, mPictureSaved);
        outState.putBoolean(Constants.PICTURE_DOWNLOADED_KEY, mPictureDownloaded);
        outState.putBoolean(Constants.MENU_OPENED_KEY, mIsFabOpened);
        outState.putBoolean(Constants.SAVED_LIBRARY_KEY, mSavedLibrary);
        outState.putInt(Constants.POSITION_KEY, mItemPosition);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        int zero = Constants.ZERO;
        boolean requestCodeSame = requestCode == REQUEST_CODE;
        boolean resultsNotEmpty = grantResults.length > zero;
        boolean permissionGranted = grantResults[zero] == PackageManager.PERMISSION_GRANTED;
        if (requestCodeSame && resultsNotEmpty && permissionGranted) {
            onDownloadClick();
        } else {
            InfoUtils.showToast(this, getString(R.string.text_permission));
        }
    }

    @Subscribe
    public void onEvent(Events.BackPressedEvent event) {
        setResultAndFinish();
    }

    @OnClick({R.id.fab_web, R.id.fab_share, R.id.fab_copy, R.id.fab_save, R.id.fab_download})
    public void onFabClicked(View view) {
        switch (view.getId()) {
            case R.id.fab_web:
                FabUtils.goWeb(this, mItem);
                break;
            case R.id.fab_share:
                FabUtils.shareUrl(this, mItem.getItemUri());
                break;
            case R.id.fab_copy:
                onCopyClick();
                break;
            case R.id.fab_save:
                onSaveClick();
                break;
            case R.id.fab_download:
                checkPermissionAndDownload();
                break;
        }
        enablePictureScreen(Boolean.TRUE);
    }

    private void setupViewPager() {
        mPicturePager.setAdapter(new PictureAdapter(getSupportFragmentManager(), mSavedLibrary));
        mPicturePager.setCurrentItem(mItemPosition);
        mPicturePager.setOffscreenPageLimit(PAGE_LIMIT);
        mPicturePager.setPageTransformer(Boolean.TRUE, new AccordionTransformer());
        mPicturePager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                mItemPosition = position;
                mPictureSaved = Boolean.FALSE;
                mPictureDownloaded = Boolean.FALSE;
                updateItemByPosition();
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });
    }

    private void onCopyClick() {
        String url = mItem.getItemUri();
        FabUtils.copyData(this, url);
        InfoUtils.showToast(this, getString(R.string.fab_copied, url));
    }

    private void onSaveClick() {
        if (!mPictureSaved) {
            mPictureSaved = Boolean.TRUE;
            FlickrLab.getInstance(this).addPicture(mItem);
            InfoUtils.showToast(this, getString(R.string.fab_picture_saved));
        } else {
            InfoUtils.showToast(this, getString(R.string.text_already_saved));
        }
    }

    private void checkPermissionAndDownload() {
        String permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{permission}, REQUEST_CODE);
        } else {
            onDownloadClick();
        }
    }

    private void onDownloadClick() {
        if (!mPictureDownloaded) {
            mPictureDownloaded = Boolean.TRUE;
            InfoUtils.showToast(this, getString(R.string.fab_downloading));
            startService(DownloadService.newIntent(this, mItem));
        } else {
            InfoUtils.showToast(this, getString(R.string.text_already_downloaded));
        }
    }

    private void onMenuClick() {
        mFabMenu.setOnMenuButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mFabMenu.isOpened()) {
                    enablePictureScreen(Boolean.TRUE);
                } else {
                    enablePictureScreen(Boolean.FALSE);
                }
            }
        });
    }

    private void enablePictureScreen(boolean enable) {
        mIsFabOpened = !enable;
        if (enable) {
            mFenceView.setVisibility(View.INVISIBLE);
            mFabMenu.close(Boolean.TRUE);
        } else {
            mFenceView.setVisibility(View.VISIBLE);
            mFabMenu.open(Boolean.TRUE);
        }
    }

    private void checkOrientationSize() {
        int orientation = InfoUtils.getOrientation(this);
        int size;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            size = FloatingActionButton.SIZE_NORMAL;
        } else {
            size = FloatingActionButton.SIZE_MINI;
        }

        mFabWeb.setButtonSize(size);
        mFabShare.setButtonSize(size);
        mFabCopy.setButtonSize(size);
        mFabSave.setButtonSize(size);
        mFabDownload.setButtonSize(size);
    }

    private void restoreData(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mPictureSaved = savedInstanceState.getBoolean(Constants.PICTURE_SAVED_KEY);
            mPictureDownloaded = savedInstanceState.getBoolean(Constants.PICTURE_DOWNLOADED_KEY);
            mIsFabOpened = savedInstanceState.getBoolean(Constants.MENU_OPENED_KEY);
            mSavedLibrary = savedInstanceState.getBoolean(Constants.SAVED_LIBRARY_KEY);
            mItemPosition = savedInstanceState.getInt(Constants.POSITION_KEY);
        } else {
            mItemPosition = getIntent().getIntExtra(Constants.POSITION_KEY, Constants.ZERO);
            mSavedLibrary = getIntent().getBooleanExtra(Constants.SAVE_KEY, Boolean.FALSE);
        }
        if (mSavedLibrary) mFabMenu.removeMenuButton(mFabSave);
    }

    private void updateItemByPosition() {
        if (mSavedLibrary) {
            mItem = SavedFragment.sSavedItems.get(mItemPosition);
        } else {
            mItem = GalleryFragment.sGalleryItems.get(mItemPosition);
        }
    }

    private void setResultAndFinish() {
        Intent intent = new Intent();
        intent.putExtra(Constants.POSITION_KEY, mItemPosition);
        setResult(RESULT_OK, intent);
        finish();
    }
}