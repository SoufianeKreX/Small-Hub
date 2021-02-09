package com.soufianekre.smallhub.ui.adapters;

import android.view.ViewGroup;

import com.soufianekre.smallhub.data.network.model.Repo;
import com.soufianekre.smallhub.ui.adapters.view_holder.RepoViewHolder;
import com.soufianekre.smallhub.ui.widgets.recycler_view.BaseRecyclerAdapter;
import com.soufianekre.smallhub.ui.widgets.recycler_view.BaseViewHolder;

import java.util.List;

import io.reactivex.annotations.NonNull;

public class ReposAdapter extends BaseRecyclerAdapter<Repo, RepoViewHolder,
        BaseViewHolder.OnItemClickListener<Repo>> {
    private boolean isStarred;
    private boolean withImage;

    public ReposAdapter(@NonNull List<Repo> data, boolean isStarred) {
        this(data, isStarred, false);
    }

    public ReposAdapter(@NonNull List<Repo> data, boolean isStarred, boolean withImage) {
        super(data);
        this.isStarred = isStarred;
        this.withImage = withImage;
    }

    @Override protected RepoViewHolder viewHolder(ViewGroup parent, int viewType) {
        return RepoViewHolder.newInstance(parent, this, isStarred, withImage);
    }

    @Override protected void onBindView(RepoViewHolder holder, int position) {
        holder.bind(getItem(position));
    }
}
