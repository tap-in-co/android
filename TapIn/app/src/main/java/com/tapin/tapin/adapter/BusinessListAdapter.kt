package com.tapin.tapin.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tapin.tapin.R
import com.tapin.tapin.model.resturants.Business
import com.tapin.tapin.utils.UrlGenerator
import kotlinx.android.synthetic.main.item_business_list.view.*
import kotlinx.android.synthetic.main.item_market_list.view.*

class BusinessListAdapter(private val callback: (Business) -> Unit): RecyclerView.Adapter<BusinessViewHolder>() {
    private val items: MutableList<Business> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BusinessViewHolder {
        return BusinessViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_business_list, parent, false))
    }

    override fun onBindViewHolder(holder: BusinessViewHolder, position: Int) {
        holder.bind(items[position], callback)
    }

    override fun getItemCount(): Int = items.size

    fun submitList(list: List<Business>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }
}


class BusinessViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    fun bind(business: Business, callback: (Business) -> Unit) {
        itemView.business_name.text = business.name
        itemView.business_description.text = business.description

        kotlin.runCatching {
            val url = UrlGenerator.getBusinessURL(business.icon)
            Glide.with(itemView.context).load(url).placeholder(R.color.gray).into(itemView.business_image)
        }

        itemView.setOnClickListener {
            callback.invoke(business)
        }
    }
}