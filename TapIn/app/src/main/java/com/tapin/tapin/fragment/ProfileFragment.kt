package com.tapin.tapin.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.iid.FirebaseInstanceId
import com.tapin.tapin.App
import com.tapin.tapin.BuildConfig
import com.tapin.tapin.R
import com.tapin.tapin.model.profile.ProfileRequest
import com.tapin.tapin.model.profile.ProfileResponse
import com.tapin.tapin.utils.Utils
import com.tapin.tapin.viewmodels.AppViewModelProvider
import com.tapin.tapin.viewmodels.ProfileViewModel

class ProfileFragment : BaseTap4MarketFragment() {
    private lateinit var etEmail: EditText
    private lateinit var etWorkEmail: EditText
    private lateinit var etZipcode: EditText
    private lateinit var etNickname: EditText
    private lateinit var etSMSNumber: EditText
    private lateinit var btnBack: Button
    private lateinit var btnSave: Button
    private lateinit var btnManageCreditCard: Button
    private lateinit var progressBar: ProgressBar

    private val app : App by lazy { requireContext().applicationContext as App }
    private val profile: ProfileResponse? by lazy { app.profile }

    private lateinit var profileViewModel: ProfileViewModel

    companion object {
        fun newInstance() = ProfileFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val factory = AppViewModelProvider.provideProfileViewModelFactory(requireContext())
        profileViewModel = ViewModelProvider(viewModelStore, factory).get(ProfileViewModel::class.java)

        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (view.findViewById<View>(R.id.tvToolbarTitle) as TextView)?.text = "Profile"

        etEmail = view.findViewById<View>(R.id.etEmail) as EditText
        etWorkEmail = view.findViewById(R.id.etWorkEmail)
        etZipcode = view.findViewById<View>(R.id.etZipcode) as EditText
        etNickname = view.findViewById<View>(R.id.etNickname) as EditText
        etSMSNumber = view.findViewById<View>(R.id.etSMSNumber) as EditText
        btnBack = view.findViewById<View>(R.id.btnBack) as Button
        btnSave = view.findViewById<View>(R.id.btnSave) as Button
        btnManageCreditCard = view.findViewById(R.id.btnManageCard)
        progressBar = view.findViewById(R.id.progress_bar)


        etEmail.setText(profile?.email1 ?: "")
        etWorkEmail.setText(profile?.email2 ?: "")
        etZipcode.setText(profile?.zipcode ?: "")
        etNickname.setText(profile?.nickname ?: "")
        etSMSNumber.setText(profile?.smsNo ?: "")

        btnBack.setOnClickListener { communication?.onProfileBackClicked() }
        btnSave.setOnClickListener { onProfileSaveClickedNew() }
        btnManageCreditCard.setOnClickListener { communication?.onProfileManageCardClicked() }

        profileViewModel.loadingLiveData.observe(viewLifecycleOwner, Observer {
            progressBar.visibility = if (it) View.VISIBLE else View.GONE
        })

        profileViewModel.profileLiveData.observe(viewLifecycleOwner, Observer {
            if (it.isSuccess) {
                app.profile = it.getOrNull()
                communication?.onProfileBackClicked()
            } else {
                Toast.makeText(requireContext(), "Profile saved successfully", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun onProfileSaveClickedNew() {
        if (etNickname.text.toString().length < 2) {
            Toast.makeText(requireContext(), "Nickname must be more the 2 chars long.", Toast.LENGTH_SHORT).show()
        } else if (Utils.isEmpty(etZipcode.text.toString())) {
            Toast.makeText(requireContext(), "Please enter zip code", Toast.LENGTH_SHORT).show()
        } else if (Utils.isEmpty(etSMSNumber.text.toString())) {
            Toast.makeText(requireContext(), "Please enter your SMS Number", Toast.LENGTH_SHORT).show()
        } else if (!Utils.isValidMobile(etSMSNumber.text.toString())) {
            Toast.makeText(requireContext(), "Please enter valid SMS Number", Toast.LENGTH_SHORT).show()
        } else if (Utils.isEmpty(etEmail.text.toString())) {
            Toast.makeText(requireContext(), "Please enter Email id", Toast.LENGTH_SHORT).show()
        } else if (!Utils.isValidEmailAddress(etEmail.text.toString())) {
            Toast.makeText(requireContext(), "Please enter valid Email id", Toast.LENGTH_SHORT).show()
        } /*else if (!Utils.isValidEmailAddress(etWorkEmail.getText().toString())) {
                messages.showCustomMessage("Stop", "Please enter valid work Email id", null);
            } */
        else {
            val profile = ProfileRequest(
                0,
                BuildConfig.VERSION_NAME,
                "update",
                FirebaseInstanceId.getInstance().token ?: "",
                etEmail.text.toString(),
                etWorkEmail.text.toString(),
                etNickname.text.toString(),
                etSMSNumber.text.toString(),
                Utils.getDeviceID(requireActivity()),
                etZipcode.text.toString()
            )
            profileViewModel.saveProfile(profile)
        }
    }
}

