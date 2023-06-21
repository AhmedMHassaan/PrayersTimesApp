package com.ahmed.m.hassaan.prayerstimesapp.base.adapter;

import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import com.ahmed.m.hassaan.prayerstimesapp.R;
import com.ahmed.m.hassaan.prayerstimesapp.base.viewholder.BaseViewHolder;
import com.ahmed.m.hassaan.prayerstimesapp.utils.ToastMessages;

import java.util.ArrayList;

public abstract class BaseRecyclerAdapter<MODEL, BINDING extends ViewDataBinding, VIEW_HOLDER extends BaseViewHolder<MODEL, BINDING>> extends RecyclerView.Adapter<VIEW_HOLDER> {

    final ArrayList<MODEL> arrayList = new ArrayList<>();
//    VIEW_HOLDER viewHolder;


    @NonNull
    @Override
    public abstract VIEW_HOLDER onCreateViewHolder(@NonNull ViewGroup parent, int viewType);


    @Override
    public void onBindViewHolder(@NonNull VIEW_HOLDER holder, int position) {
        holder.onBind(getItem(position));
    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public MODEL getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void addItem(MODEL model) {
        try {

            arrayList.add(model);

//            notifyItemInserted(getItemCount() - 2);
            notifyItemInserted(getItemCount() - 1);
/*
            notifyItemInserted(getItemCount());
            notifyItemInserted(getItemCount() + 1);
            notifyItemInserted(getItemCount() + 2);
*/

        } catch (Exception e) {
            ToastMessages.INSTANCE.error("غير قادر علي الإضافة");
        }
//        notifyDataSetChanged();
    }


    public void deleteItem(int position) {
        try {
            arrayList.remove(position);
            notifyItemRemoved(position);
        } catch (Exception e) {

            ToastMessages.INSTANCE.error("غير قادر علي حذف العنصر");
        }
    }

    public void updateItem(int position, MODEL model) {
        try {
            arrayList.set(position, model);
            notifyItemChanged(position, model);

        } catch (Exception e) {
            ToastMessages.INSTANCE.error("غير قادر علي تعديل العنصر");
        }
    }

    public ArrayList<MODEL> getList() {
        return arrayList;
    }

    @Override
    public void onViewAttachedToWindow(@NonNull VIEW_HOLDER holder) {
        holder.itemView.setAnimation(AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.item_animation_transaction));
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull VIEW_HOLDER holder) {
        holder.itemView.clearAnimation();
//        Glide.get(holder.itemView.getContext()).clearDiskCache();
//        Glide.get(holder.itemView.getContext()).clearMemory();

    }


    public boolean isEmpty() {
        return arrayList.size() == 0;
    }


    public void updateList(ArrayList<MODEL> list) {
        if (list == null)
            list = new ArrayList<>();

        this.arrayList.clear();
        this.arrayList.addAll(list);
        notifyItemRangeChanged(0,arrayList.size());
    }

    public void updateListWithNoNullItems(ArrayList<MODEL> list) {
        this.arrayList.clear();
        for (MODEL model : list) {
            if (model != null)
                this.arrayList.add(model);
        }
        notifyDataSetChanged();
    }

    public void updateListWithPaginationNoNullItems(ArrayList<MODEL> paginationList) {
        for (MODEL model : paginationList) {
            if (model != null)
                addItem(model);
        }
    }

    public void updateListWithPagination(ArrayList<MODEL> paginationList) {
        for (MODEL model : paginationList) {
            addItem(model);
        }
    }


    public void clear() {
        this.arrayList.clear();
        notifyDataSetChanged();
    }
}
