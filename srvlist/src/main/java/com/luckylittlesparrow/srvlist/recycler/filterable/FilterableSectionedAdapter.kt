package com.luckylittlesparrow.srvlist.recycler.filterable

import android.annotation.SuppressLint
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.DiffUtil
import com.luckylittlesparrow.srvlist.recycler.base.BaseListAdapter
import com.luckylittlesparrow.srvlist.recycler.section.ItemContainer
import com.luckylittlesparrow.srvlist.recycler.section.Section
import com.luckylittlesparrow.srvlist.recycler.state.SectionState

class FilterableSectionedAdapter : BaseListAdapter(), Filterable {

    init {
        sectionMediator = FilterableSectionMediator()
        //   initUtil()
    }

    override fun addSection(key: String, section: Section<*, *, *>) {
        check(section is FilterableSection) { "section must extends from FilterableSection in order to be used" }
        super.addSection(key, section)
    }

    override fun addSection(section: Section<*, *, *>): String {
        check(section is FilterableSection) { "section must extends from FilterableSection in order to be used" }
        return super.addSection(section)
    }

    override fun removeSection(section: Section<*, *, *>): Boolean {
        check(section is FilterableSection) { "section must extends from FilterableSection in order to be used" }
        return super.removeSection(section)
    }

    override fun addSections(list: List<Section<*, *, *>>) {
        check(list.isNotEmpty() && list[0] is FilterableSection) { "section must extends from FilterableSection in order to be used" }
        super.addSections(list)
    }

    /**
     * Simple reduce filter.filter(search) calling to just filter(search)
     */
    fun filter(constraint: CharSequence) {
        filter.filter(constraint)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun getFilter(): Filter {
        return object : Filter() {

            override fun publishResults(constraint: CharSequence, results: FilterResults) {
                if (results.values == null) notifyDataSetChanged()
                else dispatchUpdates((results.values as DiffUtil.DiffResult))
                recyclerView?.scrollToPosition(0)
            }

            override fun performFiltering(constraint: CharSequence): FilterResults {
                recyclerView?.stopScroll()
                val filterResult = FilterResults()

                val resultLists = ArrayList<Pair<List<ItemContainer>, List<ItemContainer>>>()

                for (section in sectionMediator.getSectionList()) {
                    if (section.value.state() == SectionState.LOADED) {
                        val result = section.value.filter(constraint)
                        result?.let { resultLists.add(result) }
                    }
                }

                if (resultLists.isEmpty()) return filterResult else {

                    val oldList = ArrayList<ItemContainer>()
                    val newList = ArrayList<ItemContainer>()

                    for (pair in resultLists) {
                        oldList.addAll(pair.first)
                        newList.addAll(pair.second)
                    }

                    diffListUtil.submitLists(oldList, newList)
                    filterResult.values = DiffUtil.calculateDiff(diffListUtil)
                }

                return filterResult
            }
        }
    }
}