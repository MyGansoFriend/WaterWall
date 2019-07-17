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

package com.luckylittlesparrow.waterwall.example.recent

import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.luckylittlesparrow.waterwall.recycler.base.SectionItemDecoration

class TransactionDecoration(private val divider: Drawable) : SectionItemDecoration() {
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