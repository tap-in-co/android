package com.tapin.tapin.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.tapin.tapin.R;
import com.tapin.tapin.activity.HomeActivity;
import com.tapin.tapin.model.BusinessInfo;
import com.tapin.tapin.model.OrderInfo;
import com.tapin.tapin.utils.AlertMessages;
import com.tapin.tapin.utils.ProgressHUD;
import com.tapin.tapin.utils.Utils;

import org.w3c.dom.Text;

import java.io.Serializable;
import java.util.ArrayList;

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

    TextView tvHotelName;
    ListView lvOrders;
    TextView tvItemPriceTotal;
    TextView tvPoints;
    Button btnContinue;

    ArrayList<OrderInfo> listOrdered = new ArrayList<>();
    OrderAdapter orderAdapter;

    BusinessInfo businessInfo;

    ProgressHUD pd;
    AlertMessages messages;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_order, container, false);

        messages = new AlertMessages(getActivity());

        orderAdapter = new OrderAdapter(getActivity());

        businessInfo = (BusinessInfo) getArguments().getSerializable("BUSINESS_INFO");

        listOrdered = (ArrayList<OrderInfo>) getArguments().getSerializable("ORDERED_LIST");

        initHeader();

        initViews();

        tvHotelName.setText("" + businessInfo.name);

        orderAdapter.addAll(listOrdered);

        if (Utils.isInternetConnected(getActivity())) {
//            getMenuOfFoods();
        } else {
            messages.showErrorInConnection();
        }

        return view;
    }

    private void initViews() {

        tvHotelName = (TextView) view.findViewById(R.id.tvHotelName);
        lvOrders = (ListView) view.findViewById(R.id.lvOrders);
        lvOrders.setAdapter(orderAdapter);

        btnContinue = (Button) view.findViewById(R.id.btnContinue);
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (orderAdapter.listOrders.size() == 0) {
                    messages.showCustomMessage("Please select menu item to place an order.");
                } else {
                    PickupOrderFragment pickupOrderFragment = new PickupOrderFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("BUSINESS_INFO", businessInfo);
                    bundle.putSerializable("ORDERED_LIST", (Serializable) listOrdered);
                    pickupOrderFragment.setArguments(bundle);
                    ((HomeActivity) getActivity()).addFragment(pickupOrderFragment);
                }
            }
        });

        ((LinearLayout) view.findViewById(R.id.llItemDesc)).setVisibility(View.GONE);
        ((LinearLayout) view.findViewById(R.id.llTotal)).setVisibility(View.VISIBLE);
        tvItemPriceTotal = (TextView) view.findViewById(R.id.tvItemPriceTotal);
        tvPoints = (TextView) view.findViewById(R.id.tvPoints);

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

    private void setTotal(ArrayList<OrderInfo> list) {

        double totalValue = 0;

        for (int i = 0; i < list.size(); i++) {

            totalValue = totalValue + (list.get(i).quantity * Double.parseDouble(list.get(i).price));

        }

        tvItemPriceTotal.setText("$ " + totalValue);

        tvPoints.setText("Earn " + Math.round(totalValue) + " Pts");

    }

    public class OrderAdapter extends BaseAdapter {

        private ArrayList<OrderInfo> listOrders = new ArrayList<OrderInfo>();

        private LayoutInflater mInflater;

        public OrderAdapter(Context context) {
            mInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return listOrders.size();
        }

        @Override
        public OrderInfo getItem(int position) {
            return listOrders.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public View getView(final int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;

            if (convertView == null) {

                holder = new ViewHolder();

                convertView = mInflater.inflate(R.layout.list_item_order_food, null);

                holder.ivIncreaseItem = (ImageView) convertView.findViewById(R.id.ivIncreaseItem);

                holder.tvItemCount = (TextView) convertView.findViewById(R.id.tvItemCount);

                holder.ivDecreaseItem = (ImageView) convertView.findViewById(R.id.ivDecreaseItem);

                holder.tvItemName = (TextView) convertView.findViewById(R.id.tvItemName);

                holder.tvItemPriceTotal = (TextView) convertView.findViewById(R.id.tvItemPriceTotal);

                convertView.setTag(holder);

            } else {

                holder = (ViewHolder) convertView.getTag();

            }

            try {

                final OrderInfo order = listOrders.get(position);

                holder.ivIncreaseItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        ++order.quantity;
                        notifyDataSetChanged();

                    }
                });

                holder.ivDecreaseItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (order.quantity != 0) {
                            --order.quantity;
                            if (order.quantity == 0) {
                                listOrders.remove(position);
                            }
                            notifyDataSetChanged();
                        }

                    }
                });

                holder.tvItemCount.setText("" + order.quantity);

                holder.tvItemName.setText("" + order.name);

                holder.tvItemPriceTotal.setText("$ " + (order.quantity * Double.parseDouble(order.price)));

                if (position == getCount() - 1) {

                    setTotal(listOrdered);

                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return convertView;
        }

        public class ViewHolder {

            ImageView ivIncreaseItem;

            TextView tvItemCount;

            ImageView ivDecreaseItem;

            TextView tvItemName;

            TextView tvItemPriceTotal;

        }

        public void addAll(ArrayList<OrderInfo> list) {

            try {

                listOrders.clear();

                listOrders.addAll(list);

            } catch (Exception e) {
                e.printStackTrace();
            }

            notifyDataSetChanged();

        }

    }

}
