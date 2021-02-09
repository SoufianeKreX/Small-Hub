package com.soufianekre.smallhub.ui.user.repos;

public class UserReposFilterBottomSheetDialog  {





    public interface ProfileReposFilterChangeListener {
        void onFilterApply();

        void onTypeSelected(String selectedType);

        void onSortOptionSelected(String selectedSortOption);

        void onSortDirectionSelected(String selectedSortDirection);

        String getLogin();
    }
}
