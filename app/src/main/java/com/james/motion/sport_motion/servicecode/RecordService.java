package com.james.motion.sport_motion.servicecode;

import com.amap.api.maps.model.LatLng;

/**
 * 上传跑步信息的Service接口
 */
public interface RecordService {

    //记录运动坐标和大概描述信息
    void recordSport(LatLng latLng, String location);
}
