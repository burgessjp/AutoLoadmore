package me.solidev.loadmore;

import android.support.annotation.LayoutRes;

/**
 *
 * load more global config
 *
 * @author _SOLID
 */

public class LoadMoreConfig {
    static int loadingView = R.layout.auto_loadmore_loading;
    static int loadFailedView = R.layout.auto_loadmore_load_failed;
    static int loadFinishView = R.layout.auto_loadmore_load_finish;

    public static void setLoadingView(@LayoutRes int loadingView) {
        LoadMoreConfig.loadingView = loadingView;
    }

    public static void setLoadFailedView(@LayoutRes int loadFailedView) {
        LoadMoreConfig.loadFailedView = loadFailedView;
    }

    public static void setLoadFinishView(@LayoutRes int loadFinishView) {
        LoadMoreConfig.loadFinishView = loadFinishView;
    }
}
