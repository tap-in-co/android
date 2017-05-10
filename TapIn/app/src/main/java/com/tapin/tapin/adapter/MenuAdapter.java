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
import com.tapin.tapin.model.BusinessMenu;
import com.tapin.tapin.model.BusinessType;
import com.tapin.tapin.utils.Debug;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Narendra on 4/24/17.
 */

public class MenuAdapter extends BaseAdapter {

    Context context;

    LayoutInflater inflater;

    ArrayList<BusinessMenu> listBusinessMenu = new ArrayList<>();

    String bg_color;
    String text_color;

    public MenuAdapter(Context c, String bg, String text) {

        this.context = c;
        inflater = (LayoutInflater.from(context));
        this.bg_color = bg;
        this.text_color = text;

    }

    public void addAll(List<BusinessMenu> list) {

        try {

            listBusinessMenu.clear();

            listBusinessMenu.addAll(list);

        } catch (Exception e) {
            e.printStackTrace();
        }

        notifyDataSetChanged();

    }

    @Override
    public int getCount() {
        return listBusinessMenu.size();
    }

    @Override
    public BusinessMenu getItem(int i) {
        return listBusinessMenu.get(i);
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
            convertView = inflater.inflate(R.layout.list_item_menu, viewGroup, false);

            holder.llMenu = (LinearLayout) convertView.findViewById(R.id.llMenu);

            holder.tvMenuType = (TextView) convertView.findViewById(R.id.tvMenuType);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        BusinessMenu businessMenu = listBusinessMenu.get(position);

        Debug.e("BG_COLOR", bg_color + " : " + text_color);

        holder.llMenu.setBackgroundColor(Color.parseColor(bg_color));

        holder.tvMenuType.setText(businessMenu.category + "(" + businessMenu.size + ")");
        holder.tvMenuType.setTextColor(Color.parseColor(text_color));

        return convertView;

    }

    public class ViewHolder {

        LinearLayout llMenu;
        TextView tvMenuType;
    }


}