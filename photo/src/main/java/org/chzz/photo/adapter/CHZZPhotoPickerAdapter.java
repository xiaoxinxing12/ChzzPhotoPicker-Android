package org.chzz.photo.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import org.chzz.adapter.CHZZRecyclerViewAdapter;
import org.chzz.adapter.CHZZViewHolderHelper;
import org.chzz.photo.R;
import org.chzz.photo.imageloader.CHZZImage;
import org.chzz.photo.model.CHZZImageFolderModel;
import org.chzz.photo.util.CHZZPhotoPickerUtil;

import java.util.ArrayList;

/**
 * 作者:copy 邮件:2499551993@qq.com
 * 创建时间:16/6/28 上午11:09
 * 描述:
 */
public class CHZZPhotoPickerAdapter extends CHZZRecyclerViewAdapter<String> {
    private ArrayList<String> mSelectedImages = new ArrayList<>();
    private int mImageWidth;
    private int mImageHeight;
    private boolean mTakePhotoEnabled;
    private Activity mActivity;
    private Context mContext;

    public CHZZPhotoPickerAdapter(Activity activity, RecyclerView recyclerView) {
        super(recyclerView, R.layout.chzz_pp_item_photo_picker,null,null,null,null);
        mImageWidth = CHZZPhotoPickerUtil.getScreenWidth(recyclerView.getContext()) / 6;
        mImageHeight = mImageWidth;
        mActivity = activity;
    }

    @Override
    public void setItemChildListener(CHZZViewHolderHelper helper) {
        helper.setItemChildClickListener(R.id.iv_item_photo_picker_flag);
        helper.setItemChildClickListener(R.id.iv_item_photo_picker_photo);
    }

    @Override
    protected void fillData(CHZZViewHolderHelper helper, int position, String model) {
        if (mTakePhotoEnabled && position == 0) {
            helper.setVisibility(R.id.tv_item_photo_picker_tip, View.VISIBLE);
            helper.getImageView(R.id.iv_item_photo_picker_photo).setScaleType(ImageView.ScaleType.CENTER);
            helper.setImageResource(R.id.iv_item_photo_picker_photo, R.mipmap.chzz_pp_ic_gallery_camera);

            helper.setVisibility(R.id.iv_item_photo_picker_flag, View.INVISIBLE);
            helper.getImageView(R.id.iv_item_photo_picker_photo).setColorFilter(null);
        } else {
            helper.setVisibility(R.id.tv_item_photo_picker_tip, View.INVISIBLE);
            helper.getImageView(R.id.iv_item_photo_picker_photo).setScaleType(ImageView.ScaleType.CENTER_CROP);
            CHZZImage.displayImage(mActivity, helper.getImageView(R.id.iv_item_photo_picker_photo), model, R.mipmap.chzz_pp_ic_holder_dark, R.mipmap.chzz_pp_ic_holder_dark, mImageWidth, mImageHeight, null);

            helper.setVisibility(R.id.iv_item_photo_picker_flag, View.VISIBLE);

            if (mSelectedImages.contains(model)) {
                helper.setImageResource(R.id.iv_item_photo_picker_flag, R.mipmap.chzz_pp_ic_cb_checked);
                helper.getImageView(R.id.iv_item_photo_picker_photo).setColorFilter(helper.getConvertView().getResources().getColor(R.color.chzz_pp_photo_selected_mask));
            } else {
                helper.setImageResource(R.id.iv_item_photo_picker_flag, R.mipmap.chzz_pp_ic_cb_normal);
                helper.getImageView(R.id.iv_item_photo_picker_photo).setColorFilter(null);
            }
        }
    }

    public void setSelectedImages(ArrayList<String> selectedImages) {
        if (selectedImages != null) {
            mSelectedImages = selectedImages;
        }
        notifyDataSetChanged();
    }

    public ArrayList<String> getSelectedImages() {
        return mSelectedImages;
    }

    public int getSelectedCount() {
        return mSelectedImages.size();
    }

    public void setImageFolderModel(CHZZImageFolderModel imageFolderModel) {
        mTakePhotoEnabled = imageFolderModel.isTakePhotoEnabled();
        setData(imageFolderModel.getImages());
    }
}
