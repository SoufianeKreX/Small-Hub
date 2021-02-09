package com.soufianekre.smallhub.ui.adapters;

import android.view.ViewGroup;

import com.soufianekre.smallhub.data.network.model.User;
import com.soufianekre.smallhub.ui.adapters.view_holder.ProfileOrgsViewHolder;
import com.soufianekre.smallhub.ui.widgets.recycler_view.BaseRecyclerAdapter;
import com.soufianekre.smallhub.ui.widgets.recycler_view.BaseViewHolder;

public class ProfileOrgsAdapter extends BaseRecyclerAdapter<User, ProfileOrgsViewHolder, BaseViewHolder.OnItemClickListener<User>> {

    @Override protected ProfileOrgsViewHolder viewHolder(ViewGroup parent, int viewType) {
        return ProfileOrgsViewHolder.newInstance(parent);
    }

    @Override protected void onBindView(ProfileOrgsViewHolder holder, int position) {
        holder.bind(getItem(position));
    }
}
