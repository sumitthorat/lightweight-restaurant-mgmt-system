package com.example.lmrs.view;

import android.content.Context;
import android.view.View;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

/**
 * Utility to call snackbar functions
 */
public class SnackbarUtil {

    public static void showSuccessSnackbar(View view, String text) {
        Snackbar.make(view, text, BaseTransientBottomBar.LENGTH_SHORT)
                .show();
    }

    public static void showErrorSnackbar(View view, String text) {
        Snackbar.make(view, text, BaseTransientBottomBar.LENGTH_SHORT)
                .show();
    }
}
