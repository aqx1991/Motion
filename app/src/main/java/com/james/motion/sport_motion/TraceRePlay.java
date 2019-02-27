package com.james.motion.sport_motion;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.amap.api.maps.model.LatLng;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * 轨迹回放工具类
 * 
 */
public class TraceRePlay implements Runnable {
	private TraceRePlayHandler mTraceHandler;
	private static final int TRACE_MOVE = 1;
	private static final int TRACE_FINISH = 2;
	private List<LatLng> mTraceList;
	private int mIntervalMillisecond;
	private TraceRePlayListener mTraceUpdateListener;
	private boolean mStop = false;

	/**
	 * 构造轨迹回放需要，List、时间间隔、回调listenenr
	 * 
	 * @param list
	 *            轨迹list
	 * @param intervalMillisecond
	 *            回调时间间隔
	 * @param listener
	 *            回调listener
	 */
	public TraceRePlay(List<LatLng> list, int intervalMillisecond,
                       TraceRePlayListener listener) {
		mTraceList = list;
		mIntervalMillisecond = intervalMillisecond;
		mTraceUpdateListener = listener;
		mTraceHandler = new TraceRePlayHandler(this);
	}

	public void stopTrace() {
		mStop = true;
	}

	/**
	 * 接收run发送的消息，达到按照设定时间间隔轮巡轨迹list目的
	 * 
	 */
	static class TraceRePlayHandler extends Handler {
		WeakReference<TraceRePlay> mTraceRePaly;

		public TraceRePlayHandler(TraceRePlay traceRePlay) {
			super(Looper.getMainLooper());
			mTraceRePaly = new WeakReference<>(traceRePlay);
		}

		@Override
		public void handleMessage(Message message) {
			super.handleMessage(message);
			TraceRePlay trace = mTraceRePaly.get();
			switch (message.what) {
			case TRACE_MOVE:
				LatLng latLng = (LatLng) message.obj;
				if (trace.mTraceUpdateListener != null) {
					trace.mTraceUpdateListener.onTraceUpdating(latLng);
				}
				break;
			case TRACE_FINISH:
				if (trace.mTraceUpdateListener != null) {
					trace.mTraceUpdateListener.onTraceUpdateFinish();
				}
				break;
			default:
				break;
			}
		}
	}

	public interface TraceRePlayListener {
		/**
		 * 轨迹回放过程回调
		 * 
		 * @param latLng
		 */
		public void onTraceUpdating(LatLng latLng);

		/**
		 * 轨迹回放结束回调
		 */
		public void onTraceUpdateFinish();

	}

	/**
	 * 将mTraceList 按照给定的时间间隔和次序发消息给TraceRePlayHandler以达到轨迹回放效果
	 */
	@Override
	public void run() {
		if (mTraceList != null) {
			for (int i = 0; i < mTraceList.size(); i++) {
				if (mStop) {
					break;
				}
				LatLng latLng = mTraceList.get(i);
				Message message = mTraceHandler.obtainMessage();
				message.what = TRACE_MOVE;
				message.obj = latLng;
				mTraceHandler.sendMessage(message);
				try {
					Thread.sleep(mIntervalMillisecond);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			if (!mStop) {
				Message finishMessage = mTraceHandler.obtainMessage();
				finishMessage.what = TRACE_FINISH;
				mTraceHandler.sendMessage(finishMessage);
			}
		}
	}
}
