package com.soufianekre.smallhub.ui.adapters.view_holder;


import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.soufianekre.smallhub.R;
import com.soufianekre.smallhub.data.network.model.User;
import com.soufianekre.smallhub.ui.widgets.AvatarLayout;
import com.soufianekre.smallhub.ui.widgets.recycler_view.BaseRecyclerAdapter;
import com.soufianekre.smallhub.ui.widgets.recycler_view.BaseViewHolder;

import org.jetbrains.annotations.NotNull;

import butterknife.BindView;

public class UsersViewHolder extends BaseViewHolder<User> {

    @BindView(R.id.avatarLayout)
    AvatarLayout avatar;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.date)
    TextView date;

    private boolean isFilter;

    private UsersViewHolder(@NonNull View itemView, @Nullable BaseRecyclerAdapter adapter, boolean isFilter) {
        super(itemView, adapter);
        this.isFilter = isFilter;
    }

    public static UsersViewHolder newInstance(@NonNull ViewGroup parent, @Nullable BaseRecyclerAdapter adapter, boolean isFilter) {
        return new UsersViewHolder(getView(parent,  R.layout.row_item_users_small), adapter, isFilter);
    }

    @Override public void onClick(View v) {
        if (isFilter) {
            super.onClick(v);
        } else {
            avatar.findViewById(R.id.avatar).callOnClick();
        }
    }

    @Override public void bind(@NotNull @NonNull User user) {}

    public void bind(@NonNull User user, boolean isContributor) {

        avatar.setUrl(user.getAvatarUrl(), user.getLogin(), false,
                false);

        title.setText(user.getLogin());
        date.setVisibility(!isContributor ? View.GONE : View.VISIBLE);

    }
}

