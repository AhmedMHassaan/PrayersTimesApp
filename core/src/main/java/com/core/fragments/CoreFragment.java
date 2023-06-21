package com.core.fragments;

import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.core.callbacks.OnPermissionsResult;
import com.core.callbacks.OnResultListener;

import java.util.Map;

public abstract class CoreFragment extends Fragment implements OnResultListener, OnPermissionsResult {

    private ActivityResultLauncher<Intent> resultLauncher;
    private ActivityResultLauncher<String[]> permissionLauncher;
    private int requestCode; // for startActivityForResult only
    private int permissionRequestCode; // for permissions

    public void onBackPressed() {
        if (super.getActivity() != null)
            super.getActivity().onBackPressed();
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);


        // result Launcher For ActivityResult
        resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> onResult(result, requestCode));


        // launcher For PermissionsResult in Fragment
        permissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> onPermissionResult(result, permissionRequestCode));


    }

    @Override
    public void onResume() {
        super.onResume();
        onBack();
    }

    private final void onBack() {

        if (getView() != null) {
            getView().setFocusableInTouchMode(true);
            getView().requestFocus();
            getView().setOnKeyListener((v, keyCode, event) -> {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    onBackPressed();
                    return true;
                }
                return false;
            });
        }
    }



    public void startActivityForResultFromFragment(Intent intent) {
        if (intent == null)
            throw new IllegalArgumentException("Intent must be not null in " + this.getClass().getName());

        resultLauncher.launch(intent);
    }

    public void startActivityForResultFromFragment(Intent intent, int requestCode) {
        if (intent == null)
            throw new IllegalArgumentException("Intent must be not null in " + this.getClass().getName());
        this.requestCode = requestCode;

        resultLauncher.launch(intent);
    }

    public void requestPermission(String[] permissions, int requestCode) {
        this.permissionRequestCode = requestCode;
        permissionLauncher.launch(permissions);
    }


    @Override
    public void onResult(ActivityResult result, int requestCode) {

    }

    @Override
    public void onPermissionResult(Map<String, Boolean> result, int requestCode) {

    }


    public abstract void onDestroyFragment();
}
