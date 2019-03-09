package com.james.motion.commmon.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * 用于记录一条轨迹，包括起点、终点、轨迹中间点、距离、耗时、时间
 */
public class PathRecord implements Parcelable {

    //主键
    private Long id;
    //运动开始点
    private LatLng mStartPoint;
    //运动结束点
    private LatLng mEndPoint;
    //运动轨迹
    private List<LatLng> mPathLinePoints = new ArrayList<>();
    //运动距离
    private Double mDistance;
    //运动时长
    private Long mDuration;
    //运动开始时间
    private Long mStartTime;
    //运动结束时间
    private Long mEndTime;
    //消耗卡路里
    private Double mCalorie;
    //平均时速(公里/小时)
    private Double mSpeed;
    //平均配速(分钟/公里)
    private Double mDistribution;
    //日期标记
    private String mDateTag;

    public PathRecord() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LatLng getStartpoint() {
        return mStartPoint;
    }

    public void setStartpoint(LatLng startpoint) {
        this.mStartPoint = startpoint;
    }

    public LatLng getEndpoint() {
        return mEndPoint;
    }

    public void setEndpoint(LatLng endpoint) {
        this.mEndPoint = endpoint;
    }

    public List<LatLng> getPathline() {
        return mPathLinePoints;
    }

    public void setPathline(List<LatLng> pathline) {
        this.mPathLinePoints = pathline;
    }

    public Double getDistance() {
        return mDistance;
    }

    public void setDistance(Double distance) {
        this.mDistance = distance;
    }

    public Long getDuration() {
        return mDuration;
    }

    public void setDuration(Long duration) {
        this.mDuration = duration;
    }

    public Long getStartTime() {
        return mStartTime;
    }

    public void setStartTime(Long mStartTime) {
        this.mStartTime = mStartTime;
    }

    public Long getEndTime() {
        return mEndTime;
    }

    public void setEndTime(Long mEndTime) {
        this.mEndTime = mEndTime;
    }

    public void addpoint(LatLng point) {
        mPathLinePoints.add(point);
    }

    public Double getCalorie() {
        return mCalorie;
    }

    public void setCalorie(Double mCalorie) {
        this.mCalorie = mCalorie;
    }

    public Double getSpeed() {
        return mSpeed;
    }

    public void setSpeed(Double mSpeed) {
        this.mSpeed = mSpeed;
    }

    public Double getDistribution() {
        return mDistribution;
    }

    public void setDistribution(Double mDistribution) {
        this.mDistribution = mDistribution;
    }

    public String getDateTag() {
        return mDateTag;
    }

    public void setDateTag(String mDateTag) {
        this.mDateTag = mDateTag;
    }

    @Override
    public String toString() {
        StringBuilder record = new StringBuilder();
        record.append("recordSize:" + getPathline().size() + ", ");
        record.append("distance:" + getDistance() + "m, ");
        record.append("duration:" + getDuration() + "s");
        return record.toString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeParcelable(this.mStartPoint, flags);
        dest.writeParcelable(this.mEndPoint, flags);
        dest.writeTypedList(this.mPathLinePoints);
        dest.writeValue(this.mDistance);
        dest.writeValue(this.mDuration);
        dest.writeValue(this.mStartTime);
        dest.writeValue(this.mEndTime);
        dest.writeValue(this.mCalorie);
        dest.writeValue(this.mSpeed);
        dest.writeValue(this.mDistribution);
        dest.writeString(this.mDateTag);
    }

    protected PathRecord(Parcel in) {
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.mStartPoint = in.readParcelable(AMapLocation.class.getClassLoader());
        this.mEndPoint = in.readParcelable(AMapLocation.class.getClassLoader());
        this.mPathLinePoints = in.createTypedArrayList(LatLng.CREATOR);
        this.mDistance = (Double) in.readValue(Double.class.getClassLoader());
        this.mDuration = (Long) in.readValue(Long.class.getClassLoader());
        this.mStartTime = (Long) in.readValue(Long.class.getClassLoader());
        this.mEndTime = (Long) in.readValue(Long.class.getClassLoader());
        this.mCalorie = (Double) in.readValue(Double.class.getClassLoader());
        this.mSpeed = (Double) in.readValue(Double.class.getClassLoader());
        this.mDistribution = (Double) in.readValue(Double.class.getClassLoader());
        this.mDateTag = in.readString();
    }

    public static final Creator<PathRecord> CREATOR = new Creator<PathRecord>() {
        @Override
        public PathRecord createFromParcel(Parcel source) {
            return new PathRecord(source);
        }

        @Override
        public PathRecord[] newArray(int size) {
            return new PathRecord[size];
        }
    };
}