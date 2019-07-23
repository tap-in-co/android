package com.tapin.tapin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tapin.tapin.R;
import com.tapin.tapin.fragment.OrderFragment;
import com.tapin.tapin.model.Business;
import com.tapin.tapin.model.OrderedInfo;
import com.tapin.tapin.utils.Constant;

import java.util.ArrayList;

/**
 * Created by Narendra on 6/11/17.
 */

public class FinalOrderSummaryAdapter extends BaseAdapter {

    private ArrayList<OrderedInfo> listOrders = new ArrayList<OrderedInfo>();

    Business business;

    private LayoutInflater mInflater;

    public FinalOrderSummaryAdapter(Context context,Business b) {
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

            holder.tvItemCount = (TextView) convertView.findViewById(R.id.tvItemCount);

            holder.tvItemName = (TextView) convertView.findViewById(R.id.tvItemName);

            holder.tvExtraItem = (TextView) convertView.findViewById(R.id.tvExtraItem);

            holder.tvItemPriceTotal = (TextView) convertView.findViewById(R.id.tvItemPriceTotal);

            convertView.setTag(holder);

        } else {

            holder = (ViewHolder) convertView.getTag();

        }

        try {

            final OrderedInfo order = listOrders.get(position);

            holder.tvItemCount.setText("" + order.quantity);

            holder.tvItemName.setText("" + order.product_name);

            holder.tvExtraItem.setText("" + order.product_option);

            holder.tvItemPriceTotal.setText(""+business.curr_symbol+" " + String.format("%.2f", (order.quantity * order.price)));

            if (position == getCount() - 1) {

//                setTotal(listOrdered);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return convertView;
    }

    public class ViewHolder {

        TextView tvItemCount;

        TextView tvItemName;

        TextView tvExtraItem;

        TextView tvItemPriceTotal;

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

}