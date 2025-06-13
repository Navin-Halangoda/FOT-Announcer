package com.example.fotannouncer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.ArrayList; // Import ArrayList

public class News extends AppCompatActivity {

    private ViewPager2 viewPager2;
    private BottomNavigationView bottomNavigationView;
    private ImageButton account;
    private ImageButton devinfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news); // Ensure this matches your main layout name

        account = findViewById(R.id.buttonaccount);
        account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(News.this, userinfo.class);
                startActivity(intent);
            }
        });
        devinfo = findViewById(R.id.buttonsetting);
        devinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(News.this, devinfo.class);
                startActivity(intent);
            }
        });

        viewPager2 = findViewById(R.id.fragment_container);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        // Setup ViewPager2 with a FragmentStateAdapter that holds all your fragments
        NewsFragmentAdapter pagerAdapter = new NewsFragmentAdapter(this);
        viewPager2.setAdapter(pagerAdapter);

        // Listener for BottomNavigationView item selection
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.home) {
                viewPager2.setCurrentItem(0, false); // false for no smooth scroll
                return true;
            } else if (itemId == R.id.sport) {
                viewPager2.setCurrentItem(1, false);
                return true;
            } else if (itemId == R.id.academic) {
                viewPager2.setCurrentItem(2, false);
                return true;
            } else if (itemId == R.id.event) {
                viewPager2.setCurrentItem(3, false);
                return true;
            }
            return false;
        });

        // Listener for ViewPager2 page changes (when user swipes)
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                // Update BottomNavigationView selection based on ViewPager2's current page
                switch (position) {
                    case 0:
                        bottomNavigationView.setSelectedItemId(R.id.home);
                        break;
                    case 1:
                        bottomNavigationView.setSelectedItemId(R.id.sport);
                        break;
                    case 2:
                        bottomNavigationView.setSelectedItemId(R.id.academic);
                        break;
                    case 3:
                        bottomNavigationView.setSelectedItemId(R.id.event);
                        break;
                }
            }
        });

        // Set initial fragment when activity starts
        if (savedInstanceState == null) {
            viewPager2.setCurrentItem(0, false); // Start with the Home fragment
        }
    }

    // Adapter for ViewPager2
    private static class NewsFragmentAdapter extends FragmentStateAdapter {
        private final ArrayList<Fragment> fragmentList = new ArrayList<>();

        public NewsFragmentAdapter(@NonNull AppCompatActivity fragmentActivity) {
            super(fragmentActivity);
            // Add all your fragments to the list in the desired order
            fragmentList.add(new fragmet_home()); // Index 0: Home
            fragmentList.add(new fragment_sport());   // Index 1: Sport (You need to create this)
            fragmentList.add(new fragment_academic()); // Index 2: Academic (You need to create this)
            fragmentList.add(new fragment_event());    // Index 3: Event (You need to create this)
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getItemCount() {
            return fragmentList.size();
        }
    }
}