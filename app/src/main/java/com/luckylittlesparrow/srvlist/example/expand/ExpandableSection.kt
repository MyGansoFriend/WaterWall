package com.luckylittlesparrow.srvlist.example.expand

import android.view.View
import com.luckylittlesparrow.srvlist.example.R
import com.luckylittlesparrow.srvlist.example.listsample.Item
import com.luckylittlesparrow.srvlist.recycler.base.BaseViewHolder
import com.luckylittlesparrow.srvlist.recycler.section.ItemContainer
import com.luckylittlesparrow.srvlist.recycler.section.Section
import com.luckylittlesparrow.srvlist.recycler.section.SectionParams
import com.luckylittlesparrow.srvlist.recycler.util.DiffUtilItemCallback

class ExpandableSection(
    headerItem: ExpandableHeader,
    itemClickListener: (ItemContainer) -> Unit,
    itemList: List<Item>? = null
) :
    Section<ExpandableHeader, Item, Void>(headerItem,itemList, itemClickListener = itemClickListener) {

    override fun getItemViewHolder(view: View): BaseViewHolder<Item> {
        return ExpandableItemViewHolder(view, itemClickListener)
    }

    override fun getHeaderViewHolder(view: View): BaseViewHolder<ExpandableHeader> {
        return ExpandableHeaderViewHolder(view, onExpandClickListener)
    }

    override fun getSectionParams(): SectionParams {
        return SectionParams.builder()
            .isExpandedByDefault(true)
            .supportExpansion(true)
            .headerResourceId(R.layout.expandable_section_header)
            .itemResourceId(R.layout.section_item)
            .emptyResourceId(R.layout.section_empty)
            .failedResourceId(R.layout.section_failed)
            .loadingResourceId(R.layout.section_loading)
            .build()
    }

    override fun getDiffUtilItemCallback(): DiffUtilItemCallback {
        return object : DiffUtilItemCallback() {
            override fun areItemsTheSame(oldItem: ItemContainer, newItem: ItemContainer): Boolean {
                return (oldItem as Item) == (newItem as Item)
            }

            override fun areContentsTheSame(oldItem: ItemContainer, newItem: ItemContainer) =
                (oldItem as Item).body == (newItem as Item).body
        }
    }
}