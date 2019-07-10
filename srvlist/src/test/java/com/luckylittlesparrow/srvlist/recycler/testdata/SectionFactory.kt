package com.luckylittlesparrow.srvlist.recycler.testdata

import com.luckylittlesparrow.srvlist.recycler.section.Section
import com.luckylittlesparrow.srvlist.recycler.section.SectionInitTest
import com.luckylittlesparrow.srvlist.recycler.section.SectionParams

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

        return object : TestSection(
            TestItemsFactory.header,
            TestItemsFactory.getNames(),
            TestItemsFactory.footer
        ) {
            override fun getSectionParams(): SectionParams {
                return sectionParameters
            }
        }
    }

    fun getSectionList(): List<Section<*, *, *>> {
        val list = ArrayList<Section<*, *, *>>()
        for (i in 1 until 100) {
            list.add(getSection())
        }
        return list
    }
}