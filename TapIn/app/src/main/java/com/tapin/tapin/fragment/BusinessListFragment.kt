package com.tapin.tapin.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.tapin.tapin.R
import com.tapin.tapin.adapter.BusinessListAdapter
import com.tapin.tapin.model.business.AllBusiness
import com.tapin.tapin.model.market.Market
import com.tapin.tapin.utils.Constant
import com.tapin.tapin.viewmodels.AppViewModelProvider
import com.tapin.tapin.viewmodels.BusinessListViewModel
import java.lang.IllegalArgumentException

class BusinessListFragment : BaseTap4MarketFragment() {
    companion object {
        private const val MARKET = "market"

        fun businessListFragment(market: Market) = BusinessListFragment().also { fragment ->
            fragment.arguments = Bundle().also { bundle ->
                bundle.putSerializable(MARKET, market)
            }
        }
    }

    private lateinit var businessListViewModel: BusinessListViewModel

    private val businessAdapter = BusinessListAdapter {
        Constant.business = it
        communication?.onBusinessSelected(business = it)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (arguments?.getSerializable(MARKET) is Market) {
            val market = arguments?.getSerializable(MARKET) as Market
            val factory = AppViewModelProvider.provideBusinessListViewModel(requireContext(), market.merchantIds)
            businessListViewModel =
                ViewModelProvider(viewModelStore, factory).get(BusinessListViewModel::class.java)
        } else {
            throw IllegalArgumentException("Please pass Market value while constructing the fragment")
        }

        return inflater.inflate(R.layout.fragment_business_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<TextView>(R.id.tvToolbarLeft).apply {
            this.visibility = View.VISIBLE
            this.text = "Back"
            this.setOnClickListener { activity?.onBackPressed() }
        }

        view.findViewById<TextView>(R.id.tvToolbarTitle).apply {
            if (arguments?.getSerializable(MARKET) is Market) {
                val market = arguments?.getSerializable(MARKET) as Market
                this.text = market.domain
            }
        }

        val recyclerView: RecyclerView? = view.findViewById(R.id.recycler_view)
        recyclerView?.run {
            this.setHasFixedSize(true)
            this.adapter = businessAdapter
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        progressHUD?.show()
        businessListViewModel.allBusinessLiveData.observe(viewLifecycleOwner, Observer { result ->
            businessAdapter.submitList(result.getOrDefault(
                AllBusiness(
                    data = emptyList(),
                    status = -1
                )
            ).data)

            progressHUD?.dismiss()
        })
    }
}
