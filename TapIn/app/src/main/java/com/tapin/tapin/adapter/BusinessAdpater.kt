package com.tapin.tapin.adapter

import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RatingBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tapin.tapin.R
import com.tapin.tapin.activity.HomeActivity
import com.tapin.tapin.fragment.BusinessDetailFragment
import com.tapin.tapin.model.resturants.Business
import com.tapin.tapin.utils.Constant
import com.tapin.tapin.utils.UrlGenerator
import com.tapin.tapin.utils.Utils
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Narendra on 2/7/2017.
 */
class BusinessAdpater(internal var activity: FragmentActivity, internal var time: String) :
    RecyclerView.Adapter<BusinessAdpater.ViewHolder>() {

    private val businesses = ArrayList<Business>()
    private val mDataOriginal = ArrayList<Business>()
    private var mSelectedView: View? = null

    private var calendar = Calendar.getInstance()

    private var isLatLngAvailable: Boolean = false
    private var currentLat: Double = 0.toDouble()
    private var currentLng: Double = 0.toDouble()


    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {

        val viewHolder: ViewHolder
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.listitem_business, viewGroup, false)
        viewHolder = ViewHolder(v)
        viewHolder.itemView.isClickable = true
        viewHolder.itemView.setOnClickListener { view ->
            if (mSelectedView != null) {
                mSelectedView!!.isSelected = false
            }
            view.isSelected = true
            mSelectedView = view
        }
        return viewHolder

    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bindViewHolder(businesses[position])
    }

    override fun getItemCount(): Int {
        return businesses.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private var tvBusinessName: TextView =
            itemView.findViewById<View>(R.id.tvBusinessName) as TextView
        private var tvDescription: TextView =
            itemView.findViewById<View>(R.id.tvDescription) as TextView
        private var tvBusinessType: TextView =
            itemView.findViewById<View>(R.id.tvBusinessType) as TextView
        private var tvNeighbourhood: TextView =
            itemView.findViewById<View>(R.id.tvNeighbourhood) as TextView
        private var tvDistance: TextView = itemView.findViewById<View>(R.id.tvDistance) as TextView
        private var ratingBar: RatingBar = itemView.findViewById<View>(R.id.ratingBar) as RatingBar
        private var ivBusinessIcon: ImageView =
            itemView.findViewById<View>(R.id.ivBusinessIcon) as ImageView
        private var card_view: LinearLayout =
            itemView.findViewById<View>(R.id.card_view) as LinearLayout
        private var tvOpenStatus: TextView =
            itemView.findViewById<View>(R.id.tvOpenStatus) as TextView
        private var tvOpeningTime: TextView =
            itemView.findViewById<View>(R.id.tvOpeningTime) as TextView

        @SuppressLint("SimpleDateFormat", "SetTextI18n")
        fun bindViewHolder(business: Business) {
            tvBusinessName.text = business.name
            tvDescription.text = business.marketingStatement
            if (business.businessTypes.isNotEmpty() && business.businessTypes != "null") {
                tvBusinessType.text = business.businessTypes
            }
            tvNeighbourhood.text = business.neighborhood

            if (isLatLngAvailable) {

                val crntLocation = Location("crntlocation")
                crntLocation.latitude = currentLat
                crntLocation.longitude = currentLng

                val newLocation = Location("newlocation")
                newLocation.latitude = java.lang.Double.parseDouble(business.lat)
                newLocation.longitude = java.lang.Double.parseDouble(business.lng)

                tvDistance.text =
                    String.format("%.2f mi", crntLocation.distanceTo(newLocation) / 1609.344)

            }

            business.icon?.let {
                Log.e("IMAGE_URL", "" + UrlGenerator.getImageIconsBaseApi() + business.icon)
                Glide.with(ivBusinessIcon.context)
                    .load(UrlGenerator.getImageIconsBaseApi() + business.icon)
                    .into(ivBusinessIcon)
            }

            // opening_time & closing_time is not applicable for Corp Order
            if (business.openingTime != null && business.closingTime != null) {
                try {

                    val df = SimpleDateFormat("HH:mm:ss")
                    val currentTime = df.format(calendar.time)

                    if (!Utils.isTimeBetweenTwoTime(
                            business.openingTime,
                            business.closingTime,
                            currentTime
                        )
                    ) {
                        tvOpenStatus.text = "NOW CLOSED"
                        tvOpenStatus.setTextColor(
                            ContextCompat.getColor(
                                tvOpenStatus.context,
                                R.color.gray
                            )
                        )
                        tvOpeningTime.text = ""
                    } else {
                        tvOpenStatus.text = "OPEN NOW"
                        tvOpenStatus.setTextColor(
                            ContextCompat.getColor(
                                tvOpenStatus.context,
                                R.color.colorPrimary
                            )
                        )
                        tvOpeningTime.text =
                            "" + Utils.getOpenTime(
                                business.openingTime,
                                business.closingTime
                            )
                    }

                } catch (e: ParseException) {
                    e.printStackTrace()
                }

            }
            ratingBar.rating = java.lang.Float.parseFloat(business.rating)

            card_view.setOnClickListener {
                val businessDetailFragment = BusinessDetailFragment()
                val bundle = Bundle()
                Constant.listOrdered.clear()
                Constant.business = businesses[adapterPosition]
                businessDetailFragment.arguments = bundle
                (activity as HomeActivity).addFragment(businessDetailFragment, R.id.frame_home)
            }
        }
    }

    fun filter(search: String) {

        businesses.clear()

        if (search.isEmpty()) {

            businesses.addAll(mDataOriginal)
            notifyDataSetChanged()
            return

        }

        for (i in mDataOriginal.indices) {
            if (mDataOriginal[i].keywords != null && mDataOriginal[i].keywords.contains(search.toLowerCase())) {
                businesses.add(mDataOriginal[i])
            }
        }

        notifyDataSetChanged()

    }

    fun addAllBusiness(data: List<Business>) {

        try {
            businesses.clear()
            mDataOriginal.clear()

            businesses.addAll(data)
            mDataOriginal.addAll(data)

        } catch (e: Exception) {
            e.printStackTrace()
        }

        notifyDataSetChanged()
    }

    fun setCurrentLatLng(lat: Double, lng: Double) {
        isLatLngAvailable = true

        currentLat = lat
        currentLng = lng

        notifyDataSetChanged()
    }
}
