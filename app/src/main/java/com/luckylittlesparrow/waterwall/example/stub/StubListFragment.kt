package com.luckylittlesparrow.waterwall.example.stub

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.luckylittlesparrow.waterwall.example.R
import com.luckylittlesparrow.waterwall.example.expand.ExpandableHeader
import com.luckylittlesparrow.waterwall.example.expand.ExpandableSection
import com.luckylittlesparrow.waterwall.example.listsample.ItemsFactory
import com.luckylittlesparrow.waterwall.recycler.section.ItemBundle
import com.luckylittlesparrow.waterwall.recycler.section.ItemContainer
import com.luckylittlesparrow.waterwall.recycler.section.StubSection
import com.luckylittlesparrow.waterwall.recycler.simple.SimpleSectionedAdapter
import com.luckylittlesparrow.waterwall.recycler.state.SectionState
import kotlinx.android.synthetic.main.fragment_expand_list.*
import kotlinx.android.synthetic.main.fragment_simple_list.simpleRecyclerView
import kotlinx.android.synthetic.main.fragment_state_list.stateEmptyButton
import kotlinx.android.synthetic.main.fragment_state_list.stateFailedButton
import kotlinx.android.synthetic.main.fragment_state_list.stateLoadedButton
import kotlinx.android.synthetic.main.fragment_state_list.stateLoadingButton

class StubListFragment : Fragment() {

    private val clickListener = { item: ItemContainer ->
        Toast.makeText(context, item.toString(), Toast.LENGTH_SHORT).show()
    }

    private val sectionAdapter = SimpleSectionedAdapter()
    private val section = ExpandableSection(ExpandableHeader("HEADER"), clickListener, clickListener)
    private val section2 =
        ExpandableSection(ExpandableHeader("HEADER"), clickListener, clickListener, ItemsFactory.getNumbersList())
    private val stubSection = StubSection(
        R.layout.section_empty,
        R.layout.section_failed,
        R.layout.section_loading
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_expand_list, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        stubSection.state = SectionState.EMPTY
        sectionAdapter.addSection(stubSection)
        section.addMoreItems(ItemBundle(contentItems = ItemsFactory.getNumbersList()))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
    }

    private fun initListeners() {
        simpleRecyclerView.adapter = sectionAdapter
        simpleRecyclerView.layoutManager = LinearLayoutManager(context)

        stateFailedButton.setOnClickListener { stubSection.state = SectionState.FAILED }
        stateLoadingButton.setOnClickListener { stubSection.state = SectionState.LOADING }
        stateLoadedButton.setOnClickListener {
            sectionAdapter.addSection(section)
            sectionAdapter.addSection(section2)
        }
        stateEmptyButton.setOnClickListener { stubSection.state = SectionState.EMPTY }

        swipeContainer.setOnRefreshListener {
            section.submitItemsWithLoadedState(
                ItemBundle(
                    headerItem = ExpandableHeader("HEADER"),
                    contentItems = ItemsFactory.getNames()
                )
            )
            swipeContainer.isRefreshing = false
        }
    }
}