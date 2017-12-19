package me.solidev.autoloadmore;

import android.app.Application;

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
//        LoadMoreConfig.setGlobalLoadFailedView(R.layout.custom_load_failed);
//        LoadMoreConfig.setGlobalLoadingView(R.layout.custom_loading);
    }
}
