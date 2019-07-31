package com.tapin.tapin.fragment;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.LogInterface;
import com.loopj.android.http.RequestParams;
import com.tapin.tapin.R;
import com.tapin.tapin.adapter.BusinessAdpater;
import com.tapin.tapin.common.GPSTracker;
import com.tapin.tapin.model.BusinessInfo;
import com.tapin.tapin.model.resturants.Business;
import com.tapin.tapin.utils.AlertMessages;
import com.tapin.tapin.utils.Constant;
import com.tapin.tapin.utils.Debug;
import com.tapin.tapin.utils.ProgressHUD;
import com.tapin.tapin.utils.UrlGenerator;
import com.tapin.tapin.utils.Utils;

import org.jetbrains.annotations.Nullable;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import cz.msebera.android.httpclient.Header;

import static android.content.Context.LOCATION_SERVICE;


public class HomeFragment extends BaseFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    Context context;
    BusinessInfo businessInfo;
    ArrayList<Business> businesses = new ArrayList<>();
    BusinessAdpater businessAdpater;
    RecyclerView recyclerViewBusiness;
    LinearLayoutManager mLayoutManager;
    Calendar calendar;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
    EditText etSearch;
    View view;
    ProgressHUD pd;
    AlertMessages messages;
    double currentLat;
    double currentLng;
    boolean isGPSSetting = false;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_home, container, false);

        messages = new AlertMessages(getActivity());

        initHeader();

        context = getActivity();

        // list data
        calendar = Calendar.getInstance();
        recyclerViewBusiness = view.findViewById(R.id.recyclerViewBusiness);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerViewBusiness.setLayoutManager(mLayoutManager);
        recyclerViewBusiness.setItemAnimator(new DefaultItemAnimator());
        businessAdpater = new BusinessAdpater(getActivity(), simpleDateFormat.format(calendar.getTime()));
        recyclerViewBusiness.setAdapter(businessAdpater);

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1001);
        } else {
            getCurrentLocation();
        }

        // search filter
        etSearch = view.findViewById(R.id.etSearch);
        etSearch.setActivated(false);
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                String text = etSearch.getText().toString();

                if (text.startsWith(" ")) {
                    etSearch.setText(text.trim());
                } else {
                    businessAdpater.filter(charSequence.toString());
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1001) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            } else {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1001);
            }

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isGPSSetting) {
            isGPSSetting = false;
            getCurrentLocation();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (Utils.isInternetConnected(getActivity())) {
            getData();

        } else {
            messages.showErrorInConnection();
        }
    }

    private void getCurrentLocation() {

        LocationManager locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

            GPSTracker gpsTracker = new GPSTracker(getActivity());

            currentLat = gpsTracker.getLatitude();
            currentLng = gpsTracker.getLongitude();

            businessAdpater.setCurrentLatLng(currentLat, currentLng);

        } else {

            showSettingsAlert();

        }

    }

    /**
     * Function to show settings alert dialog
     * On pressing Settings button will lauch Settings Options
     */
    public void showSettingsAlert() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());

        alertDialog.setTitle("GPS Settings");

        alertDialog.setMessage("GPS is not enabled. Do you want to go to Settings menu?");

        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                isGPSSetting = true;
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                getActivity().startActivity(intent);
            }
        });

        alertDialog.setNegativeButton("Retry", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                isGPSSetting = false;
                getCurrentLocation();
            }
        });

        alertDialog.show();
    }

    private void getData() {
        pd = ProgressHUD.show(getActivity(), getActivity().getResources().getString(R.string.please_wait), true, false);

        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(Constant.TIMEOUT);
        client.setLoggingEnabled(true);
        client.setLoggingLevel(LogInterface.DEBUG);

        final RequestParams params = new RequestParams();
        final String url = UrlGenerator.INSTANCE.getRestaurantsApi(isCorporateOrder(), getCorporateOrderMerchantIds());
        Debug.d("Okhttp", "API: " + url);

        client.get(getActivity(), url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                try {
                    String content = new String(responseBody, StandardCharsets.UTF_8);
                    Debug.d("Okhttp", "Success Response: " + content);

                    businessInfo = new Gson().fromJson(content, BusinessInfo.class);

                    businessAdpater.addAllBusiness(businessInfo.getListBusinesses());

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

                try {
                    String content = new String(responseBody, StandardCharsets.UTF_8);
                    Debug.d("Okhttp", "Failure Response: " + content);
                } catch (Exception e) {

                }
            }
        });

    }

    public void initHeader() {

        Toolbar toolbar = view.findViewById(R.id.toolbar);

        toolbar.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.white));

        view.findViewById(R.id.tvToolbarTitle).setVisibility(View.GONE);

        ImageView ivToolbarLogo = view.findViewById(R.id.ivToolbarLogo);
        ivToolbarLogo.setVisibility(View.VISIBLE);

    }
}
