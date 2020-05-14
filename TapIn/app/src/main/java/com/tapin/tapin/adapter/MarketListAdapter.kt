package com.tapin.tapin.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tapin.tapin.R
import com.tapin.tapin.model.business.Business
import com.tapin.tapin.model.market.Market
import kotlinx.android.synthetic.main.item_market_list.view.*

class MarketListAdapter(private val callback: (Market) -> Unit): RecyclerView.Adapter<MarketViewHolder>() {
    private val items: MutableList<Market> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarketViewHolder {
        return MarketViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_market_list, parent, false))
    }

    override fun onBindViewHolder(holder: MarketViewHolder, position: Int) {
        holder.bind(items[position], callback)
    }

    override fun getItemCount(): Int = items.size

    fun submitList(list: List<Market>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }
}


class MarketViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    fun bind(market: Market, callback: (Market) -> Unit) {
        itemView.market_name.text = market.corpName
        itemView.market_description.text = market.marketingStatement
        itemView.market_address.text = market.address
        itemView.market_cut_off_time_value_text.text = market.cutoffTime

        itemView.setOnClickListener {
            callback.invoke(market)
        }
    }
}