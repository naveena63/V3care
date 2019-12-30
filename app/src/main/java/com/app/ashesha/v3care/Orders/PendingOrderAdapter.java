package com.app.ashesha.v3care.Orders;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.ashesha.v3care.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class PendingOrderAdapter extends RecyclerView.Adapter<PendingOrderAdapter.VH> {
    Context context;
    List<CompleteOrderModel> pendingOrderModelList;

    public PendingOrderAdapter(Context context, List<CompleteOrderModel> pendingOrderModelList)
    {
        this.context = context;
        this.pendingOrderModelList = pendingOrderModelList;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).
                inflate(R.layout.pending_order_layout,
                        viewGroup,
                        false);


        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        Log.v("DDDD","");
        holder.textOrderId.setText("Order id :   "+pendingOrderModelList.get(position).getOrderId());
        holder.textPendindDate.setText("Service date :   "+pendingOrderModelList.get(position).getServiceDate());
        holder.textOrderAmount.setText("Order Amount :   "+pendingOrderModelList.get(position).getPrice());
        holder.paymentype.setText("Payment Type :   "+pendingOrderModelList.get(position).getPaymentType());


    }
    @Override
    public int getItemCount() {
        return pendingOrderModelList.size();
    }

    public class VH extends RecyclerView.ViewHolder {
        TextView textPendindDate,textOrderId,textOrderAmount,paymentype;

        public VH( View itemView) {
            super(itemView);
            textPendindDate=itemView.findViewById(R.id.textview_pendingDate);
            textOrderId=itemView.findViewById(R.id.textView_orderId);
            textOrderAmount=itemView.findViewById(R.id.textView_orderAmount);
            paymentype=itemView.findViewById(R.id.payment_type);
        }
    }
}
