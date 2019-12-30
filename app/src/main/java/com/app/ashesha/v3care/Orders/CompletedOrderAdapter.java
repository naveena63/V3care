package com.app.ashesha.v3care.Orders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.ashesha.v3care.R;

import java.util.List;

public class CompletedOrderAdapter extends RecyclerView.Adapter<CompletedOrderAdapter.VH> {
    Context context;
    List<CompleteOrderModel> completeOrderModelList;

    public CompletedOrderAdapter(Context context, List<CompleteOrderModel> completeOrderModelList)
    {
        this.context = context;
        this.completeOrderModelList = completeOrderModelList;
    }

    @Override
    public CompletedOrderAdapter.VH onCreateViewHolder( ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.complete_orderlayout,
                viewGroup,
                false);

        return new VH(view);
    }

    @Override
    public void onBindViewHolder(final CompletedOrderAdapter.VH vh, int i) {
        vh.textview_completedDate.setText("Service Date :   "+completeOrderModelList.get(i).getServiceDate());
        vh.textView_orderId.setText(("Order Id :   "+completeOrderModelList.get(i).getOrderId()));
        vh.textView_orderAmount.setText(("Order Amount :   "+completeOrderModelList.get(i).getPrice()));
        vh.paymentype.setText("Payment Type :   "+completeOrderModelList.get(i).getPaymentType());
    }

    @Override
    public int getItemCount()
    {
        return 1;
    }

    public class VH extends RecyclerView.ViewHolder {
        TextView textview_completedDate,textView_orderId,textView_orderAmount,paymentype;
        public VH(View itemView) {
            super(itemView);
            textview_completedDate = itemView.findViewById(R.id.textview_pendingDate);
            textView_orderId = itemView.findViewById(R.id.textView_orderId);
            textView_orderAmount = itemView.findViewById(R.id.textView_orderAmount);
            paymentype = itemView.findViewById(R.id.payment_type);
        }
    }
}
