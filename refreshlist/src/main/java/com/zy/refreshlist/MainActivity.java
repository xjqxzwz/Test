package com.zy.refreshlist;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RefreshListView mList;
    private List<String> mListData;
    private ListViewAdapter mListViewAdapter;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    mListData.add(0,"下拉刷新内容");
                    break;
                case 1:
                    mListData.add(mListData.size(),"加载更多内容");
                    break;
            }

            mListViewAdapter.notifyDataSetChanged();
            mList.InitStatus();
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
    }

    private void initView() {
        mList = (RefreshListView) findViewById(R.id.rlv_list);
    }

    private void initData() {
        mListData= new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            mListData.add("这是本来的内容" + i);
        }
        mListViewAdapter=new ListViewAdapter(MainActivity.this, mListData);
        mList.setAdapter(mListViewAdapter);
        mList.setOnRefreshLisenter(new RefreshListView.RefreshLisenter() {
            @Override
            public void onRefresh() {
                Log.e("onRefresh", "onRefresh");
                handler.sendEmptyMessageDelayed(0,3000);
            }

            @Override
            public void onLoadMore() {
                handler.sendEmptyMessageDelayed(1, 3000);
            }
        });
    }
}
