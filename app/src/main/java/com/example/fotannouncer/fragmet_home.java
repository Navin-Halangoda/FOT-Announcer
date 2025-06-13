package com.example.fotannouncer;

import android.os.Bundle;
import android.util.Log; // For logging errors
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment; // Important: Change to Fragment
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class fragmet_home extends Fragment { // Change to extend Fragment

    private RecyclerView recyclerView;
    private DatabaseReference database;
    private adapter newsAdapter; // Renamed for clarity, using your original 'adapter' class name
    private ArrayList<card> newsList; // Renamed for clarity

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_fragmet_home, container, false); // Use the correct layout file name

        recyclerView = view.findViewById(R.id.news_recycler_view); // Use the correct ID from fragmwngt_home.xml
        database = FirebaseDatabase.getInstance().getReference("news");

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext())); // Use getContext() for Fragment

        newsList = new ArrayList<>();
        newsAdapter = new adapter(getContext(), newsList); // Pass context
        recyclerView.setAdapter(newsAdapter);

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                newsList.clear(); // Clear the list to avoid duplicates on subsequent data changes
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    card cardItem = dataSnapshot.getValue(card.class); // Use cardItem for clarity
                    if (cardItem != null) {
                        newsList.add(cardItem);
                    }
                }
                newsAdapter.notifyDataSetChanged(); // Notify adapter after all data is added
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle database error, e.g., log it or show a toast
                Log.e("Firebase", "Failed to read value.", error.toException());
            }
        });

        // EdgeToEdge and ViewCompat.setOnApplyWindowInsetsListener are typically handled by the parent Activity
        // or a global theme/style. They are not usually set within a Fragment's onCreateView.
        // Remove the following if it's not needed or if it interferes with parent activity:
        /*
        ViewCompat.setOnApplyWindowInsetsListener(view.findViewById(R.id.home_constraint_layout), (v, insets) -> { // Use correct ID
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        */

        return view;
    }
}