package com.example.yardscapelistingprototype;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Listing implements Parcelable {
    @SerializedName("listingTitle")
    private String title = String.valueOf(R.string.demoTitle);
    @SerializedName("listingDesc")
    private String description = String.valueOf(R.string.demoDesc);
    @SerializedName("listingDate")
    private String listing_date = String.valueOf(R.string.demoDate);
    @SerializedName("listingTime")
    private String listing_time = String.valueOf(R.string.demoTime);
    @SerializedName("listingPath")
    private String listing_image_path;

    // Empty Constructor
    public Listing() {
        title = "";
        description = "";
        listing_date = "";
        listing_time = "";
        setDefaultImage();
    }

    public Listing(String inTitle, String inDesc, String inDate, String inTime) {
        title = inTitle;
        description = inDesc;
        listing_date = inDate;
        listing_time = inTime;
        setDefaultImage();
    }

    public Listing(String intitle, String indesc, String indate, String intime, String inimage) {
        title = intitle;
        description = indesc;
        listing_date = indate;
        listing_time = intime;
        listing_image_path = inimage;
    }

    // Enables the ability to read a Listing variable from a Parceable object
    protected Listing(Parcel in) {
        title = in.readString();
        description = in.readString();
        listing_date = in.readString();
        listing_time = in.readString();
        listing_image_path = in.readString();
    }

    public static final Creator<Listing> CREATOR = new Creator<Listing>() {
        @Override
        public Listing createFromParcel(Parcel in) {
            return new Listing(in);
        }

        @Override
        public Listing[] newArray(int size) {
            return new Listing[size];
        }
    };

    public void setTitle(String titleInput) {
        title = titleInput;
    }
    public void setDescription(String description1) {
        description = description1;
    }
    public void setListing_date(String dateInput) {
        listing_date = dateInput;
    }
    public void setListing_time(String timeInput) {
        listing_time = timeInput;
    }
    public void setListing_image_path(String imageInput) {listing_image_path = imageInput;}

    public String getListing_title() {
        return title;
    }
    public String getListing_description() {
        return description;
    }
    public String getListing_date(){
         return listing_date;
    }
    public String getListing_time(){
        return listing_time;
    }
    public String getListing_image_path() {return listing_image_path;}

    @Override
    public int describeContents() {
        return 0;
    }

    public String getURLForResource(int resourceID) {
        return Uri.parse("android.resource://" + R.class.getPackage().getName() + resourceID).toString();
    }

    public void setDefaultImage() {
        setListing_image_path("none");
    }

    // Enables the ability to write a Listing variable to a Parceable object
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(listing_date);
        dest.writeString(listing_time);
        dest.writeString(listing_image_path);
    }
}
