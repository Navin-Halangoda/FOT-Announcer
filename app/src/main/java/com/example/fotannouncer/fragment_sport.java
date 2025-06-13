package com.example.fotannouncer;

// Remove this unused import, it's causing an issue
// import static java.security.AccessController.getContext;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

// No need for EdgeToEdge, AppCompatActivity imports in a Fragment unless explicitly used
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
import com.google.firebase.database.Query; // Import Query
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class fragment_sport extends Fragment {

    private RecyclerView recyclerView;
    private DatabaseReference database;
    private adapter newsAdapter;
    private ArrayList<card> newsList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_fragment_sport, container, false); // Your layout file name

        recyclerView = view.findViewById(R.id.sport_recycler_view); // Make sure this ID is correct in your layout
        database = FirebaseDatabase.getInstance().getReference("news");

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        newsList = new ArrayList<>();
        newsAdapter = new adapter(getContext(), newsList);
        recyclerView.setAdapter(newsAdapter);

        // --- IMPORTANT CHANGE HERE: Filtering the data ---
        // Create a Query to order by 'category' and then filter by 'sport'
        Query sportNewsQuery = database.orderByChild("category").equalTo("sport");

        sportNewsQuery.addValueEventListener(new ValueEventListener() { // Use the query here
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
                // Handle database error
                Log.e("Firebase", "Failed to read value for sport news.", error.toException());
            }
        });

        return view;
    }
}