package me.solidev.loadmore;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * Load more wrap adapter for RecyclerView.
 *
 * @author _SOLID
 */
@SuppressWarnings("unused")
public class AutoLoadMoreAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM_TYPE_LOAD_FAILED_VIEW = Integer.MAX_VALUE - 1;
    private static final int ITEM_TYPE_NO_MORE_VIEW = Integer.MAX_VALUE - 2;
    private static final int ITEM_TYPE_LOAD_MORE_VIEW = Integer.MAX_VALUE - 3;
    private static final int ITEM_TYPE_NO_VIEW = Integer.MAX_VALUE - 4;

    private RecyclerView.Adapter mInnerAdapter;
    private AutoLoadMoreScrollListener mLoadMoreScrollListener;

    private int mCurrentItemType = ITEM_TYPE_LOAD_MORE_VIEW;
    private boolean isLoadError = false;
    private boolean isLoading = false;
    private boolean isDisabled = false;
    private boolean isLoadCompleted = false;
    private AutoLoadMoreConfig config;

    private View mLoadMoreView;
    private View mLoadMoreFailedView;
    private View mNoMoreView;
    private final LayoutInflater mInflater;

    public AutoLoadMoreAdapter(Context context, RecyclerView.Adapter adapter) {
        this.mInnerAdapter = adapter;
        mInflater = LayoutInflater.from(context);
        mLoadMoreScrollListener = new AutoLoadMoreScrollListener() {
            @Override
            public void loadMore() {
                if (isDisabled || isLoading || isLoadCompleted || isLoadError) {
                    return;
                }
                if (mOnLoadListener != null) {
                    showLoadMore();
                    mOnLoadListener.onLoadMore();
                    isLoading = true;
                }
            }
        };
        config = AutoLoadMoreConfig.defaultBuilder().create();
    }

    public void setConfig(AutoLoadMoreConfig config) {
        this.config = config;
    }

    public void finishLoading() {
        isLoading = false;
    }

    public void showLoadMore() {
        mCurrentItemType = ITEM_TYPE_LOAD_MORE_VIEW;
        isLoadError = false;
        isLoadCompleted = false;
        isLoading = false;
        notifyItemChanged(getItemCount());
    }

    public void showLoadError() {
        mCurrentItemType = ITEM_TYPE_LOAD_FAILED_VIEW;
        isLoadError = true;
        isLoadCompleted = false;
        isLoading = false;
        notifyItemChanged(getItemCount());
    }

    public void showLoadComplete() {
        mCurrentItemType = ITEM_TYPE_NO_MORE_VIEW;
        isLoadError = false;
        isLoadCompleted = true;
        isLoading = false;
        notifyItemChanged(getItemCount());
    }

    /**
     * disable auto loadmore when unnecessary
     */
    public void disable() {
        mCurrentItemType = ITEM_TYPE_NO_VIEW;
        isDisabled = true;
        notifyDataSetChanged();
    }

    private View inflateView(@LayoutRes int layoutId) {
        View view = mInflater.inflate(layoutId, null);
        view.setLayoutParams(new ViewGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT));
        return view;
    }

    private ViewHolder getLoadMoreViewHolder() {
        if (mLoadMoreView == null) {
            mLoadMoreView = inflateView(config.loadingView);
        }
        return new ViewHolder(mLoadMoreView);
    }


    private ViewHolder getLoadFailedViewHolder() {
        if (mLoadMoreFailedView == null) {
            mLoadMoreFailedView = inflateView(config.loadFailedView);
        }
        return new ViewHolder(mLoadMoreFailedView);
    }

    private ViewHolder getNoMoreViewHolder() {
        if (mNoMoreView == null) {
            mNoMoreView = inflateView(config.loadFinishView);
        }
        return new ViewHolder(mNoMoreView);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1 && !isDisabled) {
            return mCurrentItemType;
        }
        return mInnerAdapter.getItemViewType(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE_NO_MORE_VIEW) {
            return getNoMoreViewHolder();
        } else if (viewType == ITEM_TYPE_LOAD_MORE_VIEW) {
            return getLoadMoreViewHolder();
        } else if (viewType == ITEM_TYPE_LOAD_FAILED_VIEW) {
            return getLoadFailedViewHolder();
        }
        return mInnerAdapter.onCreateViewHolder(parent, viewType);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == ITEM_TYPE_LOAD_FAILED_VIEW) {
            mLoadMoreFailedView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnLoadListener != null) {
                        mOnLoadListener.onRetry();
                        showLoadMore();
                        isLoading = true;
                    }
                }
            });
            return;
        }
        if (!isFooterType(holder.getItemViewType())) {
            mInnerAdapter.onBindViewHolder(holder, position);
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        mInnerAdapter.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();

        if (layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            final GridLayoutManager.SpanSizeLookup spanSizeLookup = gridLayoutManager.getSpanSizeLookup();

            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (position == getItemCount() - 1 && !isDisabled) {
                        return gridLayoutManager.getSpanCount();
                    }
                    if (spanSizeLookup != null && !isDisabled) {
                        return spanSizeLookup.getSpanSize(position);
                    }
                    return 1;
                }
            });
            gridLayoutManager.setSpanCount(gridLayoutManager.getSpanCount());
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
        }
        recyclerView.addOnScrollListener(mLoadMoreScrollListener);
    }


    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        if (isDisabled) {
            return;
        }
        if (holder.getLayoutPosition() == getItemCount() - 1) {
            ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
            if (lp != null
                    && lp instanceof StaggeredGridLayoutManager.LayoutParams) {
                StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
                p.setFullSpan(true);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mInnerAdapter.getItemCount() + (!isDisabled ? 1 : 0);
    }

    private boolean isFooterType(int type) {

        return type == ITEM_TYPE_NO_VIEW ||
                type == ITEM_TYPE_LOAD_FAILED_VIEW ||
                type == ITEM_TYPE_NO_MORE_VIEW ||
                type == ITEM_TYPE_LOAD_MORE_VIEW;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ViewHolder(View itemView) {
            super(itemView);
        }
    }


    public interface OnLoadListener {
        /**
         * retry
         */
        void onRetry();

        /**
         * load more
         */
        void onLoadMore();
    }

    private OnLoadListener mOnLoadListener;

    public void setOnLoadListener(OnLoadListener onLoadListener) {
        this.mOnLoadListener = onLoadListener;
    }

}
