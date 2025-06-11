package com.example.fotannouncer;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils; // For TextUtils.isEmpty
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText; // Import EditText
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener; // For task listener
import com.google.android.gms.tasks.Task; // For task listener
import com.google.firebase.auth.AuthCredential; // For re-authentication if needed
import com.google.firebase.auth.EmailAuthProvider; // For re-authentication
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class userinfo extends AppCompatActivity {
    private ImageButton back;
    private Button logout;
    private Button editinfo;
    Dialog dialog;
    Dialog edit; // Your edit dialog
    Button cancel,ok;
    Button canceledit,okedit;

    // EditTexts from the edit dialog layout
    private EditText editUsernameField;
    private EditText editEmailField;

    private FirebaseAuth auth;
    private TextView infoname;
    private TextView infoemail;
    private DatabaseReference databaseReference;
    private FirebaseUser currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_userinfo);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize UI elements
        back = findViewById(R.id.backbutton2);
        logout = findViewById(R.id.signout);
        editinfo = findViewById(R.id.editinfo);
        infoname = findViewById(R.id.infoname);
        infoemail = findViewById(R.id.infoemail);

        // Initialize Firebase Auth and current user
        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();

        // --- Firebase Realtime Database Data Loading ---
        loadUserInfo(); // Call a method to load user info

        // --- Back Button Listener ---
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(userinfo.this, News.class);
                startActivity(intent);
                finish(); // Finish current activity so you don't stack them
            }
        });

        // --- Logout Dialog Setup ---
        dialog = new Dialog(userinfo.this);
        dialog.setContentView(R.layout.dialogboxout);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.dialogboxbackground));
        dialog.setCancelable(false);

        cancel = dialog.findViewById(R.id.cancel);
        ok = dialog.findViewById(R.id.ok);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(userinfo.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                dialog.dismiss();
                finish();
            }
        });

        // Set listener for the logout button to show the confirmation dialog
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });

        // --- Edit Dialog Setup ---
        edit = new Dialog(userinfo.this);
        edit.setContentView(R.layout.editdialog); // Set the custom layout for the edit dialog

        // Initialize EditTexts from the edit dialog layout
        editUsernameField = edit.findViewById(R.id.editUsername);
        editEmailField = edit.findViewById(R.id.editemail); // Your XML has editemail, not editEmail

        WindowManager.LayoutParams layoutParams = edit.getWindow().getAttributes();
        layoutParams.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
        layoutParams.y = 150; // Adjust Y offset as needed
        edit.getWindow().setAttributes(layoutParams);
        edit.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        edit.getWindow().setBackgroundDrawable(getDrawable(R.drawable.dialogboxbackground));
        edit.setCancelable(false);

        canceledit = edit.findViewById(R.id.canceledigt);
        okedit = edit.findViewById(R.id.okedit);

        canceledit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit.dismiss(); // Dismiss the edit dialog
            }
        });

        okedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserInfo(); // Call a method to handle the update logic
            }
        });


        // Set listener for the editinfo button to show the edit dialog
        editinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Populate dialog fields with current info before showing
                editUsernameField.setText(infoname.getText().toString());
                editEmailField.setText(infoemail.getText().toString());
                edit.show();
            }
        });
    }

    // Method to load user info from Firebase
    private void loadUserInfo() {
        if (currentUser != null) {
            String userId = currentUser.getUid();
            databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userId);

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String email = dataSnapshot.child("email").getValue(String.class);
                        String username = dataSnapshot.child("username").getValue(String.class);

                        if (username != null) {
                            infoname.setText(username);
                        } else {
                            infoname.setText("Username N/A");
                        }

                        if (email != null) {
                            infoemail.setText(email);
                        } else {
                            infoemail.setText("Email N/A");
                        }
                    } else {
                        Toast.makeText(userinfo.this, "User data not found in database.", Toast.LENGTH_SHORT).show();
                        infoname.setText("Data Missing");
                        infoemail.setText("Data Missing");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(userinfo.this, "Failed to load user data: " + databaseError.getMessage(), Toast.LENGTH_LONG).show();
                    infoname.setText("Error loading data");
                    infoemail.setText("Error loading data");
                }
            });
        } else {
            Toast.makeText(this, "No user logged in. Please log in first.", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(userinfo.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
    }

    // Method to update user info to Firebase
    private void updateUserInfo() {
        if (currentUser == null) {
            Toast.makeText(this, "No user logged in.", Toast.LENGTH_SHORT).show();
            return;
        }

        final String newUsername = editUsernameField.getText().toString().trim();
        final String newEmail = editEmailField.getText().toString().trim();
        final String currentEmail = currentUser.getEmail(); // Get current Firebase Auth email

        boolean changesMade = false;

        // 1. Validate inputs
        if (TextUtils.isEmpty(newUsername)) {
            editUsernameField.setError("Username cannot be empty");
            return;
        }
        if (TextUtils.isEmpty(newEmail)) {
            editEmailField.setError("Email cannot be empty");
            return;
        }
        // Basic email format validation
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(newEmail).matches()) {
            editEmailField.setError("Invalid email format");
            return;
        }


        // 2. Update username in Realtime Database (always done if newUsername is valid)
        if (!newUsername.equals(infoname.getText().toString())) { // Check if username actually changed
            databaseReference.child("username").setValue(newUsername)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(userinfo.this, "Username updated.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(userinfo.this, "Failed to update username: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
            changesMade = true;
        }


        // 3. Update email in Firebase Authentication (if changed)
        if (!newEmail.equals(currentEmail)) { // Check if email actually changed
            // IMPORTANT: Updating email requires re-authentication if the user signed in a long time ago.
            // For simplicity here, we're assuming the user has recently authenticated.
            // In a real app, you might need to prompt for current password to re-authenticate.
            // Example of re-authentication:
            // AuthCredential credential = EmailAuthProvider.getCredential(currentUser.getEmail(), "current_password");
            // currentUser.reauthenticate(credential).addOnCompleteListener(...)

            currentUser.updateEmail(newEmail)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(userinfo.this, "Email updated in Firebase Auth.", Toast.LENGTH_SHORT).show();
                            // Also update email in Realtime Database to keep it consistent
                            databaseReference.child("email").setValue(newEmail);
                        } else {
                            Toast.makeText(userinfo.this, "Failed to update email: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            // Handle specific errors like "requires-recent-login"
                            // If "requires-recent-login", you must re-authenticate the user
                            // before calling updateEmail again.
                            if (task.getException() instanceof FirebaseAuthRecentLoginRequiredException) {
                                Toast.makeText(userinfo.this, "Please log in again to update your email.", Toast.LENGTH_LONG).show();
                                // Redirect to login screen or prompt for password
                            }
                        }
                    });
            changesMade = true;
        }

        if (changesMade) {
            edit.dismiss(); // Dismiss dialog only if some changes were attempted
        } else {
            Toast.makeText(this, "No changes detected.", Toast.LENGTH_SHORT).show();
            edit.dismiss(); // Dismiss if no changes made
        }
    }
}