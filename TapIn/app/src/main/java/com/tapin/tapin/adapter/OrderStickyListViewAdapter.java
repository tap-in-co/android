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
import com.tapin.tapin.activity.HomeActivity;
import com.tapin.tapin.model.OrderInfo;
import com.tapin.tapin.model.resturants.Business;
import com.tapin.tapin.utils.PreferenceManager;
import com.tapin.tapin.utils.UrlGenerator;

import java.util.ArrayList;
import java.util.List;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

public class OrderStickyListViewAdapter extends BaseAdapter implements StickyListHeadersAdapter {

    private List<OrderInfo> listOrders = new ArrayList<>();

    private Activity context;

    private Business business;

    private AddOrder addOrder;

    public OrderStickyListViewAdapter(Activity activity, Business b, AddOrder addOrder) {
        this.context = activity;
        this.business = b;
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
        final ViewHolder holder;

        if (convertView == null) {

            convertView = inflater.inflate(R.layout.list_item_food, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);

        } else {

            holder = (ViewHolder) convertView.getTag();

        }

        final OrderInfo orderInfo = listOrders.get(position);

        holder.tvFoodTitle.setText(orderInfo.name);
        holder.tvFoodDescription.setText(orderInfo.short_description);
        holder.tvPrice.setText(business.getCurrSymbol() + " " + orderInfo.price);

        float earnPoints = Float.parseFloat(orderInfo.price);

        holder.tvEarnPoint.setText("Earn " + Math.round(earnPoints) + " Pts");

        if (orderInfo.availability_status.equalsIgnoreCase("1")) {

            holder.ivAdd.setVisibility(View.VISIBLE);
            holder.tvOut.setVisibility(View.GONE);

        } else {

            holder.ivAdd.setVisibility(View.GONE);
            holder.tvOut.setVisibility(View.VISIBLE);

        }

        holder.ivAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (context instanceof HomeActivity) {
                    // In Corporate Order View Mode so preventing users to add items to Order list
                    if (((HomeActivity)context).isCorporateOrder() && PreferenceManager.getInstance().isViewMode()) {
                        ((HomeActivity) context).showUnableToOrderDialog();
                    } else {
                        addOrder.addOrder(orderInfo);
                    }
                }
            }
        });

        if (orderInfo.pictures != null && orderInfo.pictures.length() > 0) {
            holder.ivFood.setVisibility(View.VISIBLE);
            String imageUrl = UrlGenerator.INSTANCE.getImageBaseApi() + orderInfo.businessID + "/products/" + orderInfo.pictures;
            Glide.with(context).load(imageUrl).centerCrop().placeholder(R.color.gray).into(holder.ivFood);
        } else {
            holder.ivFood.setVisibility(View.GONE);
        }

        if (orderInfo.isLiked) {
            holder.ivHeart.setImageResource(R.drawable.ic_heart_like);
        } else {
            holder.ivHeart.setImageResource(R.drawable.ic_heart_unlike);
        }

        String image = UrlGenerator.INSTANCE.getMainUrl() + orderInfo.product_icon;
        Glide.with(context).load(image).centerCrop().placeholder(R.mipmap.ic_launcher).into(holder.ivIcon);

        holder.ivHeart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((HomeActivity)context).isCorporateOrder() && PreferenceManager.getInstance().isViewMode()) {
                    ((HomeActivity) context).showUnableToOrderDialog();
                } else {
                    if (orderInfo.isLiked) {
                        holder.ivHeart.setImageResource(R.drawable.ic_heart_unlike);
                        orderInfo.isLiked = false;
                    } else {
                        holder.ivHeart.setImageResource(R.drawable.ic_heart_like);
                        orderInfo.isLiked = true;
                    }
                }
            }
        });

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

    public void addAll(List<OrderInfo> list) {

        try {

            listOrders.clear();

            listOrders.addAll(list);

        } catch (Exception e) {
            e.printStackTrace();
        }

        notifyDataSetChanged();

    }

    public interface AddOrder {

        void addOrder(OrderInfo orderInfo);

    }

    private class HeaderViewHolder {

        private LinearLayout llHeaderView;
        private TextView tvHeader;

        public HeaderViewHolder(View view) {
            llHeaderView = view.findViewById(R.id.llHeaderView);
            tvHeader = view.findViewById(R.id.tvHeader);
        }
    }

    class ViewHolder {
        private ImageView ivFood;
        private ImageView ivIcon;
        private TextView tvFoodTitle;
        private ImageView ivHeart;
        private TextView tvFoodDescription;
        private TextView tvPrice;
        private TextView tvEarnPoint;
        private ImageView ivAdd;
        private TextView tvOut;

        public ViewHolder(View view) {

            ivFood = view.findViewById(R.id.ivFood);
            ivIcon = view.findViewById(R.id.ivIcon);
            tvFoodTitle = view.findViewById(R.id.tvFoodTitle);
            ivHeart = view.findViewById(R.id.ivHeart);
            tvFoodDescription = view.findViewById(R.id.tvFoodDescription);
            tvPrice = view.findViewById(R.id.tvPrice);
            tvEarnPoint = view.findViewById(R.id.tvEarnPoint);
            ivAdd = view.findViewById(R.id.ivAdd);
            tvOut = view.findViewById(R.id.tvOut);

        }
    }

}