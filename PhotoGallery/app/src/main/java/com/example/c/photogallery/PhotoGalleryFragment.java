package com.example.c.photogallery;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

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
        private ImageView mItemImageView;

        public PhotoHolder(View itemView) {
            super(itemView);
            // 갤러리아이템의 루트가 이미지뷰라서 이렇게 가능
            mItemImageView = (ImageView) itemView;
        }

        public void bindDrawable(Drawable drawable) {
            // 외부에서 만들어서 준비가 된걸 넘겨서 세팅만.. (네트워크 속도 문제..)
            mItemImageView.setImageDrawable(drawable);
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
            // inflation 하는 부분
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View v = inflater.inflate(R.layout.gallery_item, parent, false);
            return new PhotoHolder(v);
        }

        @Override
        public void onBindViewHolder(PhotoHolder holder, int position) {
            // 스크롤되면서 데이터 바인드 시켜주는 부분
            // 뷰홀더에 멀 해라 알려주는 부분..
            GalleryItem item = mGalleryItems.get(position);
            Drawable d = getResources().getDrawable(R.mipmap.ic_launcher);
            holder.bindDrawable(d);
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
