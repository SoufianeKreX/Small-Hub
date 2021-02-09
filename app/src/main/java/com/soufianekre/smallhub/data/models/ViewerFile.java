package com.soufianekre.smallhub.data.models;

import android.os.Parcel;
import android.os.Parcelable;

public class ViewerFile implements Parcelable {
    boolean markdown;
    String content;
    String fullUrl;
    boolean repo;

    public ViewerFile() {
    }


    protected ViewerFile(Parcel in) {
        markdown = in.readByte() != 0;
        content = in.readString();
        fullUrl = in.readString();
        repo = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (markdown ? 1 : 0));
        dest.writeString(content);
        dest.writeString(fullUrl);
        dest.writeByte((byte) (repo ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ViewerFile> CREATOR = new Creator<ViewerFile>() {
        @Override
        public ViewerFile createFromParcel(Parcel in) {
            return new ViewerFile(in);
        }

        @Override
        public ViewerFile[] newArray(int size) {
            return new ViewerFile[size];
        }
    };

    public boolean isMarkdown() {
        return markdown;
    }

    public void setMarkdown(boolean markdown) {
        this.markdown = markdown;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFullUrl() {
        return fullUrl;
    }

    public void setFullUrl(String fullUrl) {
        this.fullUrl = fullUrl;
    }

    public boolean isRepo() {
        return repo;
    }

    public void setRepo(boolean repo) {
        this.repo = repo;
    }
}
