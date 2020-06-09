package com.tapin.tapin.fragment;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
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

import androidx.viewpager.widget.ViewPager;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.tapin.tapin.App;
import com.tapin.tapin.R;
import com.tapin.tapin.activity.HomeActivity;
import com.tapin.tapin.adapter.BusinessDetailAdapter;
import com.tapin.tapin.adapter.SlidingImage_Adapter;
import com.tapin.tapin.model.GetPointsResp;
import com.tapin.tapin.model.GetPreviousOrderInfo;
import com.tapin.tapin.model.Options;
import com.tapin.tapin.model.OrderedInfo;
import com.tapin.tapin.model.market.Market;
import com.tapin.tapin.model.resturants.Business;
import com.tapin.tapin.utils.*;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cz.msebera.android.httpclient.Header;


public class BusinessDetailFragment extends BaseFragment {
    View view;
    TextView tvToolbarLeft;
    TextView tvToolbarTitle;
    ImageView ivTitleDropDown;
    Timer timer;
    Calendar calendar;
    Market market;
    Business business;
    BusinessDetailAdapter adapter;
    ArrayList<OrderedInfo> listOrdered = new ArrayList<>();
    ProgressHUD pd;
    AlertMessages messages;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ViewPager mPager;
    private LinearLayout cardView;
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
    private ImageView ivFood;
    private ListView lvFoodDetail;

    TextView pickupOn;
    TextView cutOffOrdering;
    TextView pickupLocation;
    TextView hours;

    public BusinessDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BusinessDetailFragment newInstance(final Business business, final Market market) {
        BusinessDetailFragment fragment = new BusinessDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable("market", market);
        args.putSerializable("business", business);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_business_detail, container, false);

        calendar = Calendar.getInstance();

        //business = Constant.business;

        messages = new AlertMessages(getActivity());

        business = (Business) getArguments().get("business");
        market = (Market) getArguments().get("market");

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
////                    bundle.putSerializable("BUSINESS_INFO", business);
////                    businessDetailFragment.setArguments(bundle);
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
        params.put("business_id", business.getBusinessID());
        params.put("cmd", "previous_order");
        params.put("consumer_id", ((App)requireActivity().getApplication()).getProfile().getUid());

        Debug.d("Okhttp", "API: " + UrlGenerator.INSTANCE.getMainUrl() + " " + params.toString());

        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(Constant.TIMEOUT);
        client.get(UrlGenerator.INSTANCE.getMainUrl(), params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                if (pd != null && pd.isShowing()) {
                    pd.dismiss();
                    pd = null;
                }

                String content = new String(responseBody, StandardCharsets.UTF_8);
                Debug.d("Okhttp", "Success Response: " + content);

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

                    orderedInfo.businessID = business.getBusinessID();
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
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                try {
                    if (pd != null && pd.isShowing()) {
                        pd.dismiss();
                        pd = null;
                    }

                    String content = new String(responseBody, StandardCharsets.UTF_8);
                    Debug.d("Okhttp", "Failure Response: " + content);
                } catch (Exception e) {
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

        if (business.getPictures().endsWith(",")) {
            //business.setPictures(business.getPictures().substring(0, business.getPictures().length() - 1));
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

        tvAddress.setText(Utils.isNotEmpty(business.getAddress()) ? business.getAddress() : market.getAddress());
        tvWebsite.setText(Utils.isNotEmpty(business.getWebsite()) ? business.getWebsite() : market.getWebsite());
        //textViewBusinessType.setText(Utils.isNotEmpty(business.getCustomerProfileName()) ? business.getCustomerProfileName() : "");
        //tvPaymentEmail.setText(Utils.isNotEmpty(business.getNeighborhood()) ? business.getNeighborhood() : "");
        //tvTime.setText(Utils.getOpenTime(business.getOpeningTime(), business.getClosingTime()));
        //ratingBar.setRating(Float.parseFloat(business.getRating()));

        // opening_time & closing_time is not applicable for Corp Order
        /*if (business.getOpeningTime() != null && business.getClosingTime() != null) {
            try {

                SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
                String currentTime = df.format(calendar.getTime());

                if (!Utils.isTimeBetweenTwoTime(business.getOpeningTime(), business.getClosingTime(), currentTime)) {
                    textViewOpenClosed.setText("NOW CLOSED");
                    textViewOpenClosed.setTextColor(ContextCompat.getColor(getActivity(), R.color.gray));
                } else {
                    textViewOpenClosed.setText("OPEN NOW");
                    textViewOpenClosed.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }*/

    }

    private void initHeader() {

        tvToolbarLeft = view.findViewById(R.id.tvToolbarLeft);
        tvToolbarTitle = view.findViewById(R.id.tvToolbarTitle);
        ivTitleDropDown = view.findViewById(R.id.ivTitleDropDown);

        tvToolbarLeft.setVisibility(View.VISIBLE);
        tvToolbarLeft.setText("Back");

        tvToolbarLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        //tvToolbarTitle.setText(business.getShortName() + "");

        tvToolbarLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

    }

    private void initViews() {

        cardView = view.findViewById(R.id.card_view);
        tvAddress = view.findViewById(R.id.tvAddress);
        tvPrice = view.findViewById(R.id.tvPrice);
        tvWebsite = view.findViewById(R.id.tvWebsite);
        textViewBusinessType = view.findViewById(R.id.textViewBusinessType);
        tvPaymentEmail = view.findViewById(R.id.tvPaymentEmail);
        tvTime = view.findViewById(R.id.tvTime);
        ratingBar = view.findViewById(R.id.ratingBar);
        tvRateCount = view.findViewById(R.id.tvRateCount);
        textViewOpenClosed = view.findViewById(R.id.textViewOpenClosed);

        pickupOn = view.findViewById(R.id.pick_up_on_text);
        pickupOn.setText(market.getPickupDate() + " " + StringExtensionKt.toHoursAndMinutes(market.getDriverPickupTime()));
        cutOffOrdering = view.findViewById(R.id.cut_ordering_text);
        cutOffOrdering.setText(market.getCutoffDate() + " " + StringExtensionKt.toHoursAndMinutes(market.getCutoffTime()));
        pickupLocation = view.findViewById(R.id.pick_up_location_text);
        pickupLocation.setText(market.getDeliveryLocation());
        hours = view.findViewById(R.id.hours_text);
        hours.setText("Hours\n" + market.getMarketOpenHours());

        mPager = view.findViewById(R.id.pager);
        lvFoodDetail = view.findViewById(R.id.lvFoodDetail);

        adapter = new BusinessDetailAdapter(getActivity(), business.getBgColor(), business.getTextColor(), new BusinessDetailAdapter.PreviousOrderClickListener() {
            @Override
            public void clicked(String s) {

                if (s.equalsIgnoreCase("My Order")) {
                    OrderFragment orderFragment = new OrderFragment();
                    Constant.listOrdered = listOrdered;
                    Bundle bundle = new Bundle();
//                bundle.putSerializable("BUSINESS_INFO", business);
                    bundle.putSerializable("ORDERED_LIST", listOrdered);
                    orderFragment.setArguments(bundle);
                    ((HomeActivity) getActivity()).addFragment(orderFragment, R.id.frame_home);

                } else if (s.equalsIgnoreCase("Menu")) {

                    MenuFoodListFragment menuFoodListFragment = new MenuFoodListFragment();
                    Bundle bundle = new Bundle();
//                bundle.putSerializable("BUSINESS_INFO", business);
                    bundle.putSerializable("ORDERED_LIST", listOrdered);
                    menuFoodListFragment.setArguments(bundle);
                    ((HomeActivity) getActivity()).addFragment(menuFoodListFragment, R.id.frame_home);
                }


            }
        });
        adapter.addAll(Constant.getBusinessList(business.getCustomerProfileName()));
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

    private void sendSMS() {

        String messageBody = business.getName() + "\n" + business.getAddress() + "\n" + business.getCity() + "\n" + business.getState() + "-" + business.getZipcode();


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) // At least KitKat
        {
            String defaultSmsPackageName = Telephony.Sms.getDefaultSmsPackage(getActivity()); // Need to change the build to API 19

            Intent sendIntent = new Intent(Intent.ACTION_SEND);
            sendIntent.setType("text/plain");
            sendIntent.putExtra("address", business.getPhone());
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
            smsIntent.putExtra("address", "" + business.getPhone());
            smsIntent.putExtra("sms_body", "" + messageBody);
            startActivity(smsIntent);
        }
    }

    private void getAllPoints() {

        RequestParams params = new RequestParams();
        params.put("businessID", business.getBusinessID());
        params.put("cmd", "get_all_points");
        params.put("consumerID", ((App)requireActivity().getApplication()).getProfile().getUid());

        Debug.d("Okhttp", "API: " + UrlGenerator.INSTANCE.getRewardApi() + " " + params.toString());

        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(Constant.TIMEOUT);
        client.post(UrlGenerator.INSTANCE.getRewardApi(), params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                String content = new String(responseBody, StandardCharsets.UTF_8);
                Debug.d("Okhttp", "Success Response: " + content);
                GetPointsResp userInfo = new Gson().fromJson(content, GetPointsResp.class);
                //PreferenceManager.putPointsData(userInfo);
                ((App) getContext().getApplicationContext()).putPointsData(userInfo);
                if (getActivity() != null)
//                        ((HomeActivity) getActivity()).refreshPointsFragment();
                    Debug.e("getAllPoint", content + "-");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                try {
                    String content = new String(responseBody, StandardCharsets.UTF_8);
                    Debug.d("Okhttp", "Failure Response: " + content);
                } catch (Exception e) {

                }
                error.printStackTrace();
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }
        });
    }
}
