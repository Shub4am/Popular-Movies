package com.nanodegree.udacity.popularmoviesstageone;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.nanodegree.udacity.popularmoviesstageone.Database.RepositoryMovies;
import com.nanodegree.udacity.popularmoviesstageone.Models.Movie;

import java.util.List;

public class MainActivityViewModel extends AndroidViewModel {

    private static final String TAG = MainActivityViewModel.class.getSimpleName();
    private LiveData<List<Movie>> favoriteMovies;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        RepositoryMovies repositoryMovies = new RepositoryMovies(application);
        Log.d(TAG, "Getting tasks from database via ViewModel");
        favoriteMovies = repositoryMovies.loadAllFavoriteMovies();
    }

    public LiveData<List<Movie>> getFavoriteMovies() {
        return favoriteMovies;
    }
}
