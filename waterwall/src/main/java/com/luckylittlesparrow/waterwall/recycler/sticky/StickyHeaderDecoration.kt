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

package com.luckylittlesparrow.waterwall.recycler.sticky

import android.graphics.Canvas
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.luckylittlesparrow.waterwall.recycler.base.BaseListAdapter
import com.luckylittlesparrow.waterwall.recycler.base.BaseViewHolder

/**
 * @author Andrei Gusev
 * @since  1.0
 */
internal class StickyHeaderDecoration(private val stickyHeaderHelper: StickyHeaderHelper) :
    RecyclerView.ItemDecoration() {

    private var headerViewHeight: Int = 0
    private var currentStickyHeader: BaseViewHolder<*>? = null

    override fun onDrawOver(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(canvas, parent, state)
        val topChild = parent.getChildAt(0) ?: return

        val topChildPosition = parent.getChildAdapterPosition(topChild)
        if (topChildPosition == RecyclerView.NO_POSITION) return

        val headerPos = stickyHeaderHelper.getHeaderPositionForItem(topChildPosition)
        if (headerPos == -1) return
        val currentHeader = getHeaderViewForItem(headerPos, parent)

        fixLayoutSize(parent, currentHeader)

        val contactPoint = currentHeader.bottom
        val childInContact = getChildInContact(parent, contactPoint, headerPos)

        if (childInContact != null && stickyHeaderHelper.isHeader(parent.getChildAdapterPosition(childInContact))) {
            moveHeader(canvas, currentHeader, childInContact)
            return
        }

        drawHeader(canvas, currentHeader)
        (parent.adapter as BaseListAdapter).currentStickyHeader = currentStickyHeader
    }

    private fun getHeaderViewForItem(headerPosition: Int, parent: RecyclerView): View {
        val layoutResId = stickyHeaderHelper.getHeaderLayout(headerPosition)
        val header = LayoutInflater.from(parent.context).inflate(layoutResId, parent, false)
        val pair = stickyHeaderHelper.bindHeaderData(header, headerPosition)

        currentStickyHeader = pair.first

        runCatching { pair.first.bindStickyItem(pair.second) }

        return header
    }

    private fun drawHeader(canvas: Canvas, header: View) {
        canvas.apply {
            save()
            translate(0f, 0f)
            header.draw(this)
            restore()
        }
    }

    private fun moveHeader(canvas: Canvas, currentHeader: View, nextHeader: View) {
        canvas.apply {
            save()
            translate(0f, (nextHeader.top - currentHeader.height).toFloat())
            currentHeader.draw(this)
            restore()
        }
    }

    private fun getChildInContact(parent: RecyclerView, contactPoint: Int, currentHeaderPos: Int): View? {
        var childInContact: View? = null
        for (i in 0 until parent.childCount) {
            var heightLimit = 0
            val child = parent.getChildAt(i)

            if (currentHeaderPos != i) {
                val isHeader = stickyHeaderHelper.isHeader(parent.getChildAdapterPosition(child))
                if (isHeader) heightLimit = headerViewHeight - child.height
            }

            val childBottomPosition = if (child.top > 0) child.bottom + heightLimit else child.bottom

            if (childBottomPosition > contactPoint)
                if (child.top <= contactPoint) {
                    childInContact = child
                    break
                }
        }
        return childInContact
    }

    private fun fixLayoutSize(parent: ViewGroup, view: View) {

        val widthSpec = View.MeasureSpec.makeMeasureSpec(parent.width, View.MeasureSpec.EXACTLY)

        val heightSpec = View.MeasureSpec.makeMeasureSpec(parent.height, View.MeasureSpec.UNSPECIFIED)

        val childWidthSpec =
            ViewGroup.getChildMeasureSpec(widthSpec, parent.paddingLeft + parent.paddingRight, view.layoutParams.width)

        val childHeightSpec = ViewGroup.getChildMeasureSpec(
            heightSpec,
            parent.paddingTop + parent.paddingBottom,
            view.layoutParams.height
        )

        view.measure(childWidthSpec, childHeightSpec)

        headerViewHeight = view.measuredHeight
        view.layout(0, 0, view.measuredWidth, view.measuredHeight)
    }
}