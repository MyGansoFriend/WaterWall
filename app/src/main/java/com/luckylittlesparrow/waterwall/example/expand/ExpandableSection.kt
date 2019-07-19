package com.luckylittlesparrow.waterwall.example.expand

import android.view.View
import com.luckylittlesparrow.waterwall.example.R
import com.luckylittlesparrow.waterwall.example.listsample.Item
import com.luckylittlesparrow.waterwall.recycler.base.BaseViewHolder
import com.luckylittlesparrow.waterwall.recycler.section.ItemContainer
import com.luckylittlesparrow.waterwall.recycler.section.Section
import com.luckylittlesparrow.waterwall.recycler.section.SectionParams
import com.luckylittlesparrow.waterwall.recycler.util.DiffUtilItemCallback

class ExpandableSection(
    headerItem: ExpandableHeader,
    val itemClickListener: (Item) -> Unit,
    val headerClickListener: (ExpandableHeader) -> Unit,
    itemList: List<Item>? = null
) :
    Section<ExpandableHeader, Item, Void>(
        headerItem,
        itemList
    ) {

    override fun getItemViewHolder(view: View): BaseViewHolder<Item> {
        return ExpandableItemViewHolder(view, itemClickListener)
    }

    override fun getHeaderViewHolder(view: View): BaseViewHolder<ExpandableHeader> {
        return ExpandableHeaderViewHolder(view, onExpandClickListener, headerClickListener)
    }

    override fun getSectionParams(): SectionParams {
        return SectionParams.builder()
            .isExpandedByDefault(true)
            .supportExpandFunction(true)
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