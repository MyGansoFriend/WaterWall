package com.luckylittlesparrow.waterwall.example.sectionedlist

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.luckylittlesparrow.waterwall.example.R
import com.luckylittlesparrow.waterwall.example.listsample.Item
import com.luckylittlesparrow.waterwall.example.listsample.ItemsFactory
import com.luckylittlesparrow.waterwall.recycler.filterable.FilterableSectionedAdapter
import kotlinx.android.synthetic.main.fragment_filter_list.*

class FilterListFragment : Fragment() {

    private val sectionAdapter = FilterableSectionedAdapter()

    private val clickListener = { item: Item ->
        Toast.makeText(context, item.toString(), Toast.LENGTH_SHORT).show()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_filter_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
        val filterRecyclerView = view.findViewById<RecyclerView>(R.id.filterRecyclerView)
        filterRecyclerView.adapter = sectionAdapter
        filterRecyclerView.layoutManager = LinearLayoutManager(context)



        sectionEmpty.isVisible = false


        sectionAdapter.addMoreSections(listOf(section1, section2, sectionEmpty))
    }

    val section1 = FilterSection(
        FilterHeader("today"), ItemsFactory.getNames(),
        FilterFooter("today footer")
    )

    val section2 = FilterSection(
        FilterHeader("yesterday"), ItemsFactory.getNumbersList(),
        FilterFooter("yesterday footer")
    )

    val sectionEmpty = EmptyFilterItemSection(
        ItemsFactory.getNumbersList()
    )

    var switch = false

    private fun initListeners() {
        emptyItemTextView.setOnClickListener {
            switch = !switch
            if (switch)
                sectionAdapter.changeShowedSectionToAnother(listOf(section1, section2), listOf(sectionEmpty))
            else sectionAdapter.changeShowedSectionToAnother(listOf(sectionEmpty), listOf(section1, section2))

        }
        searchViewEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(editable: Editable) {
                sectionAdapter.filter(editable.toString())
            }
        })
    }
}