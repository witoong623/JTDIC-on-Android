package com.aliveplex.jtdic_on_android.Listeners;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.aliveplex.jtdic_on_android.Interfaces.ClickListener;

/**
 * Created by Aliveplex on 7/4/2560.
 */

public class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

    private GestureDetector gestureDetector;
    private ClickListener clickListener;

    public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
        this.clickListener = clickListener;

        gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                if (child != null && clickListener != null) {
                    if (Build.VERSION.SDK_INT >= 22){
                        clickListener.onLongClick(child, recyclerView.getChildAdapterPosition(child));
                    }
                    else {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        View child = rv.findChildViewUnder(e.getX(), e.getY());
        if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
            if (Build.VERSION.SDK_INT >= 22){
                clickListener.onClick(child, rv.getChildAdapterPosition(child));
            }
            else {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
        }

        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }
}
