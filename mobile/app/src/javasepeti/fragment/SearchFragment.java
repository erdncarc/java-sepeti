package com.example.javasepeti.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.javasepeti.R;
import com.example.javasepeti.customer.FilterRestaurantDialogFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

import lombok.Getter;
import lombok.Setter;

public class SearchFragment<T> extends Fragment {

    private EditText searchInput;
    private ImageView filterButton;
    private RecyclerView recyclerView;
    @Getter
    private final MutableLiveData<List<T>> filteredItems = new MutableLiveData<>(new ArrayList<>());
    private UpdatableAdapter<T> adapter;
    @Setter
    private Function<String, Predicate<T>> searchFunction;
    private androidx.lifecycle.LiveData<List<T>> liveDataSource;


    private final Observer<List<T>> observer = items -> {
        if (items != null) {
            applySearch(searchInput != null ? searchInput.getText().toString() : "");
        }
    };

    public void setAdapter(UpdatableAdapter<T> adapter) {
        this.adapter = adapter;
        if (recyclerView != null) {
            recyclerView.setAdapter((RecyclerView.Adapter<?>) adapter);
        }
    }

    public void setLiveDataSource(androidx.lifecycle.LiveData<List<T>> liveData) {
        if (this.liveDataSource != null) {
            this.liveDataSource.removeObserver(observer);
        }
        this.liveDataSource = liveData;

        getViewLifecycleOwnerLiveData().observe(this, owner -> {
            if (owner != null) {
                liveData.observe(owner, observer);
            }
        });
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        searchInput = view.findViewById(R.id.search);
        filterButton = view.findViewById(R.id.filter);
        recyclerView = view.findViewById(R.id.recycler_restaurants);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        if (adapter != null) {
            recyclerView.setAdapter((RecyclerView.Adapter<?>) adapter);
        }

        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                applySearch(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        filterButton.setOnClickListener(v -> {
            openFilterDialog();
        });

        filteredItems.observe(getViewLifecycleOwner(), new Observer<List<T>>() {
            @Override
            public void onChanged(List<T> ts) {
                updateAdapter(ts);
            }
        });

        return view;
    }

    private void openFilterDialog() {
        FilterRestaurantDialogFragment dialog = new FilterRestaurantDialogFragment();
        dialog.show(getChildFragmentManager(), "filterDialog");
    }

    private void updateAdapter(List<T> items) {
        if (adapter != null) {
            adapter.updateData(items);
        }
    }

    private void applySearch(String query) {
        if (searchFunction == null) {
            filteredItems.setValue(liveDataSource.getValue());
            return;
        }

        List<T> source = liveDataSource.getValue();
        if (source == null) return;

        Predicate<T> predicate = searchFunction.apply(query);
        List<T> filtered = new ArrayList<>();
        for (T item : liveDataSource.getValue()) {
            if (predicate.test(item)) {
                filtered.add(item);
            }
        }

        filteredItems.setValue(filtered);
    }

    public interface UpdatableAdapter<T> {
        void updateData(List<T> items);
    }
}