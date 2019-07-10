package com.luckylittlesparrow.srvlist.example.sectionedlist

import android.view.View
import com.luckylittlesparrow.srvlist.example.R
import com.luckylittlesparrow.srvlist.example.listsample.Item
import com.luckylittlesparrow.srvlist.recycler.base.BaseViewHolder
import com.luckylittlesparrow.srvlist.recycler.filterable.FilterableSection
import com.luckylittlesparrow.srvlist.recycler.section.ItemContainer
import com.luckylittlesparrow.srvlist.recycler.section.SectionParams
import com.luckylittlesparrow.srvlist.recycler.util.DiffUtilItemCallback

class EventSection(
    headerItem: EventHeader,
    contentItems: List<Item>,
    footerItem: EventFooter
) : FilterableSection<EventHeader, Item, EventFooter>(headerItem, contentItems, footerItem) {

    override fun itemFilter(search: String, item: ItemContainer): Boolean {
        return (item as Item).body.contains(search, true)
    }

    override fun getItemViewHolder(view: View): BaseViewHolder<Item> {
        return EventItemViewHolder(view, itemClickListener)
    }

    override fun getHeaderViewHolder(view: View): BaseViewHolder<EventHeader> {
        return EventHeaderViewHolder(view, headerClickListener)
    }

    override fun getFooterViewHolder(view: View): BaseViewHolder<EventFooter> {
        return EventFooterViewHolder(view)
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