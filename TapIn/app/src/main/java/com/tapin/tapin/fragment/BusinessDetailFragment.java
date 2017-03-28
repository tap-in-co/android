package com.tapin.tapin.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tapin.tapin.R;
import com.tapin.tapin.model.BusinessInfo;
import com.tapin.tapin.utils.URLs;
import com.tapin.tapin.utils.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class BusinessDetailFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_business_detail, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;
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
        ivFood = (ImageView) view.findViewById(R.id.ivFood);
        lvFoodDetail = (ListView) view.findViewById(R.id.lvFoodDetail);

        BusinessInfo businessInfo = (BusinessInfo) getArguments().getSerializable("businessInfo");

        setBusinessData(businessInfo);
    }

    private void setBusinessData(BusinessInfo businessInfo) {
        tvAddress.setText(Utils.isNotEmpty(businessInfo.address) ? businessInfo.address : "");
//        tvPrice.setText(Utils.isNotEmpty(businessInfo.website)?businessInfo.website:"");
        tvWebsite.setText(Utils.isNotEmpty(businessInfo.website) ? businessInfo.website : "");
        textViewBusinessType.setText(Utils.isNotEmpty(businessInfo.customerProfileName) ? businessInfo.customerProfileName : "");
        tvPaymentEmail.setText(Utils.isNotEmpty(businessInfo.neighborhood) ? businessInfo.neighborhood : "");
        tvTime.setText(Utils.isNotEmpty(businessInfo.address) ? businessInfo.address : "");

        ratingBar = (RatingBar) view.findViewById(R.id.ratingBar);

        tvRateCount.setText(Utils.isNotEmpty(businessInfo.address) ? businessInfo.address : "");
        textViewOpenClosed.setText(Utils.isNotEmpty(businessInfo.address) ? businessInfo.address : "");

        try {
            Date dateOpening = simpleDateFormat.parse(businessInfo.opening_time);
            Date dateClosing = simpleDateFormat.parse(businessInfo.closing_time);

            Calendar calendarOpening = Calendar.getInstance();
            calendarOpening.setTimeInMillis(dateOpening.getTime());

            Calendar calendarClosing = Calendar.getInstance();
            calendarClosing.setTimeInMillis(dateClosing.getTime());

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


        ratingBar.setRating((float) businessInfo.rating);
        Glide.with(getActivity()).load(URLs.IMAGE_URL + "" + businessInfo.icon).into(ivFood);


        lvFoodDetail = (ListView) view.findViewById(R.id.lvFoodDetail);
    }

}
