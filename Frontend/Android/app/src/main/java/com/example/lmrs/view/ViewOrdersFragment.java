package com.example.lmrs.view;

import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.lmrs.R;
import com.google.android.material.card.MaterialCardView;

import java.util.Objects;
import java.util.Random;

public class ViewOrdersFragment extends Fragment {

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


        LinearLayout llViewOrdersRoot = view.findViewById(R.id.ll_view_orders_root);

        for (int i = 0; i < 5; ++i) {
            View rootView = getLayoutInflater().inflate(R.layout.order_card_view, null);
            TextView tvTableId = rootView.findViewById(R.id.tv_table_id);
            tvTableId.setText("R1T" + i);
            MaterialCardView materialCardView = rootView.findViewById(R.id.cv_order);
            LinearLayout llItems = rootView.findViewById(R.id.ll_items);
            int items = 5;
            for (int j = 0; j < items; ++j) {
                TextView item = new TextView(getContext());
                item.setText("2x Parantha");
                item.setTextSize(18);
                llItems.addView(item);
            }
            materialCardView.setOnClickListener(orderOnClickListener);
            llViewOrdersRoot.addView(rootView);
        }
    }

    View.OnClickListener orderOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            TextView tvTableId = v.findViewById(R.id.tv_table_id);
            Log.i("ViewOrdersFragment", "Want to delete : " + tvTableId.getText());
        }
    };
}