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

public class MyFavsAdapter extends RecyclerView.Adapter<MyViewHolder> {

    private final Context context;
    private List<MovieModel> items;

    private final OkHttpClient client = new OkHttpClient();

    public MyFavsAdapter(Context context, List<MovieModel> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View movieView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        // attaches the view to the layout to the viewHolder class...
        return new MyViewHolder(movieView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        MovieModel movie = items.get(position);
        // Set data for each movie
        holder.bind(movie, this);

        // handles item click to change views
        holder.itemView.setOnClickListener(item -> {
            //Toast.makeText(context, "You picked: " + movie.getMovieName(), Toast.LENGTH_SHORT).show();
            Context context = item.getContext();
            Intent intentOpenDetails = new Intent(context, FavDetailsActivity.class);

            intentOpenDetails.putExtra("Title", movie.getMovieName());
            intentOpenDetails.putExtra("Year", movie.getMovieYear());
            intentOpenDetails.putExtra("PosterUrl", movie.getMovieImgUrl());
            intentOpenDetails.putExtra("Plot", movie.getMoviePlot());

            context.startActivity(intentOpenDetails);
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setMovies(List<MovieModel> movies) {
        this.items = movies;
        notifyDataSetChanged();
    }

    // Method which handles the downloading of the image posters
    public void fetchImage(String url, ImageView imageView) {
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
