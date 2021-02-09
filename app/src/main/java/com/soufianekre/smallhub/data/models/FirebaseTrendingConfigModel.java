package com.soufianekre.smallhub.data.models;

public class FirebaseTrendingConfigModel {
    private String pathUrl = "https://github.com/trending/";
    private String description = ".Box-row > p";
    private String forks = ".f6 > a[href*=/network]";
    private String language = ".f6 span[itemprop=programmingLanguage]";
    private String languageFallback = ".f6 span[itemprop = programmingLanguage]";
    private String listName = ".Box";
    private String listNameSublistTag = "article";
    private String stars = ".f6 a[href*=/stargazers]";
    private String title = ".Box-row > h1 > a";
    private String todayStars = ".f6 > span.float-sm-right";
    private String todayStarsFallback = ".f6 > span.float-sm-right";

    public FirebaseTrendingConfigModel() {
    }

    public FirebaseTrendingConfigModel(String pathUrl, String description, String forks, String language, String languageFallback, String listName,String listNameSublistTag, String stars, String title, String todayStars, String todayStarsFallback) {
        this.pathUrl = pathUrl;
        this.description = description;
        this.forks = forks;
        this.language = language;
        this.languageFallback = languageFallback;
        this.listName = listName;
        this.listNameSublistTag = listNameSublistTag;
        this.stars = stars;
        this.title = title;
        this.todayStars = todayStars;
        this.todayStarsFallback = todayStarsFallback;
    }


    public String getPathUrl() {
        return pathUrl;
    }

    public String getDescription() {
        return description;
    }

    public String getForks() {
        return forks;
    }

    public String getLanguage() {
        return language;
    }

    public String getLanguageFallback() {
        return languageFallback;
    }

    public String getListName() {
        return listName;
    }

    public String getListNameSublistTag() {
        return listNameSublistTag;
    }

    public String getStars() {
        return stars;
    }

    public String getTitle() {
        return title;
    }

    public String getTodayStars() {
        return todayStars;
    }

    public String getTodayStarsFallback() {
        return todayStarsFallback;
    }
}
