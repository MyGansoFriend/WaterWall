package com.luckylittlesparrow.srvlist.example.state

import android.view.View
import com.luckylittlesparrow.srvlist.example.R
import com.luckylittlesparrow.srvlist.example.listsample.Item
import com.luckylittlesparrow.srvlist.recycler.base.BaseViewHolder
import com.luckylittlesparrow.srvlist.recycler.section.ItemContainer
import com.luckylittlesparrow.srvlist.recycler.section.Section
import com.luckylittlesparrow.srvlist.recycler.section.SectionParams
import com.luckylittlesparrow.srvlist.recycler.util.DiffUtilItemCallback

class StateSection : Section<Void, Item, Void>() {

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