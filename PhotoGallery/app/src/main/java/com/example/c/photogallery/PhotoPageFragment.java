package com.example.c.photogallery;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.webkit.WebView;

/**
 * Created by c on 2016-08-14.
 */
public class PhotoPageFragment extends Fragment {
    private static final String ARG_URI = "photo_page_uri";
    private Uri mUri;
    private WebView mWebView;

    public static PhotoPageFragment newInstance(Uri uri) {

        Bundle args = new Bundle();
        args.putParcelable(ARG_URI, uri);

        PhotoPageFragment fragment = new PhotoPageFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
