package com.nanodegree.udacity.popularmoviesstageone.Network;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {

    private static Retrofit retrofit;
    private static final String THEMOVIEDB_BASE_URL = "https://api.themoviedb.org/3/";

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
    private static HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);

    public static Retrofit getRetrofitInstance() {

        if (retrofit == null) {

            httpClient.addInterceptor(loggingInterceptor);

            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(THEMOVIEDB_BASE_URL)
                    .client(httpClient.build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
