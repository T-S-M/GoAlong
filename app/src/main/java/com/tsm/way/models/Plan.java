package com.tsm.way.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Plan implements Parcelable {

    public static final Creator<Plan> CREATOR = new Creator<Plan>() {
        @Override
        public Plan createFromParcel(Parcel in) {
            return new Plan(in);
        }

        @Override
        public Plan[] newArray(int size) {
            return new Plan[size];
        }
    };
    private int eventType;
    private String placeName, placeAddress, hostName, hostUid;
    private double placeLat, placeLong;
    private long startTime, endTime;
    private String coverUrl;
    private String title, description;
    private int confirmedCount, pendingCount;
    private boolean status;
    private String fbEventId, googlePlaceID, discussionID;


    public Plan() {
    }

    protected Plan(Parcel in) {
        eventType = in.readInt();
        placeName = in.readString();
        placeAddress = in.readString();
        hostName = in.readString();
        hostUid = in.readString();
        placeLat = in.readDouble();
        placeLong = in.readDouble();
        startTime = in.readLong();
        endTime = in.readLong();
        coverUrl = in.readString();
        title = in.readString();
        description = in.readString();
        confirmedCount = in.readInt();
        pendingCount = in.readInt();
        status = in.readByte() != 0;
        fbEventId = in.readString();
        googlePlaceID = in.readString();
        discussionID = in.readString();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(eventType);
        parcel.writeString(placeName);
        parcel.writeString(placeAddress);
        parcel.writeString(hostName);
        parcel.writeString(hostUid);
        parcel.writeDouble(placeLat);
        parcel.writeDouble(placeLong);
        parcel.writeLong(startTime);
        parcel.writeLong(endTime);
        parcel.writeString(coverUrl);
        parcel.writeString(title);
        parcel.writeString(description);
        parcel.writeInt(confirmedCount);
        parcel.writeInt(pendingCount);
        parcel.writeByte((byte) (status ? 1 : 0));
        parcel.writeString(fbEventId);
        parcel.writeString(googlePlaceID);
        parcel.writeString(discussionID);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getFbEventId() {
        return fbEventId;
    }

    public void setFbEventId(String fbEventId) {
        this.fbEventId = fbEventId;
    }

    public String getGooglePlaceID() {
        return googlePlaceID;
    }

    public void setGooglePlaceID(String googlePlaceID) {
        this.googlePlaceID = googlePlaceID;
    }

    public String getDiscussionID() {
        return discussionID;
    }

    public void setDiscussionID(String discussionID) {
        this.discussionID = discussionID;
    }

    public int getEventType() {
        return eventType;
    }

    public void setEventType(int eventType) {
        this.eventType = eventType;
    }

    public String getPlaceAddress() {
        return placeAddress;
    }

    public void setPlaceAddress(String placeAddress) {
        this.placeAddress = placeAddress;
    }

    public double getPlaceLat() {
        return placeLat;
    }

    public void setPlaceLat(double placeLat) {
        this.placeLat = placeLat;
    }

    public double getPlaceLong() {
        return placeLong;
    }

    public void setPlaceLong(double placeLong) {
        this.placeLong = placeLong;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getConfirmedCount() {
        return confirmedCount;
    }

    public void setConfirmedCount(int confirmedCount) {
        this.confirmedCount = confirmedCount;
    }

    public int getPendingCount() {
        return pendingCount;
    }

    public void setPendingCount(int pendingCount) {
        this.pendingCount = pendingCount;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getHostUid() {
        return hostUid;
    }

    public void setHostUid(String hostUid) {
        this.hostUid = hostUid;
    }

}
