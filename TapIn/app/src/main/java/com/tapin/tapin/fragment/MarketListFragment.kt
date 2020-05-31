package com.tapin.tapin.fragment

import android.content.Context
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.tapin.tapin.R
import com.tapin.tapin.activity.BaseActivity
import com.tapin.tapin.adapter.MarketListAdapter
import com.tapin.tapin.callbacks.Communication
import com.tapin.tapin.model.market.AllMarkets
import com.tapin.tapin.utils.ProgressHUD
import com.tapin.tapin.viewmodels.AppViewModelProvider
import com.tapin.tapin.viewmodels.MarketListViewModel

class MarketListFragment : BaseTap4MarketFragment() {
    companion object {
        fun marketListFragment(): MarketListFragment = MarketListFragment()
    }

    private lateinit var marketListViewModel: MarketListViewModel

    private val marketAdapter = MarketListAdapter {
        communication?.onMarketSelected(market = it)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        communication = context as Communication
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val factory = AppViewModelProvider.provideMarketViewModelFactory(requireContext())
        marketListViewModel = ViewModelProvider(viewModelStore, factory).get(MarketListViewModel::class.java)

        return inflater.inflate(R.layout.fragment_market_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<TextView>(R.id.tvToolbarTitle).apply {
            this.text = "Tap4Markets"
        }

        val recyclerView: RecyclerView? = view.findViewById(R.id.recycler_view)
        recyclerView?.run {
            this.setHasFixedSize(true)
            this.adapter = marketAdapter
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        var location: Location? = null
        if (requireActivity() is BaseActivity) {
            location = ((requireActivity()) as BaseActivity).location
        }

        progressHUD?.show()
        marketListViewModel.allMarketLiveData.observe(viewLifecycleOwner, Observer { result ->
            marketAdapter.submitList(location, result.getOrDefault(AllMarkets(data = emptyList(), success = -1)).data)
            progressHUD?.dismiss()
        })
    }
}