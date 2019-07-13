package com.luckylittlesparrow.waterwall.example.expand

import android.view.View
import android.widget.TextView
import com.luckylittlesparrow.waterwall.example.R
import com.luckylittlesparrow.waterwall.example.listsample.Item
import com.luckylittlesparrow.waterwall.recycler.base.BaseViewHolder


class ExpandableItemViewHolder constructor(view: View, itemClickListener: (Item) -> Unit) :
    BaseViewHolder<Item>(view, itemClickListener) {

    private val itemDescriptionTextView: TextView = view.findViewById(R.id.itemDescriptionTextView)


    override fun bindItem(item: Item) {
        super.bindItem(item)
        itemDescriptionTextView.text = item.body
    }
}