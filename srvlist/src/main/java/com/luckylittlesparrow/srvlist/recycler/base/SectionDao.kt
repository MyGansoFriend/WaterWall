package com.luckylittlesparrow.srvlist.recycler.base

import com.luckylittlesparrow.srvlist.recycler.section.ItemContainer
import com.luckylittlesparrow.srvlist.recycler.section.Section
import com.luckylittlesparrow.srvlist.recycler.state.SectionState

internal interface SectionDao<H, I, F> {
    val section: Section<H, I, F>

    fun areContentsTheSame(oldItem: ItemContainer, newItem: ItemContainer): Boolean

    fun areItemsTheSame(oldItem: ItemContainer, newItem: ItemContainer): Boolean

    fun getItem(itemType: ItemContainer.ItemType, position: Int = -1): ItemContainer

    fun hasItems(item: ItemContainer, newItem: ItemContainer): Boolean

    fun hasItem(item: ItemContainer): Boolean

    fun getSectionList(): List<ItemContainer>

    fun sectionCurrentSize(): Int

    fun sectionOriginalSize(): Int

    fun hasHeader(): Boolean

    fun hasFooter(): Boolean

    fun isVisible(): Boolean

    fun state(): SectionState

    fun key(): String

    fun getFooterIndex(): Int

    fun filter(search: CharSequence): Pair<List<ItemContainer>, List<ItemContainer>>? = null

    fun getContentItem(position: Int): ItemContainer
}