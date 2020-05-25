package com.tapin.tapin.adapter

import android.content.Intent
import android.net.Uri
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
        itemView.distance_text_view.text = "100mi"
        itemView.market_link.text = market.website
        itemView.market_cut_off_time_value_text.text = "${market.cutoffDate} ${market.cutoffTime}"
        itemView.market_pick_up_date_value.text = "${market.pickupDate} ${market.driverPickupTime}"

        itemView.market_link.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW).also {
                it.data = Uri.parse(market.website)
            }
            itemView.context.startActivity(intent)
        }

        itemView.setOnClickListener {
            callback.invoke(market)
        }
    }
}