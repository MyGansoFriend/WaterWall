package com.luckylittlesparrow.waterwall.recycler.base

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
import com.luckylittlesparrow.waterwall.recycler.section.ItemContainer
import com.luckylittlesparrow.waterwall.recycler.util.toItemContainer

/**
 * ItemDecoration realisation with sections support, to add special drawing
 * and layout offset to specific item views from the adapter's data,
 * extend this class and override [onDrawItem] and [onDrawItemOffsets]
 *
 * @see RecyclerView.addItemDecoration
 *
 * @author Andrei Gusev
 * @since  1.0
 */
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

    /**
     * Add special drawing to item with type [ItemContainer.ItemType.ITEM]
     *
     * Draw any appropriate decorations into the Canvas supplied to the RecyclerView.
     * Any content drawn by this method will be drawn before the item views are drawn,
     * and will thus appear underneath the views.
     *
     * @param child The child view to decorate
     * @param canvas Canvas to draw into
     * @param parent RecyclerView this ItemDecoration is drawing into
     * @param state The current state of RecyclerView
     */
    abstract fun onDrawItem(child: View, canvas: Canvas, parent: RecyclerView, state: RecyclerView.State)


    /**
     * Add offsets to item with type [ItemContainer.ItemType.ITEM]
     *
     * Retrieve any offsets for the given item. Each field of <code>outRect</code> specifies
     * the number of pixels that the item view should be inset by, similar to padding or margin.
     * The default implementation sets the bounds of outRect to 0 and returns.
     *
     * <p>
     * If this ItemDecoration does not affect the positioning of item views, it should set
     * all four fields of <code>outRect</code> (left, top, right, bottom) to zero
     * before returning.
     *
     * <p>
     * If you need to access Adapter for additional data, you can call
     * {@link RecyclerView#getChildAdapterPosition(View)} to get the adapter position of the
     * View.
     *
     * @param outRect Rect to receive the output.
     * @param child    The child view to decorate
     * @param parent  RecyclerView this ItemDecoration is decorating
     * @param state   The current state of RecyclerView.
     */
    open fun onDrawItemOffsets(child: View, outRect: Rect, parent: RecyclerView, state: RecyclerView.State) {}
}