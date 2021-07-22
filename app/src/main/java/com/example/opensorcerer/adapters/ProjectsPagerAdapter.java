package com.example.opensorcerer.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.opensorcerer.ui.main.projects.CreatedProjectsFragment;
import com.example.opensorcerer.ui.main.projects.FavoritesGridFragment;

/**
 * Pager adapter for the projects pager view
 */
public class ProjectsPagerAdapter extends FragmentStateAdapter {

    private static final int PAGE_COUNT = 2;

    public ProjectsPagerAdapter(Fragment fragment) {
        super(fragment);
    }

    /**
     * Returns a Grid fragment as first page and a Linear fragment as second page
     */
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return position == 0
                ? new CreatedProjectsFragment()
                : new FavoritesGridFragment();

    }

    /**Getter for amount of pages in pager view*/
    @Override
    public int getItemCount() {
        return PAGE_COUNT;
    }
}
