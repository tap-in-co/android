package com.tapin.tapin.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.iid.FirebaseInstanceId
import com.tapin.tapin.BuildConfig
import com.tapin.tapin.R
import com.tapin.tapin.converters.toUserInfo
import com.tapin.tapin.model.profile.ProfileRequest
import com.tapin.tapin.model.profile.ProfileResponse
import com.tapin.tapin.utils.AlertMessages
import com.tapin.tapin.utils.PreferenceManager
import com.tapin.tapin.utils.ProgressHUD
import com.tapin.tapin.utils.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ProfileFragment : BaseFragment() {
    private var from: String? = null

    private var profileCall: Call<ProfileResponse>? = null

    private lateinit var etEmail: EditText
    private lateinit var etWorkEmail: EditText
    private lateinit var etZipcode: EditText
    private lateinit var etNickname: EditText
    private lateinit var etSMSNumber: EditText
    private lateinit var btnBack: Button
    private lateinit var btnSave: Button
    private lateinit var btnManageCreditCard: Button

    private var pd: ProgressHUD? = null
    private lateinit var messages: AlertMessages

    private var onClickListenerSave: View.OnClickListener =
        View.OnClickListener { onProfileSaveClickedNew() }

    private var onClickListenerBack: View.OnClickListener = View.OnClickListener {
        activity?.finish()
    }

    private var onClickListenerManageCard: View.OnClickListener = View.OnClickListener {
        activity?.let {
            val cardDetailFragment = CardDetailFragment()
            val ft = it.supportFragmentManager.beginTransaction()
            ft.add(R.id.frame_profile, cardDetailFragment)
            ft.addToBackStack(null)
            ft.commit()
        }
    }

    companion object {
        private const val FROM = "from"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param from Parameter 1.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(from: String): ProfileFragment {
            val fragment = ProfileFragment()
            val args = Bundle()
            args.putString(FROM, from)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        from = arguments?.getString(FROM) ?: ""
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        messages = AlertMessages(activity)

        initHeader()

        etEmail = view.findViewById<View>(R.id.etEmail) as EditText
        etWorkEmail = view.findViewById(R.id.etWorkEmail)
        etZipcode = view.findViewById<View>(R.id.etZipcode) as EditText
        etNickname = view.findViewById<View>(R.id.etNickname) as EditText
        etSMSNumber = view.findViewById<View>(R.id.etSMSNumber) as EditText
        btnBack = view.findViewById<View>(R.id.btnBack) as Button
        btnSave = view.findViewById<View>(R.id.btnSave) as Button
        btnManageCreditCard = view.findViewById(R.id.btnManageCard)


        /*etEmail.setText(if (Utils.isNotEmpty(PreferenceManager.getEmail())) PreferenceManager.getEmail() else "")
        etWorkEmail.setText(if (Utils.isNotEmpty(PreferenceManager.getWorkEmail())) PreferenceManager.getWorkEmail() else "")
        etZipcode.setText(if (Utils.isNotEmpty(PreferenceManager.getZipcode())) PreferenceManager.getZipcode() else "")
        etNickname.setText(if (Utils.isNotEmpty(PreferenceManager.getUsername())) PreferenceManager.getUsername() else "")
        etSMSNumber.setText(if (Utils.isNotEmpty(PreferenceManager.getPhone())) PreferenceManager.getPhone() else "")*/

        btnBack.setOnClickListener(onClickListenerBack)
        btnSave.setOnClickListener(onClickListenerSave)
        btnManageCreditCard.setOnClickListener(onClickListenerManageCard)
    }

    override fun onDestroyView() {
        super.onDestroyView()

        profileCall?.cancel()
    }

    @SuppressLint("SetTextI18n")
    fun initHeader() {
        (view?.findViewById<View>(R.id.tvToolbarTitle) as TextView).text = "Profile"
    }

    private fun onProfileSaveClickedNew() {
        if (etNickname.text.toString().length < 2) {
            messages.showCustomMessage("Stop", "Nickname must be more the 2 chars long.", null)
        } else if (Utils.isEmpty(etZipcode.text.toString())) {
            messages.showCustomMessage("Stop", "Please enter zip code", null)
        } else if (Utils.isEmpty(etSMSNumber.text.toString())) {
            messages.showCustomMessage("Stop", "Please enter your SMS Number", null)
        } else if (!Utils.isValidMobile(etSMSNumber.text.toString())) {
            messages.showCustomMessage("Stop", "Please enter valid SMS Number", null)
        } else if (Utils.isEmpty(etEmail.text.toString())) {
            messages.showCustomMessage("Stop", "Please enter Email id", null)
        } else if (!Utils.isValidEmailAddress(etEmail.text.toString())) {
            messages.showCustomMessage("Stop", "Please enter valid Email id", null)
        } /*else if (!Utils.isValidEmailAddress(etWorkEmail.getText().toString())) {
                messages.showCustomMessage("Stop", "Please enter valid work Email id", null);
            } */
        else {
            activity?.let {
                pd = ProgressHUD.show(activity, "Loading...", true, false)
            }

            val profile = ProfileRequest(
                0,
                BuildConfig.VERSION_NAME,
                "update",
                FirebaseInstanceId.getInstance().token ?: "",
                etEmail.text.toString(),
                etWorkEmail.text.toString(),
                etNickname.text.toString(),
                etSMSNumber.text.toString(),
                Utils.getDeviceID(activity!!),
                etZipcode.text.toString()
            )

            val profileApi = PreferenceManager.getInstance().api
            profileCall = profileApi.createProfile(profile)
            profileCall?.enqueue(object : Callback<ProfileResponse> {
                override fun onResponse(
                    call: Call<ProfileResponse>,
                    response: Response<ProfileResponse>
                ) {
                    activity?.let {
                        if (response.isSuccessful) {
                            response.body()?.let { profileResponse ->
                                PreferenceManager.setUserData(profileResponse.toUserInfo())
                            }

                            if (from != null && from.equals("DASHBOARD", ignoreCase = true)) {
                                it.finish()
                            } else {
                                val cardDetailFragment = CardDetailFragment()
                                val ft = it.supportFragmentManager.beginTransaction()
                                ft.add(R.id.frame_profile, cardDetailFragment)
                                ft.addToBackStack(null)
                                ft.commit()
                            }
                        } else {
                            Toast.makeText(activity, "Failed to Save Profile", Toast.LENGTH_SHORT)
                                .show()
                        }

                        if (pd != null && pd?.isShowing == true) {
                            pd?.dismiss()
                            pd = null
                        }
                    }
                }

                override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                    activity?.let {
                        Toast.makeText(activity, "Failed to Save Profile", Toast.LENGTH_SHORT)
                            .show()

                        if (pd != null && pd?.isShowing == true) {
                            pd?.dismiss()
                            pd = null
                        }
                    }
                }
            })
        }
    }
}// Required empty public constructor
