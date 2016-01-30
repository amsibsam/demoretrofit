package com.example.root.retrofitandrx.pojo;

import android.os.Parcel;
import android.os.Parcelable;

public class Location implements Parcelable {
    public final String name;
    public final float latitude;
    public final float longitude;

    public Location(String name, float latitude, float longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeFloat(this.latitude);
        dest.writeFloat(this.longitude);
    }

    protected Location(Parcel in) {
        this.name = in.readString();
        this.latitude = in.readFloat();
        this.longitude = in.readFloat();
    }

    public static final Creator<Location> CREATOR = new Creator<Location>() {
        public Location createFromParcel(Parcel source) {
            return new Location(source);
        }

        public Location[] newArray(int size) {
            return new Location[size];
        }
    };
}
