package com.soufianekre.smallhub.data.network.model;


import android.os.Parcel;
import android.os.Parcelable;

public class TeamsModel implements Parcelable {
    private long id;
    private String url;
    private String name;
    private String slug;
    private String description;
    private String privacy;
    private String permission;
    private String membersUrl;
    private String repositoriesUrl;

    @Override public int describeContents() { return 0; }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.url);
        dest.writeString(this.name);
        dest.writeString(this.slug);
        dest.writeString(this.description);
        dest.writeString(this.privacy);
        dest.writeString(this.permission);
        dest.writeString(this.membersUrl);
        dest.writeString(this.repositoriesUrl);
    }

    protected TeamsModel(Parcel in) {
        this.id = in.readLong();
        this.url = in.readString();
        this.name = in.readString();
        this.slug = in.readString();
        this.description = in.readString();
        this.privacy = in.readString();
        this.permission = in.readString();
        this.membersUrl = in.readString();
        this.repositoriesUrl = in.readString();
    }

    public static final Creator<TeamsModel> CREATOR = new Creator<TeamsModel>() {
        @Override public TeamsModel createFromParcel(Parcel source) {return new TeamsModel(source);}

        @Override public TeamsModel[] newArray(int size) {return new TeamsModel[size];}
    };

    public TeamsModel() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrivacy() {
        return privacy;
    }

    public void setPrivacy(String privacy) {
        this.privacy = privacy;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public String getMembersUrl() {
        return membersUrl;
    }

    public void setMembersUrl(String membersUrl) {
        this.membersUrl = membersUrl;
    }

    public String getRepositoriesUrl() {
        return repositoriesUrl;
    }

    public void setRepositoriesUrl(String repositoriesUrl) {
        this.repositoriesUrl = repositoriesUrl;
    }
}
