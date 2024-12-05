package com.example.assignment3.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.assignment3.databinding.RegisterLayoutBinding;

public class RegisterActivity extends AppCompatActivity {

    private RegisterLayoutBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate binding and set content view
        binding = RegisterLayoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Handle Register Button click
        binding.registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get email and password inputs
                String email = binding.emailEditText.getText().toString().trim();
                String password = binding.passwordEditText.getText().toString().trim();

                // Validate inputs - TODO: validate email address format... & authenticate
                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                } else {
                    // Navigate to MainActivity
                    Intent MainActivity = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(MainActivity);
                    //finish(); // Optional? Close RegisterActivity to prevent returning with back button??
                }
            }
        });
    }
}
