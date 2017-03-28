package com.tapin.tapin.adapter;

import android.app.Activity;
import android.location.Location;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tapin.tapin.R;
import com.tapin.tapin.common.GPSTracker;
import com.tapin.tapin.model.BusinessInfo;
import com.tapin.tapin.utils.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

/**
 * Created by abcd on 2/7/2017.
 */
public class BusinessAdpater extends RecyclerView.Adapter<BusinessAdpater.ViewHolder> {
    private ArrayList<BusinessInfo> mData = new ArrayList<>();
    private ArrayList<BusinessInfo> mDataOriginal = new ArrayList<>();
    private View mSelectedView;

    String time;
    Activity activity;

    Calendar calendar;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");

    GPSTracker gps;

    public BusinessAdpater(Activity activity, ArrayList<BusinessInfo> data, String time) {
//        mData = data;

//        mDataOriginal.addAll((Collection<? extends BusinessInfo>) mData.clone());
        mData.addAll((Collection<? extends BusinessInfo>) data.clone());
        for (int i = 0; i < mData.size(); i++) {
            mDataOriginal.add(mData.get(i));
        }

        this.activity = activity;
        this.time = time;

        gps = new GPSTracker(activity);

        try {
            Date date = simpleDateFormat.parse(time);
            calendar = Calendar.getInstance();
            calendar.setTimeInMillis(date.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
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
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        viewHolder.textViewName.setText(mData.get(position).name + "");
        viewHolder.textViewDescription.setText(Utils.isNotEmpty(mData.get(position).keywords) ? mData.get(position).keywords : "");
        viewHolder.textViewBusinessType.setText(Utils.isNotEmpty(mData.get(position).type) ? mData.get(position).type : "");
        viewHolder.textViewCity.setText(mData.get(position).city + "");

        if (gps.canGetLocation()) {
            Location crntLocation = new Location("crntlocation");
            crntLocation.setLatitude(gps.getLatitude());
            crntLocation.setLongitude(gps.getLongitude());

            Location newLocation = new Location("newlocation");
            newLocation.setLatitude(mData.get(position).lat);
            newLocation.setLongitude(mData.get(position).lng);

            viewHolder.textViewLocation.setText(String.format("%.2f mi", (crntLocation.distanceTo(newLocation) / 1609.344)));

        }

        Glide.with(activity).load("https://tapforall.com/staging/tap-in/customer_files/icons/" + mData.get(position).icon).into(viewHolder.imageViewIcon);

        try {
            Date dateOpening = simpleDateFormat.parse(mData.get(position).opening_time);
            Date dateClosing = simpleDateFormat.parse(mData.get(position).closing_time);

            Calendar calendarOpening = Calendar.getInstance();
            calendarOpening.setTimeInMillis(dateOpening.getTime());

            Calendar calendarClosing = Calendar.getInstance();
            calendarClosing.setTimeInMillis(dateClosing.getTime());

            if (calendar.getTimeInMillis() > calendarOpening.getTimeInMillis()
                    && calendar.getTimeInMillis() < calendarClosing.getTimeInMillis()) {
                viewHolder.textViewOpenClosed.setText("OPEN NOW");
                viewHolder.textViewOpenClosed.setTextColor(ContextCompat.getColor(activity, R.color.yellow));
            } else {
                viewHolder.textViewOpenClosed.setText("NOW CLOSED");
                viewHolder.textViewOpenClosed.setTextColor(ContextCompat.getColor(activity, R.color.gray));

            }

        } catch (ParseException e) {
            e.printStackTrace();
        }


        viewHolder.ratingBar.setRating((float) mData.get(position).rating);

        viewHolder.card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                FragmentTransaction trans = activity.getFragmentManager().beginTransaction();
        /*
         * IMPORTANT: We use the "root frame" defined in
         * "root_fragment.xml" as the reference to replace fragment
         */
//                trans.replace(R.id.root_frame, new SecondFragment());

        /*
         * IMPORTANT: The following lines allow us to add the fragment
         * to the stack and return to it later, by pressing back
         */
//                trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//                trans.addToBackStack(null);
//
//                trans.commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData != null ? mData.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName, textViewDescription, textViewBusinessType, textViewCity, textViewLocation, textViewOpenClosed;
        RatingBar ratingBar;
        ImageView imageViewIcon;
        RelativeLayout card_view;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewName = (TextView) itemView.findViewById(R.id.textViewName);
            textViewDescription = (TextView) itemView.findViewById(R.id.textViewDescription);
            textViewBusinessType = (TextView) itemView.findViewById(R.id.textViewBusinessType);
            textViewCity = (TextView) itemView.findViewById(R.id.textViewCity);
            textViewLocation = (TextView) itemView.findViewById(R.id.textViewLocation);
            ratingBar = (RatingBar) itemView.findViewById(R.id.ratingBar);
            textViewOpenClosed = (TextView) itemView.findViewById(R.id.textViewOpenClosed);
            imageViewIcon = (ImageView) itemView.findViewById(R.id.imageViewIcon);
            card_view = (RelativeLayout) itemView.findViewById(R.id.card_view);
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

            if (mDataOriginal.get(i).keywords.toLowerCase().contains(search.toLowerCase())) {
                mData.add(mDataOriginal.get(i));
            }
        }
        notifyDataSetChanged();


    }
}
