package com.soufianekre.smallhub.ui.adapters.view_holder;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.soufianekre.smallhub.R;
import com.soufianekre.smallhub.data.models.TrendingModel;
import com.soufianekre.smallhub.helper.ColorsProvider;
import com.soufianekre.smallhub.helper.InputHelper;
import com.soufianekre.smallhub.ui.widgets.FontTextView;
import com.soufianekre.smallhub.ui.widgets.recycler_view.BaseRecyclerAdapter;
import com.soufianekre.smallhub.ui.widgets.recycler_view.BaseViewHolder;

import org.jetbrains.annotations.NotNull;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TrendingRepoViewHolder extends BaseViewHolder<TrendingModel> {

    @BindView(R.id.trending_repo_name_text)
    TextView name;
    @BindView(R.id.trending_repo_desc_text)
    TextView description;
    @BindView(R.id.trending_repo_stars_text)
    TextView starsText;
    @BindView(R.id.trending_repo_forks_text)
    TextView forksText;
    @BindView(R.id.trending_repo_language)
    FontTextView programmingLanguageText;

    public TrendingRepoViewHolder(@NonNull View itemView,@Nullable BaseRecyclerAdapter adapter) {
        super(itemView,adapter);
    }

    public static TrendingRepoViewHolder newInstance(@NonNull ViewGroup parent, @Nullable BaseRecyclerAdapter adapter) {
        return new TrendingRepoViewHolder(getView(parent,  R.layout.row_item_trending_repo), adapter);
    }

    @Override
    public void bind(@NotNull @NonNull TrendingModel model) {
        name.setText(model.getName());
        description.setText(model.getDescription());
        starsText.setText(model.getStars());
        forksText.setText(model.getForks());
        if (InputHelper.isEmpty(model.getLanguage())){
            programmingLanguageText.setVisibility(View.GONE);
        }else{
            int color = ColorsProvider.getColorAsColor(model.getLanguage(),itemView.getContext());
            programmingLanguageText.tintDrawables(color);
            programmingLanguageText.setText(model.getLanguage());
            programmingLanguageText.setTextColor(color);
            programmingLanguageText.setVisibility(View.VISIBLE);
        }

    }
}