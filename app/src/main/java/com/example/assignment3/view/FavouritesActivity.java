package com.example.assignment3.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.assignment3.R;
import com.example.assignment3.databinding.FavouriteLayoutBinding;
import com.example.assignment3.viewmodel.FavouriteViewModel;

import java.util.ArrayList;

public class FavouritesActivity extends AppCompatActivity {

    private FavouriteLayoutBinding binding;
    private FavouriteViewModel favouriteViewModel;
    private MyFavsAdapter myFavsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize View Binding
        binding = FavouriteLayoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize ViewModel
        favouriteViewModel = new ViewModelProvider(this).get(FavouriteViewModel.class);

        // Set up RecyclerView
        myFavsAdapter = new MyFavsAdapter(this, new ArrayList<>());
        binding.recyclerView.setAdapter(myFavsAdapter);

        // Attach layout manager
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Observe the LiveData from ViewModel
        favouriteViewModel.getFavMovies().observe(this, movies -> {
            if (movies != null) {
                myFavsAdapter.setMovies(movies);
            }
        });

        // Error handling
        favouriteViewModel.getError().observe(this, error -> {
            if (error != null) {
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
            }
        });

        // Fetch our favorite movies
        favouriteViewModel.fetchFavoriteMovies();
        //String[] favs = {"tt1160419", "tt3896198"};

        // Bottom Navigation
        binding.bottomNavigationView.setSelectedItemId(R.id.favoritesID); // highlight selected

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.searchID:
                    // Navigate to MainActivity
                    startActivity(new Intent(FavouritesActivity.this, MainActivity.class));
                    return true;

                case R.id.favoritesID:
                    // Stay on MainActivity
                    return true;

                default:
                    return false;
            }
        });

    }
}
