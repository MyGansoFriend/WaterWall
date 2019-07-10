package com.luckylittlesparrow.srvlist.recycler.state

import com.luckylittlesparrow.srvlist.recycler.section.ItemContainer

internal interface SectionStateCallback {
    fun onSectionStateChanged(sectionKey: String, newState: SectionState, oldState: SectionState)

    fun onSectionContentAdded(sectionKey: String, addItemsCount: Int)

    fun onSectionContentChanged(sectionKey: String)

    fun onSectionExpandChange(sectionKey: String, isExpanded: Boolean)

    fun onSectionShowMoreChange(sectionKey: String, collapsedItemCount: Int, isShowMore: Boolean)

    fun onSectionContentUpdated(previousList: List<ItemContainer>, newList: List<ItemContainer>, sectionKey: String)
}