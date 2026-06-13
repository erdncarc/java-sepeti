package com.example.core.network;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class BaseCallback<T> implements Callback<T> {

    public abstract void onSuccess(T result);

    public abstract void onError(Throwable t);

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (response.isSuccessful() && response.body() != null) {
            onSuccess(response.body());
        } else {
            onError(new Exception("API response error: " + response.code()));
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        onError(t);
    }
}
