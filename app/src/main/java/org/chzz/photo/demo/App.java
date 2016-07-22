package org.chzz.photo.demo;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

/**
 * 作者:copy 邮件:2499551993@qq.com
 * 创建时间:16/7/11 上午10:23
 * 描述:
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        LeakCanary.install(this);
    }
}