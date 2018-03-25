package com.abrader.tmdb_client.model.api;


import com.abrader.tmdb_client.model.api.DbMovieAPI;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MdbApiController {

    static final String BASE_URL = "https://api.themoviedb.org/";

    public static DbMovieAPI getApi() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        DbMovieAPI dbmAPI = retrofit.create(DbMovieAPI.class);
        return dbmAPI;

    }
}
