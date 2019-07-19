package com.luckylittlesparrow.waterwall.example.sectionedlist

import android.view.View
import android.widget.TextView
import com.luckylittlesparrow.waterwall.example.R
import com.luckylittlesparrow.waterwall.recycler.base.BaseViewHolder

class FilterHeaderViewHolder constructor(view: View) :
    BaseViewHolder<FilterHeader>(view) {

    private val eventItemDayTextView: TextView = view.findViewById(R.id.eventItemDayTextView)

    override fun bindItem(item: FilterHeader) {
        super.bindItem(item)
        eventItemDayTextView.text = item.title
    }
}