package com.example.javasepeti.customer;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.javasepeti.R;

import java.util.ArrayList;
import java.util.List;

public class SelectDialogFragment<T> extends DialogFragment {

    public interface ItemViewBinder<T> {
        void bind(View itemView, T item);
    }

    public interface OnItemSelectedListener<T> {
        void onItemSelected(T item);
    }

    private List<T> items = new ArrayList<>();
    private int itemLayoutRes;
    private ItemViewBinder<T> binder;
    private OnItemSelectedListener<T> listener;

    public static <T> SelectDialogFragment<T> newInstance(
            List<T> items,
            int itemLayoutRes,
            ItemViewBinder<T> binder,
            OnItemSelectedListener<T> listener) {
        SelectDialogFragment<T> fragment = new SelectDialogFragment<>();
        fragment.items = (items != null ? items : new ArrayList<>());
        fragment.itemLayoutRes = itemLayoutRes;
        fragment.binder = binder;
        fragment.listener = listener;
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (items == null) {
            items = new ArrayList<>();
        }

        AlertDialog dialog = new AlertDialog.Builder(requireContext()).create();
        View view = getLayoutInflater().inflate(R.layout.select, null);
        LinearLayout listContainer = view.findViewById(R.id.list);
        LayoutInflater inflater = getLayoutInflater();

        for (T item : items) {
            View itemView = inflater.inflate(itemLayoutRes, listContainer, false);
            if (binder != null) {
                binder.bind(itemView, item);
            }
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemSelected(item);
                }
                dialog.dismiss();
            });
            listContainer.addView(itemView);
        }

        dialog.setView(view);
        dialog.setCanceledOnTouchOutside(true);
        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            Window window = dialog.getWindow();
            if (window != null) {
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                        WindowManager.LayoutParams.WRAP_CONTENT);
                WindowManager.LayoutParams params = window.getAttributes();
                params.gravity = Gravity.BOTTOM;
                params.dimAmount = 0.5f;
                window.setAttributes(params);
                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            }
        }
    }
}