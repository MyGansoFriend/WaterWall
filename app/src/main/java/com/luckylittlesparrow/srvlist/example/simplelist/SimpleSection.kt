package com.luckylittlesparrow.srvlist.example.simplelist

import android.view.View
import com.luckylittlesparrow.srvlist.example.R
import com.luckylittlesparrow.srvlist.example.listsample.Item
import com.luckylittlesparrow.srvlist.recycler.base.BaseViewHolder
import com.luckylittlesparrow.srvlist.recycler.section.ItemContainer
import com.luckylittlesparrow.srvlist.recycler.section.Section
import com.luckylittlesparrow.srvlist.recycler.section.SectionParams
import com.luckylittlesparrow.srvlist.recycler.util.DiffUtilItemCallback

class SimpleSection(
    itemClickListener: (ItemContainer) -> Unit,
    contentItems: List<ExampleItem>
) : Section<Void, ExampleItem, Void>(contentItems = contentItems, itemClickListener = itemClickListener) {

    override fun getItemViewHolder(view: View): BaseViewHolder<ExampleItem> {
        return ItemViewHolder(view, itemClickListener)
    }

    override fun getSectionParams(): SectionParams {
        return SectionParams.builder()
            .itemResourceId(R.layout.section_item)
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