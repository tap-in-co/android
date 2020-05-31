package com.tapin.tapin.adapter

import android.content.Intent
import android.location.Location
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tapin.tapin.R
import com.tapin.tapin.model.market.Market
import com.tapin.tapin.utils.UrlGenerator.getImageBaseApi
import com.tapin.tapin.utils.UrlGenerator.getImageIconsBaseApi
import com.tapin.tapin.utils.UrlGenerator.getLogoURL
import kotlinx.android.synthetic.main.item_market_list.view.*
import java.lang.Exception

class MarketListAdapter(private val callback: (Market) -> Unit): RecyclerView.Adapter<MarketViewHolder>() {
    private val items: MutableList<Market> = mutableListOf()
    private var location: Location? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarketViewHolder {
        return MarketViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_market_list, parent, false))
    }

    override fun onBindViewHolder(holder: MarketViewHolder, position: Int) {
        holder.bind(location, items[position], callback)
    }

    override fun getItemCount(): Int = items.size

    fun submitList(location: Location?, list: List<Market>) {
        this.location = location
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }
}


class MarketViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    fun bind(location: Location?, market: Market, callback: (Market) -> Unit) {
        itemView.market_name.text = market.corpName
        itemView.market_description.text = market.marketingStatement
        itemView.market_address.text = market.address
        itemView.market_cut_off_time_value_text.text = market.cutoffTime
        itemView.market_link.text = market.website
        itemView.market_cut_off_time_value_text.text = "${market.cutoffDate} ${market.cutoffTime}"
        itemView.market_pick_up_date_value.text = "${market.pickupDate} ${market.driverPickupTime}"
        itemView.market_pickup_at_value_text.text = market.deliveryLocation
        itemView.market_business_hours_value_text.text = market.marketOpenHours

        kotlin.runCatching {
            if (market.lat.isNotEmpty() && market.lng.isNotEmpty() && location != null) {
                val locationTo = Location("").apply {
                    this.latitude = market.lat.toDouble()
                    this.longitude = market.lng.toDouble()
                }
                return@runCatching location.distanceInMiles(locationTo)
            } else {
                return@runCatching "N/A"
            }
        }.onSuccess {
            itemView.distance_text_view.text = "$it"
        }.onFailure {
            itemView.distance_text_view.text = "N/A"
        }

        kotlin.runCatching {
            val images = market.pictures.split("\\s*,\\s*")
            val url = getLogoURL(images[0])
            Glide.with(itemView.context).load(url).placeholder(R.color.gray).into(itemView.market_image)
        }

        kotlin.runCatching {
            val url = getLogoURL(market.logo)
            Glide.with(itemView.context).load(url).placeholder(R.color.gray).into(itemView.logo_image_view)
        }


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

fun Location.distanceInMiles(toLocation: Location) = try {
    val distance = this.distanceTo(toLocation) * 0.000621371192
    "${distance.toInt()} mi"
} catch (e: Exception) {
    "N/A"
}