package com.example.lmrs.view;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lmrs.R;
import com.example.lmrs.model.vieworders.Order;
import com.example.lmrs.model.vieworders.OrdersRecyclerAdapter;
import com.example.lmrs.model.vieworders.ViewOrdersModel;
import com.example.lmrs.model.vieworders.ViewOrdersProtocol;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * View Orders Fragment Class
 */
public class ViewOrdersFragment extends Fragment implements ViewOrdersProtocol {

    private RecyclerView ordersRecyclerView;
    private RecyclerView.Adapter ordersRecyclerAdapter;
    private RecyclerView.LayoutManager orderRecyclerLayoutMgr;
    List<Order> orders;
    ViewOrdersModel viewOrdersModel;
    ConstraintLayout clViewOrdersRoot;

    private static final String TAG = "ViewOrdersFragment";
;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_orders, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Objects.requireNonNull(getActivity()).setTitle("View Orders");

        viewOrdersModel = new ViewOrdersModel();
        viewOrdersModel.setDelegate(this);

        clViewOrdersRoot = view.findViewById(R.id.cl_view_orders_root);


        orders = new ArrayList<>();


        ordersRecyclerView = view.findViewById(R.id.rv_view_orders);

        ordersRecyclerView.setHasFixedSize(true);
        orderRecyclerLayoutMgr = new LinearLayoutManager(getContext());
        ordersRecyclerAdapter = new OrdersRecyclerAdapter(orders);

        ordersRecyclerView.setLayoutManager(orderRecyclerLayoutMgr);
        ordersRecyclerView.setAdapter(ordersRecyclerAdapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemTouchCallback);
        itemTouchHelper.attachToRecyclerView(ordersRecyclerView);

        getOrders();


    }

    private void getOrders() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                super.run();
                String[] err = {""};
                List<Order> orderList = viewOrdersModel.getPendingOrders(err);

                if (orderList == null) {
                    SnackbarUtil.showErrorSnackbar(getView(), "Could not fetch orders");
                    return;
                }

                orders.addAll(orderList);

                updateRecyclerViewOnUIThread();


            }
        };

        thread.start();
    }

    ItemTouchHelper.SimpleCallback itemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();

            new MaterialAlertDialogBuilder(getContext())
                    .setTitle("Order Complete")
                    .setMessage("Are you sure you want to mark this order complete?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (orders.size() >= position) {
                                int orderId = Integer.parseInt(orders.get(position).getOrderId());
                                Thread thread = new Thread() {
                                    @Override
                                    public void run() {
                                        super.run();
                                        String[] err = {""};
                                        boolean value = viewOrdersModel.notifyOrderComplete(orderId, err);

                                        if (!value) {
                                            SnackbarUtil.showErrorSnackbar(clViewOrdersRoot, err[0]);
                                        } else {
                                            SnackbarUtil.showSuccessSnackbar(clViewOrdersRoot, "Order processed successfully");
                                        }

                                    }
                                };

                                thread.start();

                                ordersRecyclerAdapter.notifyItemRemoved(position);
                                orders.remove(position);
                            }
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ordersRecyclerAdapter.notifyItemChanged(position);
                            dialog.dismiss();
                        }
                    })
                    .show();
        }
    };

    @Override
    public void onReceiveNewOrder(Order order) {
        orders.add(order);
        updateRecyclerViewOnUIThread();
    }

    private void updateRecyclerViewOnUIThread() {

        if (getActivity() != null) {
            Log.i(TAG, "Activity: " + getActivity().toString());

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ordersRecyclerAdapter.notifyDataSetChanged();
                }
            });
        }

    }
}
