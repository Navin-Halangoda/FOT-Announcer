package com.example.fotannouncer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Signup extends AppCompatActivity {
    TextView signinRedirect;
    private FirebaseAuth auth;
    private EditText signupEmail, signupPassword, SignupUsername, signupconfirmpass;
    private Button signupButton;

    private DatabaseReference databaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        signinRedirect = findViewById(R.id.text_view_signin);
        signinRedirect.setOnClickListener(v -> {
            Intent intent = new Intent(Signup.this, MainActivity.class);
            startActivity(intent);
        });

        auth = FirebaseAuth.getInstance();
        databaseRef = FirebaseDatabase.getInstance().getReference("Users");

        signupEmail = findViewById(R.id.editTextfrgemail);
        signupPassword = findViewById(R.id.editTextfrgPassword);
        signupButton = findViewById(R.id.button_signup);
        SignupUsername = findViewById(R.id.editTextfrgUsername);
        signupconfirmpass = findViewById(R.id.editTextfrgconfirmpasswordd);

        signupButton.setOnClickListener(v -> {
            String user = signupEmail.getText().toString().trim();
            String pass = signupPassword.getText().toString().trim();
            String confirmpass = signupconfirmpass.getText().toString().trim();
            String username = SignupUsername.getText().toString().trim();

            if (user.isEmpty()) {
                signupEmail.setError("Email cannot be empty");
                return;
            }
            if (pass.isEmpty()) {
                signupPassword.setError("Password cannot be empty");
                return;
            }
            if (!pass.equals(confirmpass)) {
                signupPassword.setError("Passwords do not match");
                signupconfirmpass.setError("Passwords do not match");
                return;
            }
            if (username.isEmpty()) {
                SignupUsername.setError("Username cannot be empty");
                return;
            }

            auth.createUserWithEmailAndPassword(user, pass).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    String userId = auth.getCurrentUser().getUid();
                    com.example.fotannouncer.User userObject = new com.example.fotannouncer.User(username, user);

                    databaseRef.child(userId).setValue(userObject).addOnCompleteListener(dbTask -> {
                        if (dbTask.isSuccessful()) {
                            Toast.makeText(Signup.this, "Signup successful", Toast.LENGTH_SHORT).show();
                            finish(); // close signup screen
                        } else {
                            Toast.makeText(Signup.this, "Signup OK, but failed to save user data", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(Signup.this, "Signup failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        });
    }
}
