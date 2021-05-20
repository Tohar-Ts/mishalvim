package com.example.mishlavim.model;

public class CustomList {

    private int mImgResId;
    private String mMovieName, mMovieRating;

    public CustomList(int imgId, String movieN, String movieR) {
        mImgResId = imgId;
        mMovieName = movieN;
        mMovieRating = movieR;
    }

    public int getmImgResId() {
        return mImgResId;

    }

    public String getmMovieName() {
        return mMovieName;
    }

    public String getmMovieRating() {
        return mMovieRating;
    }
}


