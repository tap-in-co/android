package com.tapin.tapin.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tapin.tapin.R;
import com.tapin.tapin.model.BusinessMenu;
import com.tapin.tapin.model.OrderInfo;
import com.tapin.tapin.utils.URLs;

import java.util.ArrayList;
import java.util.List;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

public class OrderStickyListViewAdapter extends BaseAdapter implements StickyListHeadersAdapter {

    private List<OrderInfo> listOrders = new ArrayList<>();

    private Activity context;

    private AddOrder addOrder;

    public interface AddOrder {

        public void addOrder(OrderInfo orderInfo);

    }

    public OrderStickyListViewAdapter(Activity activity, AddOrder addOrder) {
        this.context = activity;
        this.addOrder = addOrder;

    }

    @Override
    public int getCount() {
        return listOrders.size();
    }

    @Override
    public Object getItem(int position) {
        return listOrders.get(position);
    }

    @Override
    public long getItemId(int position) {
        return listOrders.indexOf(getItem(position));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        ViewHolder holder;

        if (convertView == null) {

            convertView = inflater.inflate(R.layout.list_item_child, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);

        } else {

            holder = (ViewHolder) convertView.getTag();

        }

        final OrderInfo orderInfo = listOrders.get(position);

        holder.tvFoodTitle.setText(orderInfo.name);
        holder.tvFoodDescription.setText(orderInfo.short_description);
        holder.tvPrice.setText(orderInfo.price);

        float earnPoints = Float.parseFloat(orderInfo.price);

        holder.tvEarnPoint.setText("Earn " + Math.round(earnPoints) + " Pts");

        holder.ivAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                addOrder.addOrder(orderInfo);

            }
        });

        String imageUrl = URLs.IMAGE_URL1 + orderInfo.businessID + "/products/" + orderInfo.pictures;

        Glide.with(context).load(imageUrl).centerCrop().placeholder(R.color.gray).into(holder.ivFood);

        return convertView;
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {

        HeaderViewHolder holder;

        LayoutInflater inflater = LayoutInflater.from(context);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item_header, parent, false);
            holder = new HeaderViewHolder(convertView);
            convertView.setTag(holder);

        } else {
            holder = (HeaderViewHolder) convertView.getTag();
        }

        String productCategoryId = listOrders.get(position).product_category_id;

        int count = 0;
        for (int i = 0; i < listOrders.size(); i++) {

            if (listOrders.get(i).product_category_id.equalsIgnoreCase(productCategoryId)) {
                count++;
            }

        }

        //set header text based on first letter
        String headerText = "" + listOrders.get(position).category_name + " (" + count + ")";
        holder.tvHeader.setText(headerText);
        return convertView;
    }

    @Override
    public long getHeaderId(int position) {

        String productCategoryId = listOrders.get(position).product_category_id;

        return Long.parseLong(productCategoryId, 10);

    }

    private class HeaderViewHolder {

        private LinearLayout llHeaderView;
        private TextView tvHeader;

        public HeaderViewHolder(View view) {
            llHeaderView = (LinearLayout) view.findViewById(R.id.llHeaderView);
            tvHeader = (TextView) view.findViewById(R.id.tvHeader);
        }

    }

    class ViewHolder {
        private ImageView ivFood;
        private TextView tvFoodTitle;
        private ImageView ivHeart;
        private TextView tvFoodDescription;
        private TextView tvPrice;
        private TextView tvEarnPoint;
        private ImageView ivAdd;

        public ViewHolder(View view) {
            ivFood = (ImageView) view.findViewById(R.id.ivFood);
            tvFoodTitle = (TextView) view.findViewById(R.id.tvFoodTitle);
            ivHeart = (ImageView) view.findViewById(R.id.ivHeart);
            tvFoodDescription = (TextView) view.findViewById(R.id.tvFoodDescription);
            tvPrice = (TextView) view.findViewById(R.id.tvPrice);
            tvEarnPoint = (TextView) view.findViewById(R.id.tvEarnPoint);
            ivAdd = (ImageView) view.findViewById(R.id.ivAdd);
        }
    }

    public void addAll(List<OrderInfo> list) {

        try {

            listOrders.clear();

            listOrders.addAll(list);

        } catch (Exception e) {
            e.printStackTrace();
        }

        notifyDataSetChanged();

    }

}