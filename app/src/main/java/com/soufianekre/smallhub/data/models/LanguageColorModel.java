package com.soufianekre.smallhub.data.models;

import android.os.Parcel;
import android.os.Parcelable;

public class LanguageColorModel implements Parcelable {

    private String url;
    private String color;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.color);
        dest.writeString(this.url);
    }

    public LanguageColorModel() {
    }

    private LanguageColorModel(Parcel in) {
        this.color = in.readString();
        this.url = in.readString();
    }

    public static final Parcelable.Creator<LanguageColorModel> CREATOR = new Parcelable.Creator<LanguageColorModel>() {
        @Override
        public LanguageColorModel createFromParcel(Parcel source) {
            return new LanguageColorModel(source);
        }

        @Override
        public LanguageColorModel[] newArray(int size) {
            return new LanguageColorModel[size];
        }
    };

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "LanguageColorModel{" +
                "color='" + color + '\'' +
                ", url ='" + url + '\'' +
                '}';
    }
}
