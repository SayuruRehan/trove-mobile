package com.example.trove_mobile.retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl("http://10.0.2.2:5032/api/") // for development machine's local server
//                    .baseUrl("http://192.168.0.X:5032/api/")  // Replace with your machine's local (for run in real device), IP for mac type in the terminal ifconfig
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
