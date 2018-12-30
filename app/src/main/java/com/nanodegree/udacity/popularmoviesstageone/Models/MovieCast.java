package com.nanodegree.udacity.popularmoviesstageone.Models;

import com.google.gson.annotations.SerializedName;

public class MovieCast {
    @SerializedName("name")
    private String name;
    @SerializedName("profile_path")
    private String profileUrl;
    @SerializedName("character")
    private String character;

    public MovieCast(String name, String profileUrl) {
        this.name = name;
        this.profileUrl = profileUrl;
    }

    public String getName() {
        return name;
    }

    public String getCharacter() { return character; }

    public String getProfileUrl() {
        return profileUrl;
    }
}
