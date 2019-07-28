package com.tapin.tapin.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.tapin.tapin.R;
import com.tapin.tapin.activity.HomeActivity;
import com.tapin.tapin.model.OrderedInfo;
import com.tapin.tapin.model.resturants.Business;
import com.tapin.tapin.utils.AlertMessages;
import com.tapin.tapin.utils.Constant;
import com.tapin.tapin.utils.PreferenceManager;
import com.tapin.tapin.utils.ProgressHUD;
import com.tapin.tapin.utils.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Narendra on 4/25/17.
 */

public class OrderFragment extends BaseFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static boolean isOrderChanged = false;
    View view;
    TextView tvToolbarLeft;
    TextView tvToolbarTitle;
    TextView tvHotelName;
    ListView lvOrders;
    TextView tvItemPriceTotal;
    TextView tvPoints;
    Button btnContinue;
    ArrayList<OrderedInfo> listOrdered = new ArrayList<>();
    OrderAdapter orderAdapter;
    Business business;
    ProgressHUD pd;
    AlertMessages messages;
    String currentTime;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_order, container, false);

        messages = new AlertMessages(getActivity());

//        business = (Business) getArguments().getSerializable("BUSINESS_INFO");

        business = Constant.business;

        orderAdapter = new OrderAdapter(getActivity(), business);

        listOrdered = (ArrayList<OrderedInfo>) getArguments().getSerializable("ORDERED_LIST");

        initHeader();

        initViews();

        tvHotelName.setText("" + business.getName());

        orderAdapter.addAll(listOrdered);

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
        currentTime = df.format(cal.getTime());

        Log.e("CURRENT_TIME", "" + currentTime);

        try {
            if (!Utils.isTimeBetweenTwoTime(business.getOpeningTime(), business.getClosingTime(), currentTime)) {

                try {

                    SimpleDateFormat dFormat = new SimpleDateFormat("hh:mm a");
                    Date date = new SimpleDateFormat("HH:mm:ss").parse(business.getOpeningTime());
                    String openingTime = dFormat.format(date);

                    String title = business.getName() + " is closed now!";

                    String message1 = "You may add items to your cart.\nBut if you pay, your order will be ready after the opening time (" + openingTime + ").";

                    String message2 = "Average wait time: 10 min after opening";

                    messages.showCustomMessage(title, message1 + "\n\n" + message2);

                } catch (ParseException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return view;
    }

    private void initViews() {

        tvHotelName = view.findViewById(R.id.tvHotelName);
        lvOrders = view.findViewById(R.id.lvOrders);
        lvOrders.setAdapter(orderAdapter);

        btnContinue = view.findViewById(R.id.btnContinue);
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (orderAdapter.listOrders.size() == 0) {

                    messages.showCustomMessage("Please select menu item to place an order.");

                } else if (PreferenceManager.getUserInfo() == null) {

                    String positiveButton = "OK";
                    messages.alert(getActivity(), "", "We are taking you to the profile page.\nPlease update your profile info then come back to this page.", positiveButton, null, null, new AlertMessages.AlertDialogCallback() {
                        @Override
                        public void clickedButtonText(String s) {

//                            ((BottomNavigationView) getActivity().findViewById(R.id.bottom_navigation)).getMenu().performIdentifierAction(R.id.action_profile,0);
                            getActivity().findViewById(R.id.llProfile).performClick();

                        }
                    });

                } else {
                    PickupOrderFragment pickupOrderFragment = new PickupOrderFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("ORDERED_LIST", listOrdered);
                    pickupOrderFragment.setArguments(bundle);
                    ((HomeActivity) getActivity()).addFragment(pickupOrderFragment, R.id.frame_home);
                }
            }
        });

        view.findViewById(R.id.llItemDesc).setVisibility(View.GONE);
        view.findViewById(R.id.llTotal).setVisibility(View.VISIBLE);
        tvItemPriceTotal = view.findViewById(R.id.tvItemPriceTotal);
        tvPoints = view.findViewById(R.id.tvPoints);

    }

    public void initHeader() {

        tvToolbarLeft = view.findViewById(R.id.tvToolbarLeft);
        tvToolbarTitle = view.findViewById(R.id.tvToolbarTitle);

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

    private void setTotal(ArrayList<OrderedInfo> list) {

        double totalValue = 0;

        for (int i = 0; i < list.size(); i++) {

            totalValue = totalValue + (list.get(i).quantity * list.get(i).price);

        }

        tvItemPriceTotal.setText(business.getCurrSymbol() + " " + String.format("%.2f", totalValue));

        tvPoints.setText("Earn " + Math.round(totalValue) + " Pts");

    }

    public class OrderAdapter extends BaseAdapter {

        private ArrayList<OrderedInfo> listOrders = new ArrayList<OrderedInfo>();

        private Business business;

        private LayoutInflater mInflater;

        public OrderAdapter(Context context, Business b) {
            mInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.business = b;
        }

        @Override
        public int getCount() {
            return listOrders.size();
        }

        @Override
        public OrderedInfo getItem(int position) {
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

                holder.ivIncreaseItem = convertView.findViewById(R.id.ivIncreaseItem);

                holder.tvItemCount = convertView.findViewById(R.id.tvItemCount);

                holder.ivDecreaseItem = convertView.findViewById(R.id.ivDecreaseItem);

                holder.tvItemName = convertView.findViewById(R.id.tvItemName);

                holder.tvExtraItem = convertView.findViewById(R.id.tvExtraItem);

                holder.tvItemPriceTotal = convertView.findViewById(R.id.tvItemPriceTotal);

                convertView.setTag(holder);

            } else {

                holder = (ViewHolder) convertView.getTag();

            }

            try {

                final OrderedInfo order = listOrders.get(position);

                holder.ivIncreaseItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        ++order.quantity;

                        isOrderChanged = true;
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

                            isOrderChanged = true;
                            notifyDataSetChanged();
                        }

                    }
                });

                holder.tvItemCount.setText("" + order.quantity);

                holder.tvItemName.setText("" + order.product_name);

                holder.tvExtraItem.setText("" + order.product_option);

                holder.tvItemPriceTotal.setText(business.getCurrSymbol() + " " + String.format("%.2f", (order.quantity * order.price)));

                if (position == getCount() - 1) {

                    setTotal(listOrdered);

                    if (isOrderChanged) {

                        Constant.listOrdered = listOrders;

                    }

                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return convertView;
        }

        public void addAll(ArrayList<OrderedInfo> list) {

            try {

                listOrders.clear();

                listOrders.addAll(list);

            } catch (Exception e) {
                e.printStackTrace();
            }

            notifyDataSetChanged();

        }

        public class ViewHolder {

            ImageView ivIncreaseItem;

            TextView tvItemCount;

            ImageView ivDecreaseItem;

            TextView tvItemName;

            TextView tvExtraItem;

            TextView tvItemPriceTotal;

        }

    }

}
