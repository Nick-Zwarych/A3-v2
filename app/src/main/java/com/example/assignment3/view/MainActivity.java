package com.example.assignment3.view;

import com.example.assignment3.R;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.assignment3.databinding.ActivityMainBinding;
import com.example.assignment3.model.MovieModel;
import com.example.assignment3.viewmodel.MovieViewModel;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    List<MovieModel> movieList;
    MyAdapter myAdapter;
    MovieViewModel movieViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_main);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize RecyclerView
        myAdapter = new MyAdapter(getApplicationContext(), new ArrayList<>());
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(myAdapter);

        // Initialize ViewModel
        movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);

        // Observe movie data
        movieViewModel.getMovies().observe(this, movies -> {
            // Updates RecyclerView adapter with new data when the observer changes
            myAdapter.updateMovies(movies);
        });

        // Observe errors
        movieViewModel.getError().observe(this, error -> {
            // Show error messages as a toast popup
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
        });

        // Set up button click listener for the search btn
        binding.getMoviesBtn.setOnClickListener(view -> {
            String query = binding.nameField.getText().toString();
            movieViewModel.fetchMovies(query);
        });

        // Set up click listener for bottom navigation
        binding.bottomNavigationView.setSelectedItemId(R.id.searchID); // highlight selected

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.searchID:
                    // Stay on MainActivity
                    return true;

                case R.id.favoritesID:
                    // Navigate to FavouritesActivity
                    startActivity(new Intent(MainActivity.this, FavouritesActivity.class));
                    return true;

                default:
                    return false;
            }
        });

        // Remove the preview text from the search input when focused
        binding.nameField.setOnFocusChangeListener((view, hasFocus) -> {
            if (hasFocus) {
                binding.nameField.setText("");
            }
        });

        // Initialize movie list
        movieList = new ArrayList<>();

        // Set up RecyclerView layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.recyclerView.setLayoutManager(layoutManager);

        // Set up RecyclerView adapter with initial empty list
        myAdapter = new MyAdapter(getApplicationContext(), movieList);
        binding.recyclerView.setAdapter(myAdapter);

    }

}