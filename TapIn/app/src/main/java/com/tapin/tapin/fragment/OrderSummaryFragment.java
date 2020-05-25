package com.tapin.tapin.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.tapin.tapin.R;
import com.tapin.tapin.activity.HomeActivity;
import com.tapin.tapin.model.OrderSummaryInfo;
import com.tapin.tapin.model.resturants.Business;
import com.tapin.tapin.utils.Constant;
import com.tapin.tapin.utils.PreferenceManager;

/**
 * Created by Narendra on 5/28/17.
 */

public class OrderSummaryFragment extends BaseFragment {

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

        tvHotelName.setText("" + business.getName());

        for (int i = 0; i < orderSummaryInfo.listOrdered.size(); i++) {

            subTotal = subTotal + (orderSummaryInfo.listOrdered.get(i).quantity * orderSummaryInfo.listOrdered.get(i).price);

        }

        if (selectedOption.equalsIgnoreCase("COUNTER")) {

            tvDeliveryTo.setText("Pick up your food from counter at " + orderSummaryInfo.counterPickupTime);

            deliveryCharge = Double.parseDouble(business.getPickupCounterCharge());

        } else if (selectedOption.equalsIgnoreCase("TABLE")) {

            tvDeliveryTo.setText("Your Food will be deliver at table number " + orderSummaryInfo.tableNumber);

            if (business.getDeliveryTableCharge() != null && business.getDeliveryTableCharge().length() > 0) {
                String tableCharge = business.getDeliveryTableCharge().replace("%", "");
                deliveryCharge = subTotal * 0.01 * Double.parseDouble(tableCharge);
            }

        } else if (selectedOption.equalsIgnoreCase("LOCATION")) {

            tvDeliveryTo.setText("Your food will be deliver at " + orderSummaryInfo.deliveryLocation);

            deliveryCharge = Double.parseDouble(business.getDeliveryLocationCharge());

        } else if (selectedOption.equalsIgnoreCase("PARKING")) {

            tvDeliveryTo.setText("Pick up your food at " + orderSummaryInfo.parkingPickupTime + " from Parking.");

            deliveryCharge = Double.parseDouble(business.getPickupLocationCharge());

        }
        // For Corporate Order
        else {
            if (PreferenceManager.getInstance().getSelectedCorporateDomain() != null) {
                tvDeliveryTo.setText(orderSummaryInfo.deliveryLocation + " at " + orderSummaryInfo.locationDeliveryTime);
                deliveryCharge = Double.parseDouble(business.getDeliveryLocationCharge());
            }
        }

        tvSubTotal.setText("" + business.getCurrSymbol() + String.format("%.2f", subTotal));

        tvDeliveryCharge.setText("" + business.getCurrSymbol() + String.format("%.2f", deliveryCharge));

        tvTotalPoints.setText("Earn " + Math.round(subTotal) + " Pts");

        /*try {
            if (business.getCurrSymbol() != null && business.getPromotionCode().length() > 0) {

                tvPromotionalCode.setText("" + business.getCurrSymbol());

                promotionalDiscount = Double.parseDouble(business.getPromotionDiscountAmount());

                if (promotionalDiscount > subTotal) {

                    promotionalDiscount = subTotal;

                }

                tvPromotionalDiscount.setText("" + business.getCurrSymbol() + String.format("%.2f", promotionalDiscount));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        tip10 = subTotal * 0.10;
        tip15 = subTotal * 0.15;
        tip20 = subTotal * 0.20;

        tv10.setText("" + business.getCurrSymbol() + String.format("%.2f", tip10));
        tv15.setText("" + business.getCurrSymbol() + String.format("%.2f", tip15));
        tv20.setText("" + business.getCurrSymbol() + String.format("%.2f", tip20));

        if (business.getTaxRate() != null && business.getTaxRate().length() > 0) {

            tax = subTotal * Double.parseDouble(business.getTaxRate()) * 0.01;
            tvTax.setText("" + business.getCurrSymbol() + String.format("%.2f", tax));

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

        tvTotal.setText("" + business.getCurrSymbol() + String.format("%.2f", total));

    }

    private void initHeader() {

        ((TextView) view.findViewById(R.id.tvToolbarTitle)).setText(getString(R.string.order_summary));

        TextView tvToolbarLeft = view.findViewById(R.id.tvToolbarLeft);
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

        tvHotelName = view.findViewById(R.id.tvHotelName);
        tvDeliveryTo = view.findViewById(R.id.tvDeliveryTo);

        tvSubTotal = view.findViewById(R.id.tvSubTotal);
        tvTotalPoints = view.findViewById(R.id.tvTotalPoints);
        tvDeliveryCharge = view.findViewById(R.id.tvDeliveryCharge);
        tvPromotionalCode = view.findViewById(R.id.tvPromotionalCode);
        tvPromotionalDiscount = view.findViewById(R.id.tvPromotionalDiscount);

        tvNoTip = view.findViewById(R.id.tvNoTip);
        tv10 = view.findViewById(R.id.tv10);
        tv15 = view.findViewById(R.id.tv15);
        tv20 = view.findViewById(R.id.tv20);

        tvTax = view.findViewById(R.id.tvTax);

        tvTotal = view.findViewById(R.id.tvTotal);

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

        view.findViewById(R.id.btnProceed).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                orderSummaryInfo.promotion_code = tvPromotionalCode.getText().toString();
                orderSummaryInfo.total = total;
                orderSummaryInfo.business_id = business.getBusinessID();
                orderSummaryInfo.points_dollar_amount = business.getBusinessID();
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
