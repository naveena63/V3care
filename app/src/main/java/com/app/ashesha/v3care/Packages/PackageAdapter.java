package com.app.ashesha.v3care.Packages;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.app.ashesha.v3care.Cart.CartAdapter;
import com.app.ashesha.v3care.Utils.GlobalVariable;
import com.app.ashesha.v3care.services.ServicesListModel;
import com.app.ashesha.v3care.R;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class PackageAdapter extends RecyclerView.Adapter<PackageAdapter.ViewHolder> implements CartAdapter.OnItemClickListener {


    private Context context;
    private PackageListener packageListener;
    private List<PackageModel> packageModels;
    LinearLayout linearlayout;
    Button descrption;
    ViewGroup viewGroup;
    List<ServicesListModel> servicesList;


    public PackageAdapter(List<PackageModel> packageModel, PackageListener packageListener) {
        this.packageModels = packageModel;
        this.packageListener = packageListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        context = viewGroup.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.custom_packages_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, @SuppressLint("RecyclerView") final int i) {

        String imgPath = packageModels.get(i).getPackageImage();

        PackageModel packageModel = packageModels.get(i);
        String exclusion = packageModel.getExclusionl();
        String inclusion = packageModel.getInclusion();

//       Pack= packageModels.get(i);
        viewHolder.pack_imag.getLayoutParams().height = GlobalVariable.deviceHeight / 10;
        viewHolder.pack_imag.getLayoutParams().width = GlobalVariable.deviceWidth / 5;
        if (imgPath != null && !imgPath.equals("")) {

            Glide.with(context).load("http://v3care.com/" + imgPath).apply(new RequestOptions().placeholder(R.drawable.icon_placeholder).fitCenter()).into(viewHolder.pack_imag);
            Log.e("horizontalimage", "http://v3care.com/" + imgPath);
        }
        viewHolder.pack_name_tv.setText(packageModels.get(i).getPackage_name());

        viewHolder.package_slug.setText(packageModels.get(i).getService_slug());
        viewHolder.price_tv.setText("RS."+packageModels.get(i).getPackage_price());



        if (packageModels.get(i).getSub_package_status().equalsIgnoreCase("0")) {
            viewHolder.addbtn.setVisibility(View.VISIBLE);
        } else {
            viewHolder.addbtn.setVisibility(View.VISIBLE);
        }
        viewHolder.itemView.setOnClickListener(view -> {
            if (packageModels.get(i).getSub_package_status().equalsIgnoreCase("0")) {
                if (packageListener != null) {

                }
            }
        });
        viewHolder.addbtn.setOnClickListener(view -> {
            if (packageListener != null) {
                packageListener.onClickAddToCart(packageModels.get(i));
            }
        });

        descrption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Description");
                builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                View viewInflated = LayoutInflater.from(context).inflate(R.layout.popup, (ViewGroup) viewGroup, false);
                final TextView input = (TextView) viewInflated.findViewById(R.id.text);
                input.setText(exclusion);
                builder.setView(viewInflated);
                builder.show();

            }
        });
    }
    @Override
    public int getItemCount() {

        return packageModels.size();
    }

    @Override
    public void OnClick(View view, int position, String userID, String Cart_id) {

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        Button addbtn;
        TextView pack_name_tv, package_slug, price_tv, inclusion, exculsion;
        ImageView pack_imag;

        ViewHolder(View itemView) {
            super(itemView);
            addbtn = itemView.findViewById(R.id.btn);
            pack_name_tv = itemView.findViewById(R.id.pack_name_tv);
            package_slug = itemView.findViewById(R.id.package_slug);
            price_tv = itemView.findViewById(R.id.price_tv);
            linearlayout = itemView.findViewById(R.id.linearlayout);
            pack_imag = itemView.findViewById(R.id.image_view);
            descrption=itemView.findViewById(R.id.description);
        }
    }
}
