package com.example.assignment3.viewmodel;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.assignment3.model.MovieModel;
import com.example.assignment3.utils.ApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MovieViewModel extends ViewModel {

    // Highlight says these both should be 'final' -> makes them only able to initialize once, the data inside can still change
    private final MutableLiveData<List<MovieModel>> moviesLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();

    public LiveData<List<MovieModel>> getMovies() {
        return moviesLiveData;
    }

    public LiveData<String> getError() {
        return errorLiveData;
    }

    // initial api request
    public void fetchMovies(String query) {
        Log.i("tag", "Fetching movies for query: " + query);

        String getUrl = "https://www.omdbapi.com/?s=" + query + "&type=movie&plot=full&apikey=fc6e628b";

        ApiClient.get(getUrl, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e("tag", "Error fetching movies: " + e.getMessage(), e);
                errorLiveData.postValue("Failed to fetch movies. Please try again.");
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseData = response.body().string();
                    try {
                        parseData(responseData);
                    } catch (JSONException e) {
                        Log.e("tag", "JSON Parsing error: " + e.getMessage(), e);
                        errorLiveData.postValue("Error processing movie data.");
                    }
                } else {
                    Log.e("tag", "Response not successful: " + response.message());
                    errorLiveData.postValue("Failed to fetch movies. Please try again.");
                }
            }
        });
    }

    private void parseData(String data) throws JSONException {
        JSONObject json = new JSONObject(data);

        if (!json.getBoolean("Response")) {
            errorLiveData.postValue("No movies found!");
            return;
        }

        JSONArray searchResults = json.getJSONArray("Search");
        List<MovieModel> movies = new ArrayList<>();
        // we keep track of the total number of movies returned here and pass it to the next api call...
        // to make sure the second call matches the same number of results as the first
        int totalMoviesInitialSearch = searchResults.length();

        for (int i = 0; i < searchResults.length(); i++) {
            JSONObject movieObject = searchResults.getJSONObject(i);

            String title = movieObject.getString("Title");
            String year = movieObject.getString("Year");
            String posterUrl = movieObject.getString("Poster");
            // we need to use imdbID to make another api call to get the description / plot of the movie
            String imdbID = movieObject.getString("imdbID");

            fetchMoviePlot(imdbID, title, year, posterUrl, movies, totalMoviesInitialSearch);

            // Create MovieModel object and add to the list
            //MovieModel movie = new MovieModel(posterUrl, title, year, imdbID);
            //movies.add(movie);
        }

        // Update the LiveData with the new list of movies
        //moviesLiveData.postValue(movies);
    }

    // second api request for getting more specific data (plot) about each movie returned from search results
    private void fetchMoviePlot(String imdbID, String title, String year, String posterUrl, List<MovieModel> movies, int totalMoviesInitialSearch) {
        String plotUrl = "https://www.omdbapi.com/?i=" + imdbID + "&apikey=fc6e628b";

        ApiClient.get(plotUrl, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e("tag", "Error fetching plot: " + e.getMessage(), e);

                // if plot not available, assign placeholder message to display
                MovieModel movie = new MovieModel(posterUrl, title, year, "Plot not available.");
                movies.add(movie);
                checkMoviesLoaded(movies, totalMoviesInitialSearch);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    String responseData = response.body().string();
                    try {
                        JSONObject extendedMovieDetailsJSON = new JSONObject(responseData);
                        String plot = extendedMovieDetailsJSON.getString("Plot");

                        // Create MovieModel object (which can now can include the plot)
                        MovieModel movie = new MovieModel(posterUrl, title, year, plot);
                        movies.add(movie);
                    } catch (JSONException e) {
                        Log.e("tag", "JSON Parsing error: " + e.getMessage(), e);
                        // if plot not available, assign placeholder message to display
                        MovieModel movie = new MovieModel(posterUrl, title, year, "Plot not available.");
                        movies.add(movie);
                    }
                } else {
                    Log.e("tag", "Plot response not successful: " + response.message());
                    // If API call is unsuccessful, add placeholder plot
                    MovieModel movie = new MovieModel(posterUrl, title, year, "Plot not available.");
                    movies.add(movie);
                }
                checkMoviesLoaded(movies, totalMoviesInitialSearch);
            }
        });
    }

    // Check if all movies are loaded before updating LiveData...
    private void checkMoviesLoaded(List<MovieModel> movies, int totalMovies) {
        if (movies.size() == totalMovies) {
            // All movies have been processed... lets update the LiveData
            moviesLiveData.postValue(movies);
        }
    }

}
