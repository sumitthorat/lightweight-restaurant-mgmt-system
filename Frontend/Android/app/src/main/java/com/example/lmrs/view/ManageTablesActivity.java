 package com.example.lmrs.view;

 import android.os.Bundle;
 import android.view.View;
 import android.widget.ImageView;
 import android.widget.LinearLayout;
 import android.widget.TextView;

 import androidx.appcompat.app.AppCompatActivity;

 import com.example.lmrs.R;
 import com.example.lmrs.model.managetables.ManageTablesModel;
 import com.example.lmrs.model.managetables.Table;

 import java.util.List;

 public class ManageTablesActivity extends AppCompatActivity {

    ManageTablesModel manageTablesModel;
    LinearLayout llRootManageTables;

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

    void updateUI(List<Table> tables) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (Table t: tables) {
                    View tablesLayoutView = getLayoutInflater().inflate(R.layout.table_info_layout, null);
                    TextView tvTableId = tablesLayoutView.findViewById(R.id.tv_manage_qr_table_id);
                    ImageView ivQrCode = tablesLayoutView.findViewById(R.id.iv_qr_code);

                    tvTableId.setText("Table Number: " + t.getTableId());
                    ivQrCode.setImageBitmap(t.getQrCode());

                    llRootManageTables.addView(tablesLayoutView);

                }

            }
        });
    }


}