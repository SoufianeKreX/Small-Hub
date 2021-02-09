package com.soufianekre.smallhub.ui.adapters.view_holder;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.soufianekre.smallhub.R;
import com.soufianekre.smallhub.data.network.model.User;
import com.soufianekre.smallhub.ui.widgets.AvatarLayout;
import com.soufianekre.smallhub.ui.widgets.recycler_view.BaseViewHolder;

import org.jetbrains.annotations.NotNull;

import butterknife.BindView;

public class ProfileOrgsViewHolder extends BaseViewHolder<User> {

    @BindView(R.id.avatarLayout)
    AvatarLayout avatarLayout;
    @BindView(R.id.name)
    TextView name;

    @Override public void onClick(View v) {
        avatarLayout.callOnClick();
    }

    private ProfileOrgsViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    public static ProfileOrgsViewHolder newInstance(@NonNull ViewGroup parent) {
        return new ProfileOrgsViewHolder(getView(parent ,R.layout.row_item_user_overview_orgs));
    }

    @Override public void bind(@NotNull @NonNull User user) {
        name.setText(user.getLogin());
        avatarLayout.setUrl(user.getAvatarUrl(), user.getLogin(), true, false);
    }
}
