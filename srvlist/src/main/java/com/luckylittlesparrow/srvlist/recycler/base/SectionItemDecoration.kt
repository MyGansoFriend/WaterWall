package com.luckylittlesparrow.srvlist.recycler.base

import android.graphics.Canvas
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.luckylittlesparrow.srvlist.recycler.section.ItemContainer
import com.luckylittlesparrow.srvlist.recycler.util.toItemContainer

abstract class SectionItemDecoration : RecyclerView.ItemDecoration() {

    override fun onDraw(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        for (i in 0 until parent.childCount - 1) {
            val child = parent.getChildAt(i)

            val item = (parent.getChildViewHolder(child) as BaseViewHolder<*>).item
            item?.let {
                val viewType = item.toItemContainer().itemType

                if (viewType != ItemContainer.ItemType.HEADER && viewType != ItemContainer.ItemType.FOOTER)
                    onDrawItem(child, canvas, parent, state)
            }
        }
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        val item = (parent.getChildViewHolder(view) as BaseViewHolder<*>).item
        item?.let {
            val viewType = item.toItemContainer().itemType

            if (viewType != ItemContainer.ItemType.HEADER && viewType != ItemContainer.ItemType.FOOTER)
                onDrawItemOffsets(view, outRect, parent, state)
        }
    }


    abstract fun onDrawItem(child: View, canvas: Canvas, parent: RecyclerView, state: RecyclerView.State)

    open fun onDrawItemOffsets(child: View, outRect: Rect, parent: RecyclerView, state: RecyclerView.State) {}
}