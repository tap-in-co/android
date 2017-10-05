package com.tapin.tapin.fragment;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.util.Util;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.tapin.tapin.activity.HomeActivity;
import com.tapin.tapin.R;
import com.tapin.tapin.adapter.BusinessDetailAdapter;
import com.tapin.tapin.adapter.SlidingImage_Adapter;
import com.tapin.tapin.model.Business;
import com.tapin.tapin.model.GetPointsResp;
import com.tapin.tapin.model.GetPreviousOrderInfo;
import com.tapin.tapin.model.Options;
import com.tapin.tapin.model.OrderedInfo;
import com.tapin.tapin.utils.AlertMessages;
import com.tapin.tapin.utils.Constant;
import com.tapin.tapin.utils.Debug;
import com.tapin.tapin.utils.PreferenceManager;
import com.tapin.tapin.utils.ProgressHUD;
import com.tapin.tapin.utils.URLs;
import com.tapin.tapin.utils.Utils;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cz.msebera.android.httpclient.Header;


public class BusinessDetailFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ViewPager mPager;


    public BusinessDetailFragment() {
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
    public static BusinessDetailFragment newInstance(String param1, String param2) {
        BusinessDetailFragment fragment = new BusinessDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    View view;

    TextView tvToolbarLeft;
    TextView tvToolbarTitle;
    ImageView ivTitleDropDown;

    private LinearLayout cardView;
    private TextView tvAddress;
    private TextView tvPrice;
    private TextView tvWebsite;
    private TextView textViewBusinessType;
    private TextView tvPaymentEmail;
    private TextView tvTime;
    private RatingBar ratingBar;
    private TextView tvRateCount;
    private TextView textViewOpenClosed;
    private ImageView ivFood;
    private ListView lvFoodDetail;

    Timer timer;

    Calendar calendar;

    Business business;
    BusinessDetailAdapter adapter;

    ArrayList<OrderedInfo> listOrdered = new ArrayList<>();

    ProgressHUD pd;
    AlertMessages messages;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_business_detail, container, false);

        calendar = Calendar.getInstance();

        business = Constant.business;

        initHeader();

        initViews();

        setBusinessData(business);

        if (Utils.isInternetConnected(getActivity())) {

            pd = ProgressHUD.show(getActivity(), getString(R.string.please_wait), true, false);
            getPreviousOrders();
            getAllPoints();
        }

        lvFoodDetail.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if (adapter.getItem(i).display_name.equalsIgnoreCase("Catering")) {

                    CateringFragment businessDetailFragment = new CateringFragment();
//                    Bundle bundle = new Bundle();
//                    bundle.putSerializable("business", business);
//                    businessDetailFragment.setArguments(bundle);
                    ((HomeActivity) getActivity()).addFragment(businessDetailFragment, R.id.frame_home);

                } else if (adapter.getItem(i).display_name.contains("Text")) {

                    sendSMS();

                } else if (adapter.getItem(i).display_name.contains("Order Food")) {

                    MenuFoodListFragment businessDetailFragment = new MenuFoodListFragment();
//                    Bundle bundle = new Bundle();
//                    bundle.putSerializable("BUSINESS_INFO", business);
//                    businessDetailFragment.setArguments(bundle);
                    ((HomeActivity) getActivity()).addFragment(businessDetailFragment, R.id.frame_home);

                }

            }
        });

        tvWebsite.setOnClickListener(onClickListenerWebsite);
        tvAddress.setOnClickListener(onClickListenerAddress);

        return view;

    }

    private void getPreviousOrders() {

        RequestParams params = new RequestParams();
        params.put("business_id", business.businessID);
        params.put("cmd", "previous_order");
        params.put("consumer_id", PreferenceManager.getUserId());

        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(Constant.TIMEOUT);
        client.get(URLs.MAIN_BASE_URL, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                try {

                    if (pd != null && pd.isShowing()) {
                        pd.dismiss();
                        pd = null;
                    }

                    String content = new String(responseBody, "UTF-8");

                    GetPreviousOrderInfo getPreviousOrderInfo = new Gson().fromJson(content, GetPreviousOrderInfo.class);

                    listOrdered = new ArrayList<OrderedInfo>();

                    int totalQuanity = 0;

                    for (int i = 0; i < getPreviousOrderInfo.listPreviousOrder.size(); i++) {

                        totalQuanity = totalQuanity + Integer.parseInt(getPreviousOrderInfo.listPreviousOrder.get(i).quantity);

                    }

                    adapter.setPreviousOrderCount(totalQuanity);

                    for (int i = 0; i < getPreviousOrderInfo.listPreviousOrder.size(); i++) {

                        GetPreviousOrderInfo.PreviousOrder previousOrder = getPreviousOrderInfo.listPreviousOrder.get(i);

                        OrderedInfo orderedInfo = new OrderedInfo();

                        orderedInfo.businessID = business.businessID;
                        orderedInfo.item_note = previousOrder.note;
                        orderedInfo.product_id = previousOrder.product_id;
                        orderedInfo.product_description = previousOrder.product_short_description;
                        orderedInfo.product_imageurl = "";
                        orderedInfo.product_name = previousOrder.product_name;
                        orderedInfo.price = Double.parseDouble(previousOrder.price);
                        orderedInfo.quantity = Integer.parseInt(previousOrder.quantity);
                        ArrayList<String> product_option = new ArrayList<String>();
                        ArrayList<String> selected_ProductID_array = new ArrayList<String>();
                        ArrayList<String> selectedOptionData = new ArrayList<>();

                        for (int j = 0; j < previousOrder.listOptions.size(); j++) {

                            Options.OptionData optionData = previousOrder.listOptions.get(i);

                            selected_ProductID_array.add(optionData.option_id);
                            product_option.add(optionData.name + "(" + optionData.price + ")");
                            selectedOptionData.add(optionData.option_id);

                        }

                        orderedInfo.product_option = TextUtils.join(",", product_option);
                        Log.e("PRODUCT_OPTION", "" + orderedInfo.product_option);
                        orderedInfo.selected_ProductID_array = TextUtils.join(",", selected_ProductID_array);
                        Log.e("PRODUCT_OPTION_ARRAY", "" + orderedInfo.selected_ProductID_array);
                        Log.e("PRODUCT_OPTION_PRICE", "" + orderedInfo.price);
                        orderedInfo.listOptions = selectedOptionData;
                        orderedInfo.points = Math.round(orderedInfo.price);

                        listOrdered.add(orderedInfo);

                    }

                    if (getActivity() != null)
//                        ((HomeActivity) getActivity()).refreshPointsFragment();
                        Debug.e("getAllPoint", content + "-");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                try{
                    if (pd != null && pd.isShowing()) {
                        pd.dismiss();
                        pd = null;
                    }

                    Log.e("getAllPoint fail", responseBody + "-");
                }catch (Exception e){
                    e.printStackTrace();
                }

            }

            @Override
            public void onFinish() {
                super.onFinish();
            }
        });
    }


    private void setBusinessData(Business business) {

        if (business.pictures.endsWith(",")) {
            business.pictures = business.pictures.substring(0, business.pictures.length() - 1);
        }

        final List<String> image_list = Arrays.asList((business.pictures.split("\\s*,\\s*")));
        Debug.e("Arraylist", image_list + "-");
        mPager.setAdapter(new SlidingImage_Adapter(getActivity(), business.businessID, image_list));
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

        tvAddress.setText(Utils.isNotEmpty(business.address) ? business.address : "");
        tvWebsite.setText(Utils.isNotEmpty(business.website) ? business.website : "");
        textViewBusinessType.setText(Utils.isNotEmpty(business.customerProfileName) ? business.customerProfileName : "");
        tvPaymentEmail.setText(Utils.isNotEmpty(business.neighborhood) ? business.neighborhood : "");
        tvTime.setText(Utils.getOpenTime(business.opening_time, business.closing_time));
        ratingBar.setRating((float) business.rating);
        // tvRateCount.setText(Utils.isNotEmpty(business.ti_rating) ? "("+business.ti_rating +")": "");
        // tvPrice.setText(Utils.isNotEmpty(business.website)?business.website:"");

        try {

            SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
            String currentTime = df.format(calendar.getTime());

            if (!Utils.isTimeBetweenTwoTime(business.opening_time, business.closing_time, currentTime)) {
                textViewOpenClosed.setText("NOW CLOSED");
                textViewOpenClosed.setTextColor(ContextCompat.getColor(getActivity(), R.color.gray));
            } else {
                textViewOpenClosed.setText("OPEN NOW");
                textViewOpenClosed.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    private void initHeader() {

        tvToolbarLeft = (TextView) view.findViewById(R.id.tvToolbarLeft);
        tvToolbarTitle = (TextView) view.findViewById(R.id.tvToolbarTitle);
        ivTitleDropDown = (ImageView) view.findViewById(R.id.ivTitleDropDown);

        tvToolbarLeft.setVisibility(View.VISIBLE);
        tvToolbarLeft.setText("Back");

        tvToolbarLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        tvToolbarTitle.setText(business.short_name + "");

        tvToolbarLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

    }

    private void initViews() {

        cardView = (LinearLayout) view.findViewById(R.id.card_view);
        tvAddress = (TextView) view.findViewById(R.id.tvAddress);
        tvPrice = (TextView) view.findViewById(R.id.tvPrice);
        tvWebsite = (TextView) view.findViewById(R.id.tvWebsite);
        textViewBusinessType = (TextView) view.findViewById(R.id.textViewBusinessType);
        tvPaymentEmail = (TextView) view.findViewById(R.id.tvPaymentEmail);
        tvTime = (TextView) view.findViewById(R.id.tvTime);
        ratingBar = (RatingBar) view.findViewById(R.id.ratingBar);
        tvRateCount = (TextView) view.findViewById(R.id.tvRateCount);
        textViewOpenClosed = (TextView) view.findViewById(R.id.textViewOpenClosed);

        mPager = (ViewPager) view.findViewById(R.id.pager);
        lvFoodDetail = (ListView) view.findViewById(R.id.lvFoodDetail);

        adapter = new BusinessDetailAdapter(getActivity(), business.bg_color, business.text_color, new BusinessDetailAdapter.PreviousOrderClickListener() {
            @Override
            public void clicked(String s) {

                if (s.equalsIgnoreCase("My Order")) {
                    OrderFragment orderFragment = new OrderFragment();
                    Constant.listOrdered = listOrdered;
                    Bundle bundle = new Bundle();
//                bundle.putSerializable("BUSINESS_INFO", business);
                    bundle.putSerializable("ORDERED_LIST", (Serializable) listOrdered);
                    orderFragment.setArguments(bundle);
                    ((HomeActivity) getActivity()).addFragment(orderFragment, R.id.frame_home);

                } else if (s.equalsIgnoreCase("Menu")) {

                    MenuFoodListFragment menuFoodListFragment = new MenuFoodListFragment();
                    Bundle bundle = new Bundle();
//                bundle.putSerializable("BUSINESS_INFO", business);
                    bundle.putSerializable("ORDERED_LIST", (Serializable) listOrdered);
                    menuFoodListFragment.setArguments(bundle);
                    ((HomeActivity) getActivity()).addFragment(menuFoodListFragment, R.id.frame_home);
                }


            }
        });
        adapter.addAll(Constant.getBusinessList(business.customerProfileName));
        lvFoodDetail.setAdapter(adapter);

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

    View.OnClickListener onClickListenerWebsite = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            String url = tvWebsite.getText().toString();
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        }
    };

    private void sendSMS() {

        String messageBody = business.name + "\n" + business.address + "\n" + business.city + "\n" + business.state + "-" + business.zipcode;


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) // At least KitKat
        {
            String defaultSmsPackageName = Telephony.Sms.getDefaultSmsPackage(getActivity()); // Need to change the build to API 19

            Intent sendIntent = new Intent(Intent.ACTION_SEND);
            sendIntent.setType("text/plain");
            sendIntent.putExtra("address", business.phone);
            sendIntent.putExtra(Intent.EXTRA_TEXT, messageBody);

            if (defaultSmsPackageName != null)// Can be null in case that there is no default, then the user would be able to choose
            // any app that support this intent.
            {
                sendIntent.setPackage(defaultSmsPackageName);
            }
            startActivity(sendIntent);

        } else // For early versions, do what worked for you before.
        {
            Intent smsIntent = new Intent(android.content.Intent.ACTION_VIEW);
            smsIntent.setType("vnd.android-dir/mms-sms");
            smsIntent.putExtra("address", "" + business.phone);
            smsIntent.putExtra("sms_body", "" + messageBody);
            startActivity(smsIntent);
        }
    }

    private void getAllPoints() {

        RequestParams params = new RequestParams();
        params.put("businessID", business.businessID);
        params.put("cmd", "get_all_points");
        params.put("consumerID", PreferenceManager.getUserId());


        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(Constant.TIMEOUT);
        client.post(URLs.REWARD, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                try {
                    String content = new String(responseBody, "UTF-8");
                    GetPointsResp userInfo = new Gson().fromJson(content, GetPointsResp.class);
                    PreferenceManager.putPointsData(userInfo);
                    if (getActivity() != null)
//                        ((HomeActivity) getActivity()).refreshPointsFragment();
                        Debug.e("getAllPoint", content + "-");
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
}
