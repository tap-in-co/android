package com.tapin.tapin.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.tapin.tapin.R;
import com.tapin.tapin.activity.HomeActivity;
import com.tapin.tapin.adapter.FinalOrderSummaryAdapter;
import com.tapin.tapin.adapter.FinalOrderSummaryAdapterNew;
import com.tapin.tapin.model.CardInfo;
import com.tapin.tapin.model.OrderSummaryInfo;
import com.tapin.tapin.model.resturants.Business;
import com.tapin.tapin.utils.Constant;
import com.tapin.tapin.utils.Utils;

/**
 * Created by Narendra on 6/10/17.
 */

public class ConfirmationFragment extends BaseFragment {

    View view;

    TextView tvOrderId;

    ListView lvOrders;
    //FinalOrderSummaryAdapter finalOrderSummaryAdapter;
    FinalOrderSummaryAdapterNew adapterNew;

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

        initHeader();

        initViews();

        business = Constant.business;

        //finalOrderSummaryAdapter = new FinalOrderSummaryAdapter(getActivity(), business);
        adapterNew = new FinalOrderSummaryAdapterNew(business);

        if (getArguments() != null) {

            orderSummaryInfo = (OrderSummaryInfo) getArguments().getSerializable("ORDER_SUMMARY");
            orderId = getArguments().getInt("ORDER_ID");
            reward_points = getArguments().getInt("REWARD_POINTS");
            cardInfo = (CardInfo) getArguments().getSerializable("CARDINFO");

            //finalOrderSummaryAdapter.addAll(orderSummaryInfo.listOrdered);
            adapterNew.submitList(orderSummaryInfo.listOrdered);

            setData();

        }

        return view;

    }

    private void setData() {

        tvOrderId.setText("Thanks For Your Order #" + orderId);

        tvOrder.setText("" + orderId);

        tvTotal.setText("" + business.getCurrSymbol() + String.format("%.2f", orderSummaryInfo.total));

        tvAverageWait.setText(orderSummaryInfo.averageWaitTime);

        tvRewardPoints.setText("You Earned " + reward_points + " Reward Points!");

        tvRedeemedPoints.setText("You Redeemed " + orderSummaryInfo.points_redeemed + " Reward Points!");

        String cardExpiryDate = Utils.convertTime("yyyy-MM-dd", "MM/yy", cardInfo.expiration_date);

        tvCardDetail.setText(cardInfo.card_type + " xxxx xxxx xxxx " + cardInfo.cc_no.substring(cardInfo.cc_no.length() - 4) + " " + cardExpiryDate);

    }

    private void initHeader() {

        ((TextView) view.findViewById(R.id.tvToolbarTitle)).setText(getString(R.string.confirmation));

        TextView tvToolbarLeft = view.findViewById(R.id.tvToolbarLeft);
        tvToolbarLeft.setVisibility(View.VISIBLE);
        tvToolbarLeft.setText("Done");
        tvToolbarLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                    fm.popBackStack();
                }
                ((HomeActivity)getActivity()).clearOrders();
            }
        });
    }

    private void initViews() {

        tvOrderId = view.findViewById(R.id.tvOrderId);

        lvOrders = view.findViewById(R.id.lvOrders);
        //lvOrders.setAdapter(finalOrderSummaryAdapter);

        final RecyclerView recyclerView = view.findViewById(R.id.order_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(), RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(adapterNew);

        tvOrder = view.findViewById(R.id.tvOrder);
        tvTotal = view.findViewById(R.id.tvTotal);
        tvCardDetail = view.findViewById(R.id.tvCardDetail);
        tvAverageWait = view.findViewById(R.id.tvAverageWait);
        tvRewardPoints = view.findViewById(R.id.tvRewardPoints);
        tvRedeemedPoints = view.findViewById(R.id.tvRedeemedPoints);

    }
}
