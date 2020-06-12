package com.tapin.tapin.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tapin.tapin.R
import com.tapin.tapin.model.CardInfo
import com.tapin.tapin.model.OrderSummaryInfo
import com.tapin.tapin.model.OrderedInfo
import com.tapin.tapin.model.resturants.Business
import com.tapin.tapin.utils.Utils

class ConfirmationAdapter(
    private val business: Business,
    private val orderSummaryInfo: OrderSummaryInfo,
    private val orderId: Int,
    private val rewardPoint: Int,
    private val cardInfo: CardInfo
) : RecyclerView.Adapter<ConfirmationBaseViewHolder>() {
    companion object {
        const val TYPE_HEADER = 0
        const val TYPE_ORDER_ITEM = 1
        const val TYPE_FOOTER = 2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConfirmationBaseViewHolder {
        val inflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        return when(viewType) {
            TYPE_HEADER -> {
                ConfirmationHeaderViewHolder(
                    inflater.inflate(
                        R.layout.adapter_confirmation_header,
                        parent,
                        false
                    )
                )
            }
            TYPE_ORDER_ITEM -> {
                ConfirmationOrderViewHolder(
                    inflater.inflate(
                        R.layout.adapter_confirmation_items,
                        parent,
                        false
                    )
                )
            }
            TYPE_FOOTER -> {
                ConfirmationFooterViewHolder(
                    inflater.inflate(
                        R.layout.adapter_confirmation_footer,
                        parent,
                        false
                    )
                )
            }
            else -> throw IllegalArgumentException("Please fix the onCreateViewHolder Types")
        }
    }

    override fun onBindViewHolder(holder: ConfirmationBaseViewHolder, position: Int) {
        when(holder) {
            is ConfirmationHeaderViewHolder -> holder.bind(businessName = business.shortName,  orderId = orderId)
            is ConfirmationOrderViewHolder -> holder.bind(orderSummaryInfo.listOrdered[position-1])
            is ConfirmationFooterViewHolder -> holder.bind(business, orderSummaryInfo, orderId, rewardPoint, cardInfo)
        }
    }

    override fun getItemCount() = orderSummaryInfo.listOrdered.size + 2

    override fun getItemViewType(position: Int) = when(position) {
        0 -> TYPE_HEADER
        orderSummaryInfo.listOrdered.size + 1 -> TYPE_FOOTER
        else -> TYPE_ORDER_ITEM
    }
}

sealed class ConfirmationBaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

class ConfirmationHeaderViewHolder(itemView: View): ConfirmationBaseViewHolder(itemView) {
    private val textView = itemView.findViewById<TextView>(R.id.tvOrderId)

    fun bind(businessName: String, orderId: Int) {
        textView.text = "$businessName thanks you for order #$orderId"
    }
}

class ConfirmationOrderViewHolder(itemView: View): ConfirmationBaseViewHolder(itemView) {
    private val textViewQty = itemView.findViewById<TextView>(R.id.qty)
    private val textViewName = itemView.findViewById<TextView>(R.id.item_name)
    private val textViewDescription = itemView.findViewById<TextView>(R.id.item_description)
    private val textViewPrice = itemView.findViewById<TextView>(R.id.item_price)

    fun bind(orderedInfo: OrderedInfo) {
        textViewQty.text = "${orderedInfo.quantity}"
        textViewName.text = orderedInfo.product_name
        textViewDescription.text = orderedInfo.product_description
        textViewPrice.text = "$ ${String.format("%.2f", (orderedInfo.quantity * orderedInfo.price))}"
    }
}

class ConfirmationFooterViewHolder(itemView: View): ConfirmationBaseViewHolder(itemView) {
    private val tvOrder = itemView.findViewById<TextView>(R.id.tvOrder)
    private val tvTotal = itemView.findViewById<TextView>(R.id.tvTotal)
    private val tvCardDetail = itemView.findViewById<TextView>(R.id.tvCardDetail)
    private val tvAverageWait = itemView.findViewById<TextView>(R.id.tvAverageWait)
    private val tvRewardPoints = itemView.findViewById<TextView>(R.id.tvRewardPoints)
    private val tvRedeemedPoints = itemView.findViewById<TextView>(R.id.tvRedeemedPoints)

    fun bind(business: Business, orderSummaryInfo: OrderSummaryInfo, orderId: Int, rewardPoint: Int, cardInfo: CardInfo) {
        tvOrder.text = "$orderId"

        tvTotal.text = "" + business.currSymbol + String.format(
            "%.2f",
            orderSummaryInfo.total
        )

        tvAverageWait.text = orderSummaryInfo.averageWaitTime

        tvRewardPoints.text = "You Earned $rewardPoint Reward Points!"

        tvRedeemedPoints.text = "You Redeemed " + orderSummaryInfo.points_redeemed + " Reward Points!"

        val cardExpiryDate =
            Utils.convertTime("yyyy-MM-dd", "MM/yy", cardInfo.expiration_date)

        tvCardDetail.text = cardInfo.card_type + " xxxx xxxx xxxx " + cardInfo.cc_no.substring(
            cardInfo.cc_no.length - 4
        ) + " " + cardExpiryDate
    }
}