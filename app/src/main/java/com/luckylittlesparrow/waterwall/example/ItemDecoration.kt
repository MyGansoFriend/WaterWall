package com.luckylittlesparrow.waterwall.example

import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.luckylittlesparrow.waterwall.recycler.base.SectionItemDecoration


class ItemDecoration(private val divider: Drawable) : SectionItemDecoration() {

    override fun onDrawItem(child: View, canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val dividerLeft = parent.paddingLeft
        val dividerRight = parent.width - parent.paddingRight


        val params = child.layoutParams as RecyclerView.LayoutParams

        val dividerTop = child.bottom + params.bottomMargin
        val dividerBottom = dividerTop + divider.intrinsicHeight

        divider.setBounds(dividerLeft, dividerTop, dividerRight, dividerBottom)
        divider.draw(canvas)
    }
}

