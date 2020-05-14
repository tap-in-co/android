package com.tapin.tapin.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.tapin.tapin.R
import com.tapin.tapin.adapter.BusinessListAdapter
import com.tapin.tapin.adapter.MarketListAdapter
import com.tapin.tapin.callbacks.Communication
import com.tapin.tapin.model.market.AllMarkets
import com.tapin.tapin.model.market.Market
import com.tapin.tapin.viewmodels.AppViewModelProvider
import com.tapin.tapin.viewmodels.MarketListViewModel

class MarketListFragment : Fragment() {
    companion object {
        fun marketListFragment(): MarketListFragment = MarketListFragment()
    }

    private var communication: Communication? = null

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

        val recyclerView: RecyclerView? = view.findViewById(R.id.recycler_view)
        recyclerView?.run {
            this.setHasFixedSize(true)
            this.adapter = marketAdapter
        }

        marketListViewModel.allMarketLiveData.observe(viewLifecycleOwner, Observer { result ->
            marketAdapter.submitList(result.getOrDefault(AllMarkets(data = emptyList(), success = -1)).data)
        })
    }

    override fun onDetach() {
        super.onDetach()

        communication = null
    }
}