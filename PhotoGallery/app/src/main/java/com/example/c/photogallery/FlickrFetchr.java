package com.example.c.photogallery;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by c on 2016-08-07.
 */
public class FlickrFetchr {
    private static final String API_KEY = "9289b8daaf67da1c62623b2ff2c18e5f";

    // 좀 오래되긴 한듯한데.. loopj 라이브러리 같은걸 써도 된다.
    public byte[] getUrlBytes(String urlSpec) throws IOException {
        URL url = new URL(urlSpec);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException(connection.getResponseMessage() + " : with " + urlSpec);
            }

            int bytesRead = 0;
            byte[] buffer = new byte[1024];

            // 읽을게 있으면 반복
            while((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
            out.close();
            return out.toByteArray();

        } finally {
            connection.disconnect();
        }
    }

    // 바이트배열로 넘어오지만 스트링 이라는걸 알고 있을때
    public String getUrlString(String urlSpec) throws IOException {
        return new String(getUrlBytes(urlSpec));
    }

    public ArrayList<GalleryItem> fetchItems() {
        ArrayList<GalleryItem> items = new ArrayList<>();

        String url = Uri.parse("https://api.flickr.com/services/rest/")
                .buildUpon()
                .appendQueryParameter("method", "flickr.photos.getRecent")
                .appendQueryParameter("api_key", API_KEY)
                .appendQueryParameter("format", "json")
                .appendQueryParameter("nojsoncallback", "1")
                .appendQueryParameter("extras", "url_s")    // url_s는 스몰사이즈 이미지
                .build().toString();

        try {
            String jsonString = getUrlString(url);
            Log.i("json", jsonString);

            JSONObject jsonBody = new JSONObject(jsonString);
            parseItem(items, jsonBody);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return items;
    }

    private void parseItem(List<GalleryItem> items, JSONObject jsonBody) throws JSONException {
        // 여기선 직접 파싱해서 쓰는데, Gson 같은 라이브러리를 써도 된다.

        JSONObject photosJsonObject = jsonBody.getJSONObject("photos");
        JSONArray photoJsonArray = photosJsonObject.getJSONArray("photo");
        for (int i = 0; i < photoJsonArray.length(); i++) {
            JSONObject photoJsonObject = photoJsonArray.getJSONObject(i);

            GalleryItem item = new GalleryItem();
            item.setId(photoJsonObject.getString("id"));
            item.setCaption(photoJsonObject.getString("title"));

            if (photoJsonObject.has("url_s")) {
                item.setUrl(photoJsonObject.getString("url_s"));
            }

            items.add(item);
        }
    }
}
