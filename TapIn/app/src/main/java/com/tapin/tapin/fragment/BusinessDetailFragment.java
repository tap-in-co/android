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

import com.tapin.tapin.HomeActivity;
import com.tapin.tapin.R;
import com.tapin.tapin.adapter.BusinessDetailAdapter;
import com.tapin.tapin.adapter.SlidingImage_Adapter;
import com.tapin.tapin.model.BusinessInfo;
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
    View view;

    Calendar calendar;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
    BusinessDetailAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_business_detail, null);
    }

    BusinessInfo businessInfo;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;

        businessInfo = (BusinessInfo) getArguments().getSerializable("businessInfo");
        initHeader();

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
        //  ivFood = (ImageView) view.findViewById(R.id.ivFood);
        mPager = (ViewPager) view.findViewById(R.id.pager);
        lvFoodDetail = (ListView) view.findViewById(R.id.lvFoodDetail);
        adapter = new BusinessDetailAdapter(getActivity(), Utils.getColor(businessInfo.bg_color), Utils.getColor(businessInfo.text_color));
        adapter.addAll(Constant.getBusinessList(businessInfo.customerProfileName));
        lvFoodDetail.setAdapter(adapter);

        setBusinessData(businessInfo);

        lvFoodDetail.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                if (adapter.getItem(i).display_name.equalsIgnoreCase("Catering")) {
                    CateringFragment businessDetailFragment = new CateringFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("businessInfo", businessInfo);
                    businessDetailFragment.setArguments(bundle);
                    ((HomeActivity) getActivity()).addFragment(businessDetailFragment);
                } else if (adapter.getItem(i).display_name.contains("Text")) {
                    sendSMS();
                }


            }
        });
        tvWebsite.setOnClickListener(onClickListenerWebsite);
        tvAddress.setOnClickListener(onClickListenerAddress);

    }

    Timer timer;

    private void setBusinessData(BusinessInfo businessInfo) {

        // image slider

        if (businessInfo.pictures.endsWith(",")) {
            businessInfo.pictures = businessInfo.pictures.substring(0, businessInfo.pictures.length() - 1);
        }

        final List<String> image_list = Arrays.asList((businessInfo.pictures.split("\\s*,\\s*")));
        Debug.e("Arraylist", image_list + "-");
        mPager.setAdapter(new SlidingImage_Adapter(getActivity(), businessInfo.businessID, image_list));
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
        tvAddress.setText(Utils.isNotEmpty(businessInfo.address) ? businessInfo.address : "");
        tvWebsite.setText(Utils.isNotEmpty(businessInfo.website) ? businessInfo.website : "");
        textViewBusinessType.setText(Utils.isNotEmpty(businessInfo.customerProfileName) ? businessInfo.customerProfileName : "");
        tvPaymentEmail.setText(Utils.isNotEmpty(businessInfo.neighborhood) ? businessInfo.neighborhood : "");
        tvTime.setText(Utils.getOpenTime(businessInfo.opening_time, businessInfo.closing_time));
        ratingBar.setRating((float) businessInfo.rating);
        // tvRateCount.setText(Utils.isNotEmpty(businessInfo.ti_rating) ? "("+businessInfo.ti_rating +")": "");
        // tvPrice.setText(Utils.isNotEmpty(businessInfo.website)?businessInfo.website:"");


        // open - close now
        try {
            Date dateOpening = simpleDateFormat.parse(businessInfo.opening_time);
            Date dateClosing = simpleDateFormat.parse(businessInfo.closing_time);

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


        //Glide.with(getActivity()).load(URLs.IMAGE_URL + "" + businessInfo.icon).into(ivFood);


    }

    private void initHeader() {
        final ImageView ivHeaderLogo = (ImageView) view.findViewById(R.id.ivHeaderLogo);
        final TextView tvHeaderTitle = (TextView) view.findViewById(R.id.tvHeaderTitle);
        final TextView tvHeaderLeft = (TextView) view.findViewById(R.id.tvHeaderLeft);
        final TextView tvHeaderRight = (TextView) view.findViewById(R.id.tvHeaderRight);

        ivHeaderLogo.setVisibility(View.GONE);
        tvHeaderTitle.setVisibility(View.VISIBLE);
        tvHeaderLeft.setVisibility(View.VISIBLE);
        tvHeaderRight.setVisibility(View.GONE);


        tvHeaderTitle.setText(businessInfo.name + "");

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

        String messageBody = businessInfo.name + "\n" + businessInfo.address + "\n" + businessInfo.city + "\n" + businessInfo.state + "-" + businessInfo.zipcode;


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) // At least KitKat
        {
            String defaultSmsPackageName = Telephony.Sms.getDefaultSmsPackage(getActivity()); // Need to change the build to API 19

            Intent sendIntent = new Intent(Intent.ACTION_SEND);
            sendIntent.setType("text/plain");
            sendIntent.putExtra("address", businessInfo.phone);
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
            smsIntent.putExtra("address", "" + businessInfo.phone);
            smsIntent.putExtra("sms_body", "" + messageBody);
            startActivity(smsIntent);
        }
    }
}
