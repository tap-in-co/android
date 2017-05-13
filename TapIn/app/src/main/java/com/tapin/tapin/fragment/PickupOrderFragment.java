package com.tapin.tapin.fragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.tapin.tapin.R;
import com.tapin.tapin.model.BusinessDeliveryInfo;
import com.tapin.tapin.model.BusinessInfo;
import com.tapin.tapin.model.GetPointsResp;
import com.tapin.tapin.model.OrderInfo;
import com.tapin.tapin.utils.AlertMessages;
import com.tapin.tapin.utils.Constant;
import com.tapin.tapin.utils.Debug;
import com.tapin.tapin.utils.PreferenceManager;
import com.tapin.tapin.utils.ProgressHUD;
import com.tapin.tapin.utils.URLs;
import com.tapin.tapin.utils.Utils;

import org.w3c.dom.Text;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Narendra on 4/26/17.
 */

public class PickupOrderFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public PickupOrderFragment() {
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
    public static PickupOrderFragment newInstance(String param1, String param2) {
        PickupOrderFragment fragment = new PickupOrderFragment();
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

    TextView tvHotelName;

    TextView tvMessage;

    LinearLayout llCounter;
    ImageView ivCounter;
    LinearLayout llCounterPick;
    TextView tvCounterTime;

    LinearLayout llTable;
    ImageView ivTable;
    LinearLayout llTablePick;
    Spinner spinnerTable;

    LinearLayout llLocation;
    ImageView ivLocation;
    LinearLayout llLocationPick;
    Spinner spinnerLocation;
    TextView tvLocationTime;

    LinearLayout llParking;
    ImageView ivParking;
    LinearLayout llParkingPick;
    TextView tvParkingTime;

    Button btnCancel;
    Button btnOk;

    ProgressHUD progressHUD;
    AlertMessages messages;

    boolean isCounterSelected;
    boolean isTableSelected;
    boolean isLocationSelected;
    boolean isParkingSelected;

    Time timeValue;
    SimpleDateFormat format;
    Calendar calendar;

    int counterYear, counterMonth, counterDay;
    int locationYear, locationMonth, locationDay;
    int parkingYear, parkingMonth, parkingDay;

    int counterHour, counterMin;
    int locationHour, locationMin;
    int parkingHour, parkingMin;

    BusinessDeliveryInfo businessDeliveryInfo;

    ArrayList<OrderInfo> listOrdered = new ArrayList<>();

    ArrayList<String> listTables = new ArrayList<>();
    ArrayAdapter<String> tableAdapter;

    ArrayList<String> listLocations = new ArrayList<>();
    ArrayAdapter<String> locationAdapter;

    BusinessInfo businessInfo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_pickup_order, container, false);

        messages = new AlertMessages(getActivity());

        calendar = Calendar.getInstance();

        tableAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, listTables);
        tableAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        locationAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, listLocations);
        locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        counterYear = parkingYear = locationYear = calendar.get(Calendar.YEAR);
        counterMonth = parkingMonth = locationMonth = calendar.get(Calendar.MONTH);
        counterDay = parkingDay = locationDay = calendar.get(Calendar.DAY_OF_MONTH);

        counterHour = parkingHour = locationHour = calendar.get(Calendar.HOUR);
        counterMin = parkingMin = locationMin = calendar.get(Calendar.MINUTE);

        businessInfo = (BusinessInfo) getArguments().getSerializable("BUSINESS_INFO");

        listOrdered = (ArrayList<OrderInfo>) getArguments().getSerializable("ORDERED_LIST");

        initHeader();

        initViews();

        setData();

        if (Utils.isInternetConnected(getActivity())) {
            getLocationAndTable();
        } else {
            messages.showErrorInConnection();
        }

        return view;

    }

    private void setData() {

        tvHotelName.setText("" + businessInfo.name);

        if (businessInfo.pickup_counter_charge.equalsIgnoreCase("-1")) {
            llCounter.setEnabled(false);
        }
        if (businessInfo.pickup_location_charge.equalsIgnoreCase("-1")) {
            llLocation.setEnabled(false);
        }
        if (businessInfo.delivery_table_charge.equalsIgnoreCase("-1")) {
            llTable.setEnabled(false);
        }
        if (businessInfo.delivery_location_charge.equalsIgnoreCase("-1")) {
            llParking.setEnabled(false);
        }

        if (!businessInfo.pickup_counter_charge.equalsIgnoreCase("-1")) {
            selectPickLayout(ivCounter, R.drawable.ic_counter_active, llCounterPick);
        } else if (!businessInfo.pickup_location_charge.equalsIgnoreCase("-1")) {
            selectPickLayout(ivCounter, R.drawable.ic_designated_location_active, llLocationPick);
        } else if (!businessInfo.delivery_table_charge.equalsIgnoreCase("-1")) {
            selectPickLayout(ivCounter, R.drawable.ic_table_active, llTablePick);
        } else if (!businessInfo.delivery_location_charge.equalsIgnoreCase("-1")) {
            selectPickLayout(ivCounter, R.drawable.ic_parking_active, llParkingPick);
        }

    }

    private void getLocationAndTable() {

        progressHUD = ProgressHUD.show(getActivity(), getActivity().getResources().getString(R.string.please_wait), true, false);

        RequestParams params = new RequestParams();
        params.put("cmd", "get_business_delivery_info");
        params.put("business_id", businessInfo.businessID);

        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(Constant.TIMEOUT);

        String URL = URLs.BUSINESS_DELIVERY_INFO + "?cmd=get_business_delivery_info&business_id=" + businessInfo.businessID;

        Log.e("URL", "" + URL);

        client.get(getActivity(), URL, params, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                try {

                    String content = new String(responseBody, "UTF-8");

                    Log.e("RES_BUSINESS_DELIV_INFO", "-" + content + "-");

                    businessDeliveryInfo = new Gson().fromJson(content, BusinessDeliveryInfo.class);

                    if (businessDeliveryInfo.tableInfo != null) {

                        tvMessage.setText("" + businessDeliveryInfo.tableInfo.message_to_consumers);

                        listTables = new ArrayList<String>();

                        for (int i = Integer.parseInt(businessDeliveryInfo.tableInfo.table_no_min); i <= Integer.parseInt(businessDeliveryInfo.tableInfo.table_no_max); i++) {

                            listTables.add("" + i);

                        }

                        tableAdapter.clear();
                        tableAdapter.addAll(listTables);

                        spinnerTable.setAdapter(tableAdapter);
                        spinnerTable.setSelection(0);


                    }

                    if (businessDeliveryInfo.locationInfo != null) {

                        listLocations = new ArrayList<String>();

                        for (int i = 0; i < businessDeliveryInfo.locationInfo.listLocation.size(); i++) {

                            listLocations.add(businessDeliveryInfo.locationInfo.listLocation.get(i).location_name);

                        }
                        locationAdapter.clear();
                        locationAdapter.addAll(listLocations);

                        spinnerLocation.setAdapter(locationAdapter);
                        spinnerLocation.setSelection(0);

                    }

                    if (progressHUD != null && progressHUD.isShowing()) {
                        progressHUD.dismiss();
                    }

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Debug.e("getAllPoint fail", responseBody + "-");
                error.printStackTrace();
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }
        });

    }

    private void initHeader() {

        ((TextView) view.findViewById(R.id.tvToolbarTitle)).setText("Pickup Order");

    }

    private void initViews() {

        tvHotelName = (TextView) view.findViewById(R.id.tvHotelName);

        tvMessage = (TextView) view.findViewById(R.id.tvMessage);

        llCounter = (LinearLayout) view.findViewById(R.id.llCounter);
        ivCounter = (ImageView) view.findViewById(R.id.ivCounter);
        llCounterPick = (LinearLayout) view.findViewById(R.id.llCounterPick);
        tvCounterTime = (TextView) view.findViewById(R.id.tvCounterTime);

        llTable = (LinearLayout) view.findViewById(R.id.llTable);
        ivTable = (ImageView) view.findViewById(R.id.ivTable);
        llTablePick = (LinearLayout) view.findViewById(R.id.llTablePick);
        spinnerTable = (Spinner) view.findViewById(R.id.spinnerTable);

        spinnerTable.setAdapter(tableAdapter);

        llLocation = (LinearLayout) view.findViewById(R.id.llLocation);
        ivLocation = (ImageView) view.findViewById(R.id.ivLocation);
        llLocationPick = (LinearLayout) view.findViewById(R.id.llLocationPick);
        spinnerLocation = (Spinner) view.findViewById(R.id.spinnerLocation);
        tvLocationTime = (TextView) view.findViewById(R.id.tvLocationTime);

        spinnerLocation.setAdapter(locationAdapter);

        llParking = (LinearLayout) view.findViewById(R.id.llParking);
        ivParking = (ImageView) view.findViewById(R.id.ivParking);
        llParkingPick = (LinearLayout) view.findViewById(R.id.llParkingPick);
        tvParkingTime = (TextView)view.findViewById(R.id.tvParkingTime);

        btnCancel = (Button) view.findViewById(R.id.btnCancel);
        btnOk = (Button) view.findViewById(R.id.btnOk);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tvCounterTime.getText().toString().equalsIgnoreCase("Pick Time")) {
                    messages.showCustomMessage("Error", "Please select Pickup time.");
                } else {
                    showConfirmationDialog();
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

//        tvCounterDate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                DatePickerDialog dd = new DatePickerDialog(getActivity(),
//                        new DatePickerDialog.OnDateSetListener() {
//
//                            @Override
//                            public void onDateSet(DatePicker view, int year,
//                                                  int monthOfYear, int dayOfMonth) {
//
//                                try {
//
//                                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
//
//                                    String dateInString = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
//
//                                    Date date = formatter.parse(dateInString);
//
//                                    tvCounterDate.setText(formatter.format(date).toString());
//
//                                } catch (Exception ex) {
//                                    ex.printStackTrace();
//                                }
//                            }
//                        }, counterYear, counterMonth, counterDay);
//                dd.show();
//            }
//        });

        tvCounterTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog td = new TimePickerDialog(getActivity(),
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                try {
                                    String dtStart = String.valueOf(hourOfDay) + ":" + String.valueOf(minute);

                                    format = new SimpleDateFormat("HH:mm");

                                    timeValue = new Time(format.parse(dtStart).getTime());

                                    tvCounterTime.setText(String.valueOf(timeValue));

                                    String amPm = hourOfDay % 12 + ":" + minute + " " + ((hourOfDay >= 12) ? "PM" : "AM");

                                    tvCounterTime.setText(amPm);

                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                            }
                        },
                        counterHour, counterMin, DateFormat.is24HourFormat(getActivity()));
                td.show();
            }
        });

        tvLocationTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog td = new TimePickerDialog(getActivity(),
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                try {
                                    String dtStart = String.valueOf(hourOfDay) + ":" + String.valueOf(minute);

                                    format = new SimpleDateFormat("HH:mm");

                                    timeValue = new Time(format.parse(dtStart).getTime());

                                    tvLocationTime.setText(String.valueOf(timeValue));

                                    String amPm = hourOfDay % 12 + ":" + minute + " " + ((hourOfDay >= 12) ? "PM" : "AM");

                                    tvLocationTime.setText(amPm);

                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                            }
                        },
                        locationHour, locationMin, DateFormat.is24HourFormat(getActivity()));
                td.show();
            }
        });

        tvParkingTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TimePickerDialog td = new TimePickerDialog(getActivity(),
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                try {
                                    String dtStart = String.valueOf(hourOfDay) + ":" + String.valueOf(minute);

                                    format = new SimpleDateFormat("HH:mm");

                                    timeValue = new Time(format.parse(dtStart).getTime());

                                    tvParkingTime.setText(String.valueOf(timeValue));

                                    String amPm = hourOfDay % 12 + ":" + minute + " " + ((hourOfDay >= 12) ? "PM" : "AM");

                                    tvParkingTime.setText(amPm);

                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                            }
                        },
                        parkingHour, parkingMin, DateFormat.is24HourFormat(getActivity()));
                td.show();
            }
        });

        llCounter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                if (isCounterSelected) {
//                    ivCounter.setImageResource(R.drawable.ic_counter);
//                } else {
//                    ivCounter.setImageResource(R.drawable.ic_counter_active);
//                }
//                isCounterSelected = !isCounterSelected;
                selectPickLayout(ivCounter, R.drawable.ic_counter_active, llCounterPick);

            }
        });
        llTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                if (isTableSelected) {
//                    ivTable.setImageResource(R.drawable.ic_table);
//                } else {
//                    ivTable.setImageResource(R.drawable.ic_table_active);
//                }
//                isTableSelected = !isTableSelected;
                selectPickLayout(ivTable, R.drawable.ic_table_active, llTablePick);

            }
        });
        llLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                if (isLocationSelected) {
//                    ivLocation.setImageResource(R.drawable.ic_designated_location);
//                } else {
//                    ivLocation.setImageResource(R.drawable.ic_designated_location_active);
//                }
//                isLocationSelected = !isLocationSelected;
                selectPickLayout(ivLocation, R.drawable.ic_designated_location_active, llLocationPick);

            }
        });
        llParking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                if (isParkingSelected) {
//                    ivParking.setImageResource(R.drawable.ic_parking);
//                } else {
//                    ivParking.setImageResource(R.drawable.ic_parking_active);
//                }
//                isParkingSelected = !isParkingSelected;
                selectPickLayout(ivParking, R.drawable.ic_parking_active, llParkingPick);

            }
        });

    }

    private void selectPickLayout(ImageView iv, int image, LinearLayout llPick) {

        ivCounter.setImageResource(R.drawable.ic_counter);
        ivTable.setImageResource(R.drawable.ic_table);
        ivLocation.setImageResource(R.drawable.ic_designated_location);
        ivParking.setImageResource(R.drawable.ic_parking);

        iv.setImageResource(image);

        llCounterPick.setVisibility(View.GONE);
        llTablePick.setVisibility(View.GONE);
        llLocationPick.setVisibility(View.GONE);
        llParkingPick.setVisibility(View.GONE);

        llPick.setVisibility(View.VISIBLE);
    }

    private void showConfirmationDialog() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_simple_message, null);
        dialogBuilder.setView(dialogView);

        TextView tvMessage = (TextView) dialogView.findViewById(R.id.tvMessage);

        tvMessage.setText("Your Pick up time is " + tvCounterTime.getText().toString());

        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        ((Button) dialogView.findViewById(R.id.btnNegative)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        ((Button) dialogView.findViewById(R.id.btnPositive)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
    }
}