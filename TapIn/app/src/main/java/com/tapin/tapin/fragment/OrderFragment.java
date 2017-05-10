package com.tapin.tapin.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tapin.tapin.R;
import com.tapin.tapin.utils.AlertMessages;
import com.tapin.tapin.utils.ProgressHUD;
import com.tapin.tapin.utils.Utils;

/**
 * Created by Narendra on 4/25/17.
 */

public class OrderFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public OrderFragment() {
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
    public static OrderFragment newInstance(String param1, String param2) {
        OrderFragment fragment = new OrderFragment();
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

    View view;
    TextView tvToolbarLeft;
    TextView tvToolbarTitle;

    ProgressHUD pd;
    AlertMessages messages;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_order, container, false);

        messages = new AlertMessages(getActivity());

        initHeader();

        initViews();

        if (Utils.isInternetConnected(getActivity())) {
//            getMenuOfFoods();
        } else {
            messages.showErrorInConnection();
        }

        return view;
    }

    private void initViews() {


    }

    public void initHeader() {

        tvToolbarLeft = (TextView) view.findViewById(R.id.tvToolbarLeft);
        tvToolbarTitle = (TextView) view.findViewById(R.id.tvToolbarTitle);

        tvToolbarLeft.setVisibility(View.VISIBLE);
        tvToolbarLeft.setText("Back");

        tvToolbarLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        tvToolbarTitle.setText("Order");

    }

}
