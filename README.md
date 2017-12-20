[ ![Download](https://api.bintray.com/packages/solid/maven/AutoLoadmore/images/download.svg) ](https://bintray.com/solid/maven/AutoLoadmore/_latestVersion)

Auto load more for `RecyclerView` when scroll to bottom.

### Download
 ```html
  compile 'me.solidev.library:auto-loadmore:latestVersion'
 ```

### Usage
Wrap your adapter use AutoLoadMoreAdapter
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

**You can set custom status view also.**

- for global

 ```
 AutoLoadMoreConfig.setLoadFailedView(R.layout.custom_load_failed);
 AutoLoadMoreConfig.setLoadingView(R.layout.custom_loading);
 ```
- for specific

  ```
  mAutoLoadMoreAdapter.setConfig(new AutoLoadMoreConfig
                  .Builder()
                  .loadingView(R.layout.custom_loading)
                  .loadFailedView(R.layout.custom_load_failed)
                  .create());
  ```

### Other API
- disable load: `mAutoLoadMoreAdapter.disable();`
- load error: `mAutoLoadMoreAdapter.showLoadError()`
- load finish: `mAutoLoadMoreAdapter.showLoadComplete()`

### LICENSE

```html
Copyright [2017] [_SOLID]

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

```