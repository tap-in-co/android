package com.tapin.tapin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tapin.tapin.R;
import com.tapin.tapin.model.PointInfo;
import com.tapin.tapin.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class PointsAdapter extends BaseAdapter {

    SimpleDateFormat format = new SimpleDateFormat("d");
    private List<PointInfo> objects = new ArrayList<PointInfo>();
    private Context context;
    private LayoutInflater layoutInflater;

    public PointsAdapter(Context context) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {

        return objects.size();
    }

    @Override
    public PointInfo getItem(int position) {
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

        PointInfo pointInfo = getItem(position);
        holder.tvDate.setText(Utils.getPointsDate(pointInfo.activity_time));
//        holder.tvEarnedPoints.setText(pointInfo.);
        holder.tvPoints.setText(pointInfo.points);
        return convertView;
    }


    public void addAll(List<PointInfo> listPointInfos) {

        if (listPointInfos != null) {
            objects.clear();
            objects.addAll(listPointInfos);
        }
        notifyDataSetChanged();
    }

    protected class ViewHolder {
        private TextView tvDate;
        private TextView tvEarnedPoints;
        private TextView tvPoints;

        public ViewHolder(View view) {
            tvDate = view.findViewById(R.id.tvDate);
            tvEarnedPoints = view.findViewById(R.id.tvEarnedPoints);
            tvPoints = view.findViewById(R.id.tvPoints);
        }
    }
}
