 package com.example.lmrs.view;

 import android.content.DialogInterface;
 import android.content.Intent;
 import android.graphics.Bitmap;
 import android.net.Uri;
 import android.os.Bundle;
 import android.view.LayoutInflater;
 import android.view.Menu;
 import android.view.View;
 import android.widget.ImageView;
 import android.widget.LinearLayout;
 import android.widget.TextView;

 import androidx.annotation.NonNull;
 import androidx.appcompat.app.AppCompatActivity;
 import androidx.core.content.FileProvider;

 import com.example.lmrs.R;
 import com.example.lmrs.model.managetables.ManageTablesModel;
 import com.example.lmrs.model.managetables.Table;
 import com.google.android.material.dialog.MaterialAlertDialogBuilder;
 import com.google.android.material.textfield.TextInputLayout;

 import java.io.File;
 import java.io.FileOutputStream;
 import java.io.IOException;
 import java.util.List;

 public class ManageTablesActivity extends AppCompatActivity {

    ManageTablesModel manageTablesModel;
    LinearLayout llRootManageTables;
    List<Table> tables;

    private static final String TAG = "ManageTablesActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_tables);

        setTitle("Manage Tables");

        manageTablesModel = new ManageTablesModel();
        llRootManageTables = findViewById(R.id.ll_root_manage_tables);



        fetchTablesInfo();


    }

    void fetchTablesInfo() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                super.run();
                tables = manageTablesModel.getTables();

                if (tables != null) {
                    updateUI();
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

     void deleteTable(Integer tableId) {
         Thread thread = new Thread() {
             @Override
             public void run() {
                 String[] err = {""};
                 boolean value = manageTablesModel.deleteTable(tableId, err);
                 if (!value) {
                     SnackbarUtil.showErrorSnackbar(llRootManageTables, err[0]);
                 } else {
                     SnackbarUtil.showSuccessSnackbar(llRootManageTables, "Table deleted successfully");
                 }

                 fetchTablesInfo();
             }
         };

         thread.start();
     }



    void updateUI() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                llRootManageTables.removeAllViews();

                for (Table t: tables) {
                    View tablesLayoutView = getLayoutInflater().inflate(R.layout.table_info_layout, null);
                    TextView tvTableId = tablesLayoutView.findViewById(R.id.tv_manage_qr_table_id);
                    ImageView ivQrCode = tablesLayoutView.findViewById(R.id.iv_qr_code);
                    ImageView ivDelete = tablesLayoutView.findViewById(R.id.iv_delete);
                    ImageView ivShare = tablesLayoutView.findViewById(R.id.iv_share);

                    ivDelete.setTooltipText("" + t.getTableId());
                    ivShare.setTooltipText("" + t.getTableId());

                    ivDelete.setOnClickListener(deleteOnClickListener);
                    ivShare.setOnClickListener(shareOnClickListener);

                    tvTableId.setText("Table " + t.getTableId());
                    ivQrCode.setImageBitmap(t.getQrCode());

                    llRootManageTables.addView(tablesLayoutView);

                }

            }
        });
    }

    View.OnClickListener shareOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ImageView iv = (ImageView) v;
            int tableId = Integer.parseInt(iv.getTooltipText().toString());

            Bitmap bitmap = null;

            for (Table t: tables) {
                if (tableId == t.getTableId()) {
                    bitmap = t.getQrCode();
                }
            }


            try {

                File cachePath = new File(getApplicationContext().getCacheDir(), "images");
                cachePath.mkdirs();
                FileOutputStream stream = new FileOutputStream(cachePath + "/image.png"); // overwrites this image every time
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                stream.close();

            } catch (IOException e) {
                e.printStackTrace();
            }

            File imagePath = new File(getApplicationContext().getCacheDir(), "images");
            File newFile = new File(imagePath, "image.png");
            Uri contentUri = FileProvider.getUriForFile(getApplicationContext(), "com.example.lmrs.fileprovider", newFile);

            if (contentUri != null) {
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); // temp permission for receiving app to read this file
                shareIntent.setDataAndType(contentUri, getContentResolver().getType(contentUri));
                shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Table " + tableId);
                startActivity(Intent.createChooser(shareIntent, "Choose an app"));
            }
        }
    };

    View.OnClickListener deleteOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ImageView iv = (ImageView) v;
            int tableId = Integer.parseInt(iv.getTooltipText().toString());
            deleteTable(tableId);
        }
    };


}