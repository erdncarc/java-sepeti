package com.example.javasepeti.util;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class BaseAdapter<T> extends RecyclerView.Adapter<BaseAdapter.GenericViewHolder<T>> {

    private final int layoutId;
    private final BiConsumer<View, T> bindFunction;

    private final List<T> items = new ArrayList<>();
    private OnItemClickListener<T> clickListener;

    public BaseAdapter(@LayoutRes int layoutId, BiConsumer<View, T> bindFunction) {
        this.layoutId = layoutId;
        this.bindFunction = bindFunction;
    }

    public interface OnItemClickListener<T> {
        void onItemClick(T item);
    }

    public void setItems(List<T> data) {
        items.clear();
        items.addAll(data);
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener<T> listener) {
        this.clickListener = listener;
    }

    @NonNull
    @Override
    public GenericViewHolder<T> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        return new GenericViewHolder<>(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GenericViewHolder<T> holder, int position) {
        T item = items.get(position);
        bindFunction.accept(holder.itemView, item);

        holder.itemView.setOnClickListener(v -> {
            if (clickListener != null) {
                clickListener.onItemClick(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class GenericViewHolder<T> extends RecyclerView.ViewHolder {
        public GenericViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
