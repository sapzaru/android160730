package com.example.c.photogallery;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by c on 2016-08-07.
 */
public class PhotoGalleryFragment extends Fragment {
    RecyclerView mPhotoRecyclerView;

    public static PhotoGalleryFragment newInstance() {
        PhotoGalleryFragment fragment = new PhotoGalleryFragment();
        return fragment;
    }

    class FetchItemsTask extends AsyncTask<Void, Void, Void> {
        // 쓰레드에서 직접 ui에 접근하면 크래쉬가 나는데
        // AsyncTask는 쓰레드와 핸들러(UI쪽)가 합쳐진 개념으로
        // ui쪽을 호출하여도 된다.

        @Override
        protected Void doInBackground(Void... params) {
            // 쓰레드 내부라고 보면 된다.
            new FlickrFetchr().fetchItems();
            return null;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new FetchItemsTask().execute();

        /*
        // 진저브레드 이후에는 네트워크는 다른 쓰레드에서 해야함..
        FlickrFetchr f = new FlickrFetchr();
        try {
            String str = f.getUrlString("http://www.naver.com");
            Log.d("PhotoGallery", str);
        } catch (IOException e) {
            e.printStackTrace();
        }
        */
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_photo_gallery, container, false);
        mPhotoRecyclerView = (RecyclerView) v.findViewById(R.id.fragment_photo_gallery_recycler_view);
        mPhotoRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        return v;
    }
}
