package com.luckylittlesparrow.waterwall.example.simplelist

import android.view.View
import com.luckylittlesparrow.waterwall.example.R
import com.luckylittlesparrow.waterwall.example.listsample.Item
import com.luckylittlesparrow.waterwall.recycler.base.BaseViewHolder
import com.luckylittlesparrow.waterwall.recycler.section.ItemContainer
import com.luckylittlesparrow.waterwall.recycler.section.Section
import com.luckylittlesparrow.waterwall.recycler.section.SectionParams
import com.luckylittlesparrow.waterwall.recycler.util.DiffUtilItemCallback

class SimpleSection(
    private val itemClickListener: (ExampleItem) -> Unit,
    contentItems: List<ExampleItem>
) : Section<Void, ExampleItem, Void>(contentItems = contentItems) {

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