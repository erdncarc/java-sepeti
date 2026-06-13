package com.example.javasepeti.customer;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.core.dto.FilterRestaurantRequest;
import com.example.core.enums.RestaurantSortOption;
import com.example.core.enums.SortDirection;
import com.example.javasepeti.R;

public class FilterRestaurantDialogFragment extends DialogFragment {

    private CheckBox upCheckBox, downCheckBox, rateCheckBox, distanceCheckBox, openCheckBox, closeCheckBox;
    private TextView applyButton, clearButton;

    public interface OnFilterApplyListener {
        void onFilterApply(FilterRestaurantRequest req);
    }

    private OnFilterApplyListener listener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnFilterApplyListener) {
            listener = (OnFilterApplyListener) context;
        } else {
            throw new RuntimeException(context + " must implement OnFilterApplyListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        // Köşe yuvarlatma için custom stil gerekiyorsa buraya eklenebilir
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.filter_restaurant, container, false);

        // Checkbox'lar
        upCheckBox = view.findViewById(R.id.up);
        downCheckBox = view.findViewById(R.id.down);
        rateCheckBox = view.findViewById(R.id.rate);
        distanceCheckBox = view.findViewById(R.id.distance);
        openCheckBox = view.findViewById(R.id.open);
        closeCheckBox = view.findViewById(R.id.close);

        // Butonlar
        applyButton = view.findViewById(R.id.apply);
        clearButton = view.findViewById(R.id.clear);

        // Tek seçim mantığı - sort by
        upCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                downCheckBox.setChecked(false);
            }
        });

        downCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                upCheckBox.setChecked(false);
            }
        });

        // Tek seçim mantığı - open/close
        openCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                closeCheckBox.setChecked(false);
            }
        });

        closeCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                openCheckBox.setChecked(false);
            }
        });

        rateCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                distanceCheckBox.setChecked(false);
            }
        });

        distanceCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                rateCheckBox.setChecked(false);
            }
        });



        applyButton.setOnClickListener(v -> {
            if (listener != null) {
                FilterRestaurantRequest req = new FilterRestaurantRequest();
                req.setOpen(openCheckBox.isChecked());
                req.setClosed(closeCheckBox.isChecked());
                req.setSortDirection(upCheckBox.isChecked() ? SortDirection.DESC : SortDirection.ASC);
                req.setSortBy(rateCheckBox.isChecked() ? RestaurantSortOption.RATE : RestaurantSortOption.DISTANCE);
                listener.onFilterApply(req);
            }
            dismiss();
        });

        clearButton.setOnClickListener(v -> {
            upCheckBox.setChecked(false);
            downCheckBox.setChecked(false);
            rateCheckBox.setChecked(false);
            distanceCheckBox.setChecked(false);
            openCheckBox.setChecked(false);
            closeCheckBox.setChecked(false);
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null && getDialog().getWindow() != null) {
            Window window = getDialog().getWindow();
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            window.setBackgroundDrawableResource(android.R.color.transparent); // Köşeler için şeffaf arka plan
            window.setGravity(Gravity.BOTTOM);
        }
    }


}
