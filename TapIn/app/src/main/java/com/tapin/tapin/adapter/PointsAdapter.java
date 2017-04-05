package com.tapin.tapin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tapin.tapin.R;

import java.util.ArrayList;
import java.util.List;

public class PointsAdapter extends BaseAdapter {

    private List<String> objects = new ArrayList<String>();

    private Context context;
    private LayoutInflater layoutInflater;

    public PointsAdapter(Context context) {
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
            convertView = layoutInflater.inflate(R.layout.listitem_points, null);
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
        private TextView tvDate;
        private TextView tvEarnedPoints;
        private TextView tvPoints;

        public ViewHolder(View view) {
            tvDate = (TextView) view.findViewById(R.id.tvDate);
            tvEarnedPoints = (TextView) view.findViewById(R.id.tvEarnedPoints);
            tvPoints = (TextView) view.findViewById(R.id.tvPoints);
        }
    }
}
