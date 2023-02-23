package com.yxc.customercomposeview.calander.recycler

import android.graphics.Canvas
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.yxc.customercomposeview.utils.dp

/**
 * @author yxc
 * @date 2019-05-31
 */
class CalendarItemDecoration : RecyclerView.ItemDecoration() {
    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)
    }

    override fun getItemOffsets(
        outRect: Rect, view: View,
        parent: RecyclerView, state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.bottom = 20.dp
        outRect.top = 20.dp
    }
}