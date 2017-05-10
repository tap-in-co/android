package com.tapin.tapin.adapter;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.tapin.tapin.R;
import com.tapin.tapin.utils.URLs;

import java.util.List;

public class SlidingImage_Adapter extends PagerAdapter {


    private List<String> IMAGES;
    private LayoutInflater inflater;
    private Context context;
    private String businessID;

    public SlidingImage_Adapter(Context context, String businessID,List<String> IMAGES) {
        this.context = context;
        this.businessID=businessID;
        this.IMAGES = IMAGES;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return IMAGES.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View imageLayout = inflater.inflate(R.layout.list_item_image_slider, view, false);

        assert imageLayout != null;
        final ImageView imageView = (ImageView) imageLayout
                .findViewById(R.id.image);


        String URL=URLs.IMAGE_URL1 +businessID+"/"+ IMAGES.get(position);
       // Debug.e("view pager at"+position,URL);
        Glide.with(context).load(URL).placeholder(R.color.gray).into(imageView);

        view.addView(imageLayout, 0);

        return imageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }


}