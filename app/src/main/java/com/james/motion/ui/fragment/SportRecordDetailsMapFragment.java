package com.james.motion.ui.fragment;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.LocationSource.OnLocationChangedListener;
import com.amap.api.maps.TextureMapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.james.motion.R;
import com.james.motion.commmon.bean.PathRecord;
import com.james.motion.commmon.utils.LogUtils;
import com.james.motion.commmon.utils.UIHelper;
import com.james.motion.sport_motion.MotionUtils;
import com.james.motion.sport_motion.PathSmoothTool;
import com.james.motion.sport_motion.TraceRePlay;
import com.james.motion.ui.BaseFragment;
import com.james.motion.ui.activity.SportRecordDetailsActivity;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import butterknife.BindView;

/**
 * 描述: 运动记录详情-地图
 * 作者: james
 * 日期: 2019/2/27 15:25
 * 类名: SportRecordDetailsMapFragment
 */
public class SportRecordDetailsMapFragment extends BaseFragment {

    @BindView(R.id.mapView)
    TextureMapView mapView;
    @BindView(R.id.tvDate)
    TextView tvDate;
    @BindView(R.id.tvTime)
    TextView tvTime;
    @BindView(R.id.tvDistance)
    TextView tvDistance;
    @BindView(R.id.tvDuration)
    TextView tvDuration;

    private final int AMAP_LOADED = 0x0066;

    private Handler handler = new Handler(Looper.getMainLooper()) {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case AMAP_LOADED:
                    resetOriginRole();
                    setupRecord();
                    break;
                default:
                    break;
            }
        }
    };

    private Marker mOriginStartMarker, mOriginEndMarker, mOriginRoleMarker;
    private Polyline mOriginPolyline;
    private PathSmoothTool mpathSmoothTool = null;
    private PolylineOptions polylineOptions;

    private ExecutorService mThreadPool;
    private TraceRePlay mRePlay;

    private List<LatLng> mOriginLatLngList;

    private AMap aMap;

    private OnLocationChangedListener mListener;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;

    private PathRecord pathRecord = null;

    private DecimalFormat decimalFormat = new DecimalFormat("0.00");

    @Override
    public int getLayoutId() {
        return R.layout.fragment_sportrecorddetailsmap;
    }

    @Override
    public void initData(Bundle savedInstanceState) {

        mapView.onCreate(savedInstanceState);// 此方法必须重写

        int threadPoolSize = Runtime.getRuntime().availableProcessors() * 2 + 3;
        mThreadPool = Executors.newFixedThreadPool(threadPoolSize);

        Bundle bundle = getArguments();
        if (bundle != null) {
            pathRecord = bundle.getParcelable(SportRecordDetailsActivity.SPORT_DATA);
        }

        Calendar mCalendar = Calendar.getInstance();
        mCalendar.setTimeInMillis(pathRecord.getStartTime());
        int year = mCalendar.get(Calendar.YEAR);
        int month = mCalendar.get(Calendar.MONTH);
        int day = mCalendar.get(Calendar.DAY_OF_MONTH);
        int mHour = mCalendar.get(Calendar.HOUR);
        int mMinuts = mCalendar.get(Calendar.MINUTE);

        tvDate.setText(UIHelper.getString(R.string.date_month_day, month, day));
        tvTime.setText(UIHelper.getString(R.string.time_hour_minuts, mHour, mMinuts < 10 ? String.valueOf("0" + mMinuts) : mMinuts));

        tvDistance.setText(decimalFormat.format(pathRecord.getDistance() / 1000d));
        tvDuration.setText(MotionUtils.formatseconds(pathRecord.getDuration()));

        initPolyline();

        if (aMap == null)
            aMap = mapView.getMap();

        setUpMap();
    }

    private void initPolyline() {
        polylineOptions = new PolylineOptions();
        polylineOptions.color(getResources().getColor(R.color.colorAccent));
        polylineOptions.width(20f);
        polylineOptions.useGradient(true);

        mpathSmoothTool = new PathSmoothTool();
        mpathSmoothTool.setIntensity(4);
    }

    /**
     * 轨迹数据初始化
     */
    private void setupRecord() {
        if (pathRecord != null) {
            List<LatLng> recordList = pathRecord.getPathline();
            LatLng startLatLng = pathRecord.getStartpoint();
            LatLng endLatLng = pathRecord.getEndpoint();
            if (recordList == null || startLatLng == null || endLatLng == null) {
                return;
            }
            mOriginLatLngList = mpathSmoothTool.pathOptimize(recordList);
            addOriginTrace(startLatLng, endLatLng, mOriginLatLngList);
        } else {
            showToast("获取运动轨迹失败!");
        }
    }

    /**
     * 设置一些amap的属性
     */
    private void setUpMap() {
        // 自定义系统定位小蓝点
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory
                .fromResource(R.drawable.mylocation_point));// 设置小蓝点的图标
        myLocationStyle.strokeColor(Color.TRANSPARENT);// 设置圆形的边框颜色
        myLocationStyle.radiusFillColor(Color.argb(0, 0, 0, 0));// 设置圆形的填充颜色
        // myLocationStyle.anchor(int,int)//设置小蓝点的锚点
        myLocationStyle.strokeWidth(1.0f);// 设置圆形的边框粗细
        aMap.setLocationSource(MyLocationSource);// 设置定位监听
        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        aMap.getUiSettings().setZoomControlsEnabled(false);// 设置默认缩放按钮是否显示
        aMap.getUiSettings().setCompassEnabled(false);// 设置默认指南针是否显示
        aMap.getUiSettings().setScaleControlsEnabled(false);// 设置默认比例尺控件是否显示
        aMap.setMyLocationStyle(myLocationStyle);
//        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
//        aMap.setMyLocationType(AMap.MAP_TYPE_NORMAL);  // 设置定位的类型

        aMap.setOnCameraChangeListener(new AMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {

            }

            @Override
            public void onCameraChangeFinish(CameraPosition cameraPosition) {

            }
        });
    }

    private LocationSource MyLocationSource = new LocationSource() {

        //激活定位
        @Override
        public void activate(OnLocationChangedListener onLocationChangedListener) {
            mListener = onLocationChangedListener;
            if (mlocationClient == null) {
                mlocationClient = new AMapLocationClient(getActivity());
                mLocationOption = new AMapLocationClientOption();
                //设置定位监听
                mlocationClient.setLocationListener(MyAMapLocationListener);
                //设置为高精度定位模式
                mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
                mLocationOption.setNeedAddress(true);
                //设置定位间隔,单位毫秒,默认为2000ms
                mLocationOption.setInterval(5000);
                //设置定位参数
                mlocationClient.setLocationOption(mLocationOption);
                // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
                // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
                // 在定位结束后，在合适的生命周期调用onDestroy()方法
                // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
                mlocationClient.startLocation();
            }
        }

        //停止定位
        @Override
        public void deactivate() {
            mListener = null;
            if (mlocationClient != null) {
                mlocationClient.stopLocation();
                mlocationClient.onDestroy();
            }
            mlocationClient = null;
        }
    };

    //定位成功
    private AMapLocationListener MyAMapLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            if (mListener != null && aMapLocation != null) {
                if (aMapLocation.getErrorCode() == 0) {
                    mListener.onLocationChanged(aMapLocation);// 显示系统小蓝点
                    LatLng latLng = new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());

                    aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
                    if (null != mlocationClient) {
                        mlocationClient.stopLocation();
                        mlocationClient.onDestroy();
                    }
                } else {
                    String errText = "定位失败," + aMapLocation.getErrorCode() + ": " + aMapLocation.getErrorInfo();
                    LogUtils.e("定位失败  AmapErr", errText);
                }
            }
        }
    };

    @Override
    public void initListener() {
        aMap.setOnMapLoadedListener(() -> {
            Message msg = handler.obtainMessage();
            msg.what = AMAP_LOADED;
            handler.sendMessage(msg);
        });
    }

    private void startMove() {
        if (mRePlay != null) {
            mRePlay.stopTrace();
        }
        mRePlay = rePlayTrace(mOriginLatLngList, mOriginRoleMarker);
    }

    /**
     * 将原始轨迹小人设置到起点
     */
    private void resetOriginRole() {
        if (mOriginLatLngList == null) {
            return;
        }
        LatLng startLatLng = mOriginLatLngList.get(0);
        if (mOriginRoleMarker != null) {
            mOriginRoleMarker.setPosition(startLatLng);
        }
    }

    /**
     * 轨迹回放方法
     */
    private TraceRePlay rePlayTrace(List<LatLng> list, final Marker updateMarker) {
        TraceRePlay replay = new TraceRePlay(list, 400,
                new TraceRePlay.TraceRePlayListener() {

                    @Override
                    public void onTraceUpdating(LatLng latLng) {
                        if (updateMarker != null) {
                            updateMarker.setPosition(latLng); // 更新小人实现轨迹回放
                        }
                    }

                    @Override
                    public void onTraceUpdateFinish() {

                    }
                });
        mThreadPool.execute(replay);
        return replay;
    }

    private LatLngBounds getBounds() {
        LatLngBounds.Builder b = LatLngBounds.builder();
        if (mOriginLatLngList == null) {
            return b.build();
        }
        for (int i = 0; i < mOriginLatLngList.size(); i++) {
            b.include(mOriginLatLngList.get(i));
        }
        return b.build();

    }

    /**
     * 地图上添加原始轨迹线路及起终点、轨迹动画小人
     *
     * @param startPoint
     * @param endPoint
     * @param originList
     */
    private void addOriginTrace(LatLng startPoint, LatLng endPoint,
                                List<LatLng> originList) {
        polylineOptions.addAll(originList);
        mOriginPolyline = aMap.addPolyline(polylineOptions);
        mOriginStartMarker = aMap.addMarker(new MarkerOptions().position(
                startPoint).icon(
                BitmapDescriptorFactory.fromResource(R.drawable.sport_start)));
        mOriginEndMarker = aMap.addMarker(new MarkerOptions().position(
                endPoint).icon(
                BitmapDescriptorFactory.fromResource(R.drawable.sport_end)));

        try {
            aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(getBounds(), 16));
            UIHelper.postTaskDelay(this::startMove, 500);
        } catch (Exception e) {
            e.printStackTrace();
        }

        mOriginRoleMarker = aMap.addMarker(new MarkerOptions().position(
                startPoint).icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                .decodeResource(getResources(), R.drawable.sport_walk))));
    }

    @Override
    public void onResume() {
        if (null != mapView)
            mapView.onResume();
        super.onResume();
    }

    @Override
    public void onPause() {
        if (null != mapView)
            mapView.onPause();
        super.onPause();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (null != mapView)
            mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        if (null != mapView)
            mapView.onDestroy();

        if (null != handler)
            handler.removeCallbacksAndMessages(null);

        if (mThreadPool != null)
            mThreadPool.shutdownNow();

        super.onDestroy();
    }

}
