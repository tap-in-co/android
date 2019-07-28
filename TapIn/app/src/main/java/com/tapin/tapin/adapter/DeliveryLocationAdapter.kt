package com.tapin.tapin.adapter

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.tapin.tapin.R
import com.tapin.tapin.model.CorporateDomain

class DeliveryLocationAdapter(
    private val corporateDomains: List<CorporateDomain>,
    private val listener: OnDeliveryLocationSelectionListener
) : RecyclerView.Adapter<DeliveryViewHolder>() {
    private var selectedIndex = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeliveryViewHolder {
        return DeliveryViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.adapter_corporate_location,
                parent,
                false
            )
        )
    }

    override fun getItemCount() = corporateDomains.size

    override fun onBindViewHolder(holder: DeliveryViewHolder, position: Int) {
        holder.bindLocation(
            corporateDomains[position],
            listener,
            position,
            selectedIndex == position
        )
    }

    fun markAsSelected(position: Int) {
        selectedIndex = position
        notifyDataSetChanged()
    }
}

class DeliveryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val view: View = itemView.findViewById(R.id.adapter_corporate_location_background)
    private val textView: AppCompatTextView =
        itemView.findViewById(R.id.adapter_corporate_location_text)

    fun bindLocation(
        corporateDomain: CorporateDomain,
        listener: OnDeliveryLocationSelectionListener,
        position: Int,
        showSelected: Boolean = false
    ) {
        if (showSelected) {
            textView.typeface = Typeface.DEFAULT_BOLD
        } else {
            textView.typeface = Typeface.DEFAULT
        }

        textView.text = corporateDomain.locationAbbr
        view.setOnClickListener {
            listener.onLocationSelected(position, corporateDomain)
        }
    }
}

interface OnDeliveryLocationSelectionListener {
    fun onLocationSelected(position: Int, corporateDomain: CorporateDomain)
}
