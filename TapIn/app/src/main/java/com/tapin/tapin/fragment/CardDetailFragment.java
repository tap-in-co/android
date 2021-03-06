package com.tapin.tapin.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.tapin.tapin.R;
import com.tapin.tapin.adapter.CardAdapter;
import com.tapin.tapin.model.AllCardsInfo;
import com.tapin.tapin.model.CardInfo;
import com.tapin.tapin.model.UserInfo;
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

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

/**
 * Created by Narendra on 5/24/17.
 */

public class CardDetailFragment extends BaseFragment {

    public static UpdateCards updateCards;
    View view;
    EditText etCardNumber;
    EditText etMonth;
    EditText etYear;
    EditText etCvv;
    EditText etZipcode;
    SwipeMenuListView listCards;
    AllCardsInfo cardsInfo;
    CardAdapter cardAdapter;
    ProgressHUD pd;
    AlertMessages messages;
    String visa = "^4[0-9]{6,}$";
    String MasterCard = "^5[1-5][0-9]{5,}$";
    String AmericanExpress = "^3[47][0-9]{5,}$";
    String DinersClub = "^3(?:0[0-5]|[68][0-9])[0-9]{4,}$";
    String Discover = "^6(?:011|5[0-9]{2})[0-9]{3,}$";
    String JCB = "^(?:2131|1800|35[0-9]{3})[0-9]{3,}$";
    EditText.OnEditorActionListener onZipDoneClicked = new EditText.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_SEARCH
                    || actionId == EditorInfo.IME_ACTION_DONE
                    || event.getAction() == KeyEvent.ACTION_DOWN
                    && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                validateAndAddCard();
                return true;
            }
            // Return true if you have consumed the action, else false.
            return false;
        }
    };
    View.OnClickListener onClickListenerbtnAdd = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            validateAndAddCard();
        }
    };

    // TODO: Rename and change types and number of parameters
    public static CardDetailFragment newInstance(UpdateCards uc) {

        CardDetailFragment fragment = new CardDetailFragment();

        updateCards = uc;

        return fragment;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_card_detail, container, false);

        messages = new AlertMessages(getActivity());

        cardAdapter = new CardAdapter(getActivity());

        initHeader();

        initViews();

        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // Create different menus depending on the view type
                switch (menu.getViewType()) {
                    case 0:
                        SwipeMenuItem item1 = new SwipeMenuItem(
                                getActivity());
                        item1.setBackground(new ColorDrawable(Color.rgb(0xF9,
                                0x3F, 0x25)));
                        item1.setWidth(dp2px(90));
                        item1.setTitle("Delete");
                        item1.setTitleSize(18);
                        item1.setTitleColor(ContextCompat.getColor(getActivity(), R.color.white));
                        menu.addMenuItem(item1);
                        break;
                }
            }

        };

        listCards.setMenuCreator(creator);
        listCards.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {

                switch (index) {
                    case 0:
                        final CardInfo cardInfo = cardAdapter.getItem(position);
                        if (cardInfo.isDefaultCard()) {
                            showDefaultCardCanNotBeDeletedAlert();
                        } else {
                            deleteCard(position, cardAdapter.getItem(position));
                        }

                        break;
                }
                return false;
            }
        });

        listCards.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                final CardInfo cardInfo = cardAdapter.getItem(pos);
                if (cardInfo.isDefaultCard()) {
                    // Already this is default Card
                } else {
                    if (getActivity() != null) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle("Mark as default Card!")
                                .setMessage("Are you sure you want to mark this card as your default card?").setCancelable(false)
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        makeCardAsDefault(cardInfo.cc_no, cardInfo.expiration_date, cardInfo.cvv, cardInfo.zip_code, cardInfo.card_type, 1);
                                    }
                                });

                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                }
            }
        });

        if (Utils.isInternetConnected(getActivity())) {
            getCards();
        } else {
            messages.showErrorInConnection();
        }

        return view;

    }

    private void validateAndAddCard() {
        String cardNumber = etCardNumber.getText().toString();
        String month = etMonth.getText().toString();
        String year = etYear.getText().toString();
        String cvv = etCvv.getText().toString();
        String zip = etZipcode.getText().toString();

        if (cardNumber.length() < 16) {
            etCardNumber.setError("Card Number is not valid");
        } else if (month.length() < 2) {
            etMonth.setError("Please enter valid Month");
        } else if (year.length() < 2) {
            etYear.setError("Please enter valid Year");
        } else if (cvv.length() < 3) {
            etCvv.setError("Please enter valid CVV");
        } else if (zip.length() < 5) {
            etZipcode.setError("Please enter valid Zipcode");
        } else {
            if (Utils.isInternetConnected(getActivity())) {

                String card_type = "Visa";
                if (cardNumber.matches(visa)) {
                    card_type = "Visa";
                } else if (cardNumber.matches(MasterCard)) {
                    card_type = "MasterCard";
                } else if (cardNumber.matches(AmericanExpress)) {
                    card_type = "American Express";
                } else if (cardNumber.matches(DinersClub)) {
                    card_type = "Diners Club";
                } else if (cardNumber.matches(Discover)) {
                    card_type = "Discover";
                } else if (cardNumber.matches(JCB)) {
                    card_type = "JCB";
                } else {
                    card_type = "Unknown";
                }

                addCard(cardNumber, month, year, cvv, zip, card_type, cardAdapter.listCards.size() == 0 ? 1 : 0);
            } else {
                messages.showErrorInConnection();
            }

        }
    }

    private void showDefaultCardCanNotBeDeletedAlert() {
        if (getActivity() != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("This Card is your default Card!")
                    .setMessage("Please Add or Choose another card to be your default card.").setCancelable(false)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });

            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    private void deleteCard(final int pos, CardInfo cardInfo) {

        pd = ProgressHUD.show(getActivity(), getString(R.string.please_wait), true, false);

        String URL = UrlGenerator.INSTANCE.getMainUrl();

        try {

            final JSONObject json = new JSONObject();

            json.put("cmd", "remove_cc");
            json.put("consumer_id", PreferenceManager.getUserId());

            JSONObject jsonCard = new JSONObject();
            jsonCard.put("expiration_date", cardInfo.expiration_date);
            jsonCard.put("cc_no", cardInfo.cc_no);
            jsonCard.put("zip_code", cardInfo.zip_code);
            jsonCard.put("cvv", cardInfo.cvv);

            JSONArray jsonArray = new JSONArray();
            jsonArray.put(jsonCard);

            json.put("data", jsonArray);

            StringEntity entity = new StringEntity(json.toString());

            Debug.d("Okhttp", "API: " + URL + " " + json.toString());

            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(Constant.TIMEOUT);

            client.post(getActivity(), URL, entity, "application/json", new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                    try {
                        String content = new String(responseBody, StandardCharsets.UTF_8);
                        Debug.d("Okhttp", "Success Response: " + content);

                        cardAdapter.listCards.remove(pos);

                        cardAdapter.notifyDataSetChanged();

                    } catch (Exception e) {

                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                    try {
                        String content = new String(responseBody, StandardCharsets.UTF_8);
                        Debug.d("Okhttp", "Failure Response: " + content);
                    } catch (Exception e) {

                        e.printStackTrace();
                    }
                }

                @Override
                public void onFinish() {
                    super.onFinish();
                    if (pd != null && pd.isShowing()) {
                        pd.dismiss();
                        pd = null;
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getCards() {

        pd = ProgressHUD.show(getActivity(), getString(R.string.please_wait), true, false);

        try {

            UserInfo userInfo = PreferenceManager.getUserInfo();

            String URL = UrlGenerator.INSTANCE.getMainUrl() + "cmd=get_consumer_all_cc_info&consumer_id=" + userInfo.uid;

            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(Constant.TIMEOUT);

            Debug.d("Okhttp", "API: " + URL);

            client.get(getActivity(), URL, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                    try {

                        String content = new String(responseBody, StandardCharsets.UTF_8);
                        Debug.d("Okhttp", "Success Response: " + content);

                        cardsInfo = new Gson().fromJson(content, AllCardsInfo.class);

                        if (cardsInfo.status == 0) {

                            cardAdapter.addAll(cardsInfo.listCards);

                            setListViewHeightBasedOnChildren(listCards);

                        }

                        Toast.makeText(getActivity(), "Total Cards:-" + cardsInfo.listCards.size(), Toast.LENGTH_SHORT).show();

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

                    String content = new String(responseBody, StandardCharsets.UTF_8);
                    Debug.d("Okhttp", "Failure Response: " + content);
                }

                @Override
                public void onFinish() {
                    super.onFinish();

                    if (pd != null && pd.isShowing()) {
                        pd.dismiss();
                        pd = null;
                    }
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addCard(String cardNo, String month, String year, String cvv, String zipcode, String card_type, int defaultCard) {

        pd = ProgressHUD.show(getActivity(), getString(R.string.please_wait), true, false);

        try {

            final JSONObject json = new JSONObject();

            json.put("cmd", "save_cc_info");
            json.put("consumer_id", PreferenceManager.getUserId());
            json.put("cc_no", cardNo);
            json.put("expiration_date", year + "-" + month + "-01");
            json.put("cvv", cvv);
            json.put("zip_code", zipcode);
            json.put("default", defaultCard);
            json.put("card_type", card_type);

            StringEntity entity = new StringEntity(json.toString());

            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(Constant.TIMEOUT);

            String URL = UrlGenerator.INSTANCE.getMainUrl();
            Debug.d("Okhttp", "API: " + URL + " " + json.toString());

            client.post(getActivity(), URL, entity, "application/json", new AsyncHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                    if (pd != null && pd.isShowing()) {
                        pd.dismiss();
                        pd = null;
                    }

                    try {

                        String content = new String(responseBody, StandardCharsets.UTF_8);
                        Debug.d("Okhttp", "Success Response: " + content);

                        JSONObject jsonObject = new JSONObject(content);

                        String message = jsonObject.getString("message");

                        if (message.equalsIgnoreCase("success")) {

                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

                            PreferenceManager.saveCardData(json.toString());

                            if (updateCards != null) {
                                getActivity().onBackPressed();
                                updateCards.selectedCardPosition(0);
                            } else {
                                getActivity().findViewById(R.id.llHome).performClick();
                            }

                        } else {

                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

                        }

                        clearInput();
                        getCards();

                    } catch (Exception e) {

                        e.printStackTrace();
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

                        e.printStackTrace();
                    }


                }

            });
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private void makeCardAsDefault(String cardNo, String expirationDate, String cvv, String zipcode, String card_type, int defaultCard) {

        pd = ProgressHUD.show(getActivity(), getString(R.string.please_wait), true, false);

        try {

            final JSONObject json = new JSONObject();

            json.put("cmd", "save_cc_info");
            json.put("consumer_id", PreferenceManager.getUserId());
            json.put("cc_no", cardNo);
            json.put("expiration_date", expirationDate);
            json.put("cvv", cvv);
            json.put("zip_code", zipcode);
            json.put("default", defaultCard);
            json.put("card_type", card_type);

            StringEntity entity = new StringEntity(json.toString());

            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(Constant.TIMEOUT);

            String URL = UrlGenerator.INSTANCE.getMainUrl();
            Debug.d("Okhttp", "API: " + URL + " " + json.toString());

            client.post(getActivity(), URL, entity, "application/json", new AsyncHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                    if (pd != null && pd.isShowing()) {
                        pd.dismiss();
                        pd = null;
                    }

                    try {

                        String content = new String(responseBody, StandardCharsets.UTF_8);
                        Debug.d("Okhttp", "Success Response: " + content);

                        JSONObject jsonObject = new JSONObject(content);

                        String message = jsonObject.getString("message");

                        if (message.equalsIgnoreCase("success")) {

                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

                            PreferenceManager.saveCardData(json.toString());

                            if (updateCards != null) {
                                getActivity().onBackPressed();
                                updateCards.selectedCardPosition(0);
                            } else {
                                getActivity().findViewById(R.id.llHome).performClick();
                            }

                        } else {

                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

                        }

                        clearInput();
                        getCards();

                    } catch (Exception e) {

                        e.printStackTrace();
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

                        e.printStackTrace();
                    }
                }

            });
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private void initHeader() {

        ((TextView) view.findViewById(R.id.tvToolbarTitle)).setText("Save Card Detail");

    }

    private void initViews() {

        etCardNumber = view.findViewById(R.id.etCardNumber);
        etMonth = view.findViewById(R.id.etMonth);
        etYear = view.findViewById(R.id.etYear);
        etCvv = view.findViewById(R.id.etCvv);
        etZipcode = view.findViewById(R.id.etZipcode);
        etZipcode.setOnEditorActionListener(onZipDoneClicked);

        view.findViewById(R.id.btnAdd).setOnClickListener(onClickListenerbtnAdd);

        listCards = view.findViewById(R.id.listCards);
        listCards.setAdapter(cardAdapter);

    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getActivity().getResources().getDisplayMetrics());
    }

    public void setListViewHeightBasedOnChildren(ListView listView) {

        if (cardAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
        for (int i = 0; i < cardAdapter.getCount(); i++) {
            View listItem = cardAdapter.getView(i, null, listView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (cardAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();

    }

    private void clearInput() {
        etCardNumber.setText("");
        etMonth.setText("");
        etYear.setText("");
        etCvv.setText("");
        etZipcode.setText("");
    }

    public interface UpdateCards {

        void cardList();

        void selectedCardPosition(int pos);

    }
}