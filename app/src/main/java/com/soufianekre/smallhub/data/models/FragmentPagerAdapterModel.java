package com.soufianekre.smallhub.data.models;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.soufianekre.smallhub.R;
import com.soufianekre.smallhub.ui.repos.code.contributors.RepoContributorsFragment;
import com.soufianekre.smallhub.ui.repos.code.read_me.ViewerFragment;
import com.soufianekre.smallhub.ui.search.repos.SearchReposFragment;
import com.soufianekre.smallhub.ui.search.users.SearchUsersFragment;
import com.soufianekre.smallhub.ui.user.overview.UserOverviewFragment;
import com.soufianekre.smallhub.ui.user.repos.UserReposFragment;

import java.util.List;
import java.util.Objects;

public class FragmentPagerAdapterModel {

    String title;
    Fragment fragment;
    String key;

    private FragmentPagerAdapterModel(String title, Fragment fragment) {
        this(title, fragment, null);
    }

    public FragmentPagerAdapterModel(String title, Fragment fragment, String key) {
        this.title = title;
        this.fragment = fragment;
        this.key = key;
    }






    @NonNull public static List<FragmentPagerAdapterModel> buildForSearch(@NonNull Context context) {
        return Stream.of(new FragmentPagerAdapterModel(context.getString(R.string.repos), SearchReposFragment.newInstance()),
                new FragmentPagerAdapterModel(context.getString(R.string.users), SearchUsersFragment.newInstance()))
                .collect(Collectors.toList());
    }

    @NonNull public static List<FragmentPagerAdapterModel> buildForProfile(@NonNull Context context, @NonNull String login) {
        return Stream.of(new FragmentPagerAdapterModel(context.getString(R.string.overview), UserOverviewFragment.newInstance(login)),
                new FragmentPagerAdapterModel(context.getString(R.string.repos), UserReposFragment.newInstance(login))
        ).collect(Collectors.toList());
    }

    @NonNull public static List<FragmentPagerAdapterModel> buildForRepoCode(@NonNull Context context, @NonNull String repoId,
                                                                            @NonNull String login, @NonNull String url,
                                                                            @NonNull String defaultBranch,
                                                                            @NonNull String htmlUrl) {
        return Stream.of(new FragmentPagerAdapterModel(context.getString(R.string.readme), ViewerFragment.newInstance(url, htmlUrl, true)),
                new FragmentPagerAdapterModel(context.getString(R.string.contributors), RepoContributorsFragment.newInstance(repoId, login)))
                /*
                new FragmentPagerAdapterModel(context.getString(R.string.files), RepoFilePathFragment.newInstance(login, repoId, null, defaultBranch)),
                new FragmentPagerAdapterModel(context.getString(R.string.commits), RepoCommitsFragment.newInstance(repoId, login, defaultBranch)),
                new FragmentPagerAdapterModel(context.getString(R.string.releases), RepoReleasesFragment.newInstance(repoId, login)),

                 */
                .collect(Collectors.toList());
    }







    /*
    @NonNull public static List<FragmentPagerAdapterModel> buildForOrg(@NonNull Context context, @NonNull String login, boolean isMember) {
        return Stream.of(
                new FragmentPagerAdapterModel(context.getString(R.string.feeds),
                        isMember ? FeedsFragment.newInstance(login, true) : null),
                new FragmentPagerAdapterModel(context.getString(R.string.overview), OrgProfileOverviewFragment.newInstance(login)),
                new FragmentPagerAdapterModel(context.getString(R.string.repos), OrgReposFragment.newInstance(login)),
                new FragmentPagerAdapterModel(context.getString(R.string.people), OrgMembersFragment.newInstance(login)),
                new FragmentPagerAdapterModel(context.getString(R.string.teams), isMember ? OrgTeamFragment.newInstance(login) : null))
                .filter(fragmentPagerAdapterModel -> fragmentPagerAdapterModel.getFragment() != null)
                .collect(Collectors.toList());
    }

     */


    /*
    @NonNull public static List<FragmentPagerAdapterModel> buildForTheme() {
        return Stream.of(new FragmentPagerAdapterModel("", ThemeFragment.Companion.newInstance(R.style.ThemeLight)),
                new FragmentPagerAdapterModel("", ThemeFragment.Companion.newInstance(R.style.ThemeDark)),
                new FragmentPagerAdapterModel("", ThemeFragment.Companion.newInstance(R.style.ThemeAmlod)),
                new FragmentPagerAdapterModel("", ThemeFragment.Companion.newInstance(R.style.ThemeBluish)))
//                new FragmentPagerAdapterModel("", ThemeFragment.Companion.newInstance(R.style.ThemeMidnight)))
                .collect(Collectors.toList());
    }

     */


    /*
    @NonNull public static List<FragmentPagerAdapterModel> buildForDrawer(@NonNull Context context) {
        return Stream.of(new FragmentPagerAdapterModel(context.getString(R.string.menu_label), new MainDrawerFragment()),
                new FragmentPagerAdapterModel(context.getString(R.string.profile), new AccountDrawerFragment()))
                .toList();
    }

     */

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FragmentPagerAdapterModel that = (FragmentPagerAdapterModel) o;

        return Objects.equals(key, that.key);
    }

    @Override public int hashCode() {
        return key != null ? key.hashCode() : 0;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Fragment getFragment() {
        return fragment;
    }

    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
