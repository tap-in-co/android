package com.tapin.tapin.fragment;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.tapin.tapin.R;
import com.tapin.tapin.adapter.BusinessAdpater;
import com.tapin.tapin.common.NetworkStatus;
import com.tapin.tapin.common.StaticData;
import com.tapin.tapin.model.BusinessInfo;
import com.tapin.tapin.model.BusinessResp;
import com.tapin.tapin.utils.AlertMessages;
import com.tapin.tapin.utils.Constant;
import com.tapin.tapin.utils.Debug;
import com.tapin.tapin.utils.ProgressHUD;
import com.tapin.tapin.utils.URLs;
import com.tapin.tapin.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import cz.msebera.android.httpclient.Header;


public class HomeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public HomeFragment() {
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
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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

    Context context;

    ArrayList<BusinessInfo> businesses = new ArrayList<>();
    BusinessAdpater businessAdpater;
    RecyclerView recyclerViewBusiness;
    LinearLayoutManager mLayoutManager;
    LocationManager mLocationManager;
    Calendar calendar;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");

    EditText etSearch;
    View view;

    ProgressHUD pd;
    AlertMessages messages;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_home, container, false);

        messages = new AlertMessages(getActivity());

        initHeader();

        context = getActivity();

        mLocationManager = (LocationManager) getActivity().getSystemService(context.LOCATION_SERVICE);
        int permissionCheck = ContextCompat.checkSelfPermission(context,
                android.Manifest.permission.ACCESS_FINE_LOCATION);
        if (Build.VERSION.SDK_INT >= 23) {
            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1001);
            } else {
//                GPSTracker gps = new GPSTracker(this);
//                if (gps.canGetLocation()) {
//                    gps.getLatitude(); // returns latitude
//                    gps.getLongitude(); // returns longitude
//
//                    Log.w("Location", gps.getLatitude() + " " + gps.getLongitude());
//
//                }
            }
        } else {

        }

        // list data
        calendar = Calendar.getInstance();
        recyclerViewBusiness = (RecyclerView) view.findViewById(R.id.recyclerViewBusiness);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerViewBusiness.setLayoutManager(mLayoutManager);
        recyclerViewBusiness.setItemAnimator(new DefaultItemAnimator());
        businessAdpater = new BusinessAdpater(getActivity(), businesses, simpleDateFormat.format(calendar.getTime()));
        recyclerViewBusiness.setAdapter(businessAdpater);

        // search filter
        etSearch = (EditText) view.findViewById(R.id.etSearch);
        etSearch.setActivated(false);
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                businessAdpater.filter(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        if (Utils.isInternetConnected(getActivity())) {

            pd = ProgressHUD.show(getActivity(), getActivity().getResources().getString(R.string.please_wait), true, false);
            getData();

        } else {
            messages.showErrorInConnection();
        }

        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1001) {
            int permissionCheck = ContextCompat.checkSelfPermission(context,
                    android.Manifest.permission.ACCESS_FINE_LOCATION);

            if (Build.VERSION.SDK_INT >= 23) {
                if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1001);
                } else {

                }
            } else {

            }
        }
    }

    private void getData() {

        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(Constant.TIMEOUT);

        RequestParams params = new RequestParams();

        client.get(getActivity(), URLs.GET_HOME_DATA, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                try {

                    String content = new String(responseBody, "UTF-8");
                    Log.e("RES_ALL_HOTEL", "" + content);

                    BusinessResp businessResp = new Gson().fromJson(content, BusinessResp.class);

                    businessAdpater = new BusinessAdpater(getActivity(), (ArrayList<BusinessInfo>) businessResp.listBusinessInfos, simpleDateFormat.format(calendar.getTime()));
                    recyclerViewBusiness.setAdapter(businessAdpater);

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

    }

    public void initHeader() {

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);

        toolbar.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.white));

        ((TextView) view.findViewById(R.id.tvToolbarTitle)).setVisibility(View.GONE);

        ImageView ivToolbarLogo = (ImageView) view.findViewById(R.id.ivToolbarLogo);
        ivToolbarLogo.setVisibility(View.VISIBLE);

    }
}
