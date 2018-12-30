package com.nanodegree.udacity.popularmoviesstageone.Models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class MovieCredits {
    @SerializedName("id")
    private long id;

    @SerializedName("cast")
    private ArrayList<MovieCast> cast;

    public MovieCredits(long id, ArrayList<MovieCast> cast) {
        this.id = id;
        this.cast = cast;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public ArrayList<MovieCast> getCast() {
        return cast;
    }

    public void setCast(ArrayList<MovieCast> cast) {
        this.cast = cast;
    }
}