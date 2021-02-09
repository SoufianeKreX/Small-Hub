package com.soufianekre.smallhub.ui.adapters;

import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.soufianekre.smallhub.data.network.model.User;
import com.soufianekre.smallhub.ui.adapters.view_holder.UsersViewHolder;
import com.soufianekre.smallhub.ui.widgets.recycler_view.BaseRecyclerAdapter;
import com.soufianekre.smallhub.ui.widgets.recycler_view.BaseViewHolder;

import java.util.ArrayList;

public class UsersAdapter extends BaseRecyclerAdapter<User, UsersViewHolder, BaseViewHolder.OnItemClickListener<User>> {

    private boolean isContributor;
    private boolean isFilter;

    public UsersAdapter(@NonNull ArrayList<User> list) {
        this(list, false);
    }

    public UsersAdapter(@NonNull ArrayList<User> list, boolean isContributor) {
        this(list, isContributor, false);
    }

    public UsersAdapter(@NonNull ArrayList<User> list, boolean isContributor, boolean isFilter) {
        super(list);
        this.isContributor = isContributor;
        this.isFilter = isFilter;
    }

    @Override protected UsersViewHolder viewHolder(ViewGroup parent, int viewType) {
        return UsersViewHolder.newInstance(parent, this, isFilter);
    }

    @Override protected void onBindView(UsersViewHolder holder, int position) {
        holder.bind(getItem(position), isContributor);
    }
}