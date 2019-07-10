package com.luckylittlesparrow.srvlist.example.defaultrv

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.luckylittlesparrow.srvlist.example.R
import com.luckylittlesparrow.srvlist.example.listsample.ItemsFactory
import kotlinx.android.synthetic.main.fragment_defautl_list.*

class DefaultRVFragment : Fragment() {

    private val sectionAdapter = SimpleAdapter(ItemsFactory.getNumbersList())


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_defautl_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
    }

    private fun initListeners() {
     //   defaultRecyclerView.addItemDecoration(SectionItemDecoration(resources.getDrawable(R.drawable.divider_line)))

        defaultRecyclerView.adapter = sectionAdapter
        defaultRecyclerView.layoutManager = LinearLayoutManager(context)
        // simpleRecyclerView.addItemDecoration(SectionItemDecoration(ColorDrawable(resources.getColor(R.color.black))))
    }
}