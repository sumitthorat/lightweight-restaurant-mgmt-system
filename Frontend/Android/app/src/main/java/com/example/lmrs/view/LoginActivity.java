package com.example.lmrs.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.lmrs.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    static final String TAG = "LoginActivity";

    MaterialButton btnLogin;
    EditText etUsername, etPassword;
    TextView tvCreateAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Code to hide the app bar
        Objects.requireNonNull(getSupportActionBar()).hide();

        btnLogin = findViewById(R.id.btn_login);
        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        tvCreateAccount = findViewById(R.id.tv_create_account);

        btnLogin.setOnClickListener(v -> {
            Intent goToMain = new Intent(this, MainActivity.class);
            startActivity(goToMain);
            finish();
        });



        tvCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater layoutInflater = getLayoutInflater();
                View layout = layoutInflater.inflate(R.layout.register_dialog_layout, findViewById(R.id.ll_root), false);
                EditText etRegUsername = layout.findViewById(R.id.et_reg_username);
                EditText etRegPassword = layout.findViewById(R.id.et_reg_password);
                EditText etRegRole = layout.findViewById(R.id.et_reg_role);

                new MaterialAlertDialogBuilder(LoginActivity.this)
                        .setView(layout)
                        .setTitle("Register")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Parse Info
                                Log.i(TAG, etRegUsername.getText()+ ":" + etRegPassword.getText() + ":" + etRegRole.getText());
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
        });

    }


}