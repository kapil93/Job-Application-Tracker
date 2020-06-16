package com.kapil.presentation.offers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.kapil.presentation.R
import com.kapil.presentation.common.ErrorView
import com.kapil.presentation.common.LoadingView
import com.kapil.presentation.home.BottomNavFragment
import kotlinx.android.synthetic.main.error_view.*
import kotlinx.android.synthetic.main.error_view.view.*
import kotlinx.android.synthetic.main.loading_view.*
import kotlinx.android.synthetic.main.offers_fragment.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class OffersFragment : BottomNavFragment(), LoadingView, ErrorView {

    override val viewModel: OffersViewModel by viewModel()

    override fun getTitle() = R.string.label_offers_screen

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.observeOfferList()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.offers_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        offersListView.adapter = OfferListAdapter()

        viewModel.offerListViewEntity.observe(viewLifecycleOwner, Observer(::updateViewEntity))
        viewModel.isLoading.observe(viewLifecycleOwner, Observer(::updateLoadingView))
        viewModel.isError.observe(viewLifecycleOwner, Observer {
            updateErrorView(it.first, it.second)
        })
    }

    override fun onResume() {
        super.onResume()
        initSortButtonListeners { viewModel.onSortOrderChanged(it) }
    }

    override fun onPause() {
        super.onPause()
        removeSortBtnListeners()
    }

    private fun updateViewEntity(offerListViewEntity: OfferListViewEntity) {
        setSortOrderToSortBtns(offerListViewEntity.sortOrder)
        (offersListView.adapter as OfferListAdapter).submitList(offerListViewEntity.offerList)
        emptyListMsg.isVisible = offerListViewEntity.offerList.isEmpty()
        dojSortBtn.isVisible = offerListViewEntity.offerList.isNotEmpty()
        amountSortBtn.isVisible = offerListViewEntity.offerList.isNotEmpty()
    }

    private fun initSortButtonListeners(onSortOrderChanged: (SortOrder) -> Unit) {
        dojSortBtn.setOnCheckedChangeListener { btn, isChecked ->
            if (!btn.isSelected) btn.isChecked = !isChecked
            btn.isSelected = true
            amountSortBtn.isSelected = false
            onSortOrderChanged(getSortOrderFromSortBtns())
        }
        amountSortBtn.setOnCheckedChangeListener { btn, isChecked ->
            if (!btn.isSelected) btn.isChecked = !isChecked
            btn.isSelected = true
            dojSortBtn.isSelected = false
            onSortOrderChanged(getSortOrderFromSortBtns())
        }
    }

    private fun removeSortBtnListeners() {
        dojSortBtn.setOnCheckedChangeListener(null)
        amountSortBtn.setOnCheckedChangeListener(null)
    }

    private fun setSortOrderToSortBtns(sortOrder: SortOrder) {
        when (sortOrder) {
            SortOrder.DOJ_ASC -> {
                dojSortBtn.isSelected = true
                dojSortBtn.isChecked = true
                amountSortBtn.isSelected = false
            }
            SortOrder.DOJ_DESC -> {
                dojSortBtn.isSelected = true
                dojSortBtn.isChecked = false
                amountSortBtn.isSelected = false
            }
            SortOrder.AMOUNT_ASC -> {
                amountSortBtn.isSelected = true
                amountSortBtn.isChecked = true
                dojSortBtn.isSelected = false
            }
            SortOrder.AMOUNT_DESC -> {
                amountSortBtn.isSelected = true
                amountSortBtn.isChecked = false
                dojSortBtn.isSelected = false
            }
        }
    }

    private fun getSortOrderFromSortBtns() = when {
        dojSortBtn.isSelected && dojSortBtn.isChecked -> SortOrder.DOJ_ASC
        dojSortBtn.isSelected && !dojSortBtn.isChecked -> SortOrder.DOJ_DESC
        amountSortBtn.isSelected && amountSortBtn.isChecked -> SortOrder.AMOUNT_ASC
        amountSortBtn.isSelected && !amountSortBtn.isChecked -> SortOrder.AMOUNT_DESC
        // Any random value may go in else block as it is never expected to be executed
        else -> SortOrder.AMOUNT_DESC
    }

    override fun updateLoadingView(isLoading: Boolean) {
        loadingView.isVisible = isLoading
    }

    override fun updateErrorView(isError: Boolean, retry: (() -> Unit)) {
        errorView.isVisible = isError
        errorView.retryButton.setOnClickListener { retry() }
    }
}
