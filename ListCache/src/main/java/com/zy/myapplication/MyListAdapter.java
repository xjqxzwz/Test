package com.zy.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by xjqxz_000 on 2016/3/1.
 */
public class MyListAdapter extends BaseAdapter {

    private List<String> mList;
    private LayoutInflater lInflater;
    private ImageLoader mImageLoader;
    public MyListAdapter(Context context, List<String> list) {
        lInflater = LayoutInflater.from(context);
        this.mList = list;
        mImageLoader=new ImageLoader();

    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String url = mList.get(position);
        ViewHolder viewHolder = null;
        if (convertView == null) {

            viewHolder = new ViewHolder();
            convertView = lInflater.inflate(R.layout.item, null);
            viewHolder.mImage = (ImageView) convertView.findViewById(R.id.iv_image);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.mImage.setTag(url);
        viewHolder.mImage.setImageResource(R.drawable.ic_launcher);
        mImageLoader.showImageByASync(viewHolder.mImage,url);
        return convertView;
    }

    class ViewHolder {
        public ImageView mImage;
    }
}
