package com.abrader.tmdb_client;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface DbMovieAPI {
    @GET("/3/discover/movie")
    Call<FilmPage> getMoviePage(@Query("api_key") String apiKey, @Query("language") String lng,
                                @Query("region") String region, @Query("sort_by") String sortBy,
                                @Query("include_adult") boolean inAdult, @Query("include_video") boolean inVideo,
                                @Query("page") int page);

    @GET("/3/search/movie")
    Call<FilmPage> searchMovie(@Query("api_key") String apiKey, @Query("language") String lng,
                              @Query("query") String searchQuery, @Query("page") int page,
                              @Query("include_adult") boolean inAdult, @Query("region") String region);

}
