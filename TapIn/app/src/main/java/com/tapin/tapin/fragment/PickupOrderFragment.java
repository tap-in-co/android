package com.tapin.tapin.fragment;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.tapin.tapin.R;
import com.tapin.tapin.activity.HomeActivity;
import com.tapin.tapin.model.Business;
import com.tapin.tapin.model.BusinessDeliveryInfo;
import com.tapin.tapin.model.LocationInfo;
import com.tapin.tapin.model.OrderSummaryInfo;
import com.tapin.tapin.model.OrderedInfo;
import com.tapin.tapin.model.TableInfo;
import com.tapin.tapin.utils.AlertMessages;
import com.tapin.tapin.utils.Constant;
import com.tapin.tapin.utils.Debug;
import com.tapin.tapin.utils.PreferenceManager;
import com.tapin.tapin.utils.ProgressHUD;
import com.tapin.tapin.utils.RangeTimePickerDialog;
import com.tapin.tapin.utils.URLs;
import com.tapin.tapin.utils.Utils;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

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
    ImageView ivCounterDisable;
    LinearLayout llCounterPick;
    TextView tvCounterTime;

    LinearLayout llTable;
    ImageView ivTable;
    ImageView ivTableDisable;
    LinearLayout llTablePick;
    Spinner spinnerTable;

    LinearLayout llLocation;
    ImageView ivLocation;
    ImageView ivLocationDisable;
    LinearLayout llLocationPick;
    Spinner spinnerLocation;
    TextView tvLocationTime;

    LinearLayout llParking;
    ImageView ivParking;
    ImageView ivParkingDisable;
    LinearLayout llParkingPick;
    TextView tvParkingTime;

    EditText etNote;

    Button btnCancel;
    Button btnOk;

    ProgressHUD progressHUD;
    AlertMessages messages;

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

    ArrayList<OrderedInfo> listOrdered = new ArrayList<>();

    ArrayList<String> listTables = new ArrayList<>();
    ArrayAdapter<String> tableAdapter;

    ArrayList<String> listLocations = new ArrayList<>();
    ArrayAdapter<String> locationAdapter;

    Business business;

    boolean isOpened = true;
    String pickUpTime;
    String selectedTable;
    String selectedLocation;

    String currentTime;

    SimpleDateFormat dateFormat;

    String selectedOption = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_pickup_order, container, false);

        messages = new AlertMessages(getActivity());

        calendar = Calendar.getInstance();

        dateFormat = new SimpleDateFormat("HH:mm:ss");

        tableAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, listTables);
        tableAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        locationAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, listLocations);
        locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        counterYear = parkingYear = locationYear = calendar.get(Calendar.YEAR);
        counterMonth = parkingMonth = locationMonth = calendar.get(Calendar.MONTH);
        counterDay = parkingDay = locationDay = calendar.get(Calendar.DAY_OF_MONTH);

        counterHour = parkingHour = locationHour = calendar.get(Calendar.HOUR);
        counterMin = parkingMin = locationMin = calendar.get(Calendar.MINUTE);

        business = Constant.business;

        listOrdered = (ArrayList<OrderedInfo>) getArguments().getSerializable("ORDERED_LIST");

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

    private void setTime() {

        Calendar cal = Calendar.getInstance();

        currentTime = dateFormat.format(cal.getTime());

        Log.e("CURRENT_TIME", "" + currentTime);

        try {

            SimpleDateFormat dFormat = new SimpleDateFormat("hh:mm a");

            if (!Utils.isTimeBetweenTwoTime(business.opening_time, business.closing_time, currentTime)) {

                isOpened = false;

                Date inTime = new SimpleDateFormat("HH:mm:ss").parse(business.opening_time);
                Calendar calendar1 = Calendar.getInstance();
                calendar1.setTime(inTime);

                //Current Time
                Date checkTime = new SimpleDateFormat("HH:mm:ss").parse(currentTime);
                Calendar calendar3 = Calendar.getInstance();
                calendar3.setTime(checkTime);
                Date actualTime = calendar3.getTime();

                //End Time
                Date finTime = new SimpleDateFormat("HH:mm:ss").parse(business.closing_time);
                Calendar calendar2 = Calendar.getInstance();
                calendar2.setTime(finTime);

//                if (actualTime.after(calendar2.getTime())) {
//
//                    llCounter.setEnabled(false);
//                    ivCounterDisable.setVisibility(View.VISIBLE);
//
//                    llTable.performClick();
//
//                }

                try {

                    Date date = new SimpleDateFormat("HH:mm:ss").parse(business.opening_time);

//                    long t = date.getTime();
//                    Date afterAddingMins = new Date(t + (60000 * Integer.parseInt(businessDeliveryInfo.locationInfo.delivery_time_interval_in_minutes)));

                    pickUpTime = dFormat.format(date);

                    tvCounterTime.setText("" + pickUpTime);
                    tvLocationTime.setText("" + pickUpTime);
                    tvParkingTime.setText("" + pickUpTime);

                } catch (ParseException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {

                isOpened = true;

                long t = cal.getTimeInMillis();

                Date afterAddingMins;
                if (businessDeliveryInfo != null && businessDeliveryInfo.locationInfo != null && businessDeliveryInfo.locationInfo.delivery_time_interval_in_minutes != null) {
                    afterAddingMins = new Date(t + (60000 * Integer.parseInt(businessDeliveryInfo.locationInfo.delivery_time_interval_in_minutes)));
                } else {
                    afterAddingMins = new Date(t + (60000 * 15));
                }

                pickUpTime = dFormat.format(afterAddingMins);

                tvCounterTime.setText("" + pickUpTime);
                tvLocationTime.setText("" + pickUpTime);
                tvParkingTime.setText("" + pickUpTime);

            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void setData() {

        tvHotelName.setText("" + business.name);

        if (business.pickup_counter_charge.equalsIgnoreCase("-1")) {
            llCounter.setEnabled(false);
            ivCounterDisable.setVisibility(View.VISIBLE);
        }
        if (business.pickup_location_charge.equalsIgnoreCase("-1")) {
            llLocation.setEnabled(false);
            ivLocationDisable.setVisibility(View.VISIBLE);
        }
        if (business.delivery_table_charge.equalsIgnoreCase("-1")) {
            llTable.setEnabled(false);
            ivTableDisable.setVisibility(View.VISIBLE);
        }
        if (business.delivery_location_charge.equalsIgnoreCase("-1")) {
            llParking.setEnabled(false);
            ivParkingDisable.setVisibility(View.VISIBLE);
        }

        if (!business.pickup_counter_charge.equalsIgnoreCase("-1")) {
            selectPickLayout(ivCounter, R.drawable.ic_counter_active, llCounterPick, "COUNTER");
        } else if (!business.pickup_location_charge.equalsIgnoreCase("-1")) {
            selectPickLayout(ivCounter, R.drawable.ic_designated_location_active, llLocationPick, "LOCATION");
        } else if (!business.delivery_table_charge.equalsIgnoreCase("-1")) {
            selectPickLayout(ivCounter, R.drawable.ic_table_active, llTablePick, "TABLE");
        } else if (!business.delivery_location_charge.equalsIgnoreCase("-1")) {
            selectPickLayout(ivCounter, R.drawable.ic_parking_active, llParkingPick, "PARKING");
        }

    }

    private void getLocationAndTable() {

        progressHUD = ProgressHUD.show(getActivity(), getActivity().getResources().getString(R.string.please_wait), true, false);

        RequestParams params = new RequestParams();
        params.put("cmd", "get_business_delivery_info");
        params.put("business_id", business.businessID);

        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(Constant.TIMEOUT);

        String URL = URLs.BUSINESS_DELIVERY_INFO + "cmd=get_business_delivery_info&business_id=" + business.businessID;

        Log.e("URL", "" + URL);

        client.get(getActivity(), URL, params, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                try {

                    String content = new String(responseBody, "UTF-8");

                    Log.e("RES_BUSINESS_DELIV_INFO", "-" + content + "-");

                    businessDeliveryInfo = new Gson().fromJson(content, BusinessDeliveryInfo.class);

                    if (businessDeliveryInfo.tableInfo != null) {

                        TableInfo tableInfo = businessDeliveryInfo.tableInfo;

                        tvMessage.setText("" + tableInfo.message_to_consumers);

                        listTables = new ArrayList<String>();

                        for (int i = Integer.parseInt(tableInfo.table_no_min); i <= Integer.parseInt(tableInfo.table_no_max); i++) {

                            listTables.add("" + i);

                        }

                        tableAdapter.clear();
                        tableAdapter.addAll(listTables);

                        spinnerTable.setAdapter(tableAdapter);
                        spinnerTable.setSelection(0);

                        Calendar cal = Calendar.getInstance();

                        currentTime = dateFormat.format(cal.getTime());

                        Log.e("CURRENT_TIME", "" + currentTime);

                        try {
                            if (!Utils.isTimeBetweenTwoTime(tableInfo.delivery_start_time, tableInfo.delivery_end_time, currentTime)) {

                                llTable.setEnabled(false);
                                ivTableDisable.setVisibility(View.VISIBLE);

                            }

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                    }

                    if (businessDeliveryInfo.locationInfo != null) {

                        LocationInfo location = businessDeliveryInfo.locationInfo;

                        listLocations = new ArrayList<String>();

                        for (int i = 0; i < location.listLocation.size(); i++) {

                            listLocations.add(location.listLocation.get(i).location_name);

                        }
                        locationAdapter.clear();
                        locationAdapter.addAll(listLocations);

                        spinnerLocation.setAdapter(locationAdapter);
                        spinnerLocation.setSelection(0);

                        Calendar cal = Calendar.getInstance();

                        currentTime = dateFormat.format(cal.getTime());

                        Log.e("CURRENT_TIME", "" + currentTime);

                    }

                    if (progressHUD != null && progressHUD.isShowing()) {
                        progressHUD.dismiss();
                    }

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                setTime();
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
        ivCounterDisable = (ImageView) view.findViewById(R.id.ivCounterDisable);
        llCounterPick = (LinearLayout) view.findViewById(R.id.llCounterPick);
        tvCounterTime = (TextView) view.findViewById(R.id.tvCounterTime);

        llTable = (LinearLayout) view.findViewById(R.id.llTable);
        ivTable = (ImageView) view.findViewById(R.id.ivTable);
        ivTableDisable = (ImageView) view.findViewById(R.id.ivTableDisable);
        llTablePick = (LinearLayout) view.findViewById(R.id.llTablePick);
        spinnerTable = (Spinner) view.findViewById(R.id.spinnerTable);

        spinnerTable.setAdapter(tableAdapter);

        spinnerTable.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

                selectedTable = listTables.get(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        llLocation = (LinearLayout) view.findViewById(R.id.llLocation);
        ivLocation = (ImageView) view.findViewById(R.id.ivLocation);
        ivLocationDisable = (ImageView) view.findViewById(R.id.ivLocationDisable);
        llLocationPick = (LinearLayout) view.findViewById(R.id.llLocationPick);
        spinnerLocation = (Spinner) view.findViewById(R.id.spinnerLocation);
        tvLocationTime = (TextView) view.findViewById(R.id.tvLocationTime);

        spinnerLocation.setAdapter(locationAdapter);

        spinnerLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

                selectedLocation = listLocations.get(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        llParking = (LinearLayout) view.findViewById(R.id.llParking);
        ivParking = (ImageView) view.findViewById(R.id.ivParking);
        ivParkingDisable = (ImageView) view.findViewById(R.id.ivParkingDisable);
        llParkingPick = (LinearLayout) view.findViewById(R.id.llParkingPick);
        tvParkingTime = (TextView) view.findViewById(R.id.tvParkingTime);

        etNote = (EditText) view.findViewById(R.id.etNote);

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

        tvCounterTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!isOpened) {
                    messages.showCustomMessage("Sorry!", "We are not able to deliver now,\nplease Order at " + Utils.convertTime("HH:mm:ss", "hh:mm a", business.opening_time));
                } else {
                    Calendar c = Calendar.getInstance();
                    RangeTimePickerDialog td = new RangeTimePickerDialog(getActivity(),
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
                            c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE) + 5, true);

                    td.show();

                    td.setMin(Integer.parseInt(business.opening_time.split(":")[0]), Integer.parseInt(business.opening_time.split(":")[1]));
                    td.setMax(Integer.parseInt(business.closing_time.split(":")[0]), Integer.parseInt(business.closing_time.split(":")[1]));

                }
            }
        });

        tvLocationTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isOpened) {
                    messages.showCustomMessage("Sorry!", "We are not able to deliver now,\nplease Order at " + Utils.convertTime("HH:mm:ss", "hh:mm a", business.opening_time));
                } else {
                    Calendar c = Calendar.getInstance();

                    RangeTimePickerDialog td = new RangeTimePickerDialog(getActivity(),
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
                    td.setMin(Integer.parseInt(business.opening_time.split(":")[0]), Integer.parseInt(business.opening_time.split(":")[1]));
                    td.setMax(Integer.parseInt(business.closing_time.split(":")[0]), Integer.parseInt(business.closing_time.split(":")[1]));
                }
            }
        });

        tvParkingTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!isOpened) {
                    messages.showCustomMessage("Sorry!", "We are not able to deliver now,\nplease Order at " + Utils.convertTime("HH:mm:ss", "hh:mm a", business.opening_time));
                } else {
                    RangeTimePickerDialog td = new RangeTimePickerDialog(getActivity(),
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
                    td.setMin(Integer.parseInt(business.opening_time.split(":")[0]), Integer.parseInt(business.opening_time.split(":")[1]));
                    td.setMax(Integer.parseInt(business.closing_time.split(":")[0]), Integer.parseInt(business.closing_time.split(":")[1]));
                }
            }
        });

        llCounter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                selectPickLayout(ivCounter, R.drawable.ic_counter_active, llCounterPick, "COUNTER");

            }
        });
        llTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                selectPickLayout(ivTable, R.drawable.ic_table_active, llTablePick, "TABLE");

            }
        });
        llLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                selectPickLayout(ivLocation, R.drawable.ic_designated_location_active, llLocationPick, "LOCATION");

            }
        });
        llParking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                selectPickLayout(ivParking, R.drawable.ic_parking_active, llParkingPick, "PARKING");

            }
        });

    }

    private void selectPickLayout(ImageView iv, int image, LinearLayout llPick, String selected) {

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

        selectedOption = selected;
    }

    private void showConfirmationDialog() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_simple_message, null);
        dialogBuilder.setView(dialogView);

        TextView tvMessage = (TextView) dialogView.findViewById(R.id.tvMessage);

        String message = "";

        final OrderSummaryInfo orderSummaryInfo = new OrderSummaryInfo();

        orderSummaryInfo.listOrdered = listOrdered;

        if (selectedOption.equalsIgnoreCase("COUNTER")) {

            String counterTime = tvCounterTime.getText().toString();

            orderSummaryInfo.counterPickupTime = counterTime;

            orderSummaryInfo.pd_mode = "1";

            Log.e("COUNTER_TIME", "" + counterTime);

            orderSummaryInfo.pd_time = Utils.convertTime("hh:mm a", "HH:mm:ss", counterTime);

            Log.e("COUNTER_CONVERTED_TIME", "" + orderSummaryInfo.pd_time);

            String pickupTime = "Your Pickup time is " + counterTime;

            message = pickupTime;

        } else if (selectedOption.equalsIgnoreCase("TABLE")) {

            orderSummaryInfo.tableNumber = selectedTable;

            orderSummaryInfo.pd_mode = "4";

            orderSummaryInfo.pd_locations_id = selectedTable;

            orderSummaryInfo.pd_time = Utils.convertTime("hh:mm a", "HH:mm:ss", tvParkingTime.getText().toString());

            String tableNo = "Your Table Number is " + selectedTable;
            message = tableNo;

        } else if (selectedOption.equalsIgnoreCase("LOCATION")) {

            String locationDeliveryTime = tvLocationTime.getText().toString();

            orderSummaryInfo.deliveryLocation = selectedLocation;
            orderSummaryInfo.locationDeliveryTime = locationDeliveryTime;

            orderSummaryInfo.pd_mode = "8";
            orderSummaryInfo.pd_time = Utils.convertTime("hh:mm a", "HH:mm:ss", locationDeliveryTime);

            String location = "Your Delivery location is " + selectedLocation + "\n\n";
            String orderTime = "Your Order time is " + locationDeliveryTime;
            message = location + orderTime;

        } else {

            String parkingTime = tvParkingTime.getText().toString();

            orderSummaryInfo.pd_mode = "2";
            orderSummaryInfo.pd_time = Utils.convertTime("hh:mm a", "HH:mm:ss", parkingTime);
            orderSummaryInfo.parkingPickupTime = parkingTime;

            String pickupTime = "Your Pickup time is " + parkingTime;
            message = pickupTime;

        }

        tvMessage.setText("" + message);

        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        ((Button) dialogView.findViewById(R.id.btnConfirm)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();

                if (selectedOption.equalsIgnoreCase("LOCATION")) {

                    saveDeliveryInfo(orderSummaryInfo);

                } else {
                    OrderSummaryFragment fragment = new OrderSummaryFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("SELECTED_OPTION", selectedOption);
                    bundle.putSerializable("ORDER_SUMMARY", orderSummaryInfo);
                    fragment.setArguments(bundle);
                    ((HomeActivity) getActivity()).addFragment(fragment, R.id.frame_home);
                }

            }
        });

        ((Button) dialogView.findViewById(R.id.btnCancel)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
    }


    public void saveDeliveryInfo(final OrderSummaryInfo orderSummaryInfo) {

        try {

            progressHUD = ProgressHUD.show(getActivity(), getString(R.string.please_wait), true, false);

            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(Constant.TIMEOUT);

            String URL = URLs.MAIN_BASE_URL;

            final JSONObject json = new JSONObject();

            json.put("cmd", "save_consumer_delivery");
            json.put("consumer_id", PreferenceManager.getUserInfo().uid);
            json.put("delivery_address_name", selectedLocation);
            json.put("delivery_instruction", etNote.getText().toString());
            json.put("delivery_time", Utils.convertTime("hh:mm a", "HH:mm:ss", tvLocationTime.getText().toString()));

            StringEntity entity = new StringEntity(json.toString());

            Log.e("SAVE_DELIVERY_INFO", "" + json.toString());

            client.post(getActivity(), URL, entity, "application/json", new AsyncHttpResponseHandler() {

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                            if (progressHUD != null && progressHUD.isShowing()) {
                                progressHUD.dismiss();
                                progressHUD = null;
                            }

                            try {

                                String content = new String(responseBody, "UTF-8");

                                Log.e("RES_SAVE_DELIVERY_INFO", "-" + content);

                                JSONObject jsonObject = new JSONObject(content);

                                int status = jsonObject.getInt("status");

                                if (status == 1) {

                                    int consumer_delivery_id = jsonObject.getInt("consumer_delivery_id");

                                    orderSummaryInfo.consumerDeliveryId = "" + consumer_delivery_id;
                                    orderSummaryInfo.note = etNote.getText().toString();

                                    OrderSummaryFragment fragment = new OrderSummaryFragment();
                                    Bundle bundle = new Bundle();
                                    bundle.putString("SELECTED_OPTION", selectedOption);
                                    bundle.putSerializable("ORDER_SUMMARY", orderSummaryInfo);
                                    fragment.setArguments(bundle);
                                    ((HomeActivity) getActivity()).addFragment(fragment, R.id.frame_home);

                                }

                            } catch (Exception e) {

                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers,
                                              byte[] responseBody, Throwable error) {

                            if (progressHUD != null && progressHUD.isShowing()) {
                                progressHUD.dismiss();
                                progressHUD = null;
                            }

                        }
                    }

            );

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}