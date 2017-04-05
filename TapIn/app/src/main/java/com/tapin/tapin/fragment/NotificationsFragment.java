package com.tapin.tapin.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.tapin.tapin.R;
import com.tapin.tapin.adapter.NotificationAdapter;


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


}
