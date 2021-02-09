package com.soufianekre.smallhub.data.network.model;

import android.os.Parcel;
import android.os.Parcelable;

public class LicenseModel implements Parcelable {
    String key;
    String name;
    String spdxId;
    String url;
    boolean featured;

    public LicenseModel() {
    }

    protected LicenseModel(Parcel in) {
        key = in.readString();
        name = in.readString();
        spdxId = in.readString();
        url = in.readString();
        featured = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(key);
        dest.writeString(name);
        dest.writeString(spdxId);
        dest.writeString(url);
        dest.writeByte((byte) (featured ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<LicenseModel> CREATOR = new Creator<LicenseModel>() {
        @Override
        public LicenseModel createFromParcel(Parcel in) {
            return new LicenseModel(in);
        }

        @Override
        public LicenseModel[] newArray(int size) {
            return new LicenseModel[size];
        }
    };

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpdxId() {
        return spdxId;
    }

    public void setSpdxId(String spdxId) {
        this.spdxId = spdxId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isFeatured() {
        return featured;
    }

    public void setFeatured(boolean featured) {
        this.featured = featured;
    }
}
