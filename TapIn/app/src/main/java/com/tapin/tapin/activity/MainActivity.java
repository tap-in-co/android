package com.tapin.tapin.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
import com.tapin.tapin.utils.URLs;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Toolbar toolbar;
    private LinearLayout llBusinessContainer;
    private LinearLayout llCheckOrder;
    private EditText etBusinessName;
    private AlertMessages messages;
    private Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar(toolbar);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        messages = new AlertMessages(this);
        etBusinessName = (EditText) findViewById(R.id.edbusinessName);
        llBusinessContainer = (LinearLayout) findViewById(R.id.llBusinessContainer);
        btnSubmit= (Button) findViewById(R.id.btnSubmit);
        llCheckOrder = (LinearLayout) findViewById(R.id.llCheckOrder);
        findViewById(R.id.btnCheckOrder).setOnClickListener(this);
        btnSubmit.setOnClickListener(this);

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


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSubmit:
                //TODO implement
                addRestaurant();
                break;
            case R.id.btnCheckOrder:
                //TODO implement

                String url = PreferenceManager.getNotificationLink();
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
                break;
        }
    }


    ProgressDialog progressDialog;

    private void addRestaurant() {


        if(etBusinessName.getText().toString().length()==0)
        {
            etBusinessName.setError("Please enter business name");
            return;
        }
        progressDialog = ProgressDialog.show(MainActivity.this, "", "Loading...");
        progressDialog.show();
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        RequestParams params = new RequestParams();
        params.put("cmd", "set_device_token_for_business");
        params.put("business_name", etBusinessName.getText().toString());
        params.put("device_token", refreshedToken);

        Log.e("URL", URLs.REWARD);
        Log.e("Parameters", params + "");

        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(Constant.TIMEOUT);
        client.get(URLs.REWARD, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                try {
                    String content = new String(responseBody, "UTF-8");
                    Debug.e("send request succ", content + "-");

                    AddBusinessResp addBusinessResp = new Gson().fromJson(content, AddBusinessResp.class);
                    if (addBusinessResp.status > -1) {
                        PreferenceManager.putIsSubmitRestaurent(true);
                        PreferenceManager.putNotificationLink("https://tapforall.com/staging/" + addBusinessResp.data.user_name);
                        llBusinessContainer.setVisibility(View.GONE);
                        llCheckOrder.setVisibility(View.VISIBLE);
                    } else {
                        messages.showCustomMessage(addBusinessResp.error_message);
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Debug.e("send request fail", responseBody + "-");
                error.printStackTrace();
            }

            @Override
            public void onFinish() {
                super.onFinish();
                progressDialog.dismiss();
            }
        });


    }
}
