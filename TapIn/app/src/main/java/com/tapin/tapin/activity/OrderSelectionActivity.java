package com.tapin.tapin.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;

import com.tapin.tapin.R;
import com.tapin.tapin.fragment.HomeFragment;

/**
 * Created by Narendra on 5/22/17.
 */

public class OrderSelectionActivity extends BaseActivity {

    public static final int RESULT_ACTIVITY_NEW_ORDER = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_selection);

        initViews();

    }

    private void initViews() {

        ((LinearLayout) findViewById(R.id.llNewOrder)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(OrderSelectionActivity.this, HomeActivity.class);
                startActivityForResult(i, RESULT_ACTIVITY_NEW_ORDER);

            }
        });
    }
}
