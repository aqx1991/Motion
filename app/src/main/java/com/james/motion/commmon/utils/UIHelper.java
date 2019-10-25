package com.james.motion.commmon.utils;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.DimenRes;
import androidx.annotation.LayoutRes;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.james.motion.MyApplication;

import java.util.List;

public class UIHelper {

    private static Context context;

    private static final String FRAGMENT_CON = "NoSaveStateFrameLayout";

    public static Rect getLocationInView(View parent, View child) {
        if (child == null || parent == null) {
            throw new IllegalArgumentException("parent and child can not be null .");
        }

        View decorView = null;
        Context context = child.getContext();
        if (context instanceof Activity) {
            decorView = ((Activity) context).getWindow().getDecorView();
        }

        Rect result = new Rect();
        Rect tmpRect = new Rect();

        View tmp = child;

        if (child == parent) {
            child.getHitRect(result);
            return result;
        }
        while (tmp != decorView && tmp != parent) {
            tmp.getHitRect(tmpRect);
            if (!tmp.getClass().equals(FRAGMENT_CON)) {
                result.left += tmpRect.left;
                result.top += tmpRect.top;
            }
            tmp = (View) tmp.getParent();

            //added by isanwenyu@163.com fix bug #21 the wrong rect user will received in ViewPager
            if (tmp != null && tmp.getParent() != null && (tmp.getParent() instanceof ViewPager)) {
                tmp = (View) tmp.getParent();
            }
        }
        result.right = result.left + child.getMeasuredWidth();
        result.bottom = result.top + child.getMeasuredHeight();
        return result;
    }

    /**
     * 获取手机的密度
     */
    public static float getDensity(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.density;
    }

    public static int dp2px(Context context, float dp) {
        return (int) Math.ceil((double) (context.getResources().getDisplayMetrics().density * dp));
    }

    /**
     * dp转px
     */
    public static int dip2px(float dpValue) {
        float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static boolean hasData(TextView... edits) {
        for (TextView editText : edits) {
            if (!TextUtils.isEmpty(editText.getText().toString().trim())) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasEmpty(List<TextView> edits) {
        for (TextView editText : edits) {
            if (TextUtils.isEmpty(editText.getText().toString().trim())) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasEmpty(TextView... edits) {
        for (TextView editText : edits) {
            if (TextUtils.isEmpty(editText.getText().toString().trim())) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasEmpty(ImageView[] edits) {
        for (ImageView imageView : edits) {
            if (TextUtils.isEmpty(imageView.getTag().toString().trim())) {
                return true;
            }
        }
        return false;
    }


    public static void setRightDrawable(TextView textView, int draw) {
        Drawable drawable = UIHelper.getDrawable(draw);
        try {
            assert drawable != null;
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            textView.setCompoundDrawables(null, null, drawable, null);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public static void setTopDrawable(TextView textView, int draw) {
        Drawable drawable = UIHelper.getDrawable(draw);
        try {
            assert drawable != null;
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            textView.setCompoundDrawables(null, drawable, null, null);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public static void setLeftDrawable(TextView textView, int draw) {
        Drawable drawable = UIHelper.getDrawable(draw);
        try {
            assert drawable != null;
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            textView.setCompoundDrawables(drawable, null, null, null);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public static void setLeftDrawable(TextView textView, Drawable drawable) {
        try {
            assert drawable != null;
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            textView.setCompoundDrawables(drawable, null, null, null);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }


    /**
     * 资源ID获取String
     */
    public static String getString(int stringId) {
        Context context = MyApplication.getInstance();
        if (context != null) {
            return context.getString(stringId);
        }
        return " ";
    }

    public static String getString(int stringId, Object... formatArgs) {
        Context context = MyApplication.getInstance();
        if (context != null) {
            return context.getString(stringId, formatArgs);
        }
        return "";
    }

    /**
     * 获取尺寸
     */
    public static int getDimension(@DimenRes int dimenRes) {
        return (int) MyApplication.getInstance().getResources().getDimension(dimenRes);
    }

    /**
     * 获取颜色
     */
    public static int getColor(@ColorRes int color) {
        return ContextCompat.getColor(MyApplication.getInstance(), color);
    }

    /**
     * 字符串转16进制整数
     */
    public static int getColor(String color) {
        if (TextUtils.isEmpty(color) || !Validator.checkColor(color)) {
            return 0;
        }
        return Color.parseColor(color);
    }

    /**
     * 字符串转16进制整数,带默认值
     */
    public static int getColor(String color, String defaultColor) {
        if (TextUtils.isEmpty(color) || !Validator.checkColor(color)) {
            return getColor(defaultColor);
        }
        return Color.parseColor(color);
    }

    public static int getColor(String color, @ColorRes int defaultColor) {
        if (TextUtils.isEmpty(color) || !Validator.checkColor(color)) {
            return getColor(defaultColor);
        }
        return Color.parseColor(color);
    }


    /**
     * 获取Drawable
     */
    public static Drawable getDrawable(int drawable) {
        return ContextCompat.getDrawable(MyApplication.getInstance(), drawable);
    }

    public static View inflaterLayout(Context context, @LayoutRes int layoutRes) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return inflater.inflate(layoutRes, null);
    }

    /**
     * HTML颜色
     */
    public static String setHtmlColor(String color, String content) {
        if (TextUtils.isEmpty(color)) return content;
        return String.format(Htmls.color, color, content);
    }

    /**
     * 圆角Drawable
     *
     * @param radius 圆角
     * @param color  填充颜色
     */
    public static GradientDrawable getShapeDrawable(int radius, @ColorInt int color) {
        GradientDrawable gd = new GradientDrawable();
        gd.setColor(color);
        gd.setCornerRadius(radius);
        return gd;
    }

    public static void clipContent(String content) {
        ClipboardManager cm = (ClipboardManager) MyApplication.getInstance().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData mClipData = ClipData.newPlainText("Label", content);
        if (cm != null) {
            cm.setPrimaryClip(mClipData);
        }
    }

    public static String getClipContent() {
        ClipboardManager cm = (ClipboardManager) MyApplication.getInstance().getSystemService(Context.CLIPBOARD_SERVICE);
        if (cm != null && cm.getPrimaryClipDescription().hasMimeType(
                ClipDescription.MIMETYPE_TEXT_PLAIN)) {
            ClipData cdText = cm.getPrimaryClip();
            ClipData.Item item = cdText.getItemAt(0);
            return item.getText().toString();
        }
        return "";
    }

    /**
     * 得到应用程序的包名
     *
     * @return
     */
    public static String getPackageName() {
        return MyApplication.getInstance().getPackageName();
    }

    /**
     * 延迟执行任务
     *
     * @param task
     * @param delayMillis
     */
    public static void postTaskDelay(Runnable task, int delayMillis) {
        getMainThreadHandler().postDelayed(task, delayMillis);
    }

    /**
     * 得到主线程Handler
     *
     * @return
     */
    public static Handler getMainThreadHandler() {
        return MyApplication.getHandler();
    }


    public static void ScaleUpDowm(View view) {
        ScaleAnimation animation = new ScaleAnimation(1.0f, 1.0f, 0.0f, 1.0f);
        animation.setRepeatCount(-1);
        animation.setRepeatMode(Animation.RESTART);
        animation.setInterpolator(new LinearInterpolator());
        animation.setDuration(1200);
        view.startAnimation(animation);
    }

    public static Context getContext() {
        if (context != null) {
            return context;
        }
        throw new NullPointerException("请先调用init()方法");
    }

}
