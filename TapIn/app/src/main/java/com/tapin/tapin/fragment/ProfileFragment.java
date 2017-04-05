package com.tapin.tapin.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.tapin.tapin.R;
import com.tapin.tapin.model.UserInfo;
import com.tapin.tapin.utils.AlertMessages;
import com.tapin.tapin.utils.Constant;
import com.tapin.tapin.utils.Debug;
import com.tapin.tapin.utils.PreferenceManager;
import com.tapin.tapin.utils.URLs;
import com.tapin.tapin.utils.Utils;

import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;

import cz.msebera.android.httpclient.Header;


public class ProfileFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    View view;

    EditText etEmail;
    EditText etZipcode;
    EditText etNickname;
    EditText etSMSNumber;
    Button btnBack;
    Button btnSave;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profile, container, false);

        initHeader();

        etEmail = (EditText) view.findViewById(R.id.etEmail);
        etZipcode = (EditText) view.findViewById(R.id.etZipcode);
        etNickname = (EditText) view.findViewById(R.id.etNickname);
        etSMSNumber = (EditText) view.findViewById(R.id.etSMSNumber);
        btnBack = (Button) view.findViewById(R.id.btnBack);
        btnSave = (Button) view.findViewById(R.id.btnSave);

        UsPhoneNumberFormatter addLineNumberFormatter = new UsPhoneNumberFormatter(
                new WeakReference<EditText>(etSMSNumber));
        //   etSMSNumber.addTextChangedListener(addLineNumberFormatter);

        btnBack.setOnClickListener(onClickListenerBack);
        btnSave.setOnClickListener(onClickListenerSave);

        return view;
    }
    public void initHeader() {
        ImageView ivHeaderLogo = (ImageView) view.findViewById(R.id.ivHeaderLogo);
        TextView tvHeaderTitle = (TextView) view.findViewById(R.id.tvHeaderTitle);
        TextView tvHeaderLeft = (TextView) view.findViewById(R.id.tvHeaderLeft);
        TextView tvHeaderRight = (TextView) view.findViewById(R.id.tvHeaderRight);

        ivHeaderLogo.setVisibility(View.GONE);
        tvHeaderTitle.setVisibility(View.VISIBLE);
        tvHeaderLeft.setVisibility(View.GONE);
        tvHeaderRight.setVisibility(View.GONE);


    }
    ProgressDialog progressDialog;
    View.OnClickListener onClickListenerSave = new View.OnClickListener() {
        @Override
        public void onClick(View view) {


            AlertMessages messages = new AlertMessages(getActivity());

            if (etNickname.getText().toString().length() < 2) {
                messages.showCustomMessage("Stop", "Nickname must be more the 2 chars long.", null);
            } else if (Utils.isEmpty(etZipcode.getText().toString())) {
                messages.showCustomMessage("Stop", "Please enter zip code", null);
            } else if (Utils.isEmpty(etSMSNumber.getText().toString())) {
                messages.showCustomMessage("Stop", "Please enter your SMS Number", null);
            } else if (!Utils.isValidMobile(etSMSNumber.getText().toString())) {
                messages.showCustomMessage("Stop", "Please enter valid SMS Number", null);
            } else if (Utils.isEmpty(etEmail.getText().toString())) {
                messages.showCustomMessage("Stop", "Please enter Email id", null);
            } else if (!Utils.isValidEmailAddress(etEmail.getText().toString())) {
                messages.showCustomMessage("Stop", "Please enter valid Email id", null);
            } else {


                progressDialog = ProgressDialog.show(getActivity(), "", "Loading...");
                RequestParams params = new RequestParams();
                params.put("email", etEmail.getText().toString());
                params.put("zipcode", etZipcode.getText().toString());
                params.put("nickname", etNickname.getText().toString());
//                params.put("age_group", etEmail.getText().toString());
                params.put("sms_no", etSMSNumber.getText().toString());
//                params.put("device_token", etDate.getText().toString());

                if (Utils.isNotEmpty(PreferenceManager.getUserId()))
                {
                    params.put("cmd", "update");
                    params.put("uuid", PreferenceManager.getUserId());
                }

                AsyncHttpClient client = new AsyncHttpClient();
                client.setTimeout(Constant.TIMEOUT);
                client.post(URLs.PROFILE, params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                        try {
                            String content = new String(responseBody, "UTF-8");
                            UserInfo userInfo = new Gson().fromJson(content, UserInfo.class);
                            PreferenceManager.setUserData(userInfo);
                            Debug.e("save request succ", content + "-");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        Debug.e("save request fail", responseBody + "-");
                        error.printStackTrace();
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        progressDialog.dismiss();
                    }
                });

            }
        }
    };
    View.OnClickListener onClickListenerBack = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

        }
    };


    private class UsPhoneNumberFormatter implements TextWatcher {
        //This TextWatcher sub-class formats entered numbers as 1 (123) 456-7890
        private boolean mFormatting; // this is a flag which prevents the
        // stack(onTextChanged)
        private boolean clearFlag;
        private int mLastStartLocation;
        private String mLastBeforeText;
        private WeakReference<EditText> mWeakEditText;

        public UsPhoneNumberFormatter(WeakReference<EditText> weakEditText) {
            this.mWeakEditText = weakEditText;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            if (after == 0 && s.toString().equals("1 ")) {
                clearFlag = true;
            }
            mLastStartLocation = start;
            mLastBeforeText = s.toString();
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            // TODO: Do nothing
        }

        @Override
        public void afterTextChanged(Editable s) {
            // Make sure to ignore calls to afterTextChanged caused by the work
            // done below
            if (!mFormatting) {
                mFormatting = true;
                int curPos = mLastStartLocation;
                String beforeValue = mLastBeforeText;
                String currentValue = s.toString();
                String formattedValue = formatUsNumber(s);
                if (currentValue.length() > beforeValue.length()) {
                    int setCusorPos = formattedValue.length()
                            - (beforeValue.length() - curPos);
                    mWeakEditText.get().setSelection(setCusorPos < 0 ? 0 : setCusorPos);
                } else {
                    int setCusorPos = formattedValue.length()
                            - (currentValue.length() - curPos);
                    if (setCusorPos > 0 && !Character.isDigit(formattedValue.charAt(setCusorPos - 1))) {
                        setCusorPos--;
                    }
                    mWeakEditText.get().setSelection(setCusorPos < 0 ? 0 : setCusorPos);
                }
                mFormatting = false;
            }
        }

        private String formatUsNumber(Editable text) {
            StringBuilder formattedString = new StringBuilder();
            // Remove everything except digits
            int p = 0;
            while (p < text.length()) {
                char ch = text.charAt(p);
                if (!Character.isDigit(ch)) {
                    text.delete(p, p + 1);
                } else {
                    p++;
                }
            }
            // Now only digits are remaining
            String allDigitString = text.toString();

            int totalDigitCount = allDigitString.length();

            if (totalDigitCount == 0
                    || (totalDigitCount > 10 && !allDigitString.startsWith("1"))
                    || totalDigitCount > 11) {
                // May be the total length of input length is greater than the
                // expected value so we'll remove all formatting
                text.clear();
                text.append(allDigitString);
                return allDigitString;
            }
            int alreadyPlacedDigitCount = 0;
            // Only '1' is remaining and user pressed backspace and so we clear
            // the edit text.
            if (allDigitString.equals("1") && clearFlag) {
                text.clear();
                clearFlag = false;
                return "";
            }
            if (allDigitString.startsWith("1")) {
                formattedString.append("1 ");
                alreadyPlacedDigitCount++;
            }
            // The first 3 numbers beyond '1' must be enclosed in brackets "()"
            if (totalDigitCount - alreadyPlacedDigitCount > 3) {
                formattedString.append("("
                        + allDigitString.substring(alreadyPlacedDigitCount,
                        alreadyPlacedDigitCount + 3) + ") ");
                alreadyPlacedDigitCount += 3;
            }
            // There must be a '-' inserted after the next 3 numbers
            if (totalDigitCount - alreadyPlacedDigitCount > 3) {
                formattedString.append(allDigitString.substring(
                        alreadyPlacedDigitCount, alreadyPlacedDigitCount + 3)
                        + "-");
                alreadyPlacedDigitCount += 3;
            }
            // All the required formatting is done so we'll just copy the
            // remaining digits.
            if (totalDigitCount > alreadyPlacedDigitCount) {
                formattedString.append(allDigitString
                        .substring(alreadyPlacedDigitCount));
            }

            text.clear();
            text.append(formattedString.toString());
            return formattedString.toString();
        }

    }

}
