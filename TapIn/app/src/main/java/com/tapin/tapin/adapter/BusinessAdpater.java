package com.tapin.tapin.adapter;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tapin.tapin.activity.HomeActivity;
import com.tapin.tapin.R;
import com.tapin.tapin.common.GPSTracker;
import com.tapin.tapin.fragment.BusinessDetailFragment;
import com.tapin.tapin.model.Business;
import com.tapin.tapin.model.OrderedInfo;
import com.tapin.tapin.utils.Constant;
import com.tapin.tapin.utils.URLs;
import com.tapin.tapin.utils.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

/**
 * Created by Narendra on 2/7/2017.
 */
public class BusinessAdpater extends RecyclerView.Adapter<BusinessAdpater.ViewHolder> {

    private ArrayList<Business> mData = new ArrayList<>();
    private ArrayList<Business> mDataOriginal = new ArrayList<>();
    private View mSelectedView;

    String time;
    FragmentActivity activity;

    Calendar calendar;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");

    GPSTracker gps;

    public BusinessAdpater(FragmentActivity activity, ArrayList<Business> data, String time) {

        mData.addAll((Collection<? extends Business>) data.clone());
        for (int i = 0; i < mData.size(); i++) {
            mDataOriginal.add(mData.get(i));
        }

        this.activity = activity;
        this.time = time;

        gps = new GPSTracker(activity);

        calendar = Calendar.getInstance();

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        final ViewHolder viewHolder;
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.listitem_business, viewGroup, false);
        viewHolder = new ViewHolder(v);
        viewHolder.itemView.setClickable(true);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                                                   @Override
                                                   public void onClick(View v) {
                                                       if (mSelectedView != null) {
                                                           mSelectedView.setSelected(false);
                                                       }
                                                       v.setSelected(true);
                                                       mSelectedView = v;
                                                   }
                                               }
        );
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        viewHolder.tvBusinessName.setText("" + mData.get(position).name);
        viewHolder.tvDescription.setText("" + mData.get(position).marketing_statement);
        viewHolder.tvBusinessType.setText("" + mData.get(position).businessTypes);
        viewHolder.tvNeighbourhood.setText("" + mData.get(position).neighborhood);

        if (gps.canGetLocation()) {

            Location crntLocation = new Location("crntlocation");
            crntLocation.setLatitude(gps.getLatitude());
            crntLocation.setLongitude(gps.getLongitude());

            Location newLocation = new Location("newlocation");
            newLocation.setLatitude(mData.get(position).lat);
            newLocation.setLongitude(mData.get(position).lng);

            viewHolder.tvDistance.setText(String.format("%.2f mi", (crntLocation.distanceTo(newLocation) / 1609.344)));

        }

        if (mData.get(position).icon != null) {
            Glide.with(activity).load(URLs.IMAGE_URL + mData.get(position).icon).into(viewHolder.ivBusinessIcon);
        }

        try {

            SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
            String currentTime = df.format(calendar.getTime());

            if (!Utils.isTimeBetweenTwoTime(mData.get(position).opening_time, mData.get(position).closing_time, currentTime)) {
                viewHolder.tvOpenStatus.setText("NOW CLOSED");
                viewHolder.tvOpenStatus.setTextColor(ContextCompat.getColor(activity, R.color.gray));
                viewHolder.tvOpeningTime.setText("");
            } else {
                viewHolder.tvOpenStatus.setText("OPEN NOW");
                viewHolder.tvOpenStatus.setTextColor(ContextCompat.getColor(activity, R.color.colorPrimary));
                viewHolder.tvOpeningTime.setText("" + Utils.getOpenTime(mData.get(position).opening_time, mData.get(position).closing_time));
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        viewHolder.ratingBar.setRating((float) mData.get(position).rating);

        viewHolder.card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                BusinessDetailFragment businessDetailFragment = new BusinessDetailFragment();
                Bundle bundle = new Bundle();
                Constant.listOrdered.clear();
                Constant.business = mData.get(position);
                businessDetailFragment.setArguments(bundle);
                ((HomeActivity) activity).addFragment(businessDetailFragment, R.id.frame_home);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData != null ? mData.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvBusinessName;
        TextView tvDescription;
        TextView tvBusinessType;
        TextView tvNeighbourhood;
        TextView tvDistance;
        RatingBar ratingBar;
        ImageView ivBusinessIcon;
        LinearLayout card_view;
        TextView tvOpenStatus;
        TextView tvOpeningTime;

        public ViewHolder(View itemView) {
            super(itemView);

            tvBusinessName = (TextView) itemView.findViewById(R.id.tvBusinessName);
            tvDescription = (TextView) itemView.findViewById(R.id.tvDescription);
            tvBusinessType = (TextView) itemView.findViewById(R.id.tvBusinessType);
            tvNeighbourhood = (TextView) itemView.findViewById(R.id.tvNeighbourhood);
            tvDistance = (TextView) itemView.findViewById(R.id.tvDistance);
            ratingBar = (RatingBar) itemView.findViewById(R.id.ratingBar);
            ivBusinessIcon = (ImageView) itemView.findViewById(R.id.ivBusinessIcon);
            card_view = (LinearLayout) itemView.findViewById(R.id.card_view);
            tvOpenStatus = (TextView) itemView.findViewById(R.id.tvOpenStatus);
            tvOpeningTime = (TextView) itemView.findViewById(R.id.tvOpeningTime);

        }
    }

    public void filter(String search) {

        mData.clear();

        if (search.length() == 0) {

            mData.addAll(mDataOriginal);
            notifyDataSetChanged();
            return;

        }

        for (int i = 0; i < mDataOriginal.size(); i++) {

            if (mDataOriginal.get(i).keywords != null && mDataOriginal.get(i).keywords.contains(search.toLowerCase())) {
                mData.add(mDataOriginal.get(i));
            }
        }

        notifyDataSetChanged();

    }
}
