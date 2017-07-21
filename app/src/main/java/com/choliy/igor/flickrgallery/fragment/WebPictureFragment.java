package com.choliy.igor.flickrgallery.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.choliy.igor.flickrgallery.FlickrConstants;
import com.choliy.igor.flickrgallery.R;
import com.wang.avi.AVLoadingIndicatorView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WebPictureFragment extends Fragment {

    @BindView(R.id.progress_view) AVLoadingIndicatorView mProgressView;
    @BindView(R.id.web_view_picture) WebView mWebView;
    private String mItemUri;

    public static Fragment newInstance(String itemUri) {
        Bundle args = new Bundle();
        args.putString(FlickrConstants.URI_KEY, itemUri);
        WebPictureFragment fragment = new WebPictureFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mItemUri = getArguments().getString(FlickrConstants.URI_KEY);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_picture_web, container, Boolean.FALSE);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mWebView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark));
        mWebView.getSettings().setUserAgentString(getString(R.string.user_agent_string));
        mWebView.getSettings().setJavaScriptEnabled(Boolean.TRUE);
        mWebView.getSettings().setUseWideViewPort(Boolean.TRUE);
        mWebView.getSettings().setLoadWithOverviewMode(Boolean.TRUE);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                if (URLUtil.isNetworkUrl(request.getUrl().toString())) return Boolean.FALSE;
                Intent intent = new Intent(Intent.ACTION_VIEW, request.getUrl());
                startActivity(intent);
                return Boolean.TRUE;
            }
        });
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) mProgressView.smoothToHide();
                else mProgressView.smoothToShow();
            }
        });
        mWebView.loadUrl(mItemUri);
    }
}