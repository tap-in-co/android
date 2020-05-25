package com.tapin.tapin.fragment

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import android.os.Bundle
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.tapin.tapin.R
import com.tapin.tapin.callbacks.Communication
import com.tapin.tapin.model.market.AllMarkets
import com.tapin.tapin.utils.ProgressHUD
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow

open class BaseTap4MarketFragment : Fragment() {
    var communication: Communication? = null

    var progressHUD: ProgressHUD? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)

        communication = context as Communication

        /*lifecycleScope.launchWhenStarted {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getNetwork(requireContext()).flatMapLatest {
                    flow<Boolean> {
                        if (!it) {
                            Toast.makeText(requireContext(), "Lst", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }*/
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        progressHUD =
            ProgressHUD.show(requireActivity(), getString(R.string.please_wait), true, false)
        progressHUD?.dismiss()
    }

    override fun onDestroy() {
        super.onDestroy()

        progressHUD?.dismiss()
        progressHUD = null
    }

    override fun onDetach() {
        super.onDetach()

        communication = null
    }
}

@RequiresApi(Build.VERSION_CODES.M)
fun getNetwork(context: Context) = callbackFlow<Boolean> {
    val cm = context.getSystemService(ConnectivityManager::class.java)
    val callback = object: ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            cm.bindProcessToNetwork(network)
            offer(true)
        }

        override fun onLost(network: Network) {
            super.onLost(network)
            offer(false)
        }
    }
    cm.requestNetwork(NetworkRequest.Builder()
        .build(), callback)
    awaitClose {
        cm.unregisterNetworkCallback(callback)
    }
}