package me.solidev.loadmore;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;

/**
 * load more global config
 *
 * @author _SOLID
 */

public class AutoLoadMoreConfig {
    private static int globalLoadingView = R.layout.auto_loadmore_loading;
    private static int globalLoadFailedView = R.layout.auto_loadmore_load_failed;
    private static int globalLoadFinishView = R.layout.auto_loadmore_load_finish;
    int loadingView;
    int loadFailedView;
    int loadFinishView;

    private AutoLoadMoreConfig(@NonNull Builder builder) {
        if (builder.loadingView != 0) {
            this.loadingView = builder.loadingView;
        } else {
            this.loadingView = globalLoadingView;
        }
        if (builder.loadFailedView != 0) {
            this.loadFailedView = builder.loadFailedView;
        } else {
            this.loadFailedView = globalLoadFailedView;
        }
        if (builder.loadFinishView != 0) {
            this.loadFinishView = builder.loadFinishView;
        } else {
            this.loadFinishView = globalLoadFinishView;
        }
    }

    public static void setGlobalLoadingView(@LayoutRes int globalLoadingView) {
        AutoLoadMoreConfig.globalLoadingView = globalLoadingView;
    }

    public static void setGlobalLoadFailedView(@LayoutRes int globalLoadFailedView) {
        AutoLoadMoreConfig.globalLoadFailedView = globalLoadFailedView;
    }

    public static void setGlobalLoadFinishView(@LayoutRes int globalLoadFinishView) {
        AutoLoadMoreConfig.globalLoadFinishView = globalLoadFinishView;
    }

    static Builder defaultBuilder() {
        Builder builder = new Builder();
        builder.loadFinishView = globalLoadFinishView;
        builder.loadingView = globalLoadingView;
        builder.loadFailedView = globalLoadFailedView;
        return builder;
    }

    public static class Builder {
        private int loadingView;
        private int loadFailedView;
        private int loadFinishView;

        public Builder loadingView(@LayoutRes int loadingView) {
            this.loadingView = loadingView;
            return this;
        }

        public Builder loadFailedView(@LayoutRes int loadFailedView) {
            this.loadFailedView = loadFailedView;
            return this;
        }

        public Builder loadFinishView(@LayoutRes int loadFinishView) {
            this.loadFinishView = loadFinishView;
            return this;
        }

        public AutoLoadMoreConfig create() {
            return new AutoLoadMoreConfig(this);
        }
    }
}
