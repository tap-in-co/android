package com.tapin.tapin.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.tapin.tapin.R;
import com.tapin.tapin.model.AddBusinessResp;
import com.tapin.tapin.utils.AlertMessages;
import com.tapin.tapin.utils.Constant;
import com.tapin.tapin.utils.Debug;
import com.tapin.tapin.utils.PreferenceManager;
import com.tapin.tapin.utils.ProgressHUD;
import com.tapin.tapin.utils.UrlGenerator;

import java.nio.charset.StandardCharsets;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends BaseActivity {

    ProgressHUD progressHUD;
    AlertMessages messages;
    private LinearLayout llBusinessContainer;
    private LinearLayout llCheckOrder;
    private EditText etBusinessName;
    private Button btnSubmit;
    private Button btnCheckOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        messages = new AlertMessages(this);

        initViews();

    }

    private void initViews() {

        etBusinessName = findViewById(R.id.edbusinessName);
        llBusinessContainer = findViewById(R.id.llBusinessContainer);
        btnSubmit = findViewById(R.id.btnSubmit);
        llCheckOrder = findViewById(R.id.llCheckOrder);

        btnCheckOrder = findViewById(R.id.btnCheckOrder);

        btnCheckOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String url = PreferenceManager.getNotificationLink();
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);

            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addRestaurant();
            }
        });

        btnCheckOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addRestaurant();
            }
        });

        PreferenceManager.putIsSubmitRestaurent(false);

        if (PreferenceManager.getIsSubmitRestaurent()) {
            llCheckOrder.setVisibility(View.VISIBLE);
            llBusinessContainer.setVisibility(View.GONE);
        } else {
            llCheckOrder.setVisibility(View.GONE);
            llBusinessContainer.setVisibility(View.VISIBLE);

        }

        etBusinessName.setOnEditorActionListener(new EditText.OnEditorActionListener() {


            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {

                if (actionId == EditorInfo.IME_ACTION_GO) {
                    //do here your stuff f
                    btnSubmit.performClick();
                    return true;
                }
                return false;
            }


        });
    }

    private void addRestaurant() {

        if (etBusinessName.getText().toString().length() == 0) {
            etBusinessName.setError("Please enter business name");
            return;
        }

        progressHUD = ProgressHUD.show(MainActivity.this, getString(R.string.please_wait), true, false);

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        RequestParams params = new RequestParams();
        params.put("cmd", "set_device_token_for_business");
        params.put("business_name", etBusinessName.getText().toString());
        params.put("device_token", refreshedToken);

        Debug.d("Parameters", params + "");
        Debug.d("Okhttp", "API: " + UrlGenerator.INSTANCE.getRewardApi() + " " + params.toString());

        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(Constant.TIMEOUT);
        client.get(UrlGenerator.INSTANCE.getRewardApi(), params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                String content = new String(responseBody, StandardCharsets.UTF_8);
                Debug.d("Okhttp", "Success Response: " + content);

                AddBusinessResp addBusinessResp = new Gson().fromJson(content, AddBusinessResp.class);
                if (addBusinessResp.status > -1) {
                    PreferenceManager.putIsSubmitRestaurent(true);
                    PreferenceManager.putNotificationLink("https://tapforall.com/staging/" + addBusinessResp.data.user_name);
                    llBusinessContainer.setVisibility(View.GONE);
                    llCheckOrder.setVisibility(View.VISIBLE);
                } else {
                    messages.showCustomMessage(addBusinessResp.error_message);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                String content = new String(responseBody, StandardCharsets.UTF_8);
                Debug.d("Okhttp", "Success Response: " + content);

            }

            @Override
            public void onFinish() {
                super.onFinish();
                if (progressHUD != null && progressHUD.isShowing()) {
                    progressHUD.dismiss();
                    progressHUD = null;
                }
            }
        });


    }
}
