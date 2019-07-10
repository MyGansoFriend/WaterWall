package com.luckylittlesparrow.srvlist.recycler.simple

import com.luckylittlesparrow.srvlist.recycler.base.SectionDao
import com.luckylittlesparrow.srvlist.recycler.section.ItemContainer
import com.luckylittlesparrow.srvlist.recycler.section.Section
import com.luckylittlesparrow.srvlist.recycler.state.SectionState

internal open class SimpleSectionDao<H, I, F>(override val section: Section<H, I, F>) :
    SectionDao<H, I, F> {

    override fun areContentsTheSame(oldItem: ItemContainer, newItem: ItemContainer): Boolean {
        return when (oldItem.itemType) {
            ItemContainer.ItemType.ITEM -> section.getDiffUtilItemCallback().areContentsTheSame(
                oldItem,
                newItem
            )
            else -> oldItem == newItem
        }
    }

    override fun areItemsTheSame(oldItem: ItemContainer, newItem: ItemContainer): Boolean {
        return when (oldItem.itemType) {
            ItemContainer.ItemType.ITEM -> section.getDiffUtilItemCallback().areItemsTheSame(
                oldItem,
                newItem
            )
            else -> oldItem == newItem
        }
    }

    override fun getItem(itemType: ItemContainer.ItemType, position: Int): ItemContainer {
        return when (itemType) {
            ItemContainer.ItemType.HEADER -> section.sourceList.first()
            ItemContainer.ItemType.FOOTER -> section.sourceList.last()
            ItemContainer.ItemType.ITEM -> {
                check(position != -1); getContentItem(position)
            }
        }
    }

    override fun getContentItem(position: Int): ItemContainer {
        return section.sourceList[position]
    }

    override fun hasItems(item: ItemContainer, newItem: ItemContainer): Boolean {
        return hasItem(item) && hasItem(newItem)
    }

    override fun hasItem(item: ItemContainer): Boolean {
        return section.sourceList.any { it === item }
    }

    override fun getSectionList() = section.sourceList

    override fun sectionCurrentSize(): Int = section.currentSize()

    override fun sectionOriginalSize(): Int = section.originalSize()

    override fun hasHeader(): Boolean = section.hasHeader()

    override fun hasFooter(): Boolean = section.hasFooter()

    override fun isVisible(): Boolean = section.isVisible

    override fun state(): SectionState = section.state

    override fun key(): String = section.key

    override fun getFooterIndex(): Int = section.sourceList.size - 1
}