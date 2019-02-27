package com.james.motion.commmon.utils;

import android.util.Log;

import com.james.motion.MyApplication;

/**
 * 描述: Log管理类
 * 作者: james
 * 日期: 2019/2/25 15:45
 * 类名: LogUtils
 */
public class LogUtils {

    private static final String TAG = "LogUtils";

    private static final boolean isDebug = MyApplication.DEBUG_MODE;// 是否需要打印bug，可以在application的onCreate函数里面初始化

    // 下面四个是默认tag的函数
    public static void i(String msg) {
        if (isDebug)
            Log.i(TAG, Utils.isEmpry(msg));
    }

    public static void d(String msg) {
        if (isDebug)
            Log.d(TAG, Utils.isEmpry(msg));
    }

    public static void e(String msg) {
        if (isDebug)
            Log.e(TAG, Utils.isEmpry(msg));
    }

    public static void e(String msg, Exception e) {
        if (isDebug)
            Log.w(TAG, Utils.isEmpry(msg), e);
    }

    public static void e(String tag, String msg, Exception e) {
        if (isDebug)
            Log.e(tag, Utils.isEmpry(msg));
        if (null != e)
            e.printStackTrace();
    }

    public static void v(String msg) {
        if (isDebug)
            Log.v(TAG, Utils.isEmpry(msg));
    }

    public static void w(String msg) {
        if (isDebug)
            Log.w(TAG, Utils.isEmpry(msg));
    }

    // 下面是传入自定义tag的函数
    public static void i(String tag, String msg) {
        if (isDebug)
            Log.i(tag, Utils.isEmpry(msg));
    }

    public static void d(String tag, String msg) {
        if (isDebug)
            Log.d(tag, Utils.isEmpry(msg));
    }

    public static void e(String tag, String msg) {
        if (isDebug)
            Log.e(tag, Utils.isEmpry(msg));
    }

    public static void v(String tag, String msg) {
        if (isDebug)
            Log.v(tag, Utils.isEmpry(msg));
    }

    public static void w(String tag, String msg) {
        if (isDebug)
            Log.w(tag, Utils.isEmpry(msg));
    }

    /**
     * 分段打印出较长log文本
     *
     * @param logContent 打印文本
     * @param showLength 规定每段显示的长度（AndroidStudio控制台打印log的最大信息量大小为4k）
     * @param tag        打印log的标记
     */
    public static void showLargeLog(String logContent, int showLength,
                                    String tag) {
        if (!isDebug)
            return;
        if (!Utils.isString(logContent))
            return;

        if (logContent.length() > showLength) {
            String show = logContent.substring(0, showLength);
            e(tag, show);
            /* 剩余的字符串如果大于规定显示的长度，截取剩余字符串进行递归，否则打印结果 */
            if ((logContent.length() - showLength) > showLength) {
                String partLog = logContent.substring(showLength,
                        logContent.length());
                showLargeLog(partLog, showLength, tag);
            } else {
                String printLog = logContent.substring(showLength,
                        logContent.length());
                e(tag, printLog);
            }

        } else {
            e(tag, logContent);
        }
    }

    /**
     * 分段打印出较长log文本
     *
     * @param logContent 打印文本
     * @param showLength 规定每段显示的长度（AndroidStudio控制台打印log的最大信息量大小为4k）
     * @param tag        打印log的标记
     */
    public static void showLargeLog(LogType logType, String logContent, int showLength,
                                    String tag) {
        if (!isDebug)
            return;
        if (!Utils.isString(logContent))
            return;

        if (logContent.length() > showLength) {
            String show = logContent.substring(0, showLength);
            switch (logType) {
                case LOG_I:
                    i(tag, show);
                    break;
                case LOG_D:
                    d(tag, show);
                    break;
                case LOG_E:
                    e(tag, show);
                case LOG_V:
                    v(tag, show);
                case LOG_W:
                    w(tag, show);
                default:
                    break;
            }
            /* 剩余的字符串如果大于规定显示的长度，截取剩余字符串进行递归，否则打印结果 */
            if ((logContent.length() - showLength) > showLength) {
                String partLog = logContent.substring(showLength,
                        logContent.length());
                showLargeLog(logType, partLog, showLength, tag);
            } else {
                String printLog = logContent.substring(showLength,
                        logContent.length());
                switch (logType) {
                    case LOG_I:
                        i(tag, printLog);
                        break;
                    case LOG_D:
                        d(tag, printLog);
                        break;
                    case LOG_E:
                        e(tag, printLog);
                    case LOG_V:
                        v(tag, printLog);
                    case LOG_W:
                        w(tag, printLog);
                    default:
                        break;
                }
            }

        } else {
            switch (logType) {
                case LOG_I:
                    i(tag, logContent);
                    break;
                case LOG_D:
                    d(tag, logContent);
                    break;
                case LOG_E:
                    e(tag, logContent);
                case LOG_V:
                    v(tag, logContent);
                case LOG_W:
                    w(tag, logContent);
                default:
                    break;
            }
        }
    }

    public enum LogType {
        LOG_I, LOG_D, LOG_E, LOG_V, LOG_W
    }
}
