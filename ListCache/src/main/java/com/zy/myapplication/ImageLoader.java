package com.zy.myapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.LruCache;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringBufferInputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by xjqxz_000 on 2016/3/1.
 */
public class ImageLoader {

    private LruCache<String, Bitmap> mLruCache;

    public ImageLoader() {
        int cacheSize = (int) (Runtime.getRuntime().maxMemory() / 4);
        mLruCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount();
            }
        };
    }

    //向缓存中添加数据
    public void setBitmapFromCache(String url, Bitmap bitmap) {
        if (getBitmapFromCache(url) == null) {
            mLruCache.put(url, bitmap);
        }
    }

    //从缓存中获取数据
    public Bitmap getBitmapFromCache(String url) {
        return mLruCache.get(url);
    }

    private static Bitmap getBitmapFromUrl(String urlString) {
        Bitmap bitmap = null;
        InputStream is = null;
        if (urlString != null) {
            HttpURLConnection urlConnection;
            try {
                URL url = new URL(urlString);
                urlConnection = (HttpURLConnection) url.openConnection();
                is = new BufferedInputStream(urlConnection.getInputStream());
                bitmap = BitmapFactory.decodeStream(is);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return bitmap;
    }

    class MyAsyncTask extends AsyncTask<String, Void, Bitmap> {
        private ImageView mImageView;
        private String mUrl;

        public MyAsyncTask(ImageView imageview, String url) {
            mUrl = url;
            mImageView = imageview;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap bitmap = getBitmapFromUrl(params[0]);
            setBitmapFromCache(mUrl, bitmap);
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {

            super.onPostExecute(bitmap);
            if (mImageView.getTag().equals(mUrl)) {
                mImageView.setImageBitmap(bitmap);
            }
        }
    }

    public void showImageByASync(ImageView imageView, String url) {
        Bitmap bitmap=getBitmapFromCache(url);
        if(bitmap==null){
            MyAsyncTask task = new MyAsyncTask(imageView, url);
            task.execute(url);
        }else{
            imageView.setImageBitmap(bitmap);
        }

    }
}
