package com.tapin.tapin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;

import com.tapin.tapin.R;
import com.tapin.tapin.model.CardInfo;
import com.tapin.tapin.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Narendra on 6/10/17.
 */

public class CardPagerAdapter extends PagerAdapter {

    Context context;
    LayoutInflater mLayoutInflater;
    ArrayList<CardInfo> listCards = new ArrayList<>();

    public CardPagerAdapter(Context c) {

        this.context = c;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return listCards.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        View itemView = mLayoutInflater.inflate(R.layout.list_item_card_pager, container, false);

        TextView tvCardNumber = itemView.findViewById(R.id.tvCardNumber);
        TextView tvValid = itemView.findViewById(R.id.tvValid);

        CardInfo card = listCards.get(position);

        String cardExpiryDate = Utils.convertTime("yyyy-MM-dd", "MM/yy", card.expiration_date);

        tvCardNumber.setText("" + card.cc_no);
        tvValid.setText("" + cardExpiryDate);

        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }

    public void addAll(List<CardInfo> list) {

        try {

            listCards.clear();

            listCards.addAll(list);

        } catch (Exception e) {
            e.printStackTrace();
        }

        notifyDataSetChanged();

    }

}