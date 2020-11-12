package com.example.lmrs.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.lmrs.R;
import com.example.lmrs.model.editmenu.EditMenuModel;
import com.example.lmrs.model.editmenu.MenuItem;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;

import java.math.BigDecimal;
import java.text.Format;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class EditMenuFragment extends Fragment {
    final private static String TAG = "EditMenuModel";
    LinearLayout llMenuRoot;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_edit_menu, container, false);
    }

    MovableFloatingActionButton addItemsExFab;
    List<String> CATEGORIES;
    EditMenuModel editMenuModel;
    Map<String, List<MenuItem>> menu;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Objects.requireNonNull(getActivity()).setTitle("Edit Menu");

        editMenuModel = new EditMenuModel();
        llMenuRoot = view.findViewById(R.id.ll_menu_root);

        // Bind objects to views
        addItemsExFab = view.findViewById(R.id.extended_fab_edit_menu);

        // Get categories from server
        fetchCategories();

        // Fetch menu
        fetchMenu();


        addItemsExFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddItemsDialog(v);
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.edit_menu_options, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull android.view.MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.option_manage_tables) {
            Intent intent = new Intent(getActivity(), ManageTablesActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
        
    }

    private void fetchCategories() {
        String[] err = {""};
        Thread thread = new Thread() {
            @Override
            public void run() {
                CATEGORIES = editMenuModel.getAllCategories(err);
                if (CATEGORIES == null) {
                    SnackbarUtil.showErrorSnackbar(getView(), "Could not fetch categories");
                }
            }
        };
        thread.start();
    }

    private void fetchMenu() {
        String[] err = {""};
        Thread thread = new Thread() {
            @Override
            public void run() {
                menu = editMenuModel.getFullMenu();
                updateMenuUI();
            }
        };
        thread.start();
    }

    public static String getIndianRupee(String value) {
        Format format = NumberFormat.getCurrencyInstance(new Locale("en", "in"));
        return format.format(new BigDecimal(value));
    }

    private void updateMenuUI() {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    llMenuRoot.removeAllViews();

                    for (Map.Entry<String, List<MenuItem>> e: menu.entrySet()) {
                        View categoryLayout = getLayoutInflater().inflate(R.layout.category_layout, null);
                        TextView tvCategoryName = categoryLayout.findViewById(R.id.tv_category_name);
                        LinearLayout llMenuItems = categoryLayout.findViewById(R.id.ll_category_items);
                        tvCategoryName.setText(e.getKey());


                        for (MenuItem item: e.getValue()) {
                            View menuItemLayout = getLayoutInflater().inflate(R.layout.menu_item_layout, null);
                            TextView itemName = menuItemLayout.findViewById(R.id.tv_item_name);
                            TextView itemPrice = menuItemLayout.findViewById(R.id.tv_item_price);
                            itemPrice.setText(getIndianRupee(String.valueOf(item.getPrice())));
                            itemName.setText(item.getItemName());
                            llMenuItems.addView(menuItemLayout);
                        }

                        llMenuRoot.addView(categoryLayout);
                    }
                }
            });
        }


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

        TextInputLayout etItemName = layout.findViewById(R.id.et_item_name);
        TextInputLayout etPrice = layout.findViewById(R.id.et_item_price);

        categoriesDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SnackbarUtil.showSuccessSnackbar(getView(), "Selected " + position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        new MaterialAlertDialogBuilder(Objects.requireNonNull(getActivity()))
                .setView(layout)
                .setTitle("Add item")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String category = categoriesDropdown.getText().toString();
                        String itemName = etItemName.getEditText().getText().toString();
                        int price;
                        try {
                            price = Integer.parseInt(etPrice.getEditText().getText().toString());

                            // Send server request
                            Thread thread = new Thread() {
                                @Override
                                public void run() {
                                    super.run();
                                    String[] err = {""};
                                    boolean value = editMenuModel.addNewItem(itemName, category, price, err);
                                    if (!value) {
                                        SnackbarUtil.showErrorSnackbar(getView(), err[0]);
                                    } else {
                                        SnackbarUtil.showSuccessSnackbar(getView(), "Item added successfully");
                                    }
                                    fetchCategories();
                                    fetchMenu();
                                }
                            };

                            thread.start();



                        } catch (NumberFormatException nfe) {
                            SnackbarUtil.showErrorSnackbar(getView(), "Please enter valid price");
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Clear the fields (if required)
                    }
                })
                .show();
    }
}