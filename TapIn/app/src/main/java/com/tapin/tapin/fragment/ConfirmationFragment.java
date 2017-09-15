package com.tapin.tapin.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.tapin.tapin.R;
import com.tapin.tapin.adapter.FinalOrderSummaryAdapter;
import com.tapin.tapin.model.Business;
import com.tapin.tapin.model.CardInfo;
import com.tapin.tapin.model.OrderSummaryInfo;
import com.tapin.tapin.utils.Constant;
import com.tapin.tapin.utils.Utils;

import java.util.Calendar;

/**
 * Created by Narendra on 6/10/17.
 */

public class ConfirmationFragment extends Fragment {

    View view;

    TextView tvOrderId;

    ListView lvOrders;
    FinalOrderSummaryAdapter finalOrderSummaryAdapter;

    TextView tvOrder;
    TextView tvTotal;
    TextView tvCardDetail;
    TextView tvAverageWait;
    TextView tvRewardPoints;
    TextView tvRedeemedPoints;

    Business business;
    OrderSummaryInfo orderSummaryInfo;

    CardInfo cardInfo;
    int orderId;
    int reward_points;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_confirmation, container, false);

        finalOrderSummaryAdapter = new FinalOrderSummaryAdapter(getActivity());

        initHeader();

        initViews();

        business = Constant.business;

        if (getArguments() != null) {

            orderSummaryInfo = (OrderSummaryInfo) getArguments().getSerializable("ORDER_SUMMARY");
            orderId = getArguments().getInt("ORDER_ID");
            reward_points = getArguments().getInt("REWARD_POINTS");
            cardInfo = (CardInfo) getArguments().getSerializable("CARDINFO");

            finalOrderSummaryAdapter.addAll(orderSummaryInfo.listOrdered);

            setData();

        }

        return view;

    }

    private void setData() {

        tvOrderId.setText("Thanks For Your Order #" + orderId);

        tvOrder.setText("" + orderId);

        tvTotal.setText("$" + String.format("%.2f", orderSummaryInfo.total));

        tvAverageWait.setText(orderSummaryInfo.averageWaitTime);

        tvRewardPoints.setText("You Earned " + reward_points + " Reward Points!");

        tvRedeemedPoints.setText("You Redeemed " + orderSummaryInfo.points_redeemed + " Reward Points!");

        String cardExpiryDate = Utils.convertTime("yyyy-MM-dd", "MM/yy", cardInfo.expiration_date);

        tvCardDetail.setText(cardInfo.card_type + " xxxx xxxx xxxx " + cardInfo.cc_no.substring(cardInfo.cc_no.length() - 4) + " " + cardExpiryDate);

    }

    private void initHeader() {

        ((TextView) view.findViewById(R.id.tvToolbarTitle)).setText(getString(R.string.confirmation));

        TextView tvToolbarLeft = (TextView) view.findViewById(R.id.tvToolbarLeft);
        tvToolbarLeft.setVisibility(View.VISIBLE);
        tvToolbarLeft.setText("Done");
        tvToolbarLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                    fm.popBackStack();
                }
            }
        });
    }

    private void initViews() {

        tvOrderId = (TextView) view.findViewById(R.id.tvOrderId);

        lvOrders = (ListView) view.findViewById(R.id.lvOrders);
        lvOrders.setAdapter(finalOrderSummaryAdapter);

        tvOrder = (TextView) view.findViewById(R.id.tvOrder);
        tvTotal = (TextView) view.findViewById(R.id.tvTotal);
        tvCardDetail = (TextView) view.findViewById(R.id.tvCardDetail);
        tvAverageWait = (TextView) view.findViewById(R.id.tvAverageWait);
        tvRewardPoints = (TextView) view.findViewById(R.id.tvRewardPoints);
        tvRedeemedPoints = (TextView) view.findViewById(R.id.tvRedeemedPoints);

    }
}
