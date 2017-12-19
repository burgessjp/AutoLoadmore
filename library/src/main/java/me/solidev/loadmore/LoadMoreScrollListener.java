package me.solidev.loadmore;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

/**
 *
 * RecyclerView auto load more listener when scroll to bottom.
 *
 * @author _SOLID
 */
abstract class LoadMoreScrollListener extends RecyclerView.OnScrollListener {

    private int previousTotal;
    private boolean isLoading = true;
    private LinearLayoutManager lm;
    private StaggeredGridLayoutManager sm;
    private int[] lastPositions;
    private int totalItemCount;
    private int lastVisibleItemPosition;

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
            lm = (LinearLayoutManager) recyclerView.getLayoutManager();
        } else if (recyclerView.getLayoutManager() instanceof StaggeredGridLayoutManager) {
            sm = (StaggeredGridLayoutManager) recyclerView.getLayoutManager();
            lastPositions = sm.findLastVisibleItemPositions(null);
        }

        int visibleItemCount = recyclerView.getChildCount();
        if (lm != null) {
            totalItemCount = lm.getItemCount();
            lastVisibleItemPosition = lm.findLastVisibleItemPosition();
        } else if (sm != null) {
            totalItemCount = sm.getItemCount();
            lastVisibleItemPosition = lastPositions[0];
        }

        if (isLoading) {
            if (totalItemCount > previousTotal) {
                //load more end
                isLoading = false;
                previousTotal = totalItemCount;
            } else if (totalItemCount < previousTotal) {
                //refresh end
                previousTotal = totalItemCount;
                isLoading = false;
            }


        }
        if (!isLoading
                && visibleItemCount > 0
                && totalItemCount - 1 == lastVisibleItemPosition
                && recyclerView.getScrollState() == RecyclerView.SCROLL_STATE_IDLE) {
            loadMore();
        }

    }


    /**
     * load more action
     */
    public abstract void loadMore();
}
