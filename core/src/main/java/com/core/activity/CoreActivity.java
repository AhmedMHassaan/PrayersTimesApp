package com.core.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;


import com.core.callbacks.OnPermissionsResult;
import com.core.callbacks.OnResultListener;

import java.util.Map;


public abstract class CoreActivity extends AppCompatActivity implements OnResultListener, OnPermissionsResult {

    private int requestCode;
    private ActivityResultLauncher<Intent> resultLauncher;


    private int permissionRequestCode; // for permissions
    private ActivityResultLauncher<String[]> permissionLauncher;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        TransformationConfig.activityReceiverConfig(this);
//
//
//        if (UserInfoPreference.getInstance().isBlocked()) {
//            getTools().startNewActivity(AppBlockActivity.class);
//            return;
//        }

        resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                onResult(result, requestCode);
            }
        });

        // launcher For PermissionsResult in Fragment
        permissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), new ActivityResultCallback<Map<String, Boolean>>() {
            @Override
            public void onActivityResult(Map<String, Boolean> result) {
                onPermissionResult(result, permissionRequestCode);
            }
        });

    }


    public void requestRuntimePermission(String[] permissions, int requestCode) {
        this.permissionRequestCode = requestCode;
        permissionLauncher.launch(permissions);
    }


    public Context getContext() {
        return this;
    }

    public Activity getActivity() {
        return this;
    }


    @Override
    public void onResult(ActivityResult result, int requestCode) {
        Log.d("APP_TAG", "onResult: You must implement onResultMethod");
    }

    @Override
    public void onPermissionResult(Map<String, Boolean> result, int requestCode) {
        Log.d("APP_TAG", "onPermissionResult: You must implement onResultMethod");
    }

    public void startActivityResult(Intent intent, int requestCode) {
        this.requestCode = requestCode;
        resultLauncher.launch(intent);
    }

    public boolean isPermissionGranted(String permission) {
        return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
    }

    public boolean isPermissionDenied(String permission) {
        return !isPermissionGranted(permission);
    }


}
