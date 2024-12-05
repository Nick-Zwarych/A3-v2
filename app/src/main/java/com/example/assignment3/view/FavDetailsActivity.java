package com.example.assignment3.view;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.assignment3.databinding.EditFavouriteLayoutBinding;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FavDetailsActivity extends AppCompatActivity {
    private final OkHttpClient client = new OkHttpClient();

    EditFavouriteLayoutBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = EditFavouriteLayoutBinding.inflate(getLayoutInflater());
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

        //TODO implement save and delete for the movie description
        // I guess we do need to save more than just the movieID per user, i didn't account for this earlier

        // click listener for the update and delete(remove from favourites) btns

        binding.updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // get the new description
                String newDescription = binding.detailDescription.toString();



            }
        });

        binding.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO remove movie from the user's list of fav's

                finish();
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
