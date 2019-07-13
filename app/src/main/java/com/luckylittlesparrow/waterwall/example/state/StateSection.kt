package com.luckylittlesparrow.waterwall.example.state

import android.view.View
import com.luckylittlesparrow.waterwall.example.R
import com.luckylittlesparrow.waterwall.example.listsample.Item
import com.luckylittlesparrow.waterwall.recycler.base.BaseViewHolder
import com.luckylittlesparrow.waterwall.recycler.filterable.FilterableSection
import com.luckylittlesparrow.waterwall.recycler.section.ItemContainer
import com.luckylittlesparrow.waterwall.recycler.section.SectionParams
import com.luckylittlesparrow.waterwall.recycler.util.DiffUtilItemCallback

class StateSection : FilterableSection<Void, Item, Void>() {
    override fun itemFilter(search: String, item: ItemContainer): Boolean {
        return true
    }

    override fun getItemViewHolder(view: View): BaseViewHolder<Item> {
        return StateItemViewHolder(view)
    }

    override fun getSectionParams(): SectionParams {
        return SectionParams.builder()
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