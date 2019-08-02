package com.tapin.tapin.activity

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.loopj.android.http.LogInterface
import com.tapin.tapin.R
import com.tapin.tapin.adapter.DeliveryLocationAdapter
import com.tapin.tapin.adapter.OnDeliveryLocationSelectionListener
import com.tapin.tapin.model.CorporateDomain
import com.tapin.tapin.model.CorporateDomains
import com.tapin.tapin.utils.*
import cz.msebera.android.httpclient.Header

/**
 * Created by Narendra on 5/22/17.
 */

class OrderSelectionActivity : BaseActivity(), OnDeliveryLocationSelectionListener {
    private lateinit var newOrderLayout: View
    private lateinit var centerText: AppCompatTextView
    private lateinit var corporateOrderLayout: View
    private lateinit var bottomLayout: View
    private lateinit var bottomLayoutCancel: View
    private lateinit var bottomLayoutDone: View
    private lateinit var bottomRecyclerView: RecyclerView

    private var deliveryLocationAdapter: DeliveryLocationAdapter? = null
    private var corporateDomains: CorporateDomains? = null

    private var asyncHttpClient: AsyncHttpClient? = null
    private var selectedCorporateDomain: CorporateDomain? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_selection)

        initViews()
    }

    override fun onResume() {
        super.onResume()

        if (!Utils.isEmpty(PreferenceManager.getWorkEmail())) {
            checkDomain()
        }
    }

    override fun onPause() {
        super.onPause()

        asyncHttpClient?.cancelRequests(this, true)
    }

    override fun onLocationSelected(position: Int, corporateDomain: CorporateDomain) {
        selectedCorporateDomain = corporateDomain
        PreferenceManager.getInstance().selectedCorporateDomain = selectedCorporateDomain
        deliveryLocationAdapter?.markAsSelected(position)
    }

    private fun initViews() {
        newOrderLayout = findViewById(R.id.order_activity_new_order)
        newOrderLayout.setOnClickListener {
            PreferenceManager.getInstance().isCorporateOrder = false
            PreferenceManager.getInstance().corporateOrderMerchantIds = ""
            PreferenceManager.getInstance().selectedCorporateDomain = null

            isPickUpOrder = false

            val i = Intent(baseContext, HomeActivity::class.java)
            startActivity(i)
        }

        centerText = findViewById(R.id.order_activity_center_text)
        if (Utils.isEmpty(PreferenceManager.getWorkEmail())) {
            centerText.text = getText(R.string.no_work_email)
            centerText.visibility = View.VISIBLE
        }

        corporateOrderLayout = findViewById(R.id.order_activity_corporate_order)
        corporateOrderLayout.setOnClickListener { showProfileAlertDialog() }
        corporateOrderLayout.isClickable = false

        bottomLayoutCancel = findViewById(R.id.order_activity_cancel)
        bottomLayoutCancel.setOnClickListener {
            showHideBottomLayout(false)
        }
        bottomLayoutDone = findViewById(R.id.order_activity_done)
        bottomLayoutDone.setOnClickListener {
            onDoneClicked()
        }

        bottomLayout = findViewById(R.id.order_activity_bottom_layout)

        bottomRecyclerView = findViewById(R.id.order_activity_delivery_locations)

        showHideBottomLayout(false)
    }

    private fun showProfileAlertDialog() {
        if (Utils.isEmpty(PreferenceManager.getWorkEmail())) {
            val builder = AlertDialog.Builder(this)
            builder.setMessage(getString(R.string.please_update_your_work_email))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.ok)) { _, _ ->
                    // Sets the app for Corporate Orders
                    PreferenceManager.getInstance().isCorporateOrder = true
                    isPickUpOrder = true

                    val i = Intent(baseContext, HomeActivity::class.java)
                    startActivity(i)
                }

            val alert = builder.create()
            alert.show()
        } else {
            checkDomain()
        }
    }

    private fun checkDomain() {
        if (corporateDomains != null) {
            onSuccess(corporateDomains!!)
            return
        }

        asyncHttpClient = AsyncHttpClient()
        asyncHttpClient?.isLoggingEnabled = true
        asyncHttpClient?.loggingLevel = LogInterface.DEBUG
        asyncHttpClient?.setTimeout(Constant.TIMEOUT)

        Debug.d(
            "Okhttp",
            "API: " + UrlGenerator.getCorporateDomainApi(PreferenceManager.getWorkEmailDomain())
        )

        asyncHttpClient?.get(
            this,
            UrlGenerator.getCorporateDomainApi(PreferenceManager.getWorkEmailDomain()),
            object : AsyncHttpResponseHandler() {
                override fun onSuccess(
                    statusCode: Int,
                    headers: Array<out Header>?,
                    responseBody: ByteArray?
                ) {
                    try {
                        val content = String(responseBody!!, Charsets.UTF_8)
                        Debug.d("Okhttp", "Success Response: $content")
                        val corporateDomains =
                            Gson().fromJson(content, CorporateDomains::class.java)

                        if (corporateDomains.success == 0) {
                            onSuccess(corporateDomains)
                        } else {
                            onFailure()
                        }
                    } catch (e: Exception) {
                        onFailure()
                    }
                }

                override fun onFailure(
                    statusCode: Int,
                    headers: Array<out Header>?,
                    responseBody: ByteArray?,
                    error: Throwable?
                ) {
                    try {
                        val content = String(responseBody!!, Charsets.UTF_8)
                        Debug.d("Okhttp", "Failure Response: $content")
                    } catch (e: Exception) {
                    }
                    onFailure()
                }

            })
    }

    private fun onSuccess(corporateDomains: CorporateDomains) {
        this.corporateDomains = corporateDomains
        showHideBottomLayout(true)
        deliveryLocationAdapter =
            DeliveryLocationAdapter(corporateDomains = corporateDomains.data, listener = this)
        bottomRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        bottomRecyclerView.setHasFixedSize(true)
        bottomRecyclerView.adapter = deliveryLocationAdapter

        centerText.visibility = View.GONE
    }

    private fun onFailure() {
        centerText.text = getText(R.string.join_tap_in_text_with_email)
        centerText.visibility = View.VISIBLE

        showHideBottomLayout(false)
    }

    private fun showHideBottomLayout(show: Boolean) {
        bottomLayout.visibility = if (show) View.VISIBLE else View.GONE
        corporateOrderLayout.isClickable = !show
    }

    private fun onDoneClicked() {
        if (selectedCorporateDomain == null) {
            Toast.makeText(this, "Please select a Delivery location", Toast.LENGTH_SHORT).show()
        }

        selectedCorporateDomain?.let {
            showHideBottomLayout(false)
            showSelectedDeliveryLocation()
        }
    }

    private fun showSelectedDeliveryLocation() {
        selectedCorporateDomain?.let {
            val builder = AlertDialog.Builder(this)
            builder.setMessage("It's a good time to order!\nCut-off time: ${it.cutoffTime}\nfor Delivery at: ${it.deliveryTime}")
                .setCancelable(false)
                .setPositiveButton(getString(R.string.ok)) { _, _ ->
                    // Sets the app for Corporate Orders
                    PreferenceManager.getInstance().isCorporateOrder = true
                    PreferenceManager.getInstance().corporateOrderMerchantIds = it.merchantIds
                    isPickUpOrder = false

                    val i = Intent(baseContext, HomeActivity::class.java)
                    startActivity(i)
                }

            val alert = builder.create()
            alert.show()
        }
    }
}