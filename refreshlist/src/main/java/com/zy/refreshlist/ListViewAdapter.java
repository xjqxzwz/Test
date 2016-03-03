package com.zy.refreshlist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by xjqxz_000 on 2016/3/2.
 */
public class ListViewAdapter extends BaseAdapter {

    private List<String> mData;
    private LayoutInflater mLayoutInflater;
    public ListViewAdapter (Context context,List<String> data){
        mLayoutInflater=LayoutInflater.from(context);
        mData=data;
    }
    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mViewHolder;
        if(convertView==null){
            mViewHolder=new ViewHolder();
            convertView=mLayoutInflater.inflate(R.layout.item_content,null);
            mViewHolder.mContent= (TextView) convertView.findViewById(R.id.tv_content);
            convertView.setTag(mViewHolder);
        }else{
            mViewHolder= (ViewHolder) convertView.getTag();
        }
        mViewHolder.mContent.setText(mData.get(position));
        return convertView;
    }

    class ViewHolder{
        TextView mContent;
    }
}
