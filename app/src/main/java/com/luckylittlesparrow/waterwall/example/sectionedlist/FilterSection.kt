package com.luckylittlesparrow.waterwall.example.sectionedlist

import android.view.View
import com.luckylittlesparrow.waterwall.example.R
import com.luckylittlesparrow.waterwall.example.listsample.Item
import com.luckylittlesparrow.waterwall.recycler.base.BaseViewHolder
import com.luckylittlesparrow.waterwall.recycler.filterable.FilterableSection
import com.luckylittlesparrow.waterwall.recycler.section.ItemContainer
import com.luckylittlesparrow.waterwall.recycler.section.SectionParams
import com.luckylittlesparrow.waterwall.recycler.util.DiffUtilItemCallback

class FilterSection(
    headerItem: FilterHeader,
    contentItems: List<Item>,
    footerItem: FilterFooter
) : FilterableSection<FilterHeader, Item, FilterFooter>(headerItem, contentItems, footerItem) {

    override fun itemFilter(search: String, item: ItemContainer): Boolean {
        return (item as Item).body.contains(search, true)
    }

    override fun getItemViewHolder(view: View): BaseViewHolder<Item> {
        return FilterItemViewHolder(view)
    }

    override fun getHeaderViewHolder(view: View): BaseViewHolder<FilterHeader> {
        return FilterHeaderViewHolder(view)
    }

    override fun getFooterViewHolder(view: View): BaseViewHolder<FilterFooter> {
        return FilterFooterViewHolder(view)
    }

    override fun getSectionParams(): SectionParams {
        return SectionParams.builder()
            .itemResourceId(R.layout.event_section_item)
            .headerResourceId(R.layout.event_section_header)
            .footerResourceId(R.layout.event_section_footer)
            .failedResourceId(R.layout.event_section_fail)
            .loadingResourceId(R.layout.event_section_loading)
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