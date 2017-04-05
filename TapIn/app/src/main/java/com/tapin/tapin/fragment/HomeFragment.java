package com.tapin.tapin.fragment;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tapin.tapin.R;
import com.tapin.tapin.adapter.BusinessAdpater;
import com.tapin.tapin.common.NetworkStatus;
import com.tapin.tapin.common.StaticData;
import com.tapin.tapin.model.BusinessInfo;
import com.tapin.tapin.model.BusinessResp;
import com.tapin.tapin.utils.Debug;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


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

    NetworkStatus networkStatus;
    ArrayList<BusinessInfo> businesses = new ArrayList<>();
    BusinessAdpater businessAdpater;
    RecyclerView recyclerViewBusiness;
    LinearLayoutManager mLayoutManager;
    LocationManager mLocationManager;
    Calendar calendar;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");

    EditText etSearch;
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
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


        // fetch data from api
        networkStatus = new NetworkStatus(context);
        if (networkStatus.isOnline()) {
            new GetData().execute();
        } else {
            Toast.makeText(context, "Internet connection not available. Please try again later.", Toast.LENGTH_SHORT);
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

    private class GetData extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            return networkStatus.getResponce(StaticData.HOME, "GET");
        }

        @Override
        protected void onPostExecute(String result) {
            try {

                BusinessResp businessResp = new Gson().fromJson(result, BusinessResp.class);

                JSONObject object = new JSONObject(result);
//                if (object.getInt("status") == 0) {
//                    businesses.clear();

//                    JSONArray jsonArray = object.getJSONArray("data");
//
//                    for (int i = 0; i < jsonArray.length(); i++) {
//                        JSONObject jsonObject = jsonArray.getJSONObject(i);
//                        BusinessInfo business = new BusinessInfo();
//                       // business.setId(jsonObject.getString("businessID"));
//                        business.setName(jsonObject.getString("name"));
//                        business.setType(jsonObject.getString("businessTypes"));
//                        business.setKeywords(jsonObject.getString("keywords"));
//                        business.setIcon(jsonObject.getString("icon"));
//                        business.setCity(jsonObject.getString("city"));
//                        business.setRating(jsonObject.getDouble("rating"));
//                        business.setLat(jsonObject.getDouble("lat"));
//                        business.setLng(jsonObject.getDouble("lng"));
//                        business.setOpeningTime(jsonObject.getString("opening_time"));
//                        business.setClosingTime(jsonObject.getString("closing_time"));
//                        businesses.add(business);
//                    }


                businessAdpater = new BusinessAdpater(getActivity(), (ArrayList<BusinessInfo>) businessResp.listBusinessInfos, simpleDateFormat.format(calendar.getTime()));
                recyclerViewBusiness.setAdapter(businessAdpater);
                Debug.e("Data", businesses.size() + "-");


            } catch (
                    JSONException e)

            {
                e.printStackTrace();
            }
        }

        @Override
        protected void onPreExecute() {

        }
    }

    public void initHeader() {
        ImageView ivHeaderLogo = (ImageView) view.findViewById(R.id.ivHeaderLogo);
        TextView tvHeaderTitle = (TextView) view.findViewById(R.id.tvHeaderTitle);
        TextView tvHeaderLeft = (TextView) view.findViewById(R.id.tvHeaderLeft);
        TextView tvHeaderRight = (TextView) view.findViewById(R.id.tvHeaderRight);

        ivHeaderLogo.setVisibility(View.VISIBLE);
        tvHeaderTitle.setVisibility(View.GONE);
        tvHeaderLeft.setVisibility(View.GONE);
        tvHeaderRight.setVisibility(View.GONE);


    }


}
