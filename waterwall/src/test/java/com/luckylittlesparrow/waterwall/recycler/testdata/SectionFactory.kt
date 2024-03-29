package com.luckylittlesparrow.waterwall.recycler.testdata

import com.luckylittlesparrow.waterwall.recycler.base.SectionDao
import com.luckylittlesparrow.waterwall.recycler.filterable.FilterableSection
import com.luckylittlesparrow.waterwall.recycler.filterable.FilterableSectionDao
import com.luckylittlesparrow.waterwall.recycler.section.Section
import com.luckylittlesparrow.waterwall.recycler.section.SectionInitTest
import com.luckylittlesparrow.waterwall.recycler.section.SectionParams
import com.luckylittlesparrow.waterwall.recycler.simple.SimpleSectionDao
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.LinkedHashMap

object SectionFactory {
    const val itemResourceId = 1
    const val headerResourceId = 2
    const val footerResourceId = 3
    const val failedResourceId = 4
    const val loadingResourceId = 5
    const val emptyResourceId = 6

    fun getSection(): Section<*, *, *> {
        val sectionParameters = SectionParams.builder()
            .headerResourceId(SectionInitTest.headerResourceId)
            .itemResourceId(SectionInitTest.itemResourceId)
            .footerResourceId(SectionInitTest.footerResourceId)
            .failedResourceId(SectionInitTest.failedResourceId)
            .loadingResourceId(SectionInitTest.loadingResourceId)
            .emptyResourceId(SectionInitTest.emptyResourceId)
            .build()

        val section = object : TestSection(
            TestItemsFactory.header,
            TestItemsFactory.getNames(),
            TestItemsFactory.footer
        ) {
            override fun getSectionParams(): SectionParams {
                return sectionParameters
            }
        }
        section.sectionKey = UUID.randomUUID().toString()
        return section
    }

    fun getSectionList(): List<Section<*, *, *>> {
        val list = ArrayList<Section<*, *, *>>()
        for (i in 1 until 100) {
            list.add(getSection())
        }
        return list
    }

    internal fun getDAOSectionList(): MutableMap<String, SimpleSectionDao<*, *, *>> {
        val list = LinkedHashMap<String, SimpleSectionDao<*, *, *>>()
        for (i in 1 until 100) {
            val section = getSection()
            list[section.sectionKey!!] = SimpleSectionDao(section)
        }
        return list
    }

    fun getFilterableSection(): Section<*, *, *> {
        val sectionParameters = SectionParams.builder()
            .headerResourceId(SectionInitTest.headerResourceId)
            .itemResourceId(SectionInitTest.itemResourceId)
            .footerResourceId(SectionInitTest.footerResourceId)
            .failedResourceId(SectionInitTest.failedResourceId)
            .loadingResourceId(SectionInitTest.loadingResourceId)
            .emptyResourceId(SectionInitTest.emptyResourceId)
            .build()

        val section = object : FilterTestSection(
            TestItemsFactory.header,
            TestItemsFactory.getNames(),
            TestItemsFactory.footer
        ) {
            override fun getSectionParams(): SectionParams {
                return sectionParameters
            }
        }
        section.sectionKey = UUID.randomUUID().toString()
        return section
    }

    fun getFilterableSectionWithoutHeader(): Section<*, *, *> {
        val sectionParameters = SectionParams.builder()
            .itemResourceId(SectionInitTest.itemResourceId)
            .failedResourceId(SectionInitTest.failedResourceId)
            .loadingResourceId(SectionInitTest.loadingResourceId)
            .emptyResourceId(SectionInitTest.emptyResourceId)
            .build()

        val section = object : FilterTestSection(
            contentItems = TestItemsFactory.getNames()
        ) {
            override fun getSectionParams(): SectionParams {
                return sectionParameters
            }
        }
        section.sectionKey = UUID.randomUUID().toString()
        return section
    }

    fun getFilterableSectionList(): List<Section<*, *, *>> {
        val list = ArrayList<Section<*, *, *>>()
        for (i in 1 until 100) {
            list.add(getFilterableSection())
        }
        return list
    }

    internal fun getFilterableDAOSectionList(): MutableMap<String, SectionDao<Nothing, Nothing, Nothing>> {
        val list = LinkedHashMap<String, SectionDao<Nothing, Nothing, Nothing>>()
        for (i in 1 until 100) {
            val section = getFilterableSection() as FilterableSection<Nothing, Nothing, Nothing>
            list[section.sectionKey!!] = FilterableSectionDao(section)
        }
        return list
    }
}