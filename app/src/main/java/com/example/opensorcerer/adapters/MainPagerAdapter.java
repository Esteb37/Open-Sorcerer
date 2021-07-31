package com.example.opensorcerer.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.opensorcerer.models.Project;
import com.example.opensorcerer.models.User;
import com.example.opensorcerer.ui.main.MainFragment;
import com.example.opensorcerer.ui.main.details.DetailsFragment;

public class MainPagerAdapter extends FragmentStateAdapter {

    /**
     * Number of pages in this pager view
     */
    private static int PAGE_COUNT = 2;

    /**
     * Fragment that holds home timeline
     */
    private MainFragment mMainFragment;

    /**
     * Fragment that displays project details
     */
    private DetailsFragment mDetailsFragment;

    public MainPagerAdapter(FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    /**
     * Returns a Main fragment as first page and Details Fragment as second page
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

    /**
     * Gets the project currently displayed to the user
     */
    private Project getCurrentProject() {
        return mMainFragment.getCurrentProject();
    }

    /**
     * Updates the project being displayed in the details fragment
     */
    public void updateProject() {
        mDetailsFragment.updateProject(getCurrentProject());
    }

    /**
     * Hides the Details page to prevent scrolling when outside the home fragment
     */
    public void hideDetailsFragment() {
        PAGE_COUNT = 1;
    }

    /**
     * Shows the Details page again
     */
    public void showDetailsFragment() {
        PAGE_COUNT = 2;
    }

    /**
     * Determines if the fragment currently being displayed in the Details fragment is the Information
     */
    public boolean isInformationFragmentVisible() {
        return mDetailsFragment != null && mDetailsFragment.isInformationFragmentVisible();
    }

    /**
     * Adds one to the current project's count of user swipes to view details
     */
    public void addSwipeToProject() {
        Project currentProject = getCurrentProject();

        //Update the user's behavior
        User.getCurrentUser().registerSwipedProject(currentProject);

        //Add one to the project's swipe count
        currentProject.addSwipe();
    }
}
