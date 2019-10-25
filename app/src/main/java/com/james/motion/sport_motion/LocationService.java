package com.james.motion.sport_motion;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.model.LatLng;
import com.james.motion.commmon.utils.LogUtils;
import com.james.motion.sport_motion.servicecode.RecordService;
import com.james.motion.sport_motion.servicecode.impl.RecordServiceImpl;

/**
 * 定位的Service类，用户在运动时此服务会在后台进行定位。
 */
public class LocationService extends Service {

    private InterfaceLocationed interfaceLocationed = null;

    public static final String TAG = "LocationService";

    public final IBinder mBinder = new LocalBinder();

    public class LocalBinder extends Binder {
        // 在Binder中定义一个自定义的接口用于数据交互
        // 这里直接把当前的服务传回给宿主
        public LocationService getService() {
            return LocationService.this;
        }
    }

    //定位的时间间隔，单位是毫秒
    private static final int LOCATION_SPAN = 10 * 1000;

    //百度地图中定位的类
    public AMapLocationClient mLocationClient = null;
    //记录着运动中移动的坐标位置
//    private List<LatLng> mSportLatLngs = new LinkedList<>();

    //记录运动信息的Service
    private RecordService mRecordService = null;

    @Override
    public void onCreate() {
        super.onCreate();

        //声明LocationClient类
        mLocationClient = new AMapLocationClient(this);
        //给定位类加入自定义的配置
        initLocationOption();
        //注册监听函数
        mLocationClient.setLocationListener(MyAMapLocationListener);

        //初始化信息记录类
        mRecordService = new RecordServiceImpl(this);

        //启动定位
        mLocationClient.startLocation();
    }

    //初始化定位的配置
    private void initLocationOption() {
        AMapLocationClientOption mOption = new AMapLocationClientOption();
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mOption.setGpsFirst(true);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.setHttpTimeOut(30000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mOption.setInterval(4000);//可选，设置定位间隔。默认为2秒
        mOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是true
        mOption.setOnceLocation(false);//可选，设置是否单次定位。默认是false
        mOption.setOnceLocationLatest(false);//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
        mOption.setSensorEnable(false);//可选，设置是否使用传感器。默认是false
        mOption.setWifiScan(true); //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
        mOption.setLocationCacheEnable(false); //可选，设置是否使用缓存定位，默认为true
        mOption.setGeoLanguage(AMapLocationClientOption.GeoLanguage.DEFAULT);//可选，设置逆地理信息的语言，默认值为默认语言（根据所在地区选择语言）
        mLocationClient.setLocationOption(mOption);
    }

    //定位回调
    private AMapLocationListener MyAMapLocationListener = aMapLocation -> {

        if (null == aMapLocation)
            return;

        if (aMapLocation.getErrorCode() == 0) {
            //先暂时获得经纬度信息，并将其记录在List中
            LogUtils.d("纬度信息为" + aMapLocation.getLatitude() + "\n经度信息为" + aMapLocation.getLongitude());
            LatLng locationValue = new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());
//                mSportLatLngs.add(locationValue);

            //将运动信息上传至服务器
            recordLocation(locationValue, aMapLocation.getLocationDetail());

            //定位成功，发送通知
            if (null != interfaceLocationed)
                interfaceLocationed.locationed(aMapLocation);

        } else {
            String errText = "定位失败," + aMapLocation.getErrorCode() + ": " + aMapLocation.getErrorInfo();
            LogUtils.e("AmapErr", errText);
        }
    };

    private void recordLocation(LatLng latLng, String location) {
        if (mRecordService != null) {
            mRecordService.recordSport(latLng, location);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        LogUtils.i(TAG, "绑定服务 The service is binding!");
        // 绑定服务，把当前服务的IBinder对象的引用传递给宿主
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        LogUtils.i(TAG, "解除绑定服务 The service is unbinding!");
        //解除绑定后销毁服务
        stopSelf();
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != mLocationClient) {
            mLocationClient.stopLocation();
            mLocationClient.unRegisterLocationListener(MyAMapLocationListener);
            mLocationClient.onDestroy();
            mLocationClient = null;
        }
    }

    public void setInterfaceLocationed(InterfaceLocationed interfaceLocationed) {
        this.interfaceLocationed = null;
        this.interfaceLocationed = interfaceLocationed;
    }

    public interface InterfaceLocationed {
        void locationed(AMapLocation aMapLocation);
    }
}
