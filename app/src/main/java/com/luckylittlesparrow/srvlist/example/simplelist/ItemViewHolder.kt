package com.luckylittlesparrow.srvlist.example.simplelist

import android.view.View
import android.widget.TextView
import com.luckylittlesparrow.srvlist.example.R
import com.luckylittlesparrow.srvlist.recycler.base.BaseViewHolder


class ItemViewHolder constructor(view: View, itemClickedListener: (ExampleItem) -> Unit) :
    BaseViewHolder<ExampleItem>(view, itemClickedListener) {

    private val eventItemDescriptionTextView: TextView = view.findViewById(R.id.itemDescriptionTextView)

    override fun bindItem(item: ExampleItem) {
        super.bindItem(item)
        eventItemDescriptionTextView.text = item.body
    }
}