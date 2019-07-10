package com.luckylittlesparrow.srvlist.example.sectionedlist

import android.view.View
import android.widget.TextView
import com.luckylittlesparrow.srvlist.example.R
import com.luckylittlesparrow.srvlist.recycler.base.BaseViewHolder

class EventHeaderViewHolder constructor(view: View, itemClickedListener: (EventHeader) -> Unit) :
    BaseViewHolder<EventHeader>(view, itemClickedListener) {

    private val eventItemDayTextView: TextView = view.findViewById(R.id.eventItemDayTextView)

    override fun bindItem(item: EventHeader) {
        super.bindItem(item)
        eventItemDayTextView.text = item.title
    }
}