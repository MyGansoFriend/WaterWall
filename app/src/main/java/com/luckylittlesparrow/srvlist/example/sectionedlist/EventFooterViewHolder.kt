package com.luckylittlesparrow.srvlist.example.sectionedlist

import android.view.View
import android.widget.TextView
import com.luckylittlesparrow.srvlist.example.R
import com.luckylittlesparrow.srvlist.recycler.base.BaseViewHolder

class EventFooterViewHolder constructor(view: View) :
    BaseViewHolder<EventFooter>(view) {

    private val eventItemDayTextView: TextView = view.findViewById(R.id.eventFooterItemDayTextView)

    override fun bindItem(item: EventFooter) {
        super.bindItem(item)
        eventItemDayTextView.text = item.title
    }
}