package org.chzz.photo.imageloader;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.DrawableRes;
import android.view.View;
import android.widget.ImageView;

/**
 * 作者:copy 邮件:2499551993@qq.com
 * 创建时间:16/6/25 下午4:30
 * 描述:
 */
public abstract class CHZZImageLoader {

    protected String getPath(String path) {
        if (path == null) {
            path = "";
        }

        if (!path.startsWith("http") && !path.startsWith("file")) {
            path = "file://" + path;
        }
        return path;
    }

    public abstract void displayImage(Activity activity, ImageView imageView, String path, @DrawableRes int loadingResId, @DrawableRes int failResId, int width, int height, DisplayDelegate delegate);

    public abstract void downloadImage(Context context, String path, DownloadDelegate delegate);

    public interface DisplayDelegate {
        void onSuccess(View view, String path);
    }

    public interface DownloadDelegate {
        void onSuccess(String path, Bitmap bitmap);

        void onFailed(String path);
    }
}