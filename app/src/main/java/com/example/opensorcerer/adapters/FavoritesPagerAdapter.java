package com.example.opensorcerer.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.opensorcerer.ui.main.projects.FavoritesGridFragment;
import com.example.opensorcerer.ui.main.projects.FavoritesFragmentLinear;

/**
 * Pager adapter for the Favorites pager view
 */
public class FavoritesPagerAdapter extends FragmentStateAdapter {

    private static final int PAGE_COUNT = 2;

    public FavoritesPagerAdapter(Fragment fragment) {
        super(fragment);
    }

    /**
     * Returns a Grid fragment as first page and a Linear fragment as second page
     */
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return position == 0
                ? new FavoritesGridFragment()
                : new FavoritesFragmentLinear();
    }

    /**Getter for amount of pages in pager view*/
    @Override
    public int getItemCount() {
        return PAGE_COUNT;
    }
}
