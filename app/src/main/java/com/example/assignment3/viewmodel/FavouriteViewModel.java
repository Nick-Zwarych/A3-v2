package com.example.assignment3.viewmodel;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.assignment3.model.MovieModel;
import com.example.assignment3.utils.ApiClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class FavouriteViewModel extends ViewModel {

    private final MutableLiveData<List<MovieModel>> favMoviesLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();

    public LiveData<List<MovieModel>> getFavMovies() {
        return favMoviesLiveData;
    }

    public LiveData<String> getError() {
        return errorLiveData;
    }

    public void fetchFavoriteMovies() {

        // TODO Here we can query the database to get the fav movie id's
        // for now there are just two hardcoded id's
        //favMovies.add(new MovieModel("https://path-to-poster", "Movie Title", "2024", "Movie plot"));

        List<String> favIds = List.of("tt1160419", "tt3896198");  // Hardcoded movie IDs
        List<MovieModel> favMovies = new ArrayList<>();

        for (String id : favIds) {
            fetchMovieDetails(id, favMovies, favIds.toArray().length);
        }

    }

    private void fetchMovieDetails(String movieId, List<MovieModel> movies, int totalMovies) {
        String url = "https://www.omdbapi.com/?i=" + movieId + "&apikey=fc6e628b";

        ApiClient.get(url, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e("tag", "Error fetching movie details: " + e.getMessage(), e);
                checkMoviesLoaded(movies, totalMovies);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    String responseData = response.body().string();
                    try {
                        JSONObject json = new JSONObject(responseData);
                        String title = json.getString("Title");
                        String year = json.getString("Year");
                        String posterUrl = json.getString("Poster");
                        String plot = json.getString("Plot");

                        MovieModel movie = new MovieModel(posterUrl, title, year, plot);
                        movies.add(movie);
                    } catch (JSONException e) {
                        Log.e("tag", "JSON Parsing error: " + e.getMessage(), e);
                    }
                } else {
                    Log.e("tag", "Response not successful: " + response.message());
                }
                checkMoviesLoaded(movies, totalMovies);
            }

        });
    }

    private void checkMoviesLoaded(List<MovieModel> movies, int totalMovies) {
        if (movies.size() == totalMovies) {
            favMoviesLiveData.postValue(movies);
        }
    }


}
