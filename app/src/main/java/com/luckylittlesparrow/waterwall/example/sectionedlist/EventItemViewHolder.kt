package com.luckylittlesparrow.waterwall.example.sectionedlist

import android.view.View
import android.widget.TextView
import com.luckylittlesparrow.waterwall.example.R
import com.luckylittlesparrow.waterwall.example.listsample.Item
import com.luckylittlesparrow.waterwall.recycler.base.BaseViewHolder


class EventItemViewHolder constructor(view: View, itemClickedListener: (Item) -> Unit) :
    BaseViewHolder<Item>(view, itemClickedListener) {

    private val eventItemDescriptionTextView: TextView = view.findViewById(R.id.eventItemDescriptionTextView)


    override fun bindItem(item: Item) {
        super.bindItem(item)
        eventItemDescriptionTextView.text = item.body
    }
}