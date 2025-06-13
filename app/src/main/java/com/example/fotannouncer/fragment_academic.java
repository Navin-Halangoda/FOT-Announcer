package com.example.fotannouncer;

// Remove this unused and incorrect import
// import static java.security.AccessController.getContext;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

// Remove unnecessary AndroidX Activity/Window related imports for a Fragment
// import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
// import androidx.appcompat.app.AppCompatActivity;
// import androidx.core.graphics.Insets;
// import androidx.core.view.ViewCompat;
// import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query; // Import Query for filtering
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class fragment_academic extends Fragment { // Ensure the class name matches the file name

    private RecyclerView recyclerView;
    private DatabaseReference database;
    private adapter newsAdapter;
    private ArrayList<card> newsList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment.
        // IMPORTANT: Ensure you are inflating the correct layout file for fragment_academic.
        View view = inflater.inflate(R.layout.activity_fragment_academic, container, false); // Changed to academic layout

        // IMPORTANT: Ensure this RecyclerView ID matches the one in activity_fragment_academic.xml
        recyclerView = view.findViewById(R.id.academic_recycler_view); // Changed to academic RecyclerView ID
        database = FirebaseDatabase.getInstance().getReference("news");

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext())); // Correct use of getContext() for a Fragment

        newsList = new ArrayList<>();
        newsAdapter = new adapter(getContext(), newsList);
        recyclerView.setAdapter(newsAdapter);

        // --- Filter the data for 'academic' category ---
        Query academicNewsQuery = database.orderByChild("category").equalTo("academic"); // Changed to academic filter

        academicNewsQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                newsList.clear(); // Clear the list to avoid duplicates on subsequent data changes
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    card cardItem = dataSnapshot.getValue(card.class);
                    if (cardItem != null) {
                        newsList.add(cardItem);
                    }
                }
                newsAdapter.notifyDataSetChanged(); // Notify adapter after all data is added
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle database error, e.g., log it or show a toast
                Log.e("Firebase", "Failed to read value for academic news.", error.toException());
            }
        });

        // The ViewCompat.setOnApplyWindowInsetsListener part is typically handled by the parent Activity
        // or a global theme/style for the entire window. It's generally not needed or should be
        // removed from individual fragments unless you have very specific insets handling.
        /*
        ViewCompat.setOnApplyWindowInsetsListener(view.findViewById(R.id.home_constraint_layout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        */

        return view;
    }
}