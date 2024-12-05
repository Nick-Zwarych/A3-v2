package com.example.assignment3.view;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignment3.R;
import com.example.assignment3.model.MovieModel;

public class MyViewHolder extends RecyclerView.ViewHolder {

    ImageView imageView;
    TextView title;
    TextView year;
    TextView plot;

    // setting the data for each item view

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.imageview);
        title = itemView.findViewById(R.id.title_txt);
        year = itemView.findViewById(R.id.year_txt);
        plot = itemView.findViewById(R.id.plot_txt);

    }

    // Additional bind method for the favourites adapter
    public void bind(MovieModel movie, MyFavsAdapter adapter) {
        title.setText(movie.getMovieName());
        year.setText(movie.getMovieYear());
        plot.setText(movie.getMoviePlot());

        // Download and set the movie poster using the adapter's fetchImage method
        String posterUrl = movie.getMovieImgUrl();
        if (posterUrl != null && !posterUrl.isEmpty()) {
            adapter.fetchImage(posterUrl, imageView);
        }
    }
}
