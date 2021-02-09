package com.soufianekre.smallhub.ui.main;

import com.soufianekre.smallhub.data.models.TrendingModel;
import com.soufianekre.smallhub.ui.base.BaseMvpPresenter;
import com.soufianekre.smallhub.ui.base.BaseMvpView;
import com.soufianekre.smallhub.ui.widgets.recycler_view.BaseViewHolder;

import java.util.List;

interface MainMvp {
    interface Presenter<V extends View>  extends BaseMvpPresenter<V>,
            BaseViewHolder.OnItemClickListener<TrendingModel> {
        void onCallApi(String lang,String since);
        List<TrendingModel> getTrendingRepos();
    }

    interface View extends BaseMvpView {
        void onAppendNavMenuItem(String param, int color);
        void showProgress();
        void hideProgress();
        void onNotifyAdapter(List<TrendingModel> trendingModels);

        void clearNavMenu();

        void clearAdapter();

        void showTrendingRepo(int position, TrendingModel item);
    }
}
