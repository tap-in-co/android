package com.tapin.tapin.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.tapin.tapin.R;
import com.tapin.tapin.adapter.PointsAdapter;
import com.tapin.tapin.model.GetPointsResp;
import com.tapin.tapin.utils.PreferenceManager;


public class PointsFragment extends BaseFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    ListView lvPoints;
    PointsAdapter pointsAdapter;
    View view;
    TextView tvPoints;
    TextView tvTotalPoints;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PointsFragment() {
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
    public static PointsFragment newInstance(String param1, String param2) {
        PointsFragment fragment = new PointsFragment();
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
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_points, container, false);

        //initHeader();

        tvTotalPoints = view.findViewById(R.id.tvTotalPoints);
        tvPoints = view.findViewById(R.id.tvPoints);


        pointsAdapter = new PointsAdapter(getActivity());
        lvPoints = view.findViewById(R.id.lvPoints);
        lvPoints.setAdapter(pointsAdapter);

        //refreshData();

        return view;
    }

    public void initHeader() {
        ImageView ivHeaderLogo = view.findViewById(R.id.ivHeaderLogo);
        TextView tvHeaderTitle = view.findViewById(R.id.tvHeaderTitle);
        TextView tvHeaderLeft = view.findViewById(R.id.tvHeaderLeft);
        TextView tvHeaderRight = view.findViewById(R.id.tvHeaderRight);

        ivHeaderLogo.setVisibility(View.VISIBLE);
        tvHeaderTitle.setVisibility(View.GONE);
        tvHeaderLeft.setVisibility(View.GONE);
        tvHeaderRight.setVisibility(View.GONE);


    }

    public void refreshData() {

        GetPointsResp getPointsResp = PreferenceManager.getPointsData();

        if (getPointsResp != null) {

            pointsAdapter.addAll(getPointsResp.getPointsInfo.listPointInfos);
            tvPoints.setText("Points: " + getPointsResp.getPointsInfo.total_available_points);
            tvTotalPoints.setText("Your total points for all all businesses: " + getPointsResp.getPointsInfo.total_earned_points);
        }
    }
}
