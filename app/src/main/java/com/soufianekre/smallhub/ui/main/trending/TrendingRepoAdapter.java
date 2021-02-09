package com.soufianekre.smallhub.ui.main.trending;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.soufianekre.smallhub.R;
import com.soufianekre.smallhub.data.models.TrendingModel;
import com.soufianekre.smallhub.ui.adapters.view_holder.TrendingRepoViewHolder;
import com.soufianekre.smallhub.ui.widgets.recycler_view.BaseRecyclerAdapter;
import com.soufianekre.smallhub.ui.widgets.recycler_view.BaseViewHolder;

import java.util.List;

public class TrendingRepoAdapter extends BaseRecyclerAdapter<TrendingModel,TrendingRepoViewHolder,
        BaseViewHolder.OnItemClickListener<TrendingModel>> {

    private Context mContext;
    private List<TrendingModel> trendingModels;

    public TrendingRepoAdapter(Context mContext, List<TrendingModel> trendingModelList) {
        super(trendingModelList);
        this.mContext = mContext;
        this.trendingModels = trendingModelList;
    }

    @Override
    protected TrendingRepoViewHolder viewHolder(ViewGroup parent, int viewType) {
        return TrendingRepoViewHolder.newInstance(parent,this);
    }

    @Override
    protected void onBindView(TrendingRepoViewHolder holder, int position) {
        holder.bind(getItem(position));
    }
    @Override
    public int getItemCount() {
        return trendingModels.size();
    }


}
