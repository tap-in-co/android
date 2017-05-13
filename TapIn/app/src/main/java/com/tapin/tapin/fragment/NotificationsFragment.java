package com.tapin.tapin.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.tapin.tapin.activity.HomeActivity;
import com.tapin.tapin.R;
import com.tapin.tapin.adapter.NotificationAdapter;
import com.tapin.tapin.model.GetPointsResp;
import com.tapin.tapin.utils.Constant;
import com.tapin.tapin.utils.Debug;
import com.tapin.tapin.utils.PreferenceManager;
import com.tapin.tapin.utils.URLs;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.util.Date;
import java.util.TimeZone;

import cz.msebera.android.httpclient.Header;


public class NotificationsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public NotificationsFragment() {
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
    public static NotificationsFragment newInstance(String param1, String param2) {
        NotificationsFragment fragment = new NotificationsFragment();
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

    ListView lvNotifications;
    NotificationAdapter adapter;
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_notifications, container, false);

        initHeader();

        lvNotifications= (ListView) view.findViewById(R.id.lvNotifications);
        adapter=new NotificationAdapter(getActivity());
        lvNotifications.setAdapter(adapter);
        getNotifications();


        return view;
    }
    public void initHeader() {
        ImageView ivHeaderLogo = (ImageView) view.findViewById(R.id.ivHeaderLogo);
        TextView tvHeaderTitle = (TextView) view.findViewById(R.id.tvHeaderTitle);
        TextView tvHeaderLeft = (TextView) view.findViewById(R.id.tvHeaderLeft);
        TextView tvHeaderRight = (TextView) view.findViewById(R.id.tvHeaderRight);

        ivHeaderLogo.setVisibility(View.GONE);
        tvHeaderTitle.setVisibility(View.VISIBLE);
        tvHeaderLeft.setVisibility(View.GONE);
        tvHeaderRight.setVisibility(View.GONE);


    }


    private void getNotifications() {

        RequestParams params = new RequestParams();

//        params.put("businessID", businessInfo.businessID);
        params.put("cmd", "get_all_notifications_for_consume");
        params.put("consumer_id", PreferenceManager.getUserId()/*"1234570319"*/);


        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(Constant.TIMEOUT);
        client.get(URLs.REWARD, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                try {
                    String content = new String(responseBody, "UTF-8");
                    GetPointsResp userInfo = new Gson().fromJson(content, GetPointsResp.class);
                    PreferenceManager.putPointsData(userInfo);
//                    ((HomeActivity) getActivity()).refreshPointsFragment();
                    Debug.e("Notification", content + "-");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Debug.e("Notification fail", responseBody + "-");
                error.printStackTrace();
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }
        });
    }

    private void saveNotifications() {
        RequestParams params = new RequestParams();
//        params.put("businessID", businessInfo.businessID);
        params.put("cmd", "save_all_notifications_for_consumer");
        params.put("consumer_id", PreferenceManager.getUserId()/*"1234570319"*/);

        DateFormat df = DateFormat.getTimeInstance();
        df.setTimeZone(TimeZone.getTimeZone("gmt"));
        String gmtTime = df.format(new Date());
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("notification_id","");
            jsonObject.put("message","");
            jsonObject.put("image","");
            jsonObject.put("time_sent",gmtTime);
            jsonObject.put("time_read","");
            jsonObject.put("business_id","");
            jsonObject.put("is_deleted","");

        } catch (JSONException e) {
            e.printStackTrace();
        }


        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(Constant.TIMEOUT);
        client.get(URLs.REWARD, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                try {
                    String content = new String(responseBody, "UTF-8");
                    GetPointsResp userInfo = new Gson().fromJson(content, GetPointsResp.class);
                    PreferenceManager.putPointsData(userInfo);
//                    ((HomeActivity) getActivity()).refreshPointsFragment();
                    Debug.e("Notification", content + "-");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Debug.e("Notification fail", responseBody + "-");
                error.printStackTrace();
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }
        });
    }


}
