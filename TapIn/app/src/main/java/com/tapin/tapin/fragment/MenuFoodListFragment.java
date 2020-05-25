package com.tapin.tapin.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.tapin.tapin.App;
import com.tapin.tapin.R;
import com.tapin.tapin.activity.HomeActivity;
import com.tapin.tapin.adapter.MenuAdapter;
import com.tapin.tapin.adapter.OrderStickyListViewAdapter;
import com.tapin.tapin.model.BusinessMenu;
import com.tapin.tapin.model.Options;
import com.tapin.tapin.model.OrderInfo;
import com.tapin.tapin.model.OrderedInfo;
import com.tapin.tapin.model.resturants.Business;
import com.tapin.tapin.utils.AlertMessages;
import com.tapin.tapin.utils.Constant;
import com.tapin.tapin.utils.Debug;
import com.tapin.tapin.utils.PreferenceManager;
import com.tapin.tapin.utils.ProgressHUD;
import com.tapin.tapin.utils.UrlGenerator;
import com.tapin.tapin.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import se.emilsjolander.stickylistheaders.ExpandableStickyListHeadersListView;


public class MenuFoodListFragment extends BaseFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    static TextView tvOrderBadge;
    static ArrayList<OrderedInfo> listOrdered = new ArrayList<>();
    View view;
    TextView tvToolbarLeft;
    LinearLayout llToolbarTitle;
    TextView tvToolbarTitle;
    TextView tvToolbarRight;
    ImageView ivTitleDropDown;
    ListView lvMenu;
    List<BusinessMenu> listBusinessMenu = new ArrayList<>();
    List<OrderInfo> listOrders = new ArrayList<>();
    MenuAdapter menuAdapter;
    Business business;
    OrderStickyListViewAdapter adapter;
    ProgressHUD pd;
    AlertMessages messages;
    SimpleDateFormat dateFormat;
    boolean isOpened;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ExpandableStickyListHeadersListView lvOrderFood;

    public MenuFoodListFragment() {
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
    public static MenuFoodListFragment newInstance(String param1, String param2) {
        MenuFoodListFragment fragment = new MenuFoodListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static void updateOrderData(ArrayList<OrderedInfo> list) {

        try {

            listOrdered.clear();

            listOrdered.addAll(list);

        } catch (Exception e) {
            e.printStackTrace();
        }

        modifyTotalOrders();

        OrderFragment.isOrderChanged = false;

    }

    private static void modifyTotalOrders() {

        int totalQuantity = 0;

        for (int i = 0; i < listOrdered.size(); i++) {

            totalQuantity = totalQuantity + listOrdered.get(i).quantity;

        }

        if (totalQuantity == 0) {
            tvOrderBadge.setVisibility(View.GONE);
        } else {
            tvOrderBadge.setVisibility(View.VISIBLE);
            tvOrderBadge.setText("" + totalQuantity);
        }

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
        view = inflater.inflate(R.layout.fragment_menu_food_list, container, false);

        messages = new AlertMessages(getActivity());

        dateFormat = new SimpleDateFormat("HH:mm:ss");

//        business = (Business) getArguments().getSerializable("BUSINESS_INFO");
        business = Constant.business;

        checkIsOpened();

        menuAdapter = new MenuAdapter(getActivity(), "#000000"/*Utils.getColor(business.getBgColor())*/, "#000000"/*Utils.getColor(business.getTextColor()*/);

        initHeader();

        initViews();

        Constant.listOrdered = listOrdered;

        if (getArguments() != null) {

            listOrdered = (ArrayList<OrderedInfo>) getArguments().getSerializable("ORDERED_LIST");

            if (listOrdered != null) {

                Constant.listOrdered = listOrdered;

                modifyTotalOrders();

            }

        }

        if (Utils.isInternetConnected(getActivity())) {

            getMenuOfFoods();

        } else {

            messages.showErrorInConnection();

        }

        return view;
    }

    private void checkIsOpened() {

        Calendar cal = Calendar.getInstance();

        String currentTime = dateFormat.format(cal.getTime());

        /*try {

            isOpened = Utils.isTimeBetweenTwoTime(business.getOpeningTime(), business.getClosingTime(), currentTime);

        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }

    private void getMenuOfFoods() {

        pd = ProgressHUD.show(getActivity(), getActivity().getResources().getString(R.string.please_wait), true, false);

        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(Constant.TIMEOUT);

        RequestParams params = new RequestParams();
        params.put("cmd", "products_for_business");
        params.put("businessID", business.getBusinessID());
        params.put("consumer_id", ((App)requireActivity().getApplication()).getProfile().getUid()/*"1234570319"*/);
        params.put("sub_businesses", business.getSubBusinesses());


        Debug.d("Okhttp", "API: " + UrlGenerator.INSTANCE.getMainUrl() + " " + params.toString());

        client.get(getActivity(), UrlGenerator.INSTANCE.getMainUrl(), params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                try {
                    String content = new String(responseBody, StandardCharsets.UTF_8);
                    Debug.d("Okhttp", "Success Response: " + content);

                    JSONObject json = new JSONObject(content);

                    JSONObject jsonData = json.getJSONObject("data");

                    Iterator<String> keys = jsonData.keys();

                    listOrders = new ArrayList<OrderInfo>();

                    listBusinessMenu = new ArrayList<BusinessMenu>();

                    while (keys.hasNext()) {

                        ivTitleDropDown.setVisibility(View.VISIBLE);

                        String key = keys.next();

                        JSONArray jsonArray = jsonData.getJSONArray(key);

                        BusinessMenu businessMenu = new BusinessMenu();

                        businessMenu.category = key;

                        businessMenu.size = jsonArray.length();

                        listBusinessMenu.add(businessMenu);

                        if (jsonArray.length() > 0) {

                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                OrderInfo orderInfo = new Gson().fromJson(jsonObject.toString(), OrderInfo.class);

                                listOrders.add(orderInfo);

                            }

                        }

                    }

                    Collections.sort(listOrders, new Comparator<OrderInfo>() {
                        @Override
                        public int compare(OrderInfo lhs, OrderInfo rhs) {
                            return lhs.category_name.compareTo(rhs.category_name);
                        }
                    });

                    adapter.addAll(listOrders);

                    Collections.sort(listBusinessMenu, new Comparator<BusinessMenu>() {
                        @Override
                        public int compare(BusinessMenu lhs, BusinessMenu rhs) {
                            return lhs.category.compareTo(rhs.category);
                        }
                    });


                    menuAdapter.addAll(listBusinessMenu);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (pd != null && pd.isShowing()) {
                    pd.dismiss();
                    pd = null;
                }

                llToolbarTitle.performClick();

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

        tvToolbarLeft = view.findViewById(R.id.tvToolbarLeft);
        llToolbarTitle = view.findViewById(R.id.llToolbarTitle);
        tvToolbarTitle = view.findViewById(R.id.tvToolbarTitle);
        tvToolbarRight = view.findViewById(R.id.tvToolbarRight);
        tvOrderBadge = view.findViewById(R.id.tvOrderBadge);
        ivTitleDropDown = view.findViewById(R.id.ivTitleDropDown);

        tvToolbarLeft.setVisibility(View.VISIBLE);
        tvToolbarLeft.setText("Back");
        tvToolbarRight.setVisibility(View.VISIBLE);
        tvToolbarRight.setText("Order");
        ivTitleDropDown.setVisibility(View.VISIBLE);
        ivTitleDropDown.setImageResource(R.drawable.ic_arrow_drop_down);

        tvToolbarLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        llToolbarTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ivTitleDropDown.setImageResource(R.drawable.ic_arrow_drop_up);

                if (lvMenu.getVisibility() == View.VISIBLE) {
                    ivTitleDropDown.setImageResource(R.drawable.ic_arrow_drop_down);
                    lvMenu.setVisibility(View.GONE);
                } else {
                    ivTitleDropDown.setImageResource(R.drawable.ic_arrow_drop_up);
                    lvMenu.setVisibility(View.VISIBLE);
                }

            }
        });

        tvToolbarTitle.setText(business.getShortName() + " Menu");

        tvToolbarRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isCorporateOrder()) {
                    // If your is in View Mode then show Unable to Order dialog
                    if (PreferenceManager.getInstance().isViewMode()) {
                        if (getActivity() instanceof HomeActivity) {
                            ((HomeActivity) getActivity()).showUnableToOrderDialog();
                        }
                    } else {
                        showCorpOrderFlow();
                    }
                } else {
                    showIndiOrderFlow();
                }

            }
        });
    }

    private void showCorpOrderFlow() {
        OrderFragment orderFragment = new OrderFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("ORDERED_LIST", listOrdered);
        orderFragment.setArguments(bundle);
        ((HomeActivity) getActivity()).addFragment(orderFragment, R.id.frame_home);
    }

    private void showIndiOrderFlow() {
        if (isOpened) {
            OrderFragment orderFragment = new OrderFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("ORDERED_LIST", listOrdered);
            orderFragment.setArguments(bundle);
            ((HomeActivity) getActivity()).addFragment(orderFragment, R.id.frame_home);
        } else {
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());

            builder.setTitle("Sorry!");
            builder.setMessage("We are not able to deliver now,\nplease Order at " + Utils.convertTime("HH:mm:ss", "hh:mm a", ""/*business.getOpeningTime()*/)).setCancelable(false)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });

            android.app.AlertDialog alert = builder.create();
            alert.setCancelable(false);
            alert.show();
        }
    }

    private void initViews() {

        lvMenu = view.findViewById(R.id.lvMenu);
        lvMenu.setAdapter(menuAdapter);

        lvOrderFood = view.findViewById(R.id.lvOrderFood);

        adapter = new OrderStickyListViewAdapter(getActivity(), business, new OrderStickyListViewAdapter.AddOrder() {
            @Override
            public void addOrder(OrderInfo orderInfo) {

                if (orderInfo.listOptions != null && orderInfo.listOptions.size() > 0) {

                    boolean isOptionAvailable = false;

                    CHECK_OPTIONS_DATA:
                    for (int i = 0; i < orderInfo.listOptions.size(); i++) {

                        if (orderInfo.listOptions.get(i).optionData != null && orderInfo.listOptions.get(i).optionData.size() > 0) {

                            isOptionAvailable = true;

                            break CHECK_OPTIONS_DATA;

                        }

                    }

                    if (isOptionAvailable) {
                        showOptionDialog(orderInfo);
                    } else {
                        showDialog(orderInfo);
                    }

                } else {
                    showDialog(orderInfo);
                }

            }
        });

        lvOrderFood.setAdapter(adapter);

        lvMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {

                String category = listBusinessMenu.get(pos).category;

                FIND_CATEGORY:
                for (int i = 0; i < listOrders.size(); i++) {

                    if (listOrders.get(i).category_name.equalsIgnoreCase(category)) {

                        if (i != 0) {
                            lvOrderFood.setSelection(i - 1);
                        } else {
                            lvOrderFood.setSelection(i);
                        }

                        break FIND_CATEGORY;
                    }

                }

                ivTitleDropDown.setImageResource(R.drawable.ic_arrow_drop_down);
                lvMenu.setVisibility(View.GONE);

            }
        });
    }

    private void showOptionDialog(OrderInfo oInfo) {

//        Log.e("OINFO_CHECKED", "" + oInfo.listOptions.get(2).optionData.get(0).isSelected);

        final OrderInfo orderInfo = oInfo;

//        Log.e("ORDERINFO_CHECKED", "" + orderInfo.listOptions.get(2).optionData.get(0).isSelected);

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_food_option, null);
        dialogBuilder.setView(dialogView);

        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        LinearLayout llHeaderView = dialogView.findViewById(R.id.llHeaderView);

        dialogView.findViewById(R.id.tvHeader).setBackgroundColor(Color.parseColor(Utils.getColor(business.getBgColor())));
        final TextView tvOption1 = dialogView.findViewById(R.id.tvOption1);
        tvOption1.setBackgroundColor(Color.parseColor(Utils.getColor(business.getBgColor())));
        final ListView lvOption1 = dialogView.findViewById(R.id.lvOption1);
        final OptionDataAdapter optionDataAdapter1 = new OptionDataAdapter(getActivity());
        lvOption1.setAdapter(optionDataAdapter1);
        final TextView tvOption2 = dialogView.findViewById(R.id.tvOption2);
        tvOption2.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.gray));
        final ListView lvOption2 = dialogView.findViewById(R.id.lvOption2);
        final OptionDataAdapter optionDataAdapter2 = new OptionDataAdapter(getActivity());
        lvOption2.setAdapter(optionDataAdapter2);
        final TextView tvOption3 = dialogView.findViewById(R.id.tvOption3);
        tvOption3.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.gray));
        final ListView lvOption3 = dialogView.findViewById(R.id.lvOption3);
        final OptionDataAdapter optionDataAdapter3 = new OptionDataAdapter(getActivity());
        lvOption3.setAdapter(optionDataAdapter3);
        Button btnAddOrder = dialogView.findViewById(R.id.btnAddOrder);
        btnAddOrder.setBackgroundColor(Color.parseColor(Utils.getColor(business.getBgColor())));
        Button btnCancel = dialogView.findViewById(R.id.btnCancel);
        btnCancel.setBackgroundColor(Color.parseColor(Utils.getColor(business.getBgColor())));

        for (int i = 0; i < orderInfo.listOptions.size(); i++) {

            if (i == 0) {
                tvOption1.setText("" + orderInfo.listOptions.get(i).option_category_name);
                optionDataAdapter1.addAll(orderInfo.listOptions.get(i).optionData);
            } else if (i == 1) {
                tvOption2.setVisibility(View.VISIBLE);
                lvOption2.setVisibility(View.VISIBLE);
                tvOption2.setText("" + orderInfo.listOptions.get(i).option_category_name);
                optionDataAdapter2.addAll(orderInfo.listOptions.get(i).optionData);
            } else if (i == 2) {
                tvOption3.setVisibility(View.VISIBLE);
                lvOption3.setVisibility(View.VISIBLE);
                tvOption3.setText("" + orderInfo.listOptions.get(i).option_category_name);
                optionDataAdapter3.addAll(orderInfo.listOptions.get(i).optionData);

            }
        }

        if (optionDataAdapter1.getCount() > 0) {
            tvOption1.setEnabled(true);
        } else {
            tvOption1.setEnabled(false);
        }
        if (optionDataAdapter2.getCount() > 0) {
            tvOption2.setEnabled(true);
        } else {
            tvOption2.setEnabled(false);
        }
        if (optionDataAdapter3.getCount() > 0) {
            tvOption3.setEnabled(true);
        } else {
            tvOption3.setEnabled(false);
        }

        if (optionDataAdapter1.getCount() > 0) {
            tvOption1.setBackgroundColor(Color.parseColor(Utils.getColor(business.getBgColor())));
            tvOption2.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.gray));
            tvOption3.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.gray));

            lvOption1.setVisibility(View.VISIBLE);
            lvOption2.setVisibility(View.GONE);
            lvOption3.setVisibility(View.GONE);
        } else if (optionDataAdapter2.getCount() > 0) {
            tvOption1.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.gray));
            tvOption2.setBackgroundColor(Color.parseColor(Utils.getColor(business.getBgColor())));
            tvOption3.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.gray));

            lvOption1.setVisibility(View.GONE);
            lvOption2.setVisibility(View.VISIBLE);
            lvOption3.setVisibility(View.GONE);
        } else if (optionDataAdapter3.getCount() > 0) {
            tvOption1.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.gray));
            tvOption2.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.gray));
            tvOption3.setBackgroundColor(Color.parseColor(Utils.getColor(business.getBgColor())));

            lvOption1.setVisibility(View.GONE);
            lvOption2.setVisibility(View.GONE);
            lvOption3.setVisibility(View.VISIBLE);
        }

        tvOption1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                tvOption1.setBackgroundColor(Color.parseColor(Utils.getColor(business.getBgColor())));
                tvOption2.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.gray));
                tvOption3.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.gray));

                lvOption1.setVisibility(View.VISIBLE);
                lvOption2.setVisibility(View.GONE);
                lvOption3.setVisibility(View.GONE);

            }
        });

        tvOption2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                tvOption1.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.gray));
                tvOption2.setBackgroundColor(Color.parseColor(Utils.getColor(business.getBgColor())));
                tvOption3.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.gray));

                lvOption1.setVisibility(View.GONE);
                lvOption2.setVisibility(View.VISIBLE);
                lvOption3.setVisibility(View.GONE);

            }
        });

        tvOption3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                tvOption1.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.gray));
                tvOption2.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.gray));
                tvOption3.setBackgroundColor(Color.parseColor(Utils.getColor(business.getBgColor())));

                lvOption1.setVisibility(View.GONE);
                lvOption2.setVisibility(View.GONE);
                lvOption3.setVisibility(View.VISIBLE);

            }
        });

        final EditText etNote = dialogView.findViewById(R.id.etNote);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        btnAddOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alertDialog.dismiss();

                OrderedInfo orderedInfo = new OrderedInfo();

                orderedInfo.businessID = orderInfo.businessID;
                orderedInfo.item_note = etNote.getText().toString();
                orderedInfo.product_id = orderInfo.product_id;
                orderedInfo.product_description = orderInfo.short_description;
                orderedInfo.product_imageurl = orderInfo.pictures;
                orderedInfo.product_name = orderInfo.name;

                orderedInfo.price = Double.parseDouble(orderInfo.price);

                ArrayList<String> product_option = new ArrayList<String>();
                ArrayList<String> selected_ProductID_array = new ArrayList<String>();
                ArrayList<String> selectedOptionData = new ArrayList<>();

                for (int i = 0; i < optionDataAdapter1.optionDataList.size(); i++) {
                    Options.OptionData optionData = optionDataAdapter1.optionDataList.get(i);
                    if (optionData.isSelected) {
                        product_option.add(optionData.name + "(" + optionData.price + ")");
                        selected_ProductID_array.add(optionData.option_id);
                        orderedInfo.price = orderedInfo.price + Double.parseDouble(optionData.price);
                        selectedOptionData.add(optionData.option_id);
                    }
                }

                for (int i = 0; i < optionDataAdapter2.optionDataList.size(); i++) {
                    Options.OptionData optionData = optionDataAdapter2.optionDataList.get(i);
                    if (optionData.isSelected) {
                        product_option.add(optionData.name + "(" + optionData.price + ")");
                        selected_ProductID_array.add(optionData.option_id);
                        orderedInfo.price = orderedInfo.price + Double.parseDouble(optionData.price);
                        selectedOptionData.add(optionData.option_id);
                    }
                }

                for (int i = 0; i < optionDataAdapter3.optionDataList.size(); i++) {
                    Options.OptionData optionData = optionDataAdapter3.optionDataList.get(i);
                    if (optionData.isSelected) {
                        product_option.add(optionData.name + "(" + optionData.price + ")");
                        selected_ProductID_array.add(optionData.option_id);
                        orderedInfo.price = orderedInfo.price + Double.parseDouble(optionData.price);
                        selectedOptionData.add(optionData.option_id);
                    }
                }

                orderedInfo.product_option = TextUtils.join(",", product_option);
                Log.e("PRODUCT_OPTION", "" + orderedInfo.product_option);
                orderedInfo.selected_ProductID_array = TextUtils.join(",", selected_ProductID_array);
                Log.e("PRODUCT_OPTION_ARRAY", "" + orderedInfo.selected_ProductID_array);
                Log.e("PRODUCT_OPTION_PRICE", "" + orderedInfo.price);
                orderedInfo.listOptions = selectedOptionData;
                orderedInfo.points = Math.round(orderedInfo.price);

                boolean isAlreadyAdded = false;

                CHECK_PRODUCT_OPTION_ARRAY:
                for (int i = 0; i < listOrdered.size(); i++) {
                    if (listOrdered.get(i).product_id.equalsIgnoreCase(orderedInfo.product_id) && listOrdered.get(i).selected_ProductID_array.equalsIgnoreCase(orderedInfo.selected_ProductID_array)) {
                        isAlreadyAdded = true;
                        listOrdered.get(i).quantity++;
                        break CHECK_PRODUCT_OPTION_ARRAY;
                    }
                }

                if (!isAlreadyAdded) {
                    listOrdered.add(orderedInfo);
                }

                modifyTotalOrders();

            }
        });

    }

    private void showDialog(final OrderInfo orderInfo) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_note, null);
        dialogBuilder.setView(dialogView);

        final EditText etNote = dialogView.findViewById(R.id.etNote);
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        dialogView.findViewById(R.id.tvCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        dialogView.findViewById(R.id.tvOk).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alertDialog.dismiss();

                OrderedInfo orderedInfo = new OrderedInfo();

                orderedInfo.businessID = orderInfo.businessID;
                orderedInfo.item_note = etNote.getText().toString();
                orderedInfo.product_id = orderInfo.product_id;
                orderedInfo.product_description = orderInfo.short_description;
                orderedInfo.product_imageurl = orderInfo.pictures;
                orderedInfo.product_name = orderInfo.name;
                orderedInfo.product_option = etNote.getText().toString().trim();
                orderedInfo.price = Double.parseDouble(orderInfo.price);
                orderedInfo.points = Math.round(orderedInfo.price);

                boolean isAlreadyAdded = false;

                CHECK_PRODUCT_OPTION_ARRAY:
                for (int i = 0; i < listOrdered.size(); i++) {
                    if (listOrdered.get(i).product_id.equalsIgnoreCase(orderedInfo.product_id) && listOrdered.get(i).selected_ProductID_array.equalsIgnoreCase(orderedInfo.selected_ProductID_array)) {
                        isAlreadyAdded = true;
                        listOrdered.get(i).quantity++;
                        break CHECK_PRODUCT_OPTION_ARRAY;
                    }
                }

                if (!isAlreadyAdded) {

                    listOrdered.add(orderedInfo);

                }

                modifyTotalOrders();

            }
        });

    }

    @Override
    public void onResume() {

        super.onResume();

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {

                    // handle back button
                    getActivity().onBackPressed();
                    return true;

                }

                return false;
            }
        });
    }

    private void checkButtonClick() {

//        Button myButton = (Button) findViewById(R.id.findSelected);
//        myButton.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//
//                StringBuffer responseText = new StringBuffer();
//                responseText.append("The following were selected...\n");
//
//                ArrayList<Country> countryList = dataAdapter.countryList;
//                for(int i=0;i<countryList.size();i++){
//                    Country country = countryList.get(i);
//                    if(country.isSelected()){
//                        responseText.append("\n" + country.getName());
//                    }
//                }
//
//                Toast.makeText(getApplicationContext(),
//                        responseText, Toast.LENGTH_LONG).show();
//
//            }
//        });

    }

    private class OptionDataAdapter extends BaseAdapter {

        public Context context;
        LayoutInflater inflater;
        private ArrayList<Options.OptionData> optionDataList = new ArrayList<>();

        public OptionDataAdapter(Context c) {
            this.context = c;
            inflater = LayoutInflater.from(context);
        }

        public void addAll(List<Options.OptionData> list) {

            try {
                this.optionDataList.clear();
                this.optionDataList.addAll(list);

            } catch (Exception e) {
                e.printStackTrace();
            }

            notifyDataSetChanged();

        }

        @Override
        public int getCount() {
            return optionDataList.size();
        }

        @Override
        public Options.OptionData getItem(int i) {
            return optionDataList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;

            if (convertView == null) {

                convertView = inflater.inflate(R.layout.list_item_option, null);

                holder = new ViewHolder();
                holder.optionName = convertView.findViewById(R.id.optionName);
                holder.chkBox = convertView.findViewById(R.id.chkBox);
                convertView.setTag(holder);

                holder.chkBox.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {

                        CheckBox cb = (CheckBox) v;

                        Options.OptionData optionData = (Options.OptionData) cb.getTag();
                        optionData.isSelected = cb.isChecked();

                    }
                });
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            Options.OptionData optionData = optionDataList.get(position);

            holder.optionName.setText("" + optionData.name + "($" + optionData.price + ")");

            holder.chkBox.setText("");
            holder.chkBox.setChecked(optionData.isSelected);
            holder.chkBox.setTag(optionData);

            return convertView;

        }

        private class ViewHolder {

            TextView optionName;
            CheckBox chkBox;

        }
    }


}
