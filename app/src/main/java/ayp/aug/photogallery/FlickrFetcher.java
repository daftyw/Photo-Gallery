package ayp.aug.photogallery;

import android.location.Location;
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
import java.util.concurrent.ThreadFactory;

/**
 * Created by wind on 8/16/2016 AD.
 */
public class FlickrFetcher {
    private static final String TAG = "FlickrFetcher";

    public byte[] getUrlBytes(String urlSpec) throws IOException {
        URL url = new URL(urlSpec);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();

            // if connection is not OK throw new IOException
            if(connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException(connection.getResponseMessage() + ": with " + urlSpec);
            }

            int bytesRead = 0;

            byte[] buffer = new byte[2048];

            while((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }

            out.close();

            return out.toByteArray();

        } finally {
            connection.disconnect();
        }
    }

    public String getUrlString(String urlSpec) throws IOException {
        return new String(getUrlBytes(urlSpec));
    }

    //
    private static final String FLICKR_URL = "https://api.flickr.com/services/rest/";

    private static final String API_KEY = "9d495812808b84fda6313aa89484f395";

    private static final String METHOD_GET_RECENT = "flickr.photos.getRecent";
    private static final String METHOD_SEARCH = "flickr.photos.search";

    private String buildUrl(String method, String ... param) throws IOException {
        Uri baseUrl = Uri.parse(FLICKR_URL);
        Uri.Builder builder = baseUrl.buildUpon();
        builder.appendQueryParameter("method", method);
        builder.appendQueryParameter("api_key", API_KEY);
        builder.appendQueryParameter("format", "json");
        builder.appendQueryParameter("nojsoncallback", "1");
        builder.appendQueryParameter("extras", "url_s,url_z");

        if(METHOD_SEARCH.equalsIgnoreCase(method)) {
            builder.appendQueryParameter("text", param[0]);
        }

        if(param.length > 1) {
            // Lat & lon
            builder.appendQueryParameter("lat", param[1]);
            builder.appendQueryParameter("lon", param[2]);
        }

        Uri completeUrl = builder.build();
        String url = completeUrl.toString();

        Log.i(TAG, "Run URL:" + url);

        return url;
    }

    private String queryItem(String url) throws IOException {
        Log.i(TAG, "Run URL:" + url);
        String jsonString = getUrlString(url);

        Log.i(TAG, "Search: Received JSON: " + jsonString);
        return jsonString;
    }

    /**
     * Search photo then put into <b>items</b>
     *
     * @param items array target
     * @param key to search
     */
    public void searchPhotos(List<GalleryItem> items, String key) {
        try {
            String url = buildUrl(METHOD_SEARCH, key);
            fetchPhoto(items, url);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Failed to fetch items", e);
        }
    }

    /**
     *
     * @param items
     * @param key
     * @param location
     */
    public void searchPhotos(List<GalleryItem> items, String key, String lat, String lon) {
        try {
            String url = buildUrl(METHOD_SEARCH, key, lat, lon);

            fetchPhoto(items, url);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Failed to fetch items", e);
        }
    }

    public void getRecentPhotos(List<GalleryItem> items) {
        try {
            String url = buildUrl(METHOD_GET_RECENT);
            fetchPhoto(items, url);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Failed to fetch items", e);
        }
    }

    public void fetchPhoto(List<GalleryItem> items, String url) throws IOException, JSONException {
        String jsonStr = queryItem(url);
        if(jsonStr != null) {
            parseJSON(items, jsonStr);
        }
    }

    private void parseJSON(List<GalleryItem> newGalleryItemList, String jsonBodyStr)
            throws IOException, JSONException {

        JSONObject jsonBody = new JSONObject(jsonBodyStr);
        JSONObject photosJson = jsonBody.getJSONObject("photos");
        JSONArray photoListJson = photosJson.getJSONArray("photo");

        int len = photoListJson.length();
        for(int i = 0; i < len; i++) {

            JSONObject jsonPhotoItem = photoListJson.getJSONObject(i);

            GalleryItem item = new GalleryItem();

            item.setId(jsonPhotoItem.getString("id"));
            item.setTitle(jsonPhotoItem.getString("title"));
            item.setOwner(jsonPhotoItem.getString("owner"));

            if(!jsonPhotoItem.has("url_s")) {
                continue;
            }

            item.setUrl(jsonPhotoItem.getString("url_s"));

            if(!jsonPhotoItem.has("url_z")) {
                continue;
            }

            item.setBigSizeUrl(jsonPhotoItem.getString("url_z"));

            newGalleryItemList.add(item);

        }
    }
}
