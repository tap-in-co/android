package com.tapin.tapin.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tapin.tapin.R;
import com.tapin.tapin.model.BusinessType;
import com.tapin.tapin.utils.Debug;

import java.util.ArrayList;

public class BusinessDetailAdapter extends BaseAdapter {
    Context context;

    LayoutInflater inflter;
    ArrayList<BusinessType> data = new ArrayList<>();
    String bg_color;
    String text_color;

    public BusinessDetailAdapter(FragmentActivity activity, String bg, String text) {
        context = activity;
        inflter = (LayoutInflater.from(context));
        bg_color = bg;
        text_color = text;
    }

    public void addAll(ArrayList<BusinessType> businessList) {

        if (businessList != null) {
            data.addAll(businessList);
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public BusinessType getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflter.inflate(R.layout.list_item_business_detail, viewGroup, false);


            holder.llDetailContainer = (LinearLayout) convertView.findViewById(R.id.llDetailContainer);
            holder.ivCategory = (ImageView) convertView.findViewById(R.id.ivCategory);
            holder.tvBusinessType = (TextView) convertView.findViewById(R.id.tvBusinessType);
            holder.llPreviousOrder = (LinearLayout) convertView.findViewById(R.id.llPreviousOrder);
            holder.tvPreviousOrderCount = (TextView) convertView.findViewById(R.id.tvPreviousOrderCount);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        BusinessType businessType = getItem(i);
        holder.tvBusinessType.setText(businessType.display_name);

        if (businessType.display_name.equalsIgnoreCase("Order Food")) {
            holder.llPreviousOrder.setVisibility(View.VISIBLE);
        } else {
            holder.llPreviousOrder.setVisibility(View.GONE);
        }
        holder.ivCategory.setImageResource(getIcon(businessType.icon));
//        holder.llDetailContainer.setBackgroundColor(ContextCompat.getColor(context, bg_color));
        Debug.e("Colorm at "+i,bg_color+" : "+text_color);
        holder.llDetailContainer.setBackgroundColor(Color.parseColor(bg_color));
        holder.tvBusinessType.setTextColor(Color.parseColor(text_color));

        return convertView;
    }

    private int getIcon(String s) {
        Resources resources = context.getResources();
        final int resourceId = resources.getIdentifier(s, "drawable",
                context.getPackageName());
        //   Log.e("Icon",s+":"+resourceId);
        return resourceId;
    }

    class ViewHolder {
        LinearLayout llDetailContainer;
        ImageView ivCategory;
        TextView tvBusinessType;
        LinearLayout llPreviousOrder;
        TextView tvPreviousOrderCount;
    }


}