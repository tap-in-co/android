package com.tapin.tapin.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tapin.tapin.R
import com.tapin.tapin.activity.HomeActivity
import com.tapin.tapin.adapter.ConfirmationAdapter
import com.tapin.tapin.model.CardInfo
import com.tapin.tapin.model.OrderSummaryInfo
import com.tapin.tapin.utils.Constant

class ConfirmationFragmentNew : BaseFragment() {
    companion object {
        fun getConfirmationFragment(
            orderSummaryInfo: OrderSummaryInfo,
            orderId: Int,
            rewardPoint: Int,
            cardInfo: CardInfo
        ) : ConfirmationFragmentNew {
            val bundle = Bundle().also {
                it.putSerializable("ORDER_SUMMARY", orderSummaryInfo)
                it.putInt("ORDER_ID", orderId)
                it.putInt("REWARD_POINTS", rewardPoint)
                it.putSerializable("CARDINFO", cardInfo)
            }

            return ConfirmationFragmentNew().also {
                it.arguments = bundle
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_confirmation_new, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initHeader()
        initRecyclerView()
    }

    private fun initHeader() {
        view?.let { view ->
            (view.findViewById<View>(R.id.tvToolbarTitle) as TextView).text =
                getString(R.string.confirmation)
            val tvToolbarLeft = view.findViewById<TextView>(R.id.tvToolbarLeft)
            tvToolbarLeft.visibility = View.VISIBLE
            tvToolbarLeft.text = "Done"
            tvToolbarLeft.setOnClickListener {
                val fm = requireActivity().supportFragmentManager
                for (i in 0 until fm.backStackEntryCount) {
                    fm.popBackStack()
                }
                (activity as HomeActivity?)?.clearOrders()
            }
        }
    }

    private fun initRecyclerView() {
        view?.let {  view ->
            val orderSummaryInfo: OrderSummaryInfo? = arguments?.getSerializable("ORDER_SUMMARY") as OrderSummaryInfo
            val orderId: Int = arguments?.getInt("ORDER_ID") ?: 0
            val rewardPoint: Int = arguments?.getInt("REWARD_POINTS") ?: 0
            val cardInfo: CardInfo? = arguments?.getSerializable("CARDINFO") as CardInfo

            if (Constant.business != null && orderSummaryInfo != null && cardInfo != null) {
                val adapter = ConfirmationAdapter(Constant.business, orderSummaryInfo, orderId, rewardPoint, cardInfo)

                val recyclerView = view.findViewById<RecyclerView>(R.id.confirmation_recycler_view)
                recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                recyclerView.adapter = adapter
            }
        }
    }
}