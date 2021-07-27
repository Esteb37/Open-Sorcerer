package com.example.opensorcerer.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.opensorcerer.models.User;
import com.example.opensorcerer.ui.main.projects.CreatedProjectsFragment;
import com.example.opensorcerer.ui.main.projects.FavoriteProjectsFragment;

/**
 * Pager adapter for the projects pager view
 */
public class ProjectsPagerAdapter extends FragmentStateAdapter {

    /**
     * Number of pages in this pager view
     */
    private static final int PAGE_COUNT = 2;

    /**
     * The user whose projects to show
     */
    private final User mUser;

    public ProjectsPagerAdapter(Fragment fragment, User user) {
        super(fragment);
        mUser = user;
    }

    /**
     * Returns a Grid fragment as first page and a Linear fragment as second page
     */
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return position == 0
                ? new CreatedProjectsFragment(mUser)
                : new FavoriteProjectsFragment(mUser);

    }

    /**
     * Getter for amount of pages in pager view
     */
    @Override
    public int getItemCount() {
        return PAGE_COUNT;
    }
}
