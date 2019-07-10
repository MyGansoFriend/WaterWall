package com.luckylittlesparrow.srvlist.recycler.base

/*
 *  Copyright 2019 Gusev Andrei
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

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