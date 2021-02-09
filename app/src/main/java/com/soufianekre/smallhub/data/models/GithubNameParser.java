package com.soufianekre.smallhub.data.models;

import android.net.Uri;

import androidx.annotation.Nullable;

import com.soufianekre.smallhub.helper.InputHelper;

import java.util.List;

public class GithubNameParser {

    public String name;
    public String username;

    public GithubNameParser(@Nullable String url) {
        if (!InputHelper.isEmpty(url)) {
            boolean isEnterprise = false;
            if (isEnterprise) {
                url = url.replace("api/v3/", "");
            }
            Uri uri = Uri.parse(url);
            List<String> segments = uri.getPathSegments();
            if (segments == null || segments.size() < 2) {
                return;
            }
            boolean isFirstPathIsRepo = (segments.get(0).equalsIgnoreCase("repos")
                    || segments.get(0).equalsIgnoreCase("repo"));
            this.username = isFirstPathIsRepo ? segments.get(1) : segments.get(0);
            this.name = isFirstPathIsRepo ? segments.get(2) : segments.get(1);
        }
    }

    @Override public String toString() {
        return "NameParser{" +
                "name='" + name + '\'' +
                ", username='" + username + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
