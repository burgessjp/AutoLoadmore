[ ![Download](https://api.bintray.com/packages/solid/maven/AutoLoadmore/images/download.svg) ](https://bintray.com/solid/maven/AutoLoadmore/_latestVersion)
Auto load more for RecyclerView
### Download
 ```html
  compile 'me.solidev.library:status-view-layout:latestVersion'
 ```

### Usage
Wrap your adapter use LoadMoreAdapter
```
 MyAdapter myAdapter = new MyAdapter(this, list);
 mAutoLoadMoreAdapter = new AutoLoadMoreAdapter(this, myAdapter);
 mAutoLoadMoreAdapter.setOnLoadListener(new AutoLoadMoreAdapter.OnLoadListener() {
            @Override
            public void onRetry() {
               //do retry
            }

            @Override
            public void onLoadMore() {
                //do load more
            }
        });
  mRecyclerView.setAdapter(mAutoLoadMoreAdapter);
```

When load more successfully you should call ` mAutoLoadMoreAdapter.finishLoading()`