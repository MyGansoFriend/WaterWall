package com.luckylittlesparrow.srvlist.example.state

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.luckylittlesparrow.srvlist.example.R
import com.luckylittlesparrow.srvlist.example.listsample.ItemsFactory
import com.luckylittlesparrow.srvlist.recycler.filterable.FilterableSectionedAdapter
import com.luckylittlesparrow.srvlist.recycler.section.ItemBundle
import com.luckylittlesparrow.srvlist.recycler.state.SectionState
import kotlinx.android.synthetic.main.fragment_simple_list.simpleRecyclerView
import kotlinx.android.synthetic.main.fragment_state_list.*

class StateListFragment : Fragment() {

    private val sectionAdapter = FilterableSectionedAdapter()
    private val section = StateSection()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_state_list, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //  section.state = SectionState.EMPTY
        sectionAdapter.addSection(section)
        //
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
    }

    private fun initListeners() {
        simpleRecyclerView.adapter = sectionAdapter
        simpleRecyclerView.layoutManager = LinearLayoutManager(context)

        stateFailedButton.setOnClickListener { section.state = SectionState.FAILED }
        stateLoadingButton.setOnClickListener { section.state = SectionState.LOADING }
        stateLoadedButton.setOnClickListener {
            section.state = SectionState.LOADED
            section.addMoreItems(ItemBundle(contentItems = ItemsFactory.getNumbersList()))
        }
        stateEmptyButton.setOnClickListener { section.state = SectionState.EMPTY }
    }
}