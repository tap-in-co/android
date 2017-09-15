package com.tapin.tapin.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.util.Util;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.tapin.tapin.R;
import com.tapin.tapin.activity.HomeActivity;
import com.tapin.tapin.adapter.CardPagerAdapter;
import com.tapin.tapin.model.AllCardsInfo;
import com.tapin.tapin.model.Business;
import com.tapin.tapin.model.CardInfo;
import com.tapin.tapin.model.GetPointsInfo;
import com.tapin.tapin.model.GetPointsResp;
import com.tapin.tapin.model.OrderSummaryInfo;
import com.tapin.tapin.model.UserInfo;
import com.tapin.tapin.utils.AlertMessages;
import com.tapin.tapin.utils.Constant;
import com.tapin.tapin.utils.Debug;
import com.tapin.tapin.utils.PreferenceManager;
import com.tapin.tapin.utils.ProgressHUD;
import com.tapin.tapin.utils.URLs;
import com.tapin.tapin.utils.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

/**
 * Created by Narendra on 6/7/17.
 */

public class PaymentFragment extends Fragment {

    View view;

    TextView tvHotelName;
    TextView tvDate;
    TextView tvTotal;

    TextView tvTotalPoints;
    TextView tvPointsMessage;
    CheckBox chkRedeemPoints;

    ViewPager viewPager;
    TextView tvAddRemoveCard;

    double total = 0;
    int totalAvailablePoints = 0;
    double modifiedTotal = 0;
    double redeemedPoints = 0;
    double pointsDollarAmount = 0;

    Business business;

    Calendar calendar;
    OrderSummaryInfo orderSummaryInfo;

    List<CardInfo> listCards = new ArrayList<>();
    CardPagerAdapter cardPagerAdapter;

    AllCardsInfo cardsInfo;
    ProgressHUD pd;
    AlertMessages messages;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_payment, container, false);

        messages = new AlertMessages(getActivity());

        cardPagerAdapter = new CardPagerAdapter(getActivity());

        initHeader();

        initViews();

        business = Constant.business;

        calendar = Calendar.getInstance();

        if (getArguments() != null) {

            orderSummaryInfo = (OrderSummaryInfo) getArguments().getSerializable("ORDER_SUMMARY");

            setData();

        }

        if (Utils.isInternetConnected(getActivity())) {
            getAllPoints();
            getAverageTime();
        } else {
            messages.showErrorInConnection();
        }

        return view;

    }

    private void setData() {

        SimpleDateFormat df = new SimpleDateFormat("MMMM dd, yyyy h:mm a");
        String formattedDate = df.format(calendar.getTime());

        tvDate.setText("" + formattedDate);

        tvHotelName.setText("" + business.name);

        total = orderSummaryInfo.total;

        tvTotal.setText("$" + String.format("%.2f", total));

        orderSummaryInfo.points_dollar_amount = "0.0000";

    }

    private void getAllPoints() {

        RequestParams params = new RequestParams();
        params.put("businessID", business.businessID);
        params.put("cmd", "get_all_points");
        params.put("consumerID", PreferenceManager.getUserId());

        Log.e("GET_POINTS_PARAMS", "" + params.toString());

        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(Constant.TIMEOUT);

        client.post(getActivity(), URLs.REWARD, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                try {

                    String content = new String(responseBody, "UTF-8");

                    Log.e("SUCC_RESP_TOTAL_POINS", "" + content);

                    GetPointsResp geoPointsResp = new Gson().fromJson(content, GetPointsResp.class);

                    if (geoPointsResp.status == 1) {

                        GetPointsInfo getPointsInfo = geoPointsResp.getPointsInfo;

                        PreferenceManager.putPointsData(geoPointsResp);

                        tvTotalPoints.setText("" + getPointsInfo.total_available_points + " Points");

                        if (getPointsInfo.total_available_points > 0) {
                            totalAvailablePoints = getPointsInfo.total_available_points;
                            tvPointsMessage.setText(getPointsInfo.total_available_points + " points worth 10¢ each. Redeem some?");
                        } else {
                            totalAvailablePoints = 0;
                            tvPointsMessage.setText("You don't have enough points to use");
                        }

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

    private void getAverageTime() {

        pd = ProgressHUD.show(getActivity(), getString(R.string.please_wait), true, false);

        try {

            String URL = URLs.MAIN_BASE_URL + "cmd=get_average_wait_time_for_business&business_id=" + business.businessID;

            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(Constant.TIMEOUT);

            client.get(getActivity(), URL, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                    try {

                        String content = new String(responseBody, "UTF-8");

                        Log.e("RES_SUCC_AVER_TIME", "-" + content);

                        JSONObject json = new JSONObject(content);

                        JSONObject data = json.getJSONObject("data");

                        String process_time = data.getString("process_time");

                        orderSummaryInfo.averageWaitTime = process_time;

                        ((TextView) view.findViewById(R.id.tvWaitTime)).setText(process_time);

                        getCards(0);

                    } catch (Exception e) {

                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    if (pd != null && pd.isShowing()) {
                        pd.dismiss();
                        pd = null;
                    }
                }

            });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getCards(final int pos) {

        try {

            UserInfo userInfo = PreferenceManager.getUserInfo();

            String URL = URLs.GET_CARDS_INFO + "cmd=get_consumer_all_cc_info&consumer_id=" + userInfo.uid;

            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(Constant.TIMEOUT);

            client.get(getActivity(), URL, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                    try {

                        String content = new String(responseBody, "UTF-8");

                        Log.e("RES_SUCC_CARDS_INFO", "-" + content);

                        cardsInfo = new Gson().fromJson(content, AllCardsInfo.class);

                        if (cardsInfo.status == 0) {

                            if (cardsInfo.listCards.size() > 0) {
                                listCards = cardsInfo.listCards;
                                cardPagerAdapter.addAll(listCards);

                                viewPager.setCurrentItem(pos);

                                viewPager.setVisibility(View.VISIBLE);

                            } else {
                                viewPager.setVisibility(View.GONE);
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (pd != null && pd.isShowing()) {
                        pd.dismiss();
                        pd = null;
                    }

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    if (pd != null && pd.isShowing()) {
                        pd.dismiss();
                        pd = null;
                    }
                }

            });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initHeader() {

        ((TextView) view.findViewById(R.id.tvToolbarTitle)).setText(getString(R.string.payment));

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

        tvDate = (TextView) view.findViewById(R.id.tvDate);

        tvTotal = (TextView) view.findViewById(R.id.tvTotal);

        tvTotalPoints = (TextView) view.findViewById(R.id.tvTotalPoints);
        tvPointsMessage = (TextView) view.findViewById(R.id.tvPointsMessage);
        chkRedeemPoints = (CheckBox) view.findViewById(R.id.chkRedeemPoints);

        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        viewPager.setAdapter(cardPagerAdapter);

        tvAddRemoveCard = (TextView) view.findViewById(R.id.tvAddRemoveCard);

        tvAddRemoveCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CardDetailFragment fragment = CardDetailFragment.newInstance(new CardDetailFragment.UpdateCards() {
                    @Override
                    public void cardList() {

                    }

                    @Override
                    public void selectedCardPosition(int pos) {

                        getCards(pos);

                    }
                });

                ((HomeActivity) getActivity()).addFragment(fragment, R.id.frame_home);

            }
        });

        ((Button) view.findViewById(R.id.btnPayNow)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (listCards.size() > 0) {

                    if (Utils.isInternetConnected(getActivity())) {
                        payNow();
                    } else {
                        messages.showErrorInConnection();
                    }

                } else {
                    messages.showCustomMessage("Error", "Please set first any one default card.");
                }
            }
        });

        chkRedeemPoints.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (b) {

                    if (totalAvailablePoints >= total * 10) {

                        redeemedPoints = (int) (total * 10);

                    } else {

                        redeemedPoints = totalAvailablePoints;

                    }

                    Log.e("REDEEMED_POINTS",""+redeemedPoints);

                    orderSummaryInfo.total = total - (redeemedPoints / 10);

                    double points_dollar_amount = redeemedPoints / 10;

                    orderSummaryInfo.points_dollar_amount = String.format("%.2f", points_dollar_amount);

                } else {

                    redeemedPoints = 0;

                    Log.e("REDEEMED_POINTS",""+redeemedPoints);

                    orderSummaryInfo.total = total;

                    orderSummaryInfo.points_dollar_amount = "0.0000";

                }

                orderSummaryInfo.points_redeemed = (int) redeemedPoints;

                tvTotal.setText("$" + String.format("%.2f", orderSummaryInfo.total));

            }
        });

    }

    public void payNow() {

        try {

            pd = ProgressHUD.show(getActivity(), getString(R.string.please_wait), true, false);

            final JSONObject json = new JSONObject();

            JSONArray jsonArray = new JSONArray();

            for (int i = 0; i < orderSummaryInfo.listOrdered.size(); i++) {

                JSONObject jsonData = new JSONObject();

                jsonData.put("product_id", orderSummaryInfo.listOrdered.get(i).product_id);
                jsonData.put("quantity", orderSummaryInfo.listOrdered.get(i).quantity);
                jsonData.put("price", orderSummaryInfo.listOrdered.get(i).price);
                jsonData.put("points", orderSummaryInfo.listOrdered.get(i).points);
                jsonData.put("options", new JSONArray(orderSummaryInfo.listOrdered.get(i).listOptions));

                jsonArray.put(jsonData);

            }

            String card = listCards.get(viewPager.getCurrentItem()).cc_no;

            json.put("promotion_code", orderSummaryInfo.promotion_code);
            json.put("total", "" + orderSummaryInfo.total);
            json.put("business_id", business.businessID);
            json.put("pd_time", orderSummaryInfo.pd_time);
            json.put("cmd", "save_order");
            json.put("points_dollar_amount", orderSummaryInfo.points_dollar_amount);
            json.put("tax_amount", orderSummaryInfo.tax_amount);
            json.put("cc_last_4_digits", card.substring(card.length() - 4));
            json.put("pd_mode", orderSummaryInfo.pd_mode);
            json.put("consumer_delivery_id", "" + orderSummaryInfo.consumerDeliveryId);
            json.put("subtotal", orderSummaryInfo.subtotal);
            json.put("tip_amount", orderSummaryInfo.tip_amount);
            json.put("note", orderSummaryInfo.note);
            json.put("consumer_id", PreferenceManager.getUserInfo().uid);
            json.put("promotion_discount_amount", "" + orderSummaryInfo.promotion_discount_amount);
            json.put("pd_locations_id", orderSummaryInfo.pd_locations_id);
            json.put("pd_charge_amount", orderSummaryInfo.pd_charge_amount);
            json.put("points_reedemed", "" + orderSummaryInfo.points_redeemed);
            json.put("delivery_charge_amount", orderSummaryInfo.delivery_charge_amount);
            json.put("data", jsonArray);

            Log.e("PARAMS_PAY", "" + json.toString());

            StringEntity entity = new StringEntity(json.toString());

            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(Constant.TIMEOUT);

            String URL = URLs.MAIN_BASE_URL;

            client.post(getActivity(), URL, entity, "application/json", new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    try {

                        String content = new String(responseBody, "UTF-8");

                        Log.e("RES_SUCC_INFO", "-" + content);

                        JSONObject jsonObject = new JSONObject(content);

                        int status = jsonObject.getInt("status");

                        JSONObject data = jsonObject.getJSONObject("data");
                        int order_id = data.getInt("order_id");
                        int points = data.getInt("points");

                        if (status > 0) {

                            ConfirmationFragment fragment = new ConfirmationFragment();
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("ORDER_SUMMARY", orderSummaryInfo);
                            bundle.putInt("ORDER_ID", order_id);
                            bundle.putInt("REWARD_POINTS", points);
                            bundle.putSerializable("CARDINFO", listCards.get(viewPager.getCurrentItem()));
                            fragment.setArguments(bundle);
                            ((HomeActivity) getActivity()).addFragment(fragment, R.id.frame_home);

                        }

                        if (pd != null && pd.isShowing()) {
                            pd.dismiss();
                            pd = null;
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        if (pd != null && pd.isShowing()) {
                            pd.dismiss();
                            pd = null;
                        }
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    try {

                        String content = new String(responseBody, "UTF-8");

                        Log.e("RES_SUCC_INFO", "-" + content);

                    } catch (Exception e) {

                        e.printStackTrace();
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
