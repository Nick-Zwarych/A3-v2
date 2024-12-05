package com.example.assignment3.view;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.assignment3.databinding.DetailLayoutBinding;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DetailsActivity extends AppCompatActivity {

    private final OkHttpClient client = new OkHttpClient();

    DetailLayoutBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DetailLayoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // get intent extra's / data from the selected item in the recycler view
        String title = getIntent().getStringExtra("Title");
        String year = getIntent().getStringExtra("Year");
        String posterUrl = getIntent().getStringExtra("PosterUrl");
        String plot = getIntent().getStringExtra("Plot");

        // populate view
        binding.detailTitle.setText(title);
        binding.detailYear.setText(year);
        binding.detailDescription.setText(plot);
        // set the poster
        fetchImage(posterUrl, binding.detailPoster);

        // click listener to close the detail activity
        binding.detailBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Close the detail activity
            }
        });

        // click listener for adding to fav's
        binding.addToFavBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO add movie to user's favourites
            }
        });

    }

    // I didn't feel like moving it to a separate network util's class so I'm repeating this method here to download the image for the details view
    private void fetchImage(String url, ImageView imageView) {
        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                // maybe come back and display a placeholder image or error message later...
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()){
                    assert response.body() != null;
                    byte[] imageBytes = response.body().bytes();
                    Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

                    // Update the details ImageView on the UI thread
                    imageView.post(() -> imageView.setImageBitmap(bitmap));
                }
            }
        });
    }
}
