package com.soufianekre.smallhub.data.network.model;

import android.os.Parcel;
import android.os.Parcelable;

public class SearchHistory implements Parcelable {
    String text;


    public SearchHistory(String text) {
        this.text = text;
    }

    protected SearchHistory(Parcel in) {
        text = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(text);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SearchHistory> CREATOR = new Creator<SearchHistory>() {
        @Override
        public SearchHistory createFromParcel(Parcel in) {
            return new SearchHistory(in);
        }

        @Override
        public SearchHistory[] newArray(int size) {
            return new SearchHistory[size];
        }
    };
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
