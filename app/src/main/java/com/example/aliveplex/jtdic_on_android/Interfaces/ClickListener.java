package com.example.aliveplex.jtdic_on_android.Interfaces;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Aliveplex on 7/4/2560.
 */

public interface ClickListener {
    void onClick(View view, int position);

    void onLongClick(View view, int position);
}


