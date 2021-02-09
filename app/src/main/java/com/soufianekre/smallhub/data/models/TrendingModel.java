package com.soufianekre.smallhub.data.models;

import android.os.Parcel;
import android.os.Parcelable;

public class TrendingModel implements Parcelable {

    private String name;
    private String description;
    private String language;
    private String stars;
    private String forks;
    private String todayStars;


    public TrendingModel(String name, String description, String language, String stars, String forks, String todayStars) {
        this.name = name;
        this.description = description;
        this.language = language;
        this.stars = stars;
        this.forks = forks;
        this.todayStars = todayStars;
    }


    protected TrendingModel(Parcel in) {
        name = in.readString();
        description = in.readString();
        language = in.readString();
        stars = in.readString();
        forks = in.readString();
        todayStars = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(language);
        dest.writeString(stars);
        dest.writeString(forks);
        dest.writeString(todayStars);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TrendingModel> CREATOR = new Creator<TrendingModel>() {
        @Override
        public TrendingModel createFromParcel(Parcel in) {
            return new TrendingModel(in);
        }

        @Override
        public TrendingModel[] newArray(int size) {
            return new TrendingModel[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getStars() {
        return stars;
    }

    public void setStars(String stars) {
        this.stars = stars;
    }

    public String getForks() {
        return forks;
    }

    public void setForks(String forks) {
        this.forks = forks;
    }

    public String getTodayStars() {
        return todayStars;
    }

    public void setTodayStars(String todayStars) {
        this.todayStars = todayStars;
    }

    @Override
    public String toString() {
        return "TrendingModel{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", language='" + language + '\'' +
                ", stars='" + stars + '\'' +
                ", forks='" + forks + '\'' +
                ", todayStars='" + todayStars + '\'' +
                '}';
    }
}
