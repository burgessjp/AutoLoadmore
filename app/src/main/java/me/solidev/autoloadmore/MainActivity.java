package me.solidev.autoloadmore;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import me.solidev.loadmore.AutoLoadMoreAdapter;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private RecyclerView mRecyclerView;
    private List<DemoBean> list;
    private int pageIndex = 0;
    private AutoLoadMoreAdapter mAutoLoadMoreAdapter;
    private SwipeRefreshLayout mSwipeRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        mSwipeRefresh = findViewById(R.id.swipeRefresh);
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //mRecyclerView.setLayoutManager(new GridLayoutManager(this,2));
        list = new ArrayList<>();
        MyAdapter myAdapter = new MyAdapter(this, list);
        mAutoLoadMoreAdapter = new AutoLoadMoreAdapter(this, myAdapter);
//        mAutoLoadMoreAdapter.setConfig(new AutoLoadMoreConfig
//                .Builder()
//                .loadingView(R.layout.custom_loading)
//                .loadFailedView(R.layout.custom_load_failed)
//                .create());
        // mAutoLoadMoreAdapter.disable();
//        mAutoLoadMoreAdapter.showLoadError();
//        mAutoLoadMoreAdapter.showLoadComplete();
        mAutoLoadMoreAdapter.setOnLoadListener(new AutoLoadMoreAdapter.OnLoadListener() {
            @Override
            public void onRetry() {
                Log.i(TAG, "onRetry " + pageIndex);
                mockLoadmore();
            }

            @Override
            public void onLoadMore() {
                Log.i(TAG, "onLoadMore " + pageIndex);
                mockLoadmore();
            }
        });
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mAutoLoadMoreAdapter.showLoadMore();
                initData();
            }
        });
        mRecyclerView.setAdapter(mAutoLoadMoreAdapter);
        initData();

    }

    private void initData() {
        mSwipeRefresh.setRefreshing(true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                list.clear();
                pageIndex = 0;
                for (int i = 0; i < 10; i++) {
                    DemoBean demoBean = new DemoBean();
                    demoBean.setTitle("Title " + i);
                    demoBean.setDesc("Desc " + i);
                    list.add(demoBean);
                }
                mRecyclerView.getAdapter().notifyDataSetChanged();
                mSwipeRefresh.setRefreshing(false);
                pageIndex++;
            }
        }, 1000);

    }

    private void mockLoadmore() {
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                Random r = new Random();
                int n = r.nextInt(5);
                //模拟出错
                boolean isError = false;
                if (n == 2) {
                    isError = true;
                }
                if (isError) {
                    mAutoLoadMoreAdapter.showLoadError();
                } else if (pageIndex >= 10) {
                    mAutoLoadMoreAdapter.showLoadComplete();
                } else {
                    for (int i = pageIndex * 10; i < pageIndex * 10 + 10; i++) {
                        DemoBean demoBean = new DemoBean();
                        demoBean.setTitle("Title " + i);
                        demoBean.setDesc("Desc " + i);
                        list.add(demoBean);
                    }
                    pageIndex++;
                    mAutoLoadMoreAdapter.finishLoading();
                }
                mRecyclerView.getAdapter().notifyDataSetChanged();
                mSwipeRefresh.setRefreshing(false);
            }
        }, 1000);
    }
}
