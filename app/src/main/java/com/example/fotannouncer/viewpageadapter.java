package com.example.fotannouncer;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class viewpageadapter extends FragmentStateAdapter {
    public viewpageadapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch(position){
            case 0: return new home();
            case 1:return  new sport();
            case 2: return new accadamic();
            case 3 : return new event();
            default:return new home();
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
