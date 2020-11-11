 package com.example.lmrs.view;

 import android.content.DialogInterface;
 import android.os.Bundle;
 import android.view.LayoutInflater;
 import android.view.Menu;
 import android.view.View;
 import android.widget.ImageView;
 import android.widget.LinearLayout;
 import android.widget.TextView;

 import androidx.annotation.NonNull;
 import androidx.appcompat.app.AppCompatActivity;

 import com.example.lmrs.R;
 import com.example.lmrs.model.managetables.ManageTablesModel;
 import com.example.lmrs.model.managetables.Table;
 import com.google.android.material.dialog.MaterialAlertDialogBuilder;
 import com.google.android.material.textfield.TextInputLayout;

 import java.util.List;

 public class ManageTablesActivity extends AppCompatActivity {

    ManageTablesModel manageTablesModel;
    LinearLayout llRootManageTables;

    private static final String TAG = "ManageTablesActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_tables);

        manageTablesModel = new ManageTablesModel();
        llRootManageTables = findViewById(R.id.ll_root_manage_tables);

        fetchTablesInfo();


    }

    void fetchTablesInfo() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                super.run();
                List<Table> tables = manageTablesModel.getTables();

                if (tables != null) {
                    updateUI(tables);
                }
            }
        };

        thread.start();
    }

     @Override
     public boolean onCreateOptionsMenu(@NonNull Menu menu) {
         getMenuInflater().inflate(R.menu.manage_table_options, menu);
         return true;
     }

     @Override
     public boolean onOptionsItemSelected(@NonNull android.view.MenuItem item) {
         int id = item.getItemId();
         if (id == R.id.add_table) {
             showAddTableDialog();
         }
         return super.onOptionsItemSelected(item);

     }

     private void showAddTableDialog() {
         LayoutInflater layoutInflater = getLayoutInflater();
         View layout = layoutInflater.inflate(R.layout.add_table_dialog, findViewById(R.id.ll_add_table_root), false);
         TextInputLayout addTable = layout.findViewById(R.id.et_table_id);

         MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(ManageTablesActivity.this)
                 .setView(layout)
                 .setTitle("Add table")
                 .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                     @Override
                     public void onClick(DialogInterface dialog, int which) {
                         try {
                             Integer tableId = Integer.parseInt(addTable.getEditText().getText().toString());
                             addTable(tableId);
                         } catch (Exception e) {
                             SnackbarUtil.showErrorSnackbar(llRootManageTables, "Please enter valid value");
                         }

                     }
                 })
                 .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                     @Override
                     public void onClick(DialogInterface dialog, int which) {
                         dialog.dismiss();
                     }
                 });

         builder.show();

     }

    void addTable(Integer tableId) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                String[] err = {""};
                boolean value = manageTablesModel.addTable(tableId, err);
                if (!value) {
                    SnackbarUtil.showErrorSnackbar(llRootManageTables, err[0]);
                } else {
                    SnackbarUtil.showSuccessSnackbar(llRootManageTables, "Table added successfully");
                }

                fetchTablesInfo();
            }
        };

        thread.start();
    }



    void updateUI(List<Table> tables) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                llRootManageTables.removeAllViews();

                for (Table t: tables) {
                    View tablesLayoutView = getLayoutInflater().inflate(R.layout.table_info_layout, null);
                    TextView tvTableId = tablesLayoutView.findViewById(R.id.tv_manage_qr_table_id);
                    ImageView ivQrCode = tablesLayoutView.findViewById(R.id.iv_qr_code);

                    tvTableId.setText("Table " + t.getTableId());
                    ivQrCode.setImageBitmap(t.getQrCode());

                    llRootManageTables.addView(tablesLayoutView);

                }

            }
        });
    }


}