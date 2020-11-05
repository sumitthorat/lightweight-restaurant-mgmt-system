package com.example.lmrs.model.vieworders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lmrs.R;

import java.util.List;

public class OrdersRecyclerAdapter extends RecyclerView.Adapter<OrdersRecyclerAdapter.OrdersRecyclerViewHolder> {
    private List<Order> orderList;

    public static class OrdersRecyclerViewHolder extends RecyclerView.ViewHolder {

        public TextView tvOrderTableId;
        public LinearLayout llItems;

        public OrdersRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);

            tvOrderTableId = itemView.findViewById(R.id.tv_table_id);
            llItems = itemView.findViewById(R.id.ll_items);
        }
    }

    public OrdersRecyclerAdapter(List<Order> orderList) {
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public OrdersRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_card_view, parent, false);
        OrdersRecyclerViewHolder ovh = new OrdersRecyclerViewHolder(v);
        return ovh;
    }

    @Override
    public void onBindViewHolder(@NonNull OrdersRecyclerViewHolder holder, int position) {
        Order currentOrder = orderList.get(position);
        holder.tvOrderTableId.setText(currentOrder.getTableId());

        holder.llItems.removeAllViews();

        for (OrderItem o: currentOrder.getOrderItemList()) {
            View v = LayoutInflater.from(holder.tvOrderTableId.getContext()).inflate(R.layout.order_item_layout, null);
            TextView tvItemName = v.findViewById(R.id.tv_order_item_name);
            TextView tvItemQty = v.findViewById(R.id.tv_order_item_qty);

            tvItemName.setText(o.getItemName());
            tvItemQty.setText(String.valueOf(o.getQty()));
            holder.llItems.addView(v);
        }

    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }
}
