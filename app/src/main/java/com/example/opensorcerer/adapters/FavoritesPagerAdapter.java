package com.example.opensorcerer.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.opensorcerer.ui.developer.fragments.FavoritesFragmentGrid;
import com.example.opensorcerer.ui.developer.fragments.FavoritesFragmentLinear;

/**
 * Pager adapter for the Favorites pager view
 */
public class FavoritesPagerAdapter extends FragmentStateAdapter {

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
                ? new FavoritesFragmentGrid()
                : new FavoritesFragmentLinear();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
