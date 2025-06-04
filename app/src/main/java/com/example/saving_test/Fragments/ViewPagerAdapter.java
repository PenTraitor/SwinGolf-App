package com.example.saving_test.Fragments;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ViewPagerAdapter extends FragmentStateAdapter {

    public ViewPagerAdapter(FragmentActivity fa) {
        super(fa);
    }

    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0: return new SpielerFragment();
            case 1: return new AnlageFragment();
            case 2: return new TurnierFragment();
            default: return new SpielerFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
