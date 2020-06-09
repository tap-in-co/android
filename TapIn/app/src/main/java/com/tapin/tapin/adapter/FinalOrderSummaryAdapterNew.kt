package com.tapin.tapin.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.tapin.tapin.R
import com.tapin.tapin.model.OrderedInfo
import com.tapin.tapin.model.resturants.Business

class FinalOrderSummaryAdapterNew(val business: Business): RecyclerView.Adapter<FinalOrderSummaryViewHolder>() {
    private val items: MutableList<OrderedInfo> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FinalOrderSummaryViewHolder {
        return FinalOrderSummaryViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_final_order_list, parent, false))
    }

    override fun onBindViewHolder(holder: FinalOrderSummaryViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    fun submitList(list: List<OrderedInfo>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }
}


class FinalOrderSummaryViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    fun bind(orderInfo: OrderedInfo) {
        Toast.makeText(itemView.context, "Order : ${orderInfo.product_id}", Toast.LENGTH_SHORT).show()
    }
}