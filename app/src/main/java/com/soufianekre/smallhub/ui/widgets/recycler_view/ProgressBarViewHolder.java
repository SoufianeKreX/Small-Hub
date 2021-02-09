package com.soufianekre.smallhub.ui.widgets.recycler_view;


import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.soufianekre.smallhub.R;

import org.jetbrains.annotations.NotNull;

/**
 * Created by kosh on 03/08/2017.
 */

public class ProgressBarViewHolder extends BaseViewHolder {

    private ProgressBarViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    public static ProgressBarViewHolder newInstance(ViewGroup viewGroup) {
        return new ProgressBarViewHolder(getView(viewGroup, R.layout.progress_layout));
    }

    @Override public void bind(@NotNull @NonNull Object o) {

    }
}
