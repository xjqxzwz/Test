package com.zy.refreshlist;

import android.content.Context;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by xjqxz_000 on 2016/3/2.
 */
public class RefreshListView extends ListView implements AbsListView.OnScrollListener {
    private LayoutInflater mLayoutInflater;

    private RotateAnimation animation;
    private RotateAnimation reverseAnimation;

    private View mHeader, mFooter;//头布局，尾布局
    private int downY = 0;//按下时的y轴坐标
    private int mHeaderHeight;//头布局高度
    private int mFooterHeight;//尾布局高度
    private ImageView mArrow;
    private ProgressBar mProgressBar;
    private TextView mRefreshText;
    private TextView mTime;

    private boolean isLoadingMore = false;// 当前是否正在处于加载更多
    private final int PULL_REFRESH = 0;//下拉刷新
    private final int RELEASE_REFRESH = 1;//释放刷新
    private final int REFRESHING = 2;//正在刷新

    private int mCurrentStatus = PULL_REFRESH;

    public RefreshListView(Context context) {
        super(context);
        mLayoutInflater = LayoutInflater.from(context);

        init();
    }

    public RefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mLayoutInflater = LayoutInflater.from(context);
        init();
    }

    public RefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mLayoutInflater = LayoutInflater.from(context);
        init();
    }

    private void init() {
        setOnScrollListener(this);
        initAnimation();
        initView();

    }

    //初始化动画
    private void initAnimation() {
        animation = new RotateAnimation(0, -180,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(200);
        animation.setFillAfter(true);

        reverseAnimation = new RotateAnimation(-180, 0,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        reverseAnimation.setDuration(200);
        reverseAnimation.setFillAfter(true);

    }

    private void initView() {
        mFooter = mLayoutInflater.inflate(R.layout.item_footer, null);
        mHeader = mLayoutInflater.inflate(R.layout.item_header, null);

        mArrow = (ImageView) mHeader.findViewById(R.id.iv_arrow);
        mProgressBar = (ProgressBar) mHeader.findViewById(R.id.pb_head);
        mRefreshText = (TextView) mHeader.findViewById(R.id.tv_headText);
        mTime = (TextView) mHeader.findViewById(R.id.tv_headTime);
        mProgressBar.setVisibility(INVISIBLE);

        //获得头布局高度并隐藏
        measureView(mHeader);
        mHeaderHeight = mHeader.getMeasuredHeight();
        mHeader.setPadding(0, -mHeaderHeight, 0, 0);
        mHeader.findViewById(R.id.iv_arrow);
        //获得尾布局高度并隐藏
        measureView(mFooter);
        mFooterHeight=mHeader.getMeasuredHeight();
        mFooter.setPadding(0, -mFooterHeight, 0, 0);

        this.addFooterView(mFooter);
        this.addHeaderView(mHeader);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downY = (int) ev.getY();//获得点击Y轴坐标
                break;
            case MotionEvent.ACTION_MOVE:
                int moveY = (int) (ev.getY() - downY);//获得移动距离
                int currentY = -mHeaderHeight + moveY;//计算外边距
                if (currentY > -mHeaderHeight && getFirstVisiblePosition() == 0) {
                    mHeader.setPadding(0, currentY, 0, 0);
                    if (currentY >= 0 && mCurrentStatus == PULL_REFRESH) {
                        mCurrentStatus = RELEASE_REFRESH;
                        setReFreshHeadView(mCurrentStatus);
                    } else if (currentY < 0 && mCurrentStatus == RELEASE_REFRESH) {
                        mCurrentStatus = PULL_REFRESH;
                        setReFreshHeadView(mCurrentStatus);
                    }

                    return true;
                }

                break;
            case MotionEvent.ACTION_UP:
                if (mCurrentStatus == PULL_REFRESH) {
                    mHeader.setPadding(0, -mHeaderHeight, 0, 0);
                } else if (mCurrentStatus == RELEASE_REFRESH) {
                    mCurrentStatus = REFRESHING;
                    mHeader.setPadding(0, 0, 0, 0);
                }
                setReFreshHeadView(mCurrentStatus);
                if(mRefreshLisenter!=null&& getFirstVisiblePosition() == 0){
                    mRefreshLisenter.onRefresh();
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    private void setReFreshHeadView(int status) {
        switch (status) {
            case PULL_REFRESH:
                mArrow.startAnimation(reverseAnimation);
                mRefreshText.setText("下拉刷新");
                break;
            case RELEASE_REFRESH:
                mArrow.startAnimation(animation);
                mRefreshText.setText("释放刷新");
                break;
            case REFRESHING:
                mRefreshText.setText("正在刷新...");
                mProgressBar.setVisibility(VISIBLE);
                mArrow.clearAnimation();
                mArrow.setVisibility(INVISIBLE);
                break;
        }

    }
    //恢复头布局状态
    public void InitStatus() {
        if (isLoadingMore) {
            isLoadingMore = false;
            mFooter.setPadding(0,-mFooterHeight,0,0);
        } else {
            mRefreshText.setText("下拉刷新");
            mArrow.setVisibility(VISIBLE);
            mProgressBar.setVisibility(INVISIBLE);
            mHeader.setPadding(0, -mHeaderHeight, 0, 0);
            mTime.setText("刷新时间:" + getCurrentTime());
            mCurrentStatus = PULL_REFRESH;
        }

    }
    //获得当前时间
    private String getCurrentTime() {
        SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");
        return mSimpleDateFormat.format(new Date());
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == SCROLL_STATE_IDLE && getLastVisiblePosition() == (getCount() - 1) && !isLoadingMore) {
            isLoadingMore=true;
            mFooter.setPadding(0,0,0,0);
            setSelection(getCount());// 让listview最后一条显示出来

            if(mRefreshLisenter!=null){
                mRefreshLisenter.onLoadMore();
            }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }

    //获得headView的width以及height
    private void measureView(View child) {
        ViewGroup.LayoutParams params = child.getLayoutParams();
        if (params == null) {
            params = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0,
                params.width);
        int lpHeight = params.height;
        int childHeightSpec;
        if (lpHeight > 0) {
            childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight,
                    MeasureSpec.EXACTLY);
        } else {
            childHeightSpec = MeasureSpec.makeMeasureSpec(0,
                    MeasureSpec.UNSPECIFIED);
        }
        child.measure(childWidthSpec, childHeightSpec);
    }

    public void setOnRefreshLisenter(RefreshLisenter refreshlisenter){
        this.mRefreshLisenter=refreshlisenter;
    }
    private RefreshLisenter mRefreshLisenter;
    //外部接口
    public interface  RefreshLisenter{
        public void onRefresh();
        public void onLoadMore();
    }
}
