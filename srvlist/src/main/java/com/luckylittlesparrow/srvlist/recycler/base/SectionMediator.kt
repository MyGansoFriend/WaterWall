package com.luckylittlesparrow.srvlist.recycler.base

import com.luckylittlesparrow.srvlist.recycler.section.ItemContainer
import com.luckylittlesparrow.srvlist.recycler.section.Section
import com.luckylittlesparrow.srvlist.recycler.state.SectionStateCallback

internal interface SectionMediator {

    fun addSection(key: String, section: Section<*, *, *>, sectionStateCallback: SectionStateCallback): String

    fun addSection(section: Section<*, *, *>, sectionStateCallback: SectionStateCallback): String

    fun addSections(list: List<Section<*, *, *>>, sectionStateCallback: SectionStateCallback)

    fun removeSection(sectionKey: String): Boolean

    fun removeSection(section: Section<*, *, *>): Boolean

    fun getSectionList(): MutableMap<String, SectionDao<Nothing, Nothing, Nothing>>

    fun getAllItemsList(): List<ItemContainer>

    fun getSectionByKey(key: String): SectionDao<Nothing, Nothing, Nothing>

    fun getSectionByItemPosition(position: Int): SectionDao<Nothing, Nothing, Nothing>

    fun getVisibleItemCount(): Int

    fun getSectionPosition(section: Section<*, *, *>): Int

    fun getSectionPosition(sectionKey: String): Int

    fun getPositionInSection(position: Int): Int

    fun getItemByPosition(position: Int): ItemContainer

    fun getSectionByItems(item: ItemContainer, newItem: ItemContainer): SectionDao<Nothing, Nothing, Nothing>?

    fun getSectionByItem(item: ItemContainer): SectionDao<Nothing, Nothing, Nothing>?

    fun containsSection(key: String): Boolean

    fun containsSection(section: Section<*, *, *>): Boolean

    fun clearList()
}