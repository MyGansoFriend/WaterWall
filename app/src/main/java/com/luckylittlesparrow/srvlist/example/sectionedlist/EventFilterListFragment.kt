package com.luckylittlesparrow.srvlist.example.sectionedlist

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.luckylittlesparrow.srvlist.example.R
import com.luckylittlesparrow.srvlist.example.listsample.ItemsFactory
import com.luckylittlesparrow.srvlist.recycler.filterable.FilterableSectionedAdapter
import kotlinx.android.synthetic.main.fragment_filter_list.*

class EventFilterListFragment : Fragment() {

    private val sectionAdapter = FilterableSectionedAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_filter_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
        val filterRecyclerView=view.findViewById<RecyclerView>(R.id.filterRecyclerView)
        filterRecyclerView.adapter = sectionAdapter
        filterRecyclerView.layoutManager = LinearLayoutManager(context)


        val section1 = EventSection(
            EventHeader("today"), ItemsFactory.getNames(),
            EventFooter("today footer")
        )

        val section2 = EventSection(
            EventHeader("yesterday"), ItemsFactory.getNumbersList(),
            EventFooter("yesterday footer")
        )

        sectionAdapter.addSections(listOf(section1,section2))
    }

    private fun initListeners() {

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