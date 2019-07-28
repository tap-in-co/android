package com.tapin.tapin.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tapin.tapin.R;
import com.tapin.tapin.utils.AlertMessages;

/**
 * Created by Narendra on 6/16/17.
 */

public class PreviousOrderFragment extends BaseFragment {

    View view;

    AlertMessages messages;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_previous_order, container, false);

        messages = new AlertMessages(getActivity());

        initHeader();

        initViews();

        return view;

    }

    private void initHeader() {


    }

    private void initViews() {


    }
}
