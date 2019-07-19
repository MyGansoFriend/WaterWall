package com.luckylittlesparrow.waterwall.example.expand

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.luckylittlesparrow.waterwall.example.ItemDecoration
import com.luckylittlesparrow.waterwall.example.R
import com.luckylittlesparrow.waterwall.example.listsample.Item
import com.luckylittlesparrow.waterwall.example.listsample.ItemsFactory
import com.luckylittlesparrow.waterwall.recycler.section.ItemBundle
import com.luckylittlesparrow.waterwall.recycler.section.ItemContainer
import com.luckylittlesparrow.waterwall.recycler.section.StubSection
import com.luckylittlesparrow.waterwall.recycler.simple.SimpleSectionedAdapter
import com.luckylittlesparrow.waterwall.recycler.state.SectionState
import kotlinx.android.synthetic.main.fragment_simple_list.simpleRecyclerView
import kotlinx.android.synthetic.main.fragment_state_list.*

class ExpandableListFragment : Fragment() {

    private val clickListener = { item: Item ->
        Toast.makeText(context, item.toString(), Toast.LENGTH_SHORT).show()
    }

    private val headerClickListener = { item: ExpandableHeader ->
        Toast.makeText(context, item.toString(), Toast.LENGTH_SHORT).show()
    }

    private val sectionAdapter = SimpleSectionedAdapter()
    private val section = ExpandableSection(ExpandableHeader("HEADER"), clickListener, headerClickListener)
    private val section2 = ExpandableSection(ExpandableHeader("HEADER2"), clickListener, headerClickListener)
    private val section3 = ExpandableSection(ExpandableHeader("HEADER3"), clickListener, headerClickListener)
    private val section4 = ExpandableSection(ExpandableHeader("HEADER4"), clickListener, headerClickListener)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_expand_list, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        val stubSection=StubSection(
//            emptyLayoutId = R.layout.section_empty
//        )
//        stubSection.state = SectionState.EMPTY
//        sectionAdapter.addSection(
//            stubSection
//        )
        sectionAdapter.addSection(section)
        sectionAdapter.addSection(section2)
        sectionAdapter.addSection(section3)
        sectionAdapter.addSection(section4)
        section.addMoreItems(ItemBundle(contentItems = ItemsFactory.getNumbersList()))
        section.addMoreItems(ItemBundle(contentItems = ItemsFactory.getNumbersList()))
        section2.addMoreItems(ItemBundle(contentItems = ItemsFactory.getNumbersList()))
        section2.addMoreItems(ItemBundle(contentItems = ItemsFactory.getNumbersList()))
        section2.addMoreItems(ItemBundle(contentItems = ItemsFactory.getNumbersList()))
        section2.addMoreItems(ItemBundle(contentItems = ItemsFactory.getNumbersList()))
        section3.addMoreItems(ItemBundle(contentItems = ItemsFactory.getNames()))
        section3.addMoreItems(ItemBundle(contentItems = ItemsFactory.getNames()))
        section4.addMoreItems(ItemBundle(contentItems = ItemsFactory.getSecondEvents()))
        section4.addMoreItems(ItemBundle(contentItems = ItemsFactory.getSecondEvents()))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()

    }

    private fun initListeners() {
        sectionAdapter.supportStickyHeader = true
        simpleRecyclerView.adapter = sectionAdapter
        simpleRecyclerView.layoutManager = LinearLayoutManager(context)

        simpleRecyclerView.addItemDecoration(ItemDecoration(ColorDrawable(resources.getColor(R.color.black))))

        stateFailedButton.setOnClickListener { section.state = SectionState.FAILED }
        stateLoadingButton.setOnClickListener { section.state = SectionState.LOADING }
        stateLoadedButton.setOnClickListener { section.state = SectionState.LOADED }
        stateEmptyButton.setOnClickListener { section.state = SectionState.EMPTY }
    }
}