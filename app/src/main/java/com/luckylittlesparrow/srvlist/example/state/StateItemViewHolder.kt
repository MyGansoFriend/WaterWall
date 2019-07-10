package com.luckylittlesparrow.srvlist.example.state

import android.view.View
import android.widget.TextView
import com.luckylittlesparrow.srvlist.example.R
import com.luckylittlesparrow.srvlist.example.listsample.Item
import com.luckylittlesparrow.srvlist.recycler.base.BaseViewHolder


class StateItemViewHolder constructor(view: View) :
    BaseViewHolder<Item>(view) {

    private val itemDescriptionTextView: TextView = view.findViewById(R.id.itemDescriptionTextView)


    override fun bindItem(item: Item) {
        super.bindItem(item)
        itemDescriptionTextView.text = item.body
    }
}