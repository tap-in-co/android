package com.tapin.tapin.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.tapin.tapin.R;
import com.tapin.tapin.activity.HomeActivity;
import com.tapin.tapin.model.Business;
import com.tapin.tapin.model.OrderSummaryInfo;
import com.tapin.tapin.model.OrderedInfo;
import com.tapin.tapin.utils.Constant;

import java.util.ArrayList;

/**
 * Created by Narendra on 5/28/17.
 */

public class OrderSummaryFragment extends Fragment {

    View view;

    TextView tvHotelName;
    TextView tvDeliveryTo;

    TextView tvSubTotal;
    TextView tvTotalPoints;

    TextView tvDeliveryCharge;
    TextView tvPromotionalCode;
    TextView tvPromotionalDiscount;

    TextView tvNoTip;
    TextView tv10;
    TextView tv15;
    TextView tv20;

    TextView tvTax;
    TextView tvTotal;

    double subTotal = 0;
    double deliveryCharge = 0;
    double promotionalDiscount = 0;
    double tip10 = 0;
    double tip15 = 0;
    double tip20 = 0;
    double selectedTip = 0;
    double tax = 0;
    double total;

    Business business;

    String selectedOption;
    OrderSummaryInfo orderSummaryInfo;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_order_summary, container, false);

        initHeader();

        initViews();

        business = Constant.business;

        if (getArguments() != null) {

            selectedOption = getArguments().getString("SELECTED_OPTION");

            orderSummaryInfo = (OrderSummaryInfo) getArguments().getSerializable("ORDER_SUMMARY");

            setData();

        }

        return view;

    }

    private void setData() {

        tvHotelName.setText("" + business.name);

        for (int i = 0; i < orderSummaryInfo.listOrdered.size(); i++) {

            subTotal = subTotal + (orderSummaryInfo.listOrdered.get(i).quantity * orderSummaryInfo.listOrdered.get(i).price);

        }

        if (selectedOption.equalsIgnoreCase("COUNTER")) {

            tvDeliveryTo.setText("Pick up your food from counter at " + orderSummaryInfo.counterPickupTime);

            deliveryCharge = Double.parseDouble(business.pickup_counter_charge);

        } else if (selectedOption.equalsIgnoreCase("TABLE")) {

            tvDeliveryTo.setText("Your Food will be deliver at table number " + orderSummaryInfo.tableNumber);

            if (business.delivery_table_charge != null && business.delivery_table_charge.length() > 0) {
                String tableCharge = business.delivery_table_charge.replace("%", "");
                deliveryCharge = subTotal * 0.01 * Double.parseDouble(tableCharge);
            }

        } else if (selectedOption.equalsIgnoreCase("LOCATION")) {

            tvDeliveryTo.setText("Your food will be deliver at " + orderSummaryInfo.deliveryLocation);

            deliveryCharge = Double.parseDouble(business.delivery_location_charge);

        } else if (selectedOption.equalsIgnoreCase("PARKING")) {

            tvDeliveryTo.setText("Pick up your food at " + orderSummaryInfo.parkingPickupTime + " from Parking.");

            deliveryCharge = Double.parseDouble(business.pickup_location_charge);

        }

        tvSubTotal.setText("$" + String.format("%.2f", subTotal));

        tvDeliveryCharge.setText("$" + String.format("%.2f", deliveryCharge));

        tvTotalPoints.setText("Earn " + Math.round(subTotal) + " Pts");

        if (business.promotion_code != null && business.promotion_code.length() > 0) {

            tvPromotionalCode.setText("" + business.promotion_code);

            promotionalDiscount = Double.parseDouble(business.promotion_discount_amount);

            if (promotionalDiscount > subTotal) {

                promotionalDiscount = subTotal;

            }

            tvPromotionalDiscount.setText("$" + String.format("%.2f", promotionalDiscount));

        }

        tip10 = subTotal * 0.10;
        tip15 = subTotal * 0.15;
        tip20 = subTotal * 0.20;

        tv10.setText("$" + String.format("%.2f", tip10));
        tv15.setText("$" + String.format("%.2f", tip15));
        tv20.setText("$" + String.format("%.2f", tip20));

        if (business.tax_rate != null && business.tax_rate.length() > 0) {

            tax = subTotal * Double.parseDouble(business.tax_rate) * 0.01;
            tvTax.setText("$" + String.format("%.2f", tax));

        }

        tvNoTip.performClick();

    }

    private void selectTip(TextView tv, double tip) {

        tvNoTip.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.white));
        tv10.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.white));
        tv15.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.white));
        tv20.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.white));

        tvNoTip.setTextColor(ContextCompat.getColor(getActivity(), R.color.black));
        tv10.setTextColor(ContextCompat.getColor(getActivity(), R.color.black));
        tv15.setTextColor(ContextCompat.getColor(getActivity(), R.color.black));
        tv20.setTextColor(ContextCompat.getColor(getActivity(), R.color.black));

        tv.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
        tv.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));

        total = subTotal + deliveryCharge - promotionalDiscount + tip + tax;

        if (total < 0) {
            total = 0;
        }

        selectedTip = tip;

        tvTotal.setText("$" + String.format("%.2f", total));

    }

    private void initHeader() {

        ((TextView) view.findViewById(R.id.tvToolbarTitle)).setText(getString(R.string.order_summary));

        TextView tvToolbarLeft = (TextView) view.findViewById(R.id.tvToolbarLeft);
        tvToolbarLeft.setVisibility(View.VISIBLE);
        tvToolbarLeft.setText("Back");
        tvToolbarLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

    }

    private void initViews() {

        tvHotelName = (TextView) view.findViewById(R.id.tvHotelName);
        tvDeliveryTo = (TextView) view.findViewById(R.id.tvDeliveryTo);

        tvSubTotal = (TextView) view.findViewById(R.id.tvSubTotal);
        tvTotalPoints = (TextView) view.findViewById(R.id.tvTotalPoints);
        tvDeliveryCharge = (TextView) view.findViewById(R.id.tvDeliveryCharge);
        tvPromotionalCode = (TextView) view.findViewById(R.id.tvPromotionalCode);
        tvPromotionalDiscount = (TextView) view.findViewById(R.id.tvPromotionalDiscount);

        tvNoTip = (TextView) view.findViewById(R.id.tvNoTip);
        tv10 = (TextView) view.findViewById(R.id.tv10);
        tv15 = (TextView) view.findViewById(R.id.tv15);
        tv20 = (TextView) view.findViewById(R.id.tv20);

        tvTax = (TextView) view.findViewById(R.id.tvTax);

        tvTotal = (TextView) view.findViewById(R.id.tvTotal);

        tvNoTip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectTip(tvNoTip, 0);
            }
        });

        tv10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectTip(tv10, tip10);
            }
        });

        tv15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectTip(tv15, tip15);
            }
        });

        tv20.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectTip(tv20, tip20);
            }
        });

        ((Button) view.findViewById(R.id.btnProceed)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                orderSummaryInfo.promotion_code = tvPromotionalCode.getText().toString();
                orderSummaryInfo.total = total;
                orderSummaryInfo.business_id = business.businessID;
                orderSummaryInfo.points_dollar_amount = business.businessID;
                orderSummaryInfo.tax_amount = tax;
                orderSummaryInfo.subtotal = subTotal;
                orderSummaryInfo.tip_amount = selectedTip;
                orderSummaryInfo.promotion_discount_amount = promotionalDiscount;
                orderSummaryInfo.delivery_charge_amount = deliveryCharge;

                PaymentFragment fragment = new PaymentFragment();
                Bundle bundle = new Bundle();
                bundle.putString("SELECTED_OPTION", selectedOption);
                bundle.putSerializable("ORDER_SUMMARY", orderSummaryInfo);
                fragment.setArguments(bundle);
                ((HomeActivity) getActivity()).addFragment(fragment, R.id.frame_home);

            }
        });

    }
}
