package com.soufianekre.smallhub.ui.adapters.view_holder;

import android.graphics.Color;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.soufianekre.smallhub.R;
import com.soufianekre.smallhub.data.network.model.Repo;
import com.soufianekre.smallhub.helper.InputHelper;
import com.soufianekre.smallhub.helper.ParseDateFormat;
import com.soufianekre.smallhub.ui.widgets.AvatarLayout;
import com.soufianekre.smallhub.ui.widgets.LabelSpan;
import com.soufianekre.smallhub.ui.widgets.SpannableBuilder;
import com.soufianekre.smallhub.ui.widgets.recycler_view.BaseRecyclerAdapter;
import com.soufianekre.smallhub.ui.widgets.recycler_view.BaseViewHolder;

import org.jetbrains.annotations.NotNull;

import java.text.NumberFormat;

import butterknife.BindColor;
import butterknife.BindView;
import io.reactivex.annotations.NonNull;

public class RepoViewHolder extends BaseViewHolder<Repo> {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.date) TextView date;
    @BindView(R.id.stars) TextView stars;
    @BindView(R.id.forks) TextView forks;
    @BindView(R.id.language) TextView language;
    @BindView(R.id.size) TextView size;

    @Nullable @BindView(R.id.avatarLayout)
    AvatarLayout avatarLayout;
    /*
    @BindString(R.string.forked) String forked;
    @BindString(R.string.private_repo) String privateRepo;

     */

    @BindColor(R.color.material_indigo_700) int forkColor;
    @BindColor(R.color.material_grey_700) int privateColor;
    private boolean isStarred;
    private boolean withImage;

    private RepoViewHolder(@NonNull View itemView, @Nullable BaseRecyclerAdapter adapter, boolean isStarred, boolean withImage) {
        super(itemView, adapter);
        this.isStarred = isStarred;
        this.withImage = withImage;
    }

    public static RepoViewHolder newInstance(ViewGroup viewGroup, BaseRecyclerAdapter adapter, boolean isStarred, boolean withImage) {
        if (withImage) {
            return new RepoViewHolder(getView(viewGroup, R.layout.row_item_repos), adapter, isStarred, true);
        } else {
            return new RepoViewHolder(getView(viewGroup, R.layout.row_item_repos_no_image), adapter, isStarred, false);
        }

    }

    @Override public void bind(@NotNull @NonNull Repo repo) {
        if (repo.isFork() && !isStarred) {
            title.setText(SpannableBuilder.builder()
                    .append(" " + "Forked" + " ", new LabelSpan(forkColor))
                    .append(" ")
                    .append(repo.getName(), new LabelSpan(Color.TRANSPARENT)));
        } else if (repo.isPrivateX()) {
            title.setText(SpannableBuilder.builder()
                    .append(" " + "Private Repos" + " ", new LabelSpan(privateColor))
                    .append(" ")
                    .append(repo.getName(), new LabelSpan(Color.TRANSPARENT)));
        } else {
            title.setText(!isStarred ? repo.getName() : repo.getFullName());
        }
        if (withImage) {
            String avatar = repo.getOwner() != null ? repo.getOwner().getAvatarUrl() : null;
            String login = repo.getOwner() != null ? repo.getOwner().getLogin() : null;
            boolean isOrg = repo.getOwner() != null && repo.getOwner().isOrganizationType();
            if (avatarLayout != null) {
                avatarLayout.setVisibility(View.VISIBLE);
                avatarLayout.setUrl(avatar, login, isOrg,false);
            }
        }
        long repoSize = repo.getSize() > 0 ? (repo.getSize() * 1000) : repo.getSize();
        size.setText(Formatter.formatFileSize(size.getContext(), repoSize));
        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        stars.setText(numberFormat.format(repo.getStargazersCount()));
        forks.setText(numberFormat.format(repo.getForks()));
        date.setText(ParseDateFormat.getTimeAgo(repo.getUpdatedAt()));
        if (!InputHelper.isEmpty(repo.getLanguage())) {
            language.setText(repo.getLanguage());
            //language.setTextColor(ColorsProvider.getColorAsColor(repo.getLanguage(),language.getContext()));
            language.setVisibility(View.VISIBLE);
        } else {
            language.setTextColor(Color.BLACK);
            language.setVisibility(View.GONE);
            language.setText("");
        }
    }
}
