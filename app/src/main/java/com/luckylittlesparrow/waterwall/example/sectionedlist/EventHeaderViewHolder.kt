package com.luckylittlesparrow.waterwall.example.sectionedlist

import android.view.View
import android.widget.TextView
import com.luckylittlesparrow.waterwall.example.R
import com.luckylittlesparrow.waterwall.recycler.base.BaseViewHolder

class EventHeaderViewHolder constructor(view: View, itemClickedListener: (EventHeader) -> Unit) :
    BaseViewHolder<EventHeader>(view, itemClickedListener) {

    private val eventItemDayTextView: TextView = view.findViewById(R.id.eventItemDayTextView)

    override fun bindItem(item: EventHeader) {
        super.bindItem(item)
        eventItemDayTextView.text = item.title
    }
}