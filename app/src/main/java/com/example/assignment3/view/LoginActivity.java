package com.example.assignment3.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.assignment3.databinding.LoginLayoutBinding;

public class LoginActivity extends AppCompatActivity {

    private LoginLayoutBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate binding and set content view
        binding = LoginLayoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Handle Login Button click
        binding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get email and password inputs
                String email = binding.emailEditText.getText().toString().trim();
                String password = binding.passwordEditText.getText().toString().trim();

                // Validate inputs - TODO: validate email address format... & authenticate
                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                } else {
                    // Navigate to MainActivity
                    Intent MainActivity = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(MainActivity);
                    //finish(); // Optional? Close LoginActivity to prevent returning with back button???
                }
            }
        });

        // Handle Register Button click
        binding.registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to RegisterActivity
                Intent RegisterActivity = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(RegisterActivity);
            }
        });
    }
}
