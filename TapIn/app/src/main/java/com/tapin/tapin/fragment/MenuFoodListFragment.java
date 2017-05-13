package com.tapin.tapin.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.tapin.tapin.R;
import com.tapin.tapin.activity.HomeActivity;
import com.tapin.tapin.adapter.OrderStickyListViewAdapter;
import com.tapin.tapin.adapter.MenuAdapter;
import com.tapin.tapin.model.BusinessInfo;
import com.tapin.tapin.model.BusinessMenu;
import com.tapin.tapin.model.OrderInfo;
import com.tapin.tapin.utils.AlertMessages;
import com.tapin.tapin.utils.Constant;
import com.tapin.tapin.utils.PreferenceManager;
import com.tapin.tapin.utils.ProgressHUD;
import com.tapin.tapin.utils.URLs;
import com.tapin.tapin.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import se.emilsjolander.stickylistheaders.ExpandableStickyListHeadersListView;


public class MenuFoodListFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


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
    LinearLayout llToolbarTitle;
    TextView tvToolbarTitle;
    TextView tvToolbarRight;
    ImageView ivTitleDropDown;

    ListView lvMenu;
    List<BusinessMenu> listBusinessMenu = new ArrayList<>();
    List<OrderInfo> listOrders = new ArrayList<>();
    MenuAdapter menuAdapter;

    BusinessInfo businessInfo;

    private ExpandableStickyListHeadersListView lvOrderFood;
    OrderStickyListViewAdapter adapter;

    ProgressHUD pd;
    AlertMessages messages;

    int totalOrders = 0;
    List<OrderInfo> listOrdered = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_menu_food_list, container, false);

        messages = new AlertMessages(getActivity());

        businessInfo = (BusinessInfo) getArguments().getSerializable("BUSINESS_INFO");

        menuAdapter = new MenuAdapter(getActivity(), Utils.getColor(businessInfo.bg_color), Utils.getColor(businessInfo.text_color));

        initHeader();

        initViews();

        if (Utils.isInternetConnected(getActivity())) {

            getMenuOfFoods();

        } else {

            messages.showErrorInConnection();

        }

        return view;
    }

    private void getMenuOfFoods() {

        pd = ProgressHUD.show(getActivity(), getActivity().getResources().getString(R.string.please_wait), true, false);

        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(Constant.TIMEOUT);

        RequestParams params = new RequestParams();
        params.put("cmd", "products_for_business");
        params.put("businessID", businessInfo.businessID);
        params.put("consumer_id", PreferenceManager.getUserId()/*"1234570319"*/);
        params.put("sub_businesses", businessInfo.sub_businesses);

        client.get(getActivity(), URLs.BUSINESS_MENU, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                try {

                    String content = new String(responseBody, "UTF-8");

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

                    menuAdapter.addAll(listBusinessMenu);

                    adapter.addAll(listOrders);

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
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

            }
        });

    }

    private void initViews() {

        lvMenu = (ListView) view.findViewById(R.id.lvMenu);
        lvMenu.setAdapter(menuAdapter);

        lvOrderFood = (ExpandableStickyListHeadersListView) view.findViewById(R.id.lvOrderFood);

        adapter = new OrderStickyListViewAdapter(getActivity(), new OrderStickyListViewAdapter.AddOrder() {
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

    private void showOptionDialog(final OrderInfo orderInfo) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_food_option, null);
        dialogBuilder.setView(dialogView);

        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        LinearLayout llHeaderView = (LinearLayout) dialogView.findViewById(R.id.llHeaderView);

        EditText etNote = (EditText) dialogView.findViewById(R.id.etNote);

        ((Button) dialogView.findViewById(R.id.btnCancel)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        ((Button) dialogView.findViewById(R.id.btnAddOrder)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();

                boolean isAlreadyAdded = false;

                CHECK_PRODUCT_ID:
                for (int i = 0; i < listOrdered.size(); i++) {

                    if (listOrdered.get(i).product_id.equalsIgnoreCase(orderInfo.product_id)) {

                        isAlreadyAdded = true;
                        listOrdered.get(i).quantity++;
                        break CHECK_PRODUCT_ID;

                    }
                }

                if (!isAlreadyAdded) {
                    listOrdered.add(orderInfo);
                }

                totalOrders++;

                if (totalOrders <= 1) {
                    tvToolbarRight.setText("" + totalOrders + " Order");
                } else {
                    tvToolbarRight.setText("" + totalOrders + " Orders");
                }

            }
        });

    }

    private void showDialog(final OrderInfo orderInfo) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_note, null);
        dialogBuilder.setView(dialogView);

        EditText etNote = (EditText) dialogView.findViewById(R.id.etNote);
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        ((TextView) dialogView.findViewById(R.id.tvCancel)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        ((TextView) dialogView.findViewById(R.id.tvOk)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alertDialog.dismiss();

                boolean isAlreadyAdded = false;
                CHECK_PRODUCT_ID:
                for (int i = 0; i < listOrdered.size(); i++) {

                    if (listOrdered.get(i).product_id.equalsIgnoreCase(orderInfo.product_id)) {

                        isAlreadyAdded = true;
                        listOrdered.get(i).quantity++;
                        break CHECK_PRODUCT_ID;

                    }

                }

                if (!isAlreadyAdded) {
                    listOrdered.add(orderInfo);
                }

                totalOrders++;

                if (totalOrders <= 1) {
                    tvToolbarRight.setText("" + totalOrders + " Order");
                } else {
                    tvToolbarRight.setText("" + totalOrders + " Orders");
                }

            }
        });

    }

    public void initHeader() {

        tvToolbarLeft = (TextView) view.findViewById(R.id.tvToolbarLeft);
        llToolbarTitle = (LinearLayout) view.findViewById(R.id.llToolbarTitle);
        tvToolbarTitle = (TextView) view.findViewById(R.id.tvToolbarTitle);
        tvToolbarRight = (TextView) view.findViewById(R.id.tvToolbarRight);
        ivTitleDropDown = (ImageView) view.findViewById(R.id.ivTitleDropDown);

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

        tvToolbarTitle.setText(businessInfo.short_name + " Menu");

        tvToolbarRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                OrderFragment orderFragment = new OrderFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("BUSINESS_INFO", businessInfo);
                bundle.putSerializable("ORDERED_LIST", (Serializable) listOrdered);
                orderFragment.setArguments(bundle);
                ((HomeActivity) getActivity()).addFragment(orderFragment);

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


}
