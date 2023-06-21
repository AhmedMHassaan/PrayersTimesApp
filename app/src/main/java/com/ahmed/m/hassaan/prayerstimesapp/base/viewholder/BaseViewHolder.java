package com.ahmed.m.hassaan.prayerstimesapp.base.viewholder;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;



public abstract class BaseViewHolder<MODEL, BINDING extends ViewDataBinding> extends RecyclerView.ViewHolder {

    BINDING binding;

    public BINDING getBinding() {
        return binding;
    }


    public BaseViewHolder(@NonNull View itemView) {
        super(itemView);
        binding = DataBindingUtil.bind(itemView);
    }

    public abstract void  onBind(MODEL model);



}
