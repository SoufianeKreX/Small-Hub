package com.soufianekre.smallhub.data.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Pageable<M extends Parcelable> implements Parcelable {

    public int first;
    public int next;
    public int prev;
    public int last;
    public int totalCount;
    public boolean incompleteResults;
    public List<M> items;

    public Pageable() {
    }

    @Override public int describeContents() { return 0; }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.first);
        dest.writeInt(this.next);
        dest.writeInt(this.prev);
        dest.writeInt(this.last);
        dest.writeInt(this.totalCount);
        dest.writeByte(this.incompleteResults ? (byte) 1 : (byte) 0);
        dest.writeTypedList(this.items);
    }

    @SuppressWarnings("WeakerAccess") protected Pageable(Parcel in) {
        this.first = in.readInt();
        this.next = in.readInt();
        this.prev = in.readInt();
        this.last = in.readInt();
        this.totalCount = in.readInt();
        this.incompleteResults = in.readByte() != 0;
        in.readList(this.items, this.items.getClass().getClassLoader());
    }

    public static final Creator<Pageable> CREATOR = new Creator<Pageable>() {
        @Override public Pageable createFromParcel(Parcel source) {return new Pageable(source);}

        @Override public Pageable[] newArray(int size) {return new Pageable[size];}
    };

    public int getFirst() {
        return first;
    }

    public void setFirst(int first) {
        this.first = first;
    }

    public int getNext() {
        return next;
    }

    public void setNext(int next) {
        this.next = next;
    }

    public int getPrev() {
        return prev;
    }

    public void setPrev(int prev) {
        this.prev = prev;
    }

    public int getLast() {
        return last;
    }

    public void setLast(int last) {
        this.last = last;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public boolean isIncompleteResults() {
        return incompleteResults;
    }

    public void setIncompleteResults(boolean incompleteResults) {
        this.incompleteResults = incompleteResults;
    }

    public List<M> getItems() {
        return items;
    }

    public void setItems(List<M> items) {
        this.items = items;
    }
}
