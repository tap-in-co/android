package com.tapin.tapin.fragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.tapin.tapin.R;
import com.tapin.tapin.adapter.SlidingImage_Adapter;
import com.tapin.tapin.model.resturants.Business;
import com.tapin.tapin.utils.AlertMessages;
import com.tapin.tapin.utils.Constant;
import com.tapin.tapin.utils.Debug;
import com.tapin.tapin.utils.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class CateringFragment extends BaseFragment {

    Business business;
    Calendar calendar;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
    EditText etFirstname;
    EditText etLastname;
    EditText etPhoneNumber;
    EditText etEmail;
    EditText etBudget;
    EditText etDate;
    EditText etTime;
    EditText etAttendees;
    EditText etLocation;
    EditText etAdditionalNotes;
    Button btnSendRequest;
    View view;
    Timer timer;
    View.OnClickListener onClickListenerSendRequest = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            sendRequest();
        }
    };
    private TextView tvAddress;
    View.OnClickListener onClickListenerAddress = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?daddr=" + tvAddress.getText().toString()));
            intent.setPackage("com.google.android.apps.maps");
            try {
                startActivity(intent);
            } catch (ActivityNotFoundException ex) {
                try {
                    Intent unrestrictedIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?daddr=" + tvAddress.getText().toString()));
                    startActivity(unrestrictedIntent);
                } catch (ActivityNotFoundException innerEx) {
                    Toast.makeText(getActivity(), "Please install a maps application", Toast.LENGTH_LONG).show();
                }
            }
        }
    };
    private TextView tvPrice;
    private TextView tvWebsite;
    View.OnClickListener onClickListenerWebsite = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            String url = tvWebsite.getText().toString();
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        }
    };
    private TextView textViewBusinessType;
    private TextView tvPaymentEmail;
    private TextView tvTime;
    private RatingBar ratingBar;
    private TextView tvRateCount;
    private TextView textViewOpenClosed;
    private ViewPager mPager;
    private int mYear, mMonth, mDay;
    View.OnClickListener onClickListenerDate = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            // Get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), R.style.datepicker,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            etDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                        }
                    }, mYear, mMonth, mDay);

            datePickerDialog.show();
        }
    };
    private int mHour, mMinute;
    View.OnClickListener onClickListenerTime = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            // Get Current Time
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), R.style.datepicker,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {

                            etTime.setText(hourOfDay + ":" + minute);
                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_catering, null);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvAddress = view.findViewById(R.id.tvAddress);
        tvPrice = view.findViewById(R.id.tvPrice);
        tvWebsite = view.findViewById(R.id.tvWebsite);
        textViewBusinessType = view.findViewById(R.id.textViewBusinessType);
        tvPaymentEmail = view.findViewById(R.id.tvPaymentEmail);
        tvTime = view.findViewById(R.id.tvTime);
        ratingBar = view.findViewById(R.id.ratingBar);
        tvRateCount = view.findViewById(R.id.tvRateCount);
        textViewOpenClosed = view.findViewById(R.id.textViewOpenClosed);
        mPager = view.findViewById(R.id.pager);

        etFirstname = getView().findViewById(R.id.etFirstname);
        etLastname = getView().findViewById(R.id.etLastname);
        etPhoneNumber = getView().findViewById(R.id.etPhoneNumber);
        etEmail = getView().findViewById(R.id.etEmail);
        etBudget = getView().findViewById(R.id.etBudget);
        etDate = getView().findViewById(R.id.etDate);
        etTime = getView().findViewById(R.id.etTime);
        etAttendees = getView().findViewById(R.id.etAttendees);
        etLocation = getView().findViewById(R.id.etLocation);
        etAdditionalNotes = getView().findViewById(R.id.etAdditionalNotes);
        btnSendRequest = getView().findViewById(R.id.btnSendRequest);
        Utils.disableEditText(etDate);
        Utils.disableEditText(etTime);

//        business = (Business) getArguments().getSerializable("business");
        business = Constant.business;

        mPager = view.findViewById(R.id.pager);
        setBusinessData(business);

        initHeader();

        etDate.setOnClickListener(onClickListenerDate);
        etTime.setOnClickListener(onClickListenerTime);
        btnSendRequest.setOnClickListener(onClickListenerSendRequest);

        tvWebsite.setOnClickListener(onClickListenerWebsite);
        tvAddress.setOnClickListener(onClickListenerAddress);

    }

    private void setBusinessData(Business business) {

        // image slider

        if (business.getPictures().endsWith(",")) {
            business.setPictures(business.getPictures().substring(0, business.getPictures().length() - 1));
        }

        final List<String> image_list = Arrays.asList((business.getPictures().split("\\s*,\\s*")));
        Debug.e("Arraylist", image_list + "-");
        mPager.setAdapter(new SlidingImage_Adapter(getActivity(), business.getBusinessID(), image_list));
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                mPager.post(new Runnable() {

                    @Override
                    public void run() {
                        mPager.setCurrentItem((mPager.getCurrentItem() + 1) % image_list.size());
                    }
                });
            }
        };
        timer = new Timer();
        timer.schedule(timerTask, 3000, 3000);


        //  views
        tvAddress.setText(Utils.isNotEmpty(business.getAddress()) ? business.getAddress() : "");
        tvWebsite.setText(Utils.isNotEmpty(business.getWebsite()) ? business.getWebsite() : "");
        textViewBusinessType.setText(Utils.isNotEmpty(business.getCustomerProfileName()) ? business.getCustomerProfileName() : "");
        tvPaymentEmail.setText(Utils.isNotEmpty(business.getNeighborhood()) ? business.getNeighborhood() : "");
        tvTime.setText(Utils.getOpenTime(business.getOpeningTime(), business.getClosingTime()));
        ratingBar.setRating(Float.parseFloat(business.getRating()));
        // tvRateCount.setText(Utils.isNotEmpty(business.ti_rating) ? "("+business.ti_rating +")": "");
        // tvPrice.setText(Utils.isNotEmpty(business.website)?business.website:"");


        // open - close now
        try {
            Date dateOpening = simpleDateFormat.parse(business.getOpeningTime());
            Date dateClosing = simpleDateFormat.parse(business.getClosingTime());

            Calendar calendarOpening = Calendar.getInstance();
            calendarOpening.setTimeInMillis(dateOpening.getTime());

            Calendar calendarClosing = Calendar.getInstance();
            calendarClosing.setTimeInMillis(dateClosing.getTime());
            calendar = Calendar.getInstance();
            if (calendar.getTimeInMillis() > calendarOpening.getTimeInMillis()
                    && calendar.getTimeInMillis() < calendarClosing.getTimeInMillis()) {
                textViewOpenClosed.setText("OPEN NOW");
                textViewOpenClosed.setTextColor(ContextCompat.getColor(getActivity(), R.color.yellow));
            } else {
                textViewOpenClosed.setText("NOW CLOSED");
                textViewOpenClosed.setTextColor(ContextCompat.getColor(getActivity(), R.color.gray));

            }

        } catch (ParseException e) {
            e.printStackTrace();
        }


        //Glide.with(getActivity()).load(UrlGenerator.IMAGE_URL + "" + business.icon).into(ivFood);


    }

    private void initHeader() {
        final ImageView ivHeaderLogo = view.findViewById(R.id.ivHeaderLogo);
        final TextView tvHeaderTitle = view.findViewById(R.id.tvHeaderTitle);
        final TextView tvHeaderLeft = view.findViewById(R.id.tvHeaderLeft);
        final TextView tvHeaderRight = view.findViewById(R.id.tvHeaderRight);

        ivHeaderLogo.setVisibility(View.GONE);
        tvHeaderTitle.setVisibility(View.GONE);
        tvHeaderLeft.setVisibility(View.VISIBLE);
        tvHeaderRight.setVisibility(View.GONE);


        tvHeaderTitle.setText(business.getName() + "");

        tvHeaderLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                getActivity().onBackPressed();
            }
        });

    }

    @Override
    public void onResume() {

        super.onResume();

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {

                    // handle back button
                    getActivity().onBackPressed();
                    return true;

                }

                return false;
            }
        });
    }

    private void sendRequest() {
        AlertMessages messages = new AlertMessages(getActivity());

        if (Utils.isEmpty(etFirstname.getText().toString())) {
            messages.showCustomMessage("Stop", "Please enter First Name", null);
        } else if (Utils.isEmpty(etLastname.getText().toString())) {
            messages.showCustomMessage("Stop", "Please enter Last Name", null);
        } else if (Utils.isEmpty(etPhoneNumber.getText().toString())) {
            messages.showCustomMessage("Stop", "Please enter your Phone Number", null);
        } else if (!Utils.isValidMobile(etPhoneNumber.getText().toString())) {
            messages.showCustomMessage("Stop", "Please enter valid Phone Number", null);
        } else if (Utils.isEmpty(etEmail.getText().toString())) {
            messages.showCustomMessage("Stop", "Please enter Email id", null);
        } else if (!Utils.isValidEmailAddress(etEmail.getText().toString())) {
            messages.showCustomMessage("Stop", "Please enter valid Email id", null);
        } else if (Utils.isEmpty(etDate.getText().toString())) {
            messages.showCustomMessage("Stop", "Please enter event date", null);
        } else if (Utils.isEmpty(etTime.getText().toString())) {
            messages.showCustomMessage("Stop", "Please enter event time", null);
        } else if (Utils.isEmpty(etAttendees.getText().toString())) {
            messages.showCustomMessage("Stop", "Please enter event attendees", null);
        } else if (Utils.isEmpty(etLocation.getText().toString())) {
            messages.showCustomMessage("Stop", "Please enter event location", null);
        } else {


            if (!Utils.isInternetConnected(getActivity())) {
                messages = new AlertMessages(getActivity());
                messages.showNetworkAlert();
                return;
            }


            String emailTitle = "Catering";
            String messageBody = "First name: " + etFirstname.getText().toString() + " Last name: " + etLastname.getText().toString();
            messageBody += " \nPhone number: " + etPhoneNumber.getText().toString();
            messageBody += " \n Email: " + etEmail.getText().toString();
            messageBody += " \nBudget:" + etBudget.getText().toString();
            messageBody += " \nDate: " + etPhoneNumber.getText().toString() + " Time: " + etTime.getText().toString() + "    Attendies: " + etAttendees.getText().toString();
            messageBody += " \nBudget:" + etBudget.getText().toString();
            messageBody += " \nLocation: " + etLocation.getText().toString();
            messageBody += " \nAdditional Notes: " + etAdditionalNotes.getText().toString();


            Intent emailIntent = new Intent(Intent.ACTION_SEND);

            emailIntent.setData(Uri.parse("mailto:"));
            emailIntent.setType("text/plain");
            emailIntent.putExtra(Intent.EXTRA_EMAIL, "");
            emailIntent.putExtra(Intent.EXTRA_CC, "");
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "" + emailTitle);
            emailIntent.putExtra(Intent.EXTRA_TEXT, messageBody);

            try {
                startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(getActivity(), "There is no email client installed.", Toast.LENGTH_SHORT).show();
            }
        }

//        RequestParams params = new RequestParams();
//        params.put("firstname", etFirstname.getText().toString());
//        params.put("last_", etLastname.getText().toString());
//        params.put("firstname", etPhoneNumber.getText().toString());
//        params.put("firstname", etEmail.getText().toString());
//        params.put("firstname", etBudget.getText().toString());
//        params.put("firstname", etDate.getText().toString());
//        params.put("firstname", etTime.getText().toString());
//        params.put("etAttendees", etAttendees.getText().toString());
//        params.put("etAdditionalNotes", etAdditionalNotes.getText().toString());
//
//        AsyncHttpClient client = new AsyncHttpClient();
//        client.setTimeout(Constant.TIMEOUT);
//        client.put(UrlGenerator.BASE_URL, params, new AsyncHttpResponseHandler() {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//
//                try {
//                    String content = new String(responseBody, "UTF-8");
//                    Debug.e("send request succ", content + "-");
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//                Debug.e("send request fail", responseBody + "-");
//                error.printStackTrace();
//            }
//        });


    }


}
