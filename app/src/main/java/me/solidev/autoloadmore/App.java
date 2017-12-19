package me.solidev.autoloadmore;

import android.app.Application;

import me.solidev.loadmore.LoadMoreConfig;

/**
 * @author _SOLID
 * @date 2017/12/19 14:19
 * @contact https://github.com/burgessjp
 * @desc
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        LoadMoreConfig.setLoadFailedView(R.layout.custom_load_failed);
        LoadMoreConfig.setLoadingView(R.layout.custom_loading);
    }
}
