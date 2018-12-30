package com.nanodegree.udacity.popularmoviesstageone;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import com.nanodegree.udacity.popularmoviesstageone.Database.RepositoryMovies;
import com.nanodegree.udacity.popularmoviesstageone.Models.Movie;

public class DetailsActivityViewModel extends AndroidViewModel {

    private RepositoryMovies repositoryMovies;

    public DetailsActivityViewModel(@NonNull Application application) {
        super(application);
        repositoryMovies = new RepositoryMovies(application);
    }
    public boolean isFavorite(int movieId) {
        return repositoryMovies.isFavorite(movieId);
    }

    public void addMovieToFavorites(Movie movie) {
        repositoryMovies.addMovieToFavorites(movie);
    }

    public void removeMovieFromFavorites(Movie movie) {
        repositoryMovies.deleteFavoriteMovie(movie);
    }

    public void updateFavoriteMovie(int movieId, boolean isFavorite) {
        repositoryMovies.updateFavoriteMovie(movieId, isFavorite);
    }
}
