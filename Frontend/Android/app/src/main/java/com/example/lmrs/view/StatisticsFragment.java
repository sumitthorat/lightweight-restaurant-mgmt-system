package com.example.lmrs.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.lmrs.R;
import com.example.lmrs.model.statistics.StatisticsModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class StatisticsFragment extends Fragment {

    StatisticsModel statisticsModel;
    TextInputLayout etTodaysSale, etMostSoldItem, etItemSaleAmt, etItemSaleName, etAvgOrderTime;
    MaterialButton btnGetItemSale;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_statistics, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        statisticsModel = new StatisticsModel();

        etMostSoldItem = view.findViewById(R.id.et_most_sold);
        etTodaysSale = view.findViewById(R.id.et_todays_sale);
        etItemSaleAmt = view.findViewById(R.id.et_item_sale_amt);
        btnGetItemSale = view.findViewById(R.id.btn_get_item_sale);
        etItemSaleName = view.findViewById(R.id.et_item_sale_name);
        etAvgOrderTime = view.findViewById(R.id.et_avg_order_time);


        btnGetItemSale.setOnClickListener(getItemSaleBtnListener);

        fetchValues();

        Objects.requireNonNull(getActivity()).setTitle("Statistics");
    }

    View.OnClickListener getItemSaleBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
              getItemSale();
        }
    };

    void getItemSale() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                String itemName = etItemSaleName.getEditText().getText().toString();
                int days = 5;
                String[] err = {""};
                int amount = statisticsModel.getItemSale(itemName, days, err);

                if (amount < 0) {
                    SnackbarUtil.showErrorSnackbar(getView(), err[0]);
                } else {
                    updateItemSaleUI(amount);
                }
            }
        };

        thread.start();
    }

    private void updateItemSaleUI(int amount) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    etItemSaleAmt.getEditText().setText(String.valueOf(amount));
                }
            });
        }
    }

    void fetchValues() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                int todaysSale = statisticsModel.getTodaysSale();
                String[] err = {""};
                String mostSoldItem = statisticsModel.getMostSoldItem(err);
                if (mostSoldItem == null) {
                    SnackbarUtil.showErrorSnackbar(getView(), err[0]);
                }

                double avgOrderTime = statisticsModel.getAvgOrderTime();

                updateUI(todaysSale, mostSoldItem, avgOrderTime);
            }
        };

        thread.start();
    }

    void updateUI(int todaysSale, String mostSoldItem, double avgOrderTime) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    etTodaysSale.getEditText().setText(String.valueOf(todaysSale));
                    etMostSoldItem.getEditText().setText((mostSoldItem == null ? "" : mostSoldItem));
                    etAvgOrderTime.getEditText().setText(String.valueOf(avgOrderTime));
                }
            });
        }
    }
}