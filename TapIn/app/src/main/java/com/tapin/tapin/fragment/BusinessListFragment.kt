package com.tapin.tapin.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView

import com.tapin.tapin.R
import com.tapin.tapin.adapter.BusinessListAdapter
import com.tapin.tapin.adapter.MarketListAdapter
import com.tapin.tapin.callbacks.Communication
import com.tapin.tapin.model.business.AllBusiness
import com.tapin.tapin.model.business.Business
import com.tapin.tapin.model.market.AllMarkets
import com.tapin.tapin.model.market.Market
import com.tapin.tapin.viewmodels.AppViewModelProvider
import com.tapin.tapin.viewmodels.BusinessListViewModel
import com.tapin.tapin.viewmodels.MarketListViewModel

class BusinessListFragment : Fragment() {
    companion object {
        private const val IDS = "ids"

        fun businessListFragment(ids: String) = BusinessListFragment().also { fragment ->
            fragment.arguments = Bundle().also { bundle ->
                bundle.putString(IDS, ids)
            }
        }
    }

    private var communication: Communication? = null

    private lateinit var businessListViewModel: BusinessListViewModel

    private val businessAdapter = BusinessListAdapter {
        Toast.makeText(requireContext(), "$it", Toast.LENGTH_LONG).show()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        communication = context as Communication
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val factory = AppViewModelProvider.provideBusinessListViewModel(requireContext(), arguments?.getString(IDS) ?: "")
        businessListViewModel =
            ViewModelProvider(viewModelStore, factory).get(BusinessListViewModel::class.java)

        return inflater.inflate(R.layout.fragment_business_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView: RecyclerView? = view.findViewById(R.id.recycler_view)
        recyclerView?.run {
            this.setHasFixedSize(true)
            this.adapter = businessAdapter
        }

        businessListViewModel.allBusinessLiveData.observe(viewLifecycleOwner, Observer { result ->
            businessAdapter.submitList(result.getOrDefault(
                AllBusiness(
                    data = emptyList(),
                    status = -1
                )
            ).data)
        })
    }

    override fun onDetach() {
        super.onDetach()

        communication = null
    }
}
