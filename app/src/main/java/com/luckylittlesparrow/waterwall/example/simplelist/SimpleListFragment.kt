package com.luckylittlesparrow.waterwall.example.simplelist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.luckylittlesparrow.waterwall.example.R
import com.luckylittlesparrow.waterwall.example.simplelist.ExampleItem.ExampleType
import com.luckylittlesparrow.waterwall.recycler.section.ItemContainer
import com.luckylittlesparrow.waterwall.recycler.simple.SimpleSectionedAdapter
import kotlinx.android.synthetic.main.fragment_simple_list.*

class SimpleListFragment : Fragment() {

    private val sectionAdapter = SimpleSectionedAdapter()

    private val clickListener = { item: ItemContainer ->
        when ((item as ExampleItem).type) {
            ExampleType.FILTER -> {
                findNavController().navigate(R.id.action_simpleListFragment_to_eventFilterListFragment)
            }
            ExampleType.STATES -> {
                findNavController().navigate(R.id.action_simpleListFragment_to_stateListFragment)
            }
            ExampleType.EXPAND -> {
                findNavController().navigate(R.id.action_simpleListFragment_to_expandableListFragment)
            }
            ExampleType.STUB -> {
                findNavController().navigate(R.id.action_simpleListFragment_to_stubListFragment)
            }
            ExampleType.DEFAULT -> {
                findNavController().navigate(R.id.action_simpleListFragment_to_defaultRVFragment)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val section = SimpleSection(clickListener, getExamples())
        sectionAdapter.setDefaultOptimizationSettings()
        sectionAdapter.addSection(section)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_simple_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
    }

    private fun initListeners() {
        simpleRecyclerView.adapter = sectionAdapter
        simpleRecyclerView.layoutManager = LinearLayoutManager(context)

      //  simpleRecyclerView.addItemDecoration(ItemDecoration(ColorDrawable(resources.getColor(R.color.black))))
    }

    private fun getExamples() = listOf(
        ExampleItem(ExampleType.FILTER, "List with filter"),
        ExampleItem(ExampleType.STATES, "List with states"),
        ExampleItem(ExampleType.EXPAND, "Expandable list"),
        ExampleItem(ExampleType.STUB, "Stub section list"),
        ExampleItem(ExampleType.DEFAULT, "DEFAULT list")
    )
}