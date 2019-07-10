package com.luckylittlesparrow.srvlist.recycler.filterable

import com.luckylittlesparrow.srvlist.recycler.base.BaseSectionMediator
import com.luckylittlesparrow.srvlist.recycler.state.SectionStateCallback
import com.luckylittlesparrow.srvlist.recycler.section.Section
import java.util.*

internal class FilterableSectionMediator : BaseSectionMediator() {

    @Suppress("UNCHECKED_CAST")
    override fun addSection(key: String, section: Section<*, *, *>, sectionStateCallback: SectionStateCallback): String {
        section.key = key
        section.sectionStateCallback = sectionStateCallback
        sections[key] = FilterableSectionDao(section as FilterableSection<Nothing, Nothing, Nothing>)
        return key
    }

    @Suppress("UNCHECKED_CAST")
    override fun addSections(list: List<Section<*, *, *>>, sectionStateCallback: SectionStateCallback) {
        list.forEach {
            val key = UUID.randomUUID().toString()
            it.key = key
            it.sectionStateCallback = sectionStateCallback
            sections[key] = FilterableSectionDao(it as FilterableSection<Nothing, Nothing, Nothing>)
        }
    }
}