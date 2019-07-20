package com.luckylittlesparrow.waterwall.example.expand

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.luckylittlesparrow.waterwall.example.R
import com.luckylittlesparrow.waterwall.recycler.base.BaseViewHolder

class ExpandableHeaderViewHolder constructor(
    view: View, val expandClickedListener: () -> Unit?,
    headerClickListener: (ExpandableHeader) -> Unit
) :
    BaseViewHolder<ExpandableHeader>(view, headerClickListener) {

    private var isExpanded = true
    private val itemDayTextView: TextView = view.findViewById(R.id.itemDayTextView)
    private val expandItem: ImageView = view.findViewById(R.id.expandItem)

    override fun bindItem(item: ExpandableHeader) {
        super.bindItem(item)
        itemDayTextView.text = item.title
        expandItem.setOnClickListener {
            expandClickedListener.invoke()
            isExpanded = !isExpanded
            expandItem.setImageResource(if (isExpanded) R.drawable.ic_expand_less_black_24dp else R.drawable.ic_expand_more_black_24dp)
        }
    }
}