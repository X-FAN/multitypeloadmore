package com.xf.multitypeloadmore;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;

public class MainActivity extends AppCompatActivity implements LoadMoreDelegate.OnLoadMoreListener {
    private Items mItems = new Items();

    private LoadMoreDelegate mLoadMoreDelegate;
    @Bind(R.id.show)
    RecyclerView mShow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        for (int i = 0; i < 20; i++) {
            mItems.add("data***" + i);
        }


        MultiTypeAdapter multiTypeAdapter = new MultiTypeAdapter(mItems);
        multiTypeAdapter.register(String.class, new NormalViewProvider());
        multiTypeAdapter.register(LoadMore.class, new LoadMoreViewProvider());
        mShow.setLayoutManager(new LinearLayoutManager(this));
        mShow.setAdapter(multiTypeAdapter);
        mLoadMoreDelegate = new LoadMoreDelegate(multiTypeAdapter, mItems, this);
        mLoadMoreDelegate.attach(mShow);
    }

    @Override
    public void onLoadMore() {
        getMore();
    }

    private void getMore() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Items strings = new Items();
                            for (int i = 0; i < 20; i++) {
                                strings.add("data***more" + i);
                            }
                            mLoadMoreDelegate.addData(strings);
                            mLoadMoreDelegate.loadMoreComplete();
                        }
                    });

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


            }
        }).start();
    }
}
