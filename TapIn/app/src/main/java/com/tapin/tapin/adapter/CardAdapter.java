package com.tapin.tapin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tapin.tapin.R;
import com.tapin.tapin.model.CardInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Narendra on 6/10/17.
 */

public class CardAdapter extends BaseAdapter {

    public ArrayList<CardInfo> listCards = new ArrayList<>();
    Context context;
    LayoutInflater inflater;

    public CardAdapter(Context c) {

        this.context = c;
        inflater = (LayoutInflater.from(context));

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

    @Override
    public int getCount() {
        return listCards.size();
    }

    @Override
    public CardInfo getItem(int i) {
        return listCards.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {

        ViewHolder holder;

        if (convertView == null) {

            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.list_item_card, viewGroup, false);

            holder.tvCardDetail = convertView.findViewById(R.id.tvCardDetail);
            holder.imageViewDefalutCard = convertView.findViewById(R.id.imgvDefaultCard);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        CardInfo cardInfo = listCards.get(position);

        holder.tvCardDetail.setText(cardInfo.card_type + "     " + "xxxx xxxx xxxx " + cardInfo.cc_no.substring(cardInfo.cc_no.length() - 4));
        holder.imageViewDefalutCard.setVisibility(cardInfo.isDefaultCard() ? View.VISIBLE : View.GONE);

        return convertView;

    }

    public class ViewHolder {

        TextView tvCardDetail;
        ImageView imageViewDefalutCard;
    }

}