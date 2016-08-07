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
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by c on 2016-08-07.
 */
public class PhotoGalleryFragment extends Fragment {
    RecyclerView mPhotoRecyclerView;
    private ArrayList<GalleryItem> mItems = new ArrayList<>();

    public static PhotoGalleryFragment newInstance() {
        PhotoGalleryFragment fragment = new PhotoGalleryFragment();
        return fragment;
    }

    // 뷰홀더 (리사이클러뷰 어댑터가 가져다가 작업)
    class PhotoHolder extends RecyclerView.ViewHolder {
        private TextView mTitleTextView;

        public PhotoHolder(View itemView) {
            super(itemView);
            mTitleTextView = (TextView) itemView;
        }

        public void bindGalleryItem(GalleryItem item) {
            mTitleTextView.setText(item.toString());
        }
    }

    // 어댑터
    class PhotoAdapter extends RecyclerView.Adapter<PhotoHolder> {
        private ArrayList<GalleryItem> mGalleryItems;// 전체 아이템

        public PhotoAdapter(ArrayList<GalleryItem> galleryItems) {
            mGalleryItems = galleryItems;
        }

        @Override
        public PhotoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // inflation 하는 부분인데 여기선 임시로 간단히 textView만 넘기도록..
            TextView textView = new TextView(getActivity());
            return new PhotoHolder(textView);
        }

        @Override
        public void onBindViewHolder(PhotoHolder holder, int position) {
            // 스크롤되면서 데이터 바인드 시켜주는 부분
            // 뷰홀더에 멀 해라 알려주는 부분..
            GalleryItem item = mGalleryItems.get(position);
            holder.bindGalleryItem(item);
        }

        @Override
        public int getItemCount() {
            return mGalleryItems.size();
        }
    }

    // AsyncTask
    class FetchItemsTask extends AsyncTask<Void, Void, ArrayList<GalleryItem>> {
        // 쓰레드에서 직접 ui에 접근하면 크래쉬가 나는데
        // AsyncTask는 쓰레드와 핸들러(UI쪽)가 합쳐진 개념으로
        // ui쪽을 호출하여도 된다.

        @Override
        protected ArrayList<GalleryItem> doInBackground(Void... params) {
            // 쓰레드 내부라고 보면 된다.
            // 여기서 리턴한게 onPostExecute로 넘어간다.
            return new FlickrFetchr().fetchItems();
        }

        @Override
        protected void onPostExecute(ArrayList<GalleryItem> galleryItems) {
            super.onPostExecute(galleryItems);
            // 쓰레드 처리가 끝나고 UI단 처리
            mItems = galleryItems;
            setupAdapter();
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
        setupAdapter();
        return v;
    }

    private void setupAdapter() {
        // 여기선 이미 add 된 상태지만, 다른데서 호출 할때를 대비 체크하도록..
        if (isAdded()) {
            mPhotoRecyclerView.setAdapter(new PhotoAdapter(mItems));
        }
    }
}
