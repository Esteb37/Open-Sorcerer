package com.example.opensorcerer.models;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import org.jetbrains.annotations.NotNull;

/**
 * Scroll listener for handling endless scrolling in recyclerviews
 * Retrieved from:
 * Title: Endless Scrolling with AdapterViews and RecyclerView
 * Author: CodePath
 * Availability: https://guides.codepath.org/android/Endless-Scrolling-with-AdapterViews-and-RecyclerView
 */
@SuppressWarnings("unused")
public abstract class EndlessRecyclerViewScrollListener extends RecyclerView.OnScrollListener {

    /**
     * Sets the starting page index.
     */
    private final int startingPageIndex = 0;

    /**
     * Layout manager of the listened recyclerview.
     */
    private final RecyclerView.LayoutManager mLayoutManager;

    /**
     * The minimum amount of items to have below the current scroll position
     * before loading more.
     */
    private int visibleThreshold = 5;

    /**
     * The current offset index of loaded data.
     */
    private int currentPage = 0;

    /**
     * The total number of items in the dataset after the last load.
     */
    private int previousTotalItemCount = 0;

    /**
     * True if we are still waiting for the last set of data to load.
     */
    private boolean loading = true;

    /**
     * Constructor for RecyclerViews with Linear Layouts
     *
     * @param layoutManager The recyclerview's linear layout manager
     */
    public EndlessRecyclerViewScrollListener(LinearLayoutManager layoutManager) {
        this.mLayoutManager = layoutManager;
    }

    /**
     * Constructor for RecyclerViews with Grid Layouts
     *
     * @param layoutManager The recyclerview's grid layout manager
     */
    public EndlessRecyclerViewScrollListener(GridLayoutManager layoutManager) {
        this.mLayoutManager = layoutManager;
        visibleThreshold = visibleThreshold * layoutManager.getSpanCount();
    }

    /**
     * Constructor for RecyclerViews with Staggered Grid Layouts
     *
     * @param layoutManager The recyclerview's staggered grid layout manager
     */
    public EndlessRecyclerViewScrollListener(StaggeredGridLayoutManager layoutManager) {
        this.mLayoutManager = layoutManager;
        visibleThreshold = visibleThreshold * layoutManager.getSpanCount();
    }

    /**
     * Returns the last item visible to the user
     */
    public int getLastVisibleItem(int[] lastVisibleItemPositions) {
        int maxSize = 0;
        for (int i = 0; i < lastVisibleItemPositions.length; i++) {
            if (i == 0) {
                maxSize = lastVisibleItemPositions[i];
            } else if (lastVisibleItemPositions[i] > maxSize) {
                maxSize = lastVisibleItemPositions[i];
            }
        }
        return maxSize;
    }

    @Override
    public void onScrolled(@NotNull RecyclerView view, int dx, int dy) {
        int lastVisibleItemPosition = 0;
        int totalItemCount = mLayoutManager.getItemCount();

        if (mLayoutManager instanceof StaggeredGridLayoutManager) {
            int[] lastVisibleItemPositions = ((StaggeredGridLayoutManager) mLayoutManager).findLastVisibleItemPositions(null);
            // get maximum element within the list
            lastVisibleItemPosition = getLastVisibleItem(lastVisibleItemPositions);
        } else if (mLayoutManager instanceof GridLayoutManager) {
            lastVisibleItemPosition = ((GridLayoutManager) mLayoutManager).findLastVisibleItemPosition();
        } else if (mLayoutManager instanceof LinearLayoutManager) {
            lastVisibleItemPosition = ((LinearLayoutManager) mLayoutManager).findLastVisibleItemPosition();
        }

        // If the total item count is zero and the previous isn't, assume the
        // list is invalidated and should be reset back to initial state
        if (totalItemCount < previousTotalItemCount) {
            this.currentPage = this.startingPageIndex;
            this.previousTotalItemCount = totalItemCount;
            if (totalItemCount == 0) {
                this.loading = true;
            }
        }

        // If it’s still loading, we check to see if the dataset count has
        // changed, if so we conclude it has finished loading and update the current page
        // number and total item count.
        if (loading && (totalItemCount > previousTotalItemCount)) {
            loading = false;
            previousTotalItemCount = totalItemCount;
        }

        // If it isn’t currently loading, we check to see if we have breached
        // the visibleThreshold and need to reload more data.
        // If we do need to reload some more data, we execute onLoadMore to fetch the data.
        // threshold should reflect how many total columns there are too
        if (!loading && (lastVisibleItemPosition + visibleThreshold) > totalItemCount) {
            currentPage++;
            onLoadMore(currentPage, totalItemCount, view);
            loading = true;
        }
    }

    /**
     * Resets the state of the endless scroll listener
     */
    public void resetState() {
        this.currentPage = this.startingPageIndex;
        this.previousTotalItemCount = 0;
        this.loading = true;
    }

    /**
     * Defines the process for actually loading more data based on page
     */
    public abstract void onLoadMore(int page, int totalItemsCount, RecyclerView view);
}