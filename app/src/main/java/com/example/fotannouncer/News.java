package com.example.fotannouncer;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;


import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class News extends AppCompatActivity {
    private ImageButton account;
    private ImageButton devinfo;
    ViewPager2 viewPager2;
    viewpageadapter viewpageadapter;

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_news);
        bottomNavigationView=findViewById(R.id.bottomNavigationView);
        viewPager2 = findViewById(R.id.fragment_container);
        viewpageadapter = new viewpageadapter( this);
        viewPager2.setAdapter(viewpageadapter);

        // CHANGE THIS LINE: from setOnItemReselectedListener to setOnItemSelectedListener
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) { // Change method name
                int id = item.getItemId();
                if (id == R.id.home) {
                    viewPager2.setCurrentItem(0);
                } else if (id == R.id.sport) {
                    viewPager2.setCurrentItem(1);
                } else if (id == R.id.academic) {
                    viewPager2.setCurrentItem(2);
                } else if (id == R.id.event) {
                    viewPager2.setCurrentItem(3);
                }
                return true; // Important: return true to indicate the item selection was handled
            }

        });
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        bottomNavigationView.getMenu().findItem(R.id.home).setChecked(true);
                        break;
                    case 1:
                        bottomNavigationView.getMenu().findItem(R.id.sport).setChecked(true);
                        break;
                    case 2:
                        bottomNavigationView.getMenu().findItem(R.id.academic).setChecked(true);
                        break;
                    case 3:
                        bottomNavigationView.getMenu().findItem(R.id.event).setChecked(true);
                        break;
                }
                super.onPageSelected(position);
            }
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
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
    }

}