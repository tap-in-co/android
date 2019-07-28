package com.tapin.tapin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.tapin.tapin.R;

import java.util.ArrayList;
import java.util.List;

public class NotificationAdapter extends BaseAdapter {

    private List<String> objects = new ArrayList<String>();

    private Context context;
    private LayoutInflater layoutInflater;

    public NotificationAdapter(Context context) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {

        return 10;
        //return objects.size();
    }

    @Override
    public String getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.list_item_notification, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        return convertView;
    }

    private void initializeViews(String object, ViewHolder holder) {
        //TODO implement


    }

    protected class ViewHolder {
        private TextView tvTime;
        private RatingBar ratingBar;
        private TextView tvDesc;
        private TextView textViewOpenClosed;
        private TextView tvOpenTime;

        public ViewHolder(View view) {
            tvTime = view.findViewById(R.id.tvTime);
            ratingBar = view.findViewById(R.id.ratingBar);
            tvDesc = view.findViewById(R.id.tvDesc);
            textViewOpenClosed = view.findViewById(R.id.textViewOpenClosed);
            tvOpenTime = view.findViewById(R.id.tvOpenTime);
        }
    }
}
