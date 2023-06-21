package com.ahmed.m.hassaan.prayerstimesapp.base.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public abstract class BaseFullScreenBottomSheet extends BottomSheetDialogFragment {


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {

        BottomSheetDialog dialog = new BottomSheetDialog(getContext(), getTheme());

        dialog.setOnShowListener(dialog1 -> {

            BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) dialog1;
            View parentLayout = bottomSheetDialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);
            BottomSheetBehavior<View> behavior = BottomSheetBehavior.from(parentLayout);
            setUpFullHeight(parentLayout);
            behavior.setState(BottomSheetBehavior.STATE_EXPANDED);

        });

        return dialog;
    }

    private void setUpFullHeight(View bottomSheet) {
        ViewGroup.LayoutParams layoutParams = bottomSheet.getLayoutParams();
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        bottomSheet.setLayoutParams(layoutParams);
    }


}
