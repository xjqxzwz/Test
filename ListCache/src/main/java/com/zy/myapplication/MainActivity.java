package com.zy.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView mListView;
    public final static String[] IMAGE_URLS = new String[]{
            "http://xjqxzwz.applinzi.com/png/1.png",
            "http://xjqxzwz.applinzi.com/png/2.png",
            "http://xjqxzwz.applinzi.com/png/3.png",
            "http://xjqxzwz.applinzi.com/png/4.png",
            "http://xjqxzwz.applinzi.com/png/5.png",
            "http://xjqxzwz.applinzi.com/png/6.png",
            "http://xjqxzwz.applinzi.com/png/7.png",
            "http://xjqxzwz.applinzi.com/png/8.png",
            "http://xjqxzwz.applinzi.com/png/9.png",
            "http://xjqxzwz.applinzi.com/png/10.png",
            "http://xjqxzwz.applinzi.com/png/11.png",
            "http://xjqxzwz.applinzi.com/png/12.png",
            "http://xjqxzwz.applinzi.com/png/13.png",
            "http://xjqxzwz.applinzi.com/png/14.png",
            "http://xjqxzwz.applinzi.com/png/15.png",
            "http://xjqxzwz.applinzi.com/png/16.png",
            "http://xjqxzwz.applinzi.com/png/17.png",
            "http://xjqxzwz.applinzi.com/png/18.png",
            "http://xjqxzwz.applinzi.com/png/19.png",
            "http://xjqxzwz.applinzi.com/png/20.png",
           };
    private List<String> mData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mListView = (ListView) findViewById(R.id.lv_list);
        mData = Arrays.asList(IMAGE_URLS);
        mListView.setAdapter(new MyListAdapter(this, mData));
    }
}
