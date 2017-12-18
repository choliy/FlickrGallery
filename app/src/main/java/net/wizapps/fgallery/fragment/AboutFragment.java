package net.wizapps.fgallery.fragment;

import android.view.View;

import net.wizapps.fgallery.R;
import net.wizapps.fgallery.base.BaseDialog;
import net.wizapps.fgallery.util.NavUtils;

import butterknife.OnClick;

public class AboutFragment extends BaseDialog {

    public static final String TAG = AboutFragment.class.getSimpleName();

    @Override
    protected int layoutRes() {
        return R.layout.dialog_about;
    }

    @OnClick({R.id.facebookIcon, R.id.linkedInIcon, R.id.googlePlusIcon, R.id.gitHubIcon, R.id.buttonClose})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.facebookIcon:
                loadUrl(R.string.url_facebook);
                break;
            case R.id.linkedInIcon:
                loadUrl(R.string.url_linked_in);
                break;
            case R.id.googlePlusIcon:
                loadUrl(R.string.url_google_plus);
                break;
            case R.id.gitHubIcon:
                loadUrl(R.string.url_git_hub);
                break;
            case R.id.buttonClose:
                dismiss();
                break;
        }
    }

    private void loadUrl(int textResId) {
        NavUtils.webIntent(getActivity(), textResId);
    }
}