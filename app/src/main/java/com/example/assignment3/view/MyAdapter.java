package com.example.assignment3.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignment3.R;
import com.example.assignment3.model.MovieModel;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

    Context context;
    List<MovieModel> movies;

    // since we are downloading the image posters I figure this should be a part of the adapter
    // since the adapter is responsible for dealing with assigning the dead to each item in the recycler view
    private final OkHttpClient client = new OkHttpClient();

    public MyAdapter(Context context, List<MovieModel> movies){
        this.context = context;
        this.movies = movies;
    }

    // 1/3 auto generated methods for a recyclerView
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View movieView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        // attaches the view to the layout to the viewHolder class...
        return new MyViewHolder(movieView);
    }

    // 2/3 auto generated methods for a recyclerView
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        MovieModel movie = movies.get(position);

        holder.title.setText(movie.getMovieName());
        holder.year.setText(movie.getMovieYear());
        holder.plot.setText(movie.getMoviePlot());

        // download and set the movie poster
        String posterUrl = movie.getMovieImgUrl();
        if (posterUrl != null && !posterUrl.isEmpty()) {
            fetchImage(posterUrl, holder.imageView);
        }

        // handles item click to change views
        holder.itemView.setOnClickListener(item -> {
            //Toast.makeText(context, "You picked: " + movie.getMovieName(), Toast.LENGTH_SHORT).show();
            Context context = item.getContext();
            Intent intentOpenDetails = new Intent(context, DetailsActivity.class);

            intentOpenDetails.putExtra("Title", movie.getMovieName());
            intentOpenDetails.putExtra("Year", movie.getMovieYear());
            intentOpenDetails.putExtra("PosterUrl", movie.getMovieImgUrl());
            intentOpenDetails.putExtra("Plot", movie.getMoviePlot());

            context.startActivity(intentOpenDetails);
        });


    }

    // 3/3 auto generated methods for a recyclerView
    @Override
    public int getItemCount() {
        return movies.size();
    }


    // Updates the movie list and notifies the adapter to refresh the data via liveData
    public void updateMovies(List<MovieModel> newMovies) {
        this.movies = newMovies;
        notifyDataSetChanged();
    }

    // Method which handles the downloading of the image posters
    private void fetchImage(String url, ImageView imageView) {
        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()){
                    assert response.body() != null;
                    byte[] imageBytes = response.body().bytes();
                    Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

                    // Update ImageView on the UI thread
                    imageView.post(() -> imageView.setImageBitmap(bitmap));
                }
            }
        });
    }
}

