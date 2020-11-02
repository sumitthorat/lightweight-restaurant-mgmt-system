package com.example.lmrs.view;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.example.lmrs.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.Objects;

public class EditMenuFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_menu, container, false);
    }

    ExtendedFloatingActionButton addItemsExFab;
    String[] CATEGORIES;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Objects.requireNonNull(getActivity()).setTitle("Edit Menu");

        // Bind objects to views
        addItemsExFab = view.findViewById(R.id.extended_fab_edit_menu);

        // Get categories from server
        CATEGORIES = new String[] {"Item 1", "Item 2", "Item 3", "Item 4"};


        addItemsExFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddItemsDialog(v);
            }
        });
    }

    private void openAddItemsDialog(View v) {
        LayoutInflater layoutInflater = getLayoutInflater();
        View layout = layoutInflater.inflate(R.layout.add_items_layout, v.findViewById(R.id.ll_add_items), false);

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        getContext(),
                        R.layout.add_items_exposed_dropdown_item,
                        CATEGORIES);

        AutoCompleteTextView categoriesDropdown =
                layout.findViewById(R.id.categories_dropdown);
        categoriesDropdown.setAdapter(adapter);

        new MaterialAlertDialogBuilder(Objects.requireNonNull(getActivity()))
                .setView(layout)
                .setTitle("Add item")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Parse Info
                        // Send server request
                        // Start processing dialog
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Clear the fields (if required)
                        dialog.dismiss();
                    }
                })
                .show();
    }
}