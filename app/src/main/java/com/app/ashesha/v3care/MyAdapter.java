package com.app.ashesha.v3care;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.app.ashesha.v3care.HomeBanners.Banner;
import com.app.ashesha.v3care.Utils.AppConstants;
import com.bumptech.glide.Glide;

import java.util.List;

public class MyAdapter extends PagerAdapter {

    private List<Banner> images;
    private LayoutInflater inflater;
    private Context context;

    public MyAdapter(Context context, List<Banner> images) {
        this.context = context;
        this.images=images;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup view, int position) {
        View myImageLayout = inflater.inflate(R.layout.image_slider_layout_item, view, false);
        ImageView myImage = myImageLayout
                .findViewById(R.id.image);
         Banner banner = images.get(position);
        Glide.with(context).load(AppConstants.PICASSO_BASE_URL+banner.getBannerImage()).into(myImage);
        view.addView(myImageLayout, 0);
        return myImageLayout;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }
}