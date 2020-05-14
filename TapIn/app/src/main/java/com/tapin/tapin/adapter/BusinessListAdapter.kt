package com.tapin.tapin.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tapin.tapin.R
import com.tapin.tapin.model.business.Business
import kotlinx.android.synthetic.main.item_business_list.view.*

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
        itemView.business_description.text = business.marketingStatement
        itemView.business_address.text = business.address
        itemView.business_cut_off_time_value_text.text = business.cutoffTime

        itemView.setOnClickListener {
            callback.invoke(business)
        }
    }
}