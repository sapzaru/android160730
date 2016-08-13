package com.example.c.photogallery;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
    private ThumbnailDownloader<PhotoHolder> mThumbnailDownloader;

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

            // 다운로드
            mThumbnailDownloader.queueThumbnail(holder, item.getUrl());
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

        String query;

        public FetchItemsTask(String query) {
            this.query = query;
        }

        @Override
        protected void onPostExecute(ArrayList<GalleryItem> galleryItems) {
            super.onPostExecute(galleryItems);
            // 쓰레드 처리가 끝나고 UI단 처리
            mItems = galleryItems;
            setupAdapter();
        }

        @Override
        protected ArrayList<GalleryItem> doInBackground(Void... params) {
            // 쓰레드 내부라고 보면 된다.
            // 여기서 리턴한게 onPostExecute로 넘어간다.
            if (query == null)
                return new FlickrFetchr().fetchRecentPhotos();
            else
                return new FlickrFetchr().searchPhotos(query);
        }
    }

    Handler responseHandler = new Handler();
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 메뉴가 있다는걸 알려줘야 onCreateOptionsMenu()가 호출됨
        setHasOptionsMenu(true);

        updateItems();

        //PollService.setServiceAlarm(getActivity(), true);

        // 썸네일 다운로더
        mThumbnailDownloader = new ThumbnailDownloader(responseHandler);
        mThumbnailDownloader.setThumbnailLoadListener(
                new ThumbnailDownloader.ThumbnailLoadListener<PhotoHolder>() {
                    @Override
                    public void onThumbnailDownloaded(PhotoHolder target, Bitmap thumbnail) {
                        // 프래그먼트가 없어졌을 경우를 방지
                        if (isAdded()) {
                            Drawable drawable = new BitmapDrawable(getResources(), thumbnail);
                            target.bindDrawable(drawable);
                        }
                    }
                }
        );
        mThumbnailDownloader.start();
        mThumbnailDownloader.getLooper();   // 이걸 호출하면 onLooperPrepared()가 호출된다.

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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_photo_gallery, menu);

        MenuItem searchItem = menu.findItem(R.id.menu_item_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d("SearchView", "QueryText : " + query);
                QueryPreperence.setStoredQuery(getActivity(), query);

                updateItems();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void updateItems() {
        String query = QueryPreperence.getStoredQuery(getActivity());
        new FetchItemsTask(query).execute();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_item_clear) {
            QueryPreperence.setStoredQuery(getActivity(), null);
            updateItems();
            return true;
        } else if (item.getItemId() == R.id.menu_item_toggle_polling) {
            boolean shouldStartAlarm = !PollService.isServiceAlarmOn(getActivity());
            PollService.setServiceAlarm(getActivity(), shouldStartAlarm);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mThumbnailDownloader.clearQueue();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mThumbnailDownloader.quit();
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
