package com.luckylittlesparrow.waterwall.example

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer

import androidx.navigation.Navigation
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.luckylittlesparrow.waterwall.example.recent.TransactionDecoration
import com.luckylittlesparrow.waterwall.example.recent.TransactionSection
import com.luckylittlesparrow.waterwall.example.recent.TransactionViewModel
import com.luckylittlesparrow.waterwall.recycler.section.ItemBundle
import com.luckylittlesparrow.waterwall.recycler.section.StubSection
import com.luckylittlesparrow.waterwall.recycler.simple.SimpleSectionedAdapter
import com.luckylittlesparrow.waterwall.recycler.state.SectionState
import kotlinx.android.synthetic.main.transactions_layout.*

class MainActivity : AppCompatActivity() {

    private lateinit var listAdapter: SimpleSectionedAdapter

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<*>

    private lateinit var stubSection: StubSection

    //DON'T DO THAT, ITS JUST AN EXAMPLE OF LIST,NOT OF ARCHITECTURE
    private val viewModel = TransactionViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.nav_host)

        listAdapter = SimpleSectionedAdapter()

        listAdapter
            .supportStickyHeader(true)
            .into(transactionListRecyclerView)

        stubSection = StubSection(
            loadingLayoutId = R.layout.loading_layout,
            emptyLayoutId = R.layout.section_empty
        )

        bottomSheetBehavior =
            BottomSheetBehavior.from<View>(transactionListBottomSheetView)

        //listAdapter.into(transactionListRecyclerView)

        transactionListRecyclerView.adapter = listAdapter


        transactionListRecyclerView.addItemDecoration(
            TransactionDecoration(
                ColorDrawable(
                    resources.getColor(
                        R.color.black
                    )
                )
            )
        )

        initListeners()

        val navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        observeViewModel()
        viewModel.onStart()
    }

    private fun observeViewModel() {
        viewModel.transactions.observe(this, Observer<List<ItemBundle>> {
            showList(it)
        })
    }

    private fun showList(list: List<ItemBundle>) {
        if (list.isEmpty()) {
            stubSection.state = SectionState.EMPTY
        } else {

            val sections = ArrayList<TransactionSection>()
            list.forEach {
                sections.add(TransactionSection(it.headerItem, it.contentItems))
            }
            listAdapter.submitSections(sections)
        }
    }

    private fun initListeners() {
        transactionListUpdateTextView.setOnClickListener { viewModel.onRefresh() }
        transactionListTitleTextView.setOnClickListener { onRecentTextClicked() }
        transactionListCloseImageView.setOnClickListener { onCloseClicked() }
        bottomSheetBehavior.setBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(view: View, state: Int) {
                when (state) {
                    BottomSheetBehavior.STATE_EXPANDED -> transactionListCloseImageView.visibility =
                        View.VISIBLE
                    BottomSheetBehavior.STATE_COLLAPSED -> transactionListCloseImageView.visibility =
                        View.GONE
                }
            }

            override fun onSlide(view: View, v: Float) {

            }
        })
    }

    private fun onRecentTextClicked() {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    private fun onCloseClicked() {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }
}
