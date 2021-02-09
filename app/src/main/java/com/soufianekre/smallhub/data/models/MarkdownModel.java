package com.soufianekre.smallhub.data.models;

import android.os.Parcel;
import android.os.Parcelable;

public class MarkdownModel implements Parcelable {

    private String text;
    private String mode = "gfm";
    private String context;

    public MarkdownModel() {
    }

    protected MarkdownModel(Parcel in) {
        text = in.readString();
        mode = in.readString();
        context = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(text);
        dest.writeString(mode);
        dest.writeString(context);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MarkdownModel> CREATOR = new Creator<MarkdownModel>() {
        @Override
        public MarkdownModel createFromParcel(Parcel in) {
            return new MarkdownModel(in);
        }

        @Override
        public MarkdownModel[] newArray(int size) {
            return new MarkdownModel[size];
        }
    };

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }
}
