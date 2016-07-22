package org.chzz.photo.adapter;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import org.chzz.photo.R;
import org.chzz.photo.imageloader.CHZZImage;
import org.chzz.photo.util.CHZZBrowserPhotoViewAttacher;
import org.chzz.photo.util.CHZZPhotoPickerUtil;
import org.chzz.photo.widget.CHZZPhotoImageView;

import java.util.ArrayList;

import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * 作者:copy 邮件:2499551993@qq.com
 * 创建时间:16/6/27 下午6:35
 * 描述:
 */
public class CHZZPhotoPageAdapter extends PagerAdapter {
    private ArrayList<String> mPreviewImages;
    private PhotoViewAttacher.OnViewTapListener mOnViewTapListener;
    private Activity mActivity;

    public CHZZPhotoPageAdapter(Activity activity, PhotoViewAttacher.OnViewTapListener onViewTapListener, ArrayList<String> previewImages) {
        mOnViewTapListener = onViewTapListener;
        mPreviewImages = previewImages;
        mActivity = activity;
    }

    @Override
    public int getCount() {
        return mPreviewImages == null ? 0 : mPreviewImages.size();
    }

    @Override
    public View instantiateItem(ViewGroup container, int position) {
        final CHZZPhotoImageView imageView = new CHZZPhotoImageView(container.getContext());
        container.addView(imageView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        final CHZZBrowserPhotoViewAttacher photoViewAttacher = new CHZZBrowserPhotoViewAttacher(imageView);
        photoViewAttacher.setOnViewTapListener(mOnViewTapListener);
        imageView.setDelegate(new CHZZPhotoImageView.Delegate() {
            @Override
            public void onDrawableChanged(Drawable drawable) {
                if (drawable != null && drawable.getIntrinsicHeight() > drawable.getIntrinsicWidth() && drawable.getIntrinsicHeight() > CHZZPhotoPickerUtil.getScreenHeight(imageView.getContext())) {
                    photoViewAttacher.setIsSetTopCrop(true);
                    photoViewAttacher.setUpdateBaseMatrix();
                } else {
                    photoViewAttacher.update();
                }
            }
        });

        CHZZImage.displayImage(mActivity, imageView, mPreviewImages.get(position), R.mipmap.chzz_pp_ic_holder_dark, R.mipmap.chzz_pp_ic_holder_dark, CHZZPhotoPickerUtil.getScreenWidth(imageView.getContext()), CHZZPhotoPickerUtil.getScreenHeight(imageView.getContext()), null);

        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    public String getItem(int position) {
        return mPreviewImages == null ? "" : mPreviewImages.get(position);
    }
}