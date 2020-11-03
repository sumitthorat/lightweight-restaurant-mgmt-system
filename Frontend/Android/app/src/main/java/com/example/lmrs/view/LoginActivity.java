package com.example.lmrs.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.lmrs.R;
import com.example.lmrs.model.login.LoginModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    static final String TAG = "LoginActivity";

    MaterialButton btnLogin;
    TextInputLayout textInputUsername, textInputPassword;
    TextView tvCreateAccount;
    LoginModel loginModel;
    ConstraintLayout clRoot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        // Code to hide the app bar
        Objects.requireNonNull(getSupportActionBar()).hide();

        loginModel = new LoginModel();

        btnLogin = findViewById(R.id.btn_login);
        textInputUsername = findViewById(R.id.et_username);
        textInputPassword = findViewById(R.id.et_password);
        tvCreateAccount = findViewById(R.id.tv_create_account);
        clRoot = findViewById(R.id.cl_root);


        btnLogin.setOnClickListener(v -> {
            Intent goToMain = new Intent(this, MainActivity.class);
            startActivity(goToMain);
            finish();
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = Objects.requireNonNull(textInputUsername.getEditText()).getText().toString();
                String password = Objects.requireNonNull(textInputPassword.getEditText()).getText().toString();


                if (!validateLoginInfo(username, password)) {
                    SnackbarUtil.showErrorSnackbar(clRoot, "Please enter valid username and password");
                    return;
                }

                Thread loginThread = new Thread(() -> {
                    String[] err = {""};
                    boolean value = loginModel.attemptLogin(username, password, err);
                    if (value) {
                       startMainactivity();
                    } else {
                        SnackbarUtil.showErrorSnackbar(clRoot, err[0]);
                    }
                });

                loginThread.start();

            }
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

    void startMainactivity() {
        Intent goToMain = new Intent(this, MainActivity.class);
        startActivity(goToMain);
        finish();
    }

    boolean validateLoginInfo(String username, String password) {
        return username.length() > 0 && password.length() > 0;
    }

}