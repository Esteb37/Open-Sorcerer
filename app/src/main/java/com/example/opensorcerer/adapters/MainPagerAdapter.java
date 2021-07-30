package com.example.opensorcerer.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.opensorcerer.models.Project;
import com.example.opensorcerer.models.User;
import com.example.opensorcerer.ui.main.MainFragment;
import com.example.opensorcerer.ui.main.details.DetailsFragment;
import com.example.opensorcerer.ui.main.projects.CreatedProjectsFragment;
import com.example.opensorcerer.ui.main.projects.FavoriteProjectsFragment;

public class MainPagerAdapter extends FragmentStateAdapter {

    /**
     * Number of pages in this pager view
     */
    private static int PAGE_COUNT = 2;

    private MainFragment mMainFragment;

    private DetailsFragment mDetailsFragment;

    private Project mProject;

    public MainPagerAdapter(FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    /**
     * Returns a Grid fragment as first page and a Linear fragment as second page
     */
    @NonNull
    @Override
    public Fragment createFragment(int position) {

        if (position == 0)
            mMainFragment = new MainFragment();
        else
            mDetailsFragment = new DetailsFragment(null);

        return position == 0
                ? mMainFragment
                : mDetailsFragment;
    }

    /**
     * Getter for amount of pages in pager view
     */
    @Override
    public int getItemCount() {
        return PAGE_COUNT;
    }

    private Project getCurrentProject(){
        return mMainFragment.getCurrentProject();
    }

    public void updateProject() {
        mDetailsFragment.updateProject(getCurrentProject());
    }

    public void hideDetailsFragment(){
        PAGE_COUNT = 1;
    }

    public void showDetailsFragment(){
        PAGE_COUNT = 2;
    }

    public boolean isInformationFragmentVisible() {
        return mDetailsFragment != null && mDetailsFragment.isInformationFragmentVisible();
    }

    public void addSwipeToProject() {
        getCurrentProject().addSwipe();
    }
}
