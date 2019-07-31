package com.tapin.tapin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tapin.tapin.R;
import com.tapin.tapin.model.OrderedInfo;
import com.tapin.tapin.model.resturants.Business;

import java.util.ArrayList;

/**
 * Created by Narendra on 6/11/17.
 */

public class FinalOrderSummaryAdapter extends BaseAdapter {

    Business business;
    private ArrayList<OrderedInfo> listOrders = new ArrayList<OrderedInfo>();
    private LayoutInflater mInflater;

    public FinalOrderSummaryAdapter(Context context, Business b) {
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

            convertView = mInflater.inflate(R.layout.list_item_final_order, null);

            holder.tvItemCount = convertView.findViewById(R.id.tvItemCount);

            holder.tvItemName = convertView.findViewById(R.id.tvItemName);

            holder.tvExtraItem = convertView.findViewById(R.id.tvExtraItem);

            holder.tvItemPriceTotal = convertView.findViewById(R.id.tvItemPriceTotal);

            convertView.setTag(holder);

        } else {

            holder = (ViewHolder) convertView.getTag();

        }

        try {

            final OrderedInfo order = listOrders.get(position);

            holder.tvItemCount.setText("" + order.quantity);

            holder.tvItemName.setText("" + order.product_name);

            holder.tvExtraItem.setText("" + order.product_option);

            holder.tvItemPriceTotal.setText("" + business.getCurrSymbol() + " " + String.format("%.2f", (order.quantity * order.price)));

            if (position == getCount() - 1) {

//                setTotal(listOrdered);

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

        TextView tvItemCount;

        TextView tvItemName;

        TextView tvExtraItem;

        TextView tvItemPriceTotal;

    }

}