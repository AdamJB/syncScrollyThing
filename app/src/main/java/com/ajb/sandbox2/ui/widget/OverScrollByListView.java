package com.ajb.sandbox2.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class OverScrollByListView extends ListView {

    private ScrollByCallback mCallback;

    public OverScrollByListView(Context context) {
        super(context);
    }

    public OverScrollByListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public OverScrollByListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setCallback(ScrollByCallback callback) {
        mCallback = callback;
    }

    @Override
    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY,
            int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY,
            boolean isTouchEvent) {
        boolean isClamped = super
                .overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY,
                        maxOverScrollX, maxOverScrollY, isTouchEvent);
        if (this.mCallback != null) {
            this.mCallback.overScrolledY(deltaY, scrollY, isTouchEvent);
        }
        return isClamped;
    }

    public static abstract interface ScrollByCallback {

        public abstract void overScrolledY(int deltaY, int scrollY, boolean isTouchEvent);
    }

}
