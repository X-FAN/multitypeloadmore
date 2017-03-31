package com.xf.multitypeloadmore;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;

/**
 * Created by X-FAN on 2017/3/21.
 */

public class LoadMoreDelegate {

    private Items mItems;

    private MultiTypeAdapter mMultiTypeAdapter;
    private OnLoadMoreListener mOnLoadMoreListener;
    private ScrollListener mScrollListener;


    public LoadMoreDelegate(MultiTypeAdapter multiTypeAdapter, Items items, OnLoadMoreListener onLoadMoreListener) {
        mMultiTypeAdapter = multiTypeAdapter;
        mItems = items;
        this.mOnLoadMoreListener = onLoadMoreListener;
    }

    public void attach(RecyclerView recyclerView) {
        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        mScrollListener = new ScrollListener(mMultiTypeAdapter, mItems, linearLayoutManager, mOnLoadMoreListener);
        recyclerView.addOnScrollListener(mScrollListener);
    }

    public void loadMoreComplete() {
        mScrollListener.setLoading(false);
    }

    public void addData(Items items) {
        int originSize = mItems.size() - 1;
        mItems.remove(originSize);//删除"加载更多"
        mItems.addAll(items);//添加新数据
        //从最后的位置插入新数据
        mMultiTypeAdapter.notifyItemRangeInserted(originSize, items.size() - 1);
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    private static class ScrollListener extends RecyclerView.OnScrollListener {
        private final int size = 2;
        private boolean mLoading = false;
        private Items mItems;
        private final LoadMore mLoadMore = new LoadMore();

        private LinearLayoutManager mLinearLayoutManager;
        private OnLoadMoreListener mOnLoadMoreListener;
        private MultiTypeAdapter mMultiTypeAdapter;

        ScrollListener(MultiTypeAdapter multiTypeAdapter, Items datas, LinearLayoutManager linearLayoutManager, OnLoadMoreListener onLoadMoreListener) {
            mItems = datas;
            mMultiTypeAdapter = multiTypeAdapter;
            this.mLinearLayoutManager = linearLayoutManager;
            this.mOnLoadMoreListener = onLoadMoreListener;
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            if (dy < 0) {//下滑忽略
                return;
            }
            int totalNum = mLinearLayoutManager.getItemCount();
            int lastVisiblePosition = mLinearLayoutManager.findLastCompletelyVisibleItemPosition();
            if (!mLoading && lastVisiblePosition == totalNum - size) {//最后可见的view的位置为倒数第size个,触发加载更多
                mLoading = true;
                mItems.add(mLoadMore);
                mMultiTypeAdapter.notifyItemInserted(mItems.size() - 1);
                mOnLoadMoreListener.onLoadMore();
            }
        }

        void setLoading(boolean loading) {
            this.mLoading = loading;
        }
    }

}
