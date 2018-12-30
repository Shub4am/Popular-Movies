package com.nanodegree.udacity.popularmoviesstageone.Interface;

import com.nanodegree.udacity.popularmoviesstageone.Models.Movie;
import com.nanodegree.udacity.popularmoviesstageone.Models.MovieCredits;
import com.nanodegree.udacity.popularmoviesstageone.Models.MovieResponse;
import com.nanodegree.udacity.popularmoviesstageone.Models.MovieReviews;
import com.nanodegree.udacity.popularmoviesstageone.Models.MovieTrailer;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Interface {

    @GET("movie/popular")
    Call<MovieResponse> getPopularMovies(@Query("api_key") String apiKey, @Query("language") String language,
                                         @Query("page") int page);

    @GET("movie/top_rated")
    Call<MovieResponse> getTopRatedMovies(@Query("api_key") String apiKey, @Query("language") String language,
                                          @Query("page") int page);

    @GET("movie/{movie_id}/credits")
    Call<MovieCredits> getMovieCredits(@Path("movie_id") int movieId, @Query("api_key") String apiKey);

    @GET("movie/{movie_id}/reviews")
    Call<MovieReviews> getMovieReviews(@Path("movie_id") int movieId, @Query("api_key") String apiKey, @Query("page") int page);

    @GET("movie/{movie_id}")
    Call<Movie> getMovieDetails(@Path("movie_id") int movieId, @Query("api_key") String apiKey, @Query("append_to_response") String append_to_response);

    @GET("movie/{movie_id}")
    Call<Movie> getMovieDetails(@Path("movie_id") int movieId, @Query("api_key") String apiKey);

    @GET("movie/{movie_id}/videos")
    Call<MovieTrailer> getMovieTrailers(@Path("movie_id") int movieId, @Query("api_key") String apiKey);
}

