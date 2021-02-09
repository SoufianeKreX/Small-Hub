package com.soufianekre.smallhub.data.network.loadmore;

import androidx.annotation.Nullable;


import com.soufianekre.smallhub.ui.base.PaginationListener;
import com.soufianekre.smallhub.ui.widgets.recycler_view.scroll.InfiniteScroll;


public class OnLoadMore<P> extends InfiniteScroll {

    private PaginationListener<P> presenter;
    @Nullable private P parameter;

    public OnLoadMore(PaginationListener<P> presenter) {
        this(presenter, null);
    }

    public OnLoadMore(PaginationListener<P> presenter, @Nullable P parameter) {
        super();
        this.presenter = presenter;
        this.parameter = parameter;
    }


    public void setParameter(@Nullable P parameter) {
        this.parameter = parameter;
    }

    @Nullable public P getParameter() {
        return parameter;
    }

    @Override public boolean onLoadMore(int page, int totalItemsCount) {
        if (presenter != null) {
            presenter.setPreviousTotal(totalItemsCount);
            return presenter.onCallApi(page + 1, parameter);
        }
        return false;
    }
}