package com.app.ashesha.v3care.HomeBanners;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.app.ashesha.v3care.R;
import com.app.ashesha.v3care.Utils.AppConstants;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;


public class BannerAdapter extends PagerAdapter {
    private List<Banner> images;
    private LayoutInflater inflater;
    private Context context;


    public BannerAdapter(Context context, List<Banner> images) {
        this.context = context;
        this.images = images;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, final int position) {
        View myImageLayout = LayoutInflater.from(view.getContext()).inflate(R.layout.slidin_image, view, false);
        final Banner banner = images.get(position);
        ImageView myImage = myImageLayout.findViewById(R.id.image);

        Picasso.with(context)
                .load(AppConstants.PICASSO_BASE_URL + banner.getBannerImage())
                .placeholder(R.drawable.logo)
                .error(R.drawable.error_black)
                .into(myImage, new Callback() {
                    @Override
                    public void onSuccess() {
                        Log.e("image_slide", "image_slide " + AppConstants.PICASSO_BASE_URL + banner.getBannerImage());
                    }

                    @Override
                    public void onError() {
                        Picasso.with(context)
                                .load(AppConstants.PICASSO_BASE_URL + banner.getBannerImage())
                                .error(R.drawable.error_black)
                                .into(myImage, new Callback() {
                                    @Override
                                    public void onSuccess() {
                                        Log.e("image_s", AppConstants.PICASSO_BASE_URL + banner.getBannerImage());
                                    }

                                    @Override
                                    public void onError() {
                                        Log.e("Picasso", "Could not fetch image");
                                    }
                                });
                    }
                });
        view.addView(myImageLayout, 0);
        return view;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }
}
