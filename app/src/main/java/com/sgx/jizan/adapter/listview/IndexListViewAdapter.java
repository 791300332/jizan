package com.sgx.jizan.adapter.listview;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.support.v4.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.sgx.jizan.R;
import com.sgx.jizan.model.IndexModel;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Created by sgx on 2017/11/22.
 */

public class IndexListViewAdapter extends BaseAdapter {

    private List<IndexModel> list;

    private LayoutInflater mInflater;

    private LruCache<String, BitmapDrawable> mImageCache;
    private ListView listview;

    public IndexListViewAdapter(Context context , List<IndexModel> l) {
        mInflater = LayoutInflater.from(context);
        list = l;

        int maxCache = (int) Runtime.getRuntime().maxMemory();
        int cacheSize = maxCache / 8;
        mImageCache = new LruCache<String, BitmapDrawable>(cacheSize) {
            @Override
            protected int sizeOf(String key, BitmapDrawable value) {
                return value.getBitmap().getByteCount();
            }
        };
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(listview == null) {
            listview = (ListView) parent;
        }
        convertView = mInflater.inflate(R.layout.index_list_item,null);
        ImageView iv = (ImageView) convertView.findViewById(R.id.index_list_img);
        TextView tv1 = (TextView) convertView.findViewById(R.id.index_list_tv1);
        TextView tv2 = (TextView) convertView.findViewById(R.id.index_list_tv2);
        TextView tv3 = (TextView) convertView.findViewById(R.id.index_list_tv3);
        IndexModel model = list.get(position);
        try{
            if(mImageCache.get(model.getImgUrl()) != null) {
                iv.setImageDrawable(mImageCache.get(model.getImgUrl()));
            } else {
                ImageTask it = new ImageTask();
                it.execute(model.getImgUrl());
            }
            tv1.setText(model.getTitle());
            tv2.setText(model.getGroupCount());
            tv3.setText(model.getLookCount());
        }catch (Exception e) {
            e.printStackTrace();
        }
        return convertView;
    }


    @Override
    public boolean isEmpty() {
        return list == null?true:false;
    }


    class ImageTask extends AsyncTask<String, Void, BitmapDrawable> {

        private String imageUrl;

        @Override
        protected BitmapDrawable doInBackground(String... params) {
            imageUrl = params[0];
            Bitmap bitmap = downloadImage();
            BitmapDrawable db = new BitmapDrawable(listview.getResources(),
                    bitmap);
            // 如果本地还没缓存该图片，就缓存
            if (mImageCache.get(imageUrl) == null) {
                mImageCache.put(imageUrl, db);
            }
            return db;
        }

        @Override
        protected void onPostExecute(BitmapDrawable result) {
            // 通过Tag找到我们需要的ImageView，如果该ImageView所在的item已被移出页面，就会直接返回null
            ImageView iv = (ImageView) listview.findViewWithTag(imageUrl);
            if (iv != null && result != null) {
                iv.setImageDrawable(result);
            }
        }

        /**
         * 根据url从网络上下载图片
         *
         * @return
         */
        private Bitmap downloadImage() {
            HttpURLConnection con = null;
            Bitmap bitmap = null;
            try {
                URL url = new URL(imageUrl);
                con = (HttpURLConnection) url.openConnection();
                con.setConnectTimeout(5 * 1000);
                con.setReadTimeout(10 * 1000);
                bitmap = BitmapFactory.decodeStream(con.getInputStream());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (con != null) {
                    con.disconnect();
                }
            }

            return bitmap;
        }

    }
}
