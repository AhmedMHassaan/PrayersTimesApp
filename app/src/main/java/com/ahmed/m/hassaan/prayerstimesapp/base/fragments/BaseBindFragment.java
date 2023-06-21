package com.ahmed.m.hassaan.prayerstimesapp.base.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;


public abstract class BaseBindFragment<BINDING extends ViewDataBinding> extends BaseFragment {


    BINDING binding;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false);

        return binding.getRoot();
    }


    public abstract void onViewCreated(@NonNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState);




    public abstract @LayoutRes
    int getLayoutId();

    public BINDING getBinding() {
        return binding;
    }


    @Override
    public void onDestroyView() {

        if (binding != null) {
//            Log.d(App.APP_TAG, "onDestroyView.BaseBindFragment:  view distroyed and bind  = null "+binding.getClass().getSimpleName());
            binding.unbind();
//            binding = null;
        }
        super.onDestroyView();
    }


}
