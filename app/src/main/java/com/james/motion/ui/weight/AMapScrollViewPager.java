package com.james.motion.ui.weight;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.viewpager.widget.ViewPager;

/**
 * 描述: 重写ViewPager canScroll方法，解决ViewPager和地图横向滑动冲突
 * 作者: james
 * 日期: 2019/2/27 15:16
 * 类名: AMapScrollViewPager
*/
public class AMapScrollViewPager extends ViewPager {

    public AMapScrollViewPager(Context context) {
        super(context);
    }

    public AMapScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean canScroll(View v, boolean checkV, int dx, int x, int y) {
        if (Math.abs(dx) > 50) {
            return super.canScroll(v, checkV, dx, x, y);
        } else {
            return true;
        }
    }
}
