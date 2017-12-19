package me.solidev.autoloadmore;

import android.os.Bundle;
import android.os.Handler;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //mRecyclerView.setLayoutManager(new GridLayoutManager(this,2));
        list = new ArrayList<>();
        initData();
        MyAdapter myAdapter = new MyAdapter(this, list);
        mAutoLoadMoreAdapter = new AutoLoadMoreAdapter(this, myAdapter);
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
        mRecyclerView.setAdapter(mAutoLoadMoreAdapter);


    }

    private void initData() {
        for (int i = 0; i < 10; i++) {
            DemoBean demoBean = new DemoBean();
            demoBean.setTitle("Title " + i);
            demoBean.setDesc("Desc " + i);
            list.add(demoBean);
        }
        pageIndex++;
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
            }
        }, 1000);
    }
}
