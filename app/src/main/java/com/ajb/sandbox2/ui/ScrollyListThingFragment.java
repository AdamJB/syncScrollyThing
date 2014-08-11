package com.ajb.sandbox2.ui;

import com.ajb.sandbox2.R;
import com.ajb.sandbox2.ui.widget.ObservableScrollView;
import com.ajb.sandbox2.ui.widget.OverScrollByListView;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ScrollyListThingFragment extends Fragment
        implements OverScrollByListView.ScrollByCallback, AbsListView.OnScrollListener,
        ObservableScrollView.Callbacks {

    private static final String TAG = ScrollyListThingFragment.class.getSimpleName();

    private ViewTreeObserver.OnGlobalLayoutListener mGlobalLayoutListener
            = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            Log.d(TAG, "GlobalLayout Measured Header Height? " + mHeader.getMeasuredHeight());
            resizeContainer();
            onScrollChanged();
        }
    };

    protected View mRootView;

    protected ObservableScrollView mScrollView;

    @InjectView(R.id.container)
    protected View mContainer;

    @InjectView(R.id.list)
    protected OverScrollByListView mList;

    @InjectView(R.id.header)
    protected View mHeader;

    @InjectView(R.id.middle)
    protected View mMiddle;

    private SimpleAdapter mAdapter;

    public ScrollyListThingFragment() {
    }

    /**
     * Resize the Container View height to allow Scrolling with the ScrollView
     */
    private void resizeContainer() {
        int scrollHeight = mRootView.getMeasuredHeight();
        int headerHeight = mHeader.getMeasuredHeight();
        int middleHeight = mMiddle.getMeasuredHeight();

        // Setup Container Height
        int currentContainerHeight = mContainer.getMeasuredHeight();
        int newContainerHeight = scrollHeight + headerHeight;
        if (newContainerHeight != currentContainerHeight) {
            ViewGroup.LayoutParams params = mContainer.getLayoutParams();
            params.height = newContainerHeight;
            mContainer.setLayoutParams(params);
        }

        // Setup ListView height to fill the rest
        int currentListHeight = mList.getMeasuredHeight();
        int newListHeight = newContainerHeight - (headerHeight + middleHeight);
        if (newListHeight != currentListHeight) {
            ViewGroup.LayoutParams params = mList.getLayoutParams();
            params.height = newListHeight;
            mList.setLayoutParams(params);
        }
        mScrollView.setScrollY(0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_my, container, false);

        ButterKnife.inject(this, mRootView);

        mScrollView = (ObservableScrollView) mRootView;

        Log.d(TAG, "OnCreateView Measured Header Height? " + mHeader.getMeasuredHeight());

        setupCustomScrolling();
        setupListData();

        return mRootView;
    }

    // Setup Custom Scrolling stuff

    private void setupCustomScrolling() {
        mScrollView.setCallbacks(this);
        ViewTreeObserver vto = mScrollView.getViewTreeObserver();
        if (vto.isAlive()) {
            vto.addOnGlobalLayoutListener(mGlobalLayoutListener);
        }
    }

    // ObservableListView Callbacks

    @Override
    public void onScrollChanged() {

    }

    @Override
    public void onScrollViewOverScrollBy(int deltaY, int scrollY, boolean isTouchEvent) {
        Log.d(TAG, "OverScroll ScrollView DeltaY: " + deltaY + " ScrollY? : " + scrollY);
        // TODO: Height of Header/top padding?
        if (scrollY == 300 && deltaY > 0) {
            mScrollView.setScrollingCanceled(true);
        }
    }

    // OverScroll ListView callbacks

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
            int totalItemCount) {
    }


    @Override
    public void overScrolledY(int deltaY, int scrollY, boolean isTouchEvent) {
        Log.d(TAG, "ListOverScroll DeltaY: " + deltaY + " ScrollY: " + 0);
        if (scrollY == 0 && deltaY < 0) {
            mScrollView.setScrollingCanceled(false);
            mScrollView.smoothScrollTo(0, 0);
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    // Data section

    private void setupListData() {
        ArrayList<Map<String, String>> data = new ArrayList<Map<String, String>>();
        for (int i = 0; i < 20; i++) {
            HashMap<String, String> d = new HashMap<String, String>();
            d.put("key", "Something " + i);
            data.add(d);
        }

        String[] from = new String[]{
                "key",
        };
        int[] to = new int[]{
                android.R.id.text1,
        };

        mAdapter = new SimpleAdapter(getActivity(), data, R.layout.simple_list_item, from,
                to);
        mList.setAdapter(mAdapter);

        mList.setCallback(this);

        mList.setOnScrollListener(this);
    }

}
