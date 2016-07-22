package org.chzz.photo.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 作者:copy 邮件:2499551993@qq.com
 * 创建时间:16/6/24 下午4:49
 * 描述:
 */
public class CHZZHackyViewPager extends ViewPager {

    public CHZZHackyViewPager(Context context) {
        super(context);
    }

    public CHZZHackyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            return super.onInterceptTouchEvent(ev);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return false;
        }
    }
}