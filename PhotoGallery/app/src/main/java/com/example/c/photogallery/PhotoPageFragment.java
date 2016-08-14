package com.example.c.photogallery;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

/**
 * Created by c on 2016-08-14.
 */
public class PhotoPageFragment extends Fragment {
    private static final String ARG_URI = "photo_page_uri";
    private Uri mUri;
    private WebView mWebView;
    private ProgressBar mProgressBar;

    public static PhotoPageFragment newInstance(Uri uri) {
        Bundle args = new Bundle();
        args.putParcelable(ARG_URI, uri);

        PhotoPageFragment fragment = new PhotoPageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUri = getArguments().getParcelable(ARG_URI);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_photo_page, container, false);
        mProgressBar = (ProgressBar) v.findViewById(R.id.webview_loading_progressbar);
        mWebView = (WebView) v.findViewById(R.id.fragment_photo_page_web_view);
        mWebView.setWebViewClient(new WebViewClient() {
            ProgressDialog dlg = new ProgressDialog(getActivity());

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                dlg.show();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                dlg.dismiss();
            }
        });
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress == 100) {
                    mProgressBar.setVisibility(View.GONE);
                } else {
                    mProgressBar.setVisibility(View.VISIBLE);
                    mProgressBar.setProgress(newProgress);
                }
            }
        });
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadUrl(mUri.toString());

        // 웹뷰에서 Activity가 닫히는게 아니라 history를 back하고 싶을때 이런식으로 쓴다.
        // 현재는 Fragment에서 backPress를 오버라이딩 하기가 애매해서 그냥 안함
        // Activity에서 웹뷰를 바로 올려서 했다면 backPress 오버라이드 가능
        //mWebView.canGoBack();
        //mWebView.goBack();

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Fragment에서 Activity의 backPress 사용방법 (지금 안되는데 관련해서 검색해보자.)
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();

        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        if (mWebView.canGoBack()) {
                            mWebView.goBack();
                        } else {
                            getActivity().onBackPressed();
                        }
                    }
                    return true;
                }
                return false;
            }
        });
    }
}
