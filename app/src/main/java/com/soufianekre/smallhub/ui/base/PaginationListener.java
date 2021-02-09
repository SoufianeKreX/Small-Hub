package com.soufianekre.smallhub.ui.base;

import androidx.annotation.Nullable;

public interface PaginationListener<P> {
    int getCurrentPage();

    int getPreviousTotal();

    void setCurrentPage(int page);

    void setPreviousTotal(int previousTotal);

    boolean onCallApi(int page, @Nullable P parameter);
}
