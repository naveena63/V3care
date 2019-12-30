package com.app.ashesha.v3care.OfferBanners;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.app.ashesha.v3care.R;
import com.app.ashesha.v3care.Utils.GlobalVariable;
import com.app.ashesha.v3care.Utils.PrefManager;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class OffersAdapter  extends RecyclerView.Adapter<OffersAdapter.ViewHolder> {


    private Context context;
    private List<OfferBanners> offerBannerList;
    PrefManager prefManager;

    public OffersAdapter(Context context, List<OfferBanners> offerBannerList) {
        this.context = context;
        this.offerBannerList = offerBannerList;
    }

    @NonNull
    @Override
    public OffersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.offers_banner, viewGroup, false);
        prefManager = new PrefManager(context);
        return new OffersAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OffersAdapter.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        String imgPath = offerBannerList.get(position).getBannerImage();
        holder.serviceImg.getLayoutParams().height = GlobalVariable.deviceHeight / 10;
        holder.serviceImg.getLayoutParams().width = GlobalVariable.deviceWidth / 5;
        if (imgPath != null && !imgPath.equals("")) {
            Glide.with(context).load("http://v3care.com/" + imgPath).apply(new RequestOptions().placeholder(R.drawable.icon_placeholder).fitCenter()).into(holder.serviceImg);

            //https://ashesha.in/ ase path ok
           //upload/banner/d7ddcccd8bd53cf6729cef8ee604710f.jpg   image path

            Log.e("horizontalimage", "http://v3care.com/" + imgPath);
        }
        holder.serviceNameTv.setText(offerBannerList.get(position).getImageName());
        holder.itemView.setOnClickListener(view -> {

//            Intent intent = new Intent(context, PackagesActivity.class);
//            context.startActivity(intent);

            //  Log.e("ServiceId","hhdhdhdh"+servicesListModel.get(position).getServiceId());

            //  prefManager.setServiceId(offerBannerList.get(position).getId());


            // homeScreenListener.onClickGridItem(servicesListModel.get(position).getServiceId(),servicesListModel.get(position).getServiceName());

        });
    }

    @Override
    public int getItemCount() {

        return offerBannerList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView serviceImg;
        TextView serviceNameTv;

        ViewHolder(View itemView) {
            super(itemView);
            serviceImg = itemView.findViewById(R.id.image_view);
            serviceNameTv = itemView.findViewById(R.id.name);
        }
    }
}
