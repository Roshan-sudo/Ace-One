package com.ace.services.one.capital.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;

public class LoanDetailsFragmentAdapter extends FragmentStateAdapter {
    private final ArrayList<Fragment> fragments = new ArrayList<>();

    public LoanDetailsFragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragments.get(position);
    }

    @Override
    public int getItemCount() {
        return fragments.size();
    }

    /**
     * Function to add fragments to the ArrayList
     * @param fragment fragment to display in ViewPager
     */
    public void addFragment(Fragment fragment){
        fragments.add(fragment);
    }
}
