package com.example.opensorcerer.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.opensorcerer.ui.main.favorites.FavoritesFragmentGrid;
import com.example.opensorcerer.ui.main.favorites.FavoritesFragmentLinear;

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

    /**Getter for amount of pages in pager view*/
    @Override
    public int getItemCount() {
        return 2;
    }
}
