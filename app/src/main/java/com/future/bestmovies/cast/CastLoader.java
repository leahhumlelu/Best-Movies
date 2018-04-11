package com.future.bestmovies.cast;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.future.bestmovies.utils.NetworkUtils;
import java.util.ArrayList;


public class CastLoader extends AsyncTaskLoader<ArrayList<Cast>> {
    private final int movieId;

    public CastLoader(Context context, int movieId) {
        super(context);
        this.movieId = movieId;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public ArrayList<Cast> loadInBackground() {
        return NetworkUtils.fetchMovieCast(movieId);
    }
}