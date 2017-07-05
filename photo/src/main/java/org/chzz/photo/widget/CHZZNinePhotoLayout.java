package org.chzz.photo.widget;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;

import org.chzz.adapter.CHZZAdapterViewAdapter;
import org.chzz.adapter.CHZZViewHolderHelper;
import org.chzz.photo.R;
import org.chzz.photo.imageloader.CHZZImage;
import org.chzz.photo.util.CHZZPhotoPickerUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者:copy 邮件:2499551993@qq.com
 * 创建时间:16/7/8 下午2:41
 * 描述:
 */
public class CHZZNinePhotoLayout extends FrameLayout implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener, View.OnClickListener, View.OnLongClickListener {
    private PhotoAdapter mPhotoAdapter;
    private ImageView mPhotoIv;
    private CHZZHeightWrapGridView mPhotoGv;
    private Delegate mDelegate;
    private int mCurrentClickItemPosition;
    private Activity mActivity;

    public CHZZNinePhotoLayout(Context context) {
        this(context, null);
    }

    public CHZZNinePhotoLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CHZZNinePhotoLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPhotoIv = new ImageView(context);
        mPhotoIv.setClickable(true);
        mPhotoIv.setOnClickListener(this);
        mPhotoIv.setOnLongClickListener(this);

        mPhotoGv = new CHZZHeightWrapGridView(context);
        int spacing = context.getResources().getDimensionPixelSize(R.dimen.chzz_pp_size_photo_divider);
        mPhotoGv.setHorizontalSpacing(spacing);
        mPhotoGv.setVerticalSpacing(spacing);
        mPhotoGv.setNumColumns(3);
        mPhotoGv.setOnItemClickListener(this);
        mPhotoGv.setOnItemLongClickListener(this);
        mPhotoAdapter = new PhotoAdapter(context);
        mPhotoGv.setAdapter(mPhotoAdapter);

        addView(mPhotoIv, new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        addView(mPhotoGv);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        mCurrentClickItemPosition = position;
        if (mDelegate != null) {
            mDelegate.onClickNinePhotoItem(this, view, mCurrentClickItemPosition, mPhotoAdapter.getItem(mCurrentClickItemPosition), mPhotoAdapter.getData());
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
        mCurrentClickItemPosition = position;
        if (mDelegate != null) {
            return mDelegate.onLongClickNinePhotoItem(this, view, position, mPhotoAdapter.getItem(position), mPhotoAdapter.getData());
        }
        return false;
    }

    @Override
    public void onClick(View view) {
        mCurrentClickItemPosition = 0;
        if (mDelegate != null) {
            mDelegate.onClickNinePhotoItem(this, view, mCurrentClickItemPosition, mPhotoAdapter.getItem(mCurrentClickItemPosition), mPhotoAdapter.getData());
        }
    }

    @Override
    public boolean onLongClick(View view) {
        mCurrentClickItemPosition = 0;
        if (mDelegate != null) {
            return mDelegate.onLongClickNinePhotoItem(this, view, mCurrentClickItemPosition, mPhotoAdapter.getItem(mCurrentClickItemPosition), mPhotoAdapter.getData());
        }
        return false;
    }

    public void init(Activity activity) {
        mActivity = activity;
    }

    public void setDatas(ArrayList<String> photos) {
        if (mActivity == null) {
            throw new RuntimeException("请先调用init方法进行初始化");
        }

        int itemWidth = CHZZPhotoPickerUtil.getScreenWidth(getContext()) / 4;
        if (photos.size() == 0) {
            setVisibility(GONE);
        } else if (photos.size() == 1) {
            setVisibility(VISIBLE);
            mPhotoGv.setVisibility(GONE);
            mPhotoAdapter.setData(photos);
            mPhotoIv.setVisibility(VISIBLE);

            mPhotoIv.setMaxWidth(itemWidth * 2);
            mPhotoIv.setMaxHeight(itemWidth * 2);

            CHZZImage.displayImage(mActivity, mPhotoIv, photos.get(0), R.mipmap.chzz_pp_ic_holder_light, R.mipmap.chzz_pp_ic_holder_light, itemWidth * 2, itemWidth * 2, null);
        } else {
            setVisibility(VISIBLE);
            mPhotoIv.setVisibility(GONE);
            mPhotoGv.setVisibility(VISIBLE);

            ViewGroup.LayoutParams layoutParams = mPhotoGv.getLayoutParams();
            if (photos.size() == 2) {
                mPhotoGv.setNumColumns(2);
                layoutParams.width = itemWidth * 2;
                layoutParams.height = itemWidth * 1;
            } else if (photos.size() == 4) {
                mPhotoGv.setNumColumns(2);
                layoutParams.width = itemWidth * 2;
                layoutParams.height = itemWidth * 2;
            } else {
                mPhotoGv.setNumColumns(3);
                layoutParams.width = itemWidth * 3;
                layoutParams.height = itemWidth * 3;
            }
            mPhotoGv.setLayoutParams(layoutParams);
            mPhotoAdapter.setData(photos);
        }
    }

    public void setDelegate(Delegate delegate) {
        mDelegate = delegate;
    }

    public ArrayList<String> getDatas() {
        return (ArrayList<String>) mPhotoAdapter.getData();
    }

    public int getItemCount() {
        return mPhotoAdapter.getCount();
    }

    public String getCurrentClickItem() {
        return mPhotoAdapter.getItem(mCurrentClickItemPosition);
    }

    public int getCurrentClickItemPosition() {
        return mCurrentClickItemPosition;
    }

    private class PhotoAdapter extends CHZZAdapterViewAdapter<String> {
        private int mImageWidth;
        private int mImageHeight;

        public PhotoAdapter(Context context) {
            super(context, R.layout.chzz_pp_item_nine_photo);
            mImageWidth = CHZZPhotoPickerUtil.getScreenWidth(context) / 6;
            mImageHeight = mImageWidth;
        }

        @Override
        protected void fillData(CHZZViewHolderHelper helper, int position, String model) {
            CHZZImage.displayImage(mActivity, helper.getImageView(R.id.iv_item_nine_photo_photo), model, R.mipmap.chzz_pp_ic_holder_light, R.mipmap.chzz_pp_ic_holder_light, mImageWidth, mImageHeight, null);
        }
    }

    public interface Delegate {
        void onClickNinePhotoItem(CHZZNinePhotoLayout ninePhotoLayout, View view, int position, String model, List<String> models);

        boolean onLongClickNinePhotoItem(CHZZNinePhotoLayout ninePhotoLayout, View view, int position, String model, List<String> models);
    }

    public abstract static class SimpleDelegate implements Delegate {
        @Override
        public boolean onLongClickNinePhotoItem(CHZZNinePhotoLayout ninePhotoLayout, View view, int position, String model, List<String> models) {
            return false;
        }
    }
}