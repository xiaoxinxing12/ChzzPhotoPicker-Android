package org.chzz.photo.pw;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

import org.chzz.adapter.CHZZOnRVItemClickListener;
import org.chzz.adapter.CHZZRecyclerViewAdapter;
import org.chzz.adapter.CHZZViewHolderHelper;
import org.chzz.photo.R;
import org.chzz.photo.imageloader.CHZZImage;
import org.chzz.photo.model.CHZZImageFolderModel;
import org.chzz.photo.util.CHZZPhotoPickerUtil;

import java.util.ArrayList;


/**
 * 作者:copy 邮件:2499551993@qq.com
 * 创建时间:16/6/24 下午6:19
 * 描述:选择图片目录的PopupWindow
 */
public class CHZZPhotoFolderPw extends CHZZBasePopupWindow implements CHZZOnRVItemClickListener {
    public static final int ANIM_DURATION = 300;
    private LinearLayout mRootLl;
    private RecyclerView mContentRv;
    private FolderAdapter mFolderAdapter;
    private Delegate mDelegate;
    private int mCurrentPosition;

    public CHZZPhotoFolderPw(Activity activity, View anchorView, Delegate delegate) {
        super(activity, R.layout.chzz_pp_pw_photo_folder, anchorView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        mDelegate = delegate;
    }

    @Override
    protected void initView() {
        mRootLl = getViewById(R.id.ll_photo_folder_root);
        mContentRv = getViewById(R.id.rv_photo_folder_content);
    }

    @Override
    protected void setListener() {
        mRootLl.setOnClickListener(this);
        mFolderAdapter = new FolderAdapter(mContentRv);
        mFolderAdapter.setOnRVItemClickListener(this);
    }

    @Override
    protected void processLogic() {
        setAnimationStyle(android.R.style.Animation);
        setBackgroundDrawable(new ColorDrawable(0x90000000));

        mContentRv.setLayoutManager(new LinearLayoutManager(mActivity));
        mContentRv.setAdapter(mFolderAdapter);
    }

    /**
     * 设置目录数据集合
     *
     * @param datas
     */
    public void setData(ArrayList<CHZZImageFolderModel> datas) {
        mFolderAdapter.setDatas(datas);
    }

    @Override
    public void show() {
        showAsDropDown(mAnchorView);
        ViewCompat.animate(mContentRv).translationY(-mWindowRootView.getHeight()).setDuration(0).start();
        ViewCompat.animate(mContentRv).translationY(0).setDuration(ANIM_DURATION).start();
        ViewCompat.animate(mRootLl).alpha(0).setDuration(0).start();
        ViewCompat.animate(mRootLl).alpha(1).setDuration(ANIM_DURATION).start();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.ll_photo_folder_root) {
            dismiss();
        }
    }

    @Override
    public void dismiss() {
        ViewCompat.animate(mContentRv).translationY(-mWindowRootView.getHeight()).setDuration(ANIM_DURATION).start();
        ViewCompat.animate(mRootLl).alpha(1).setDuration(0).start();
        ViewCompat.animate(mRootLl).alpha(0).setDuration(ANIM_DURATION).start();

        if (mDelegate != null) {
            mDelegate.executeDismissAnim();
        }

        mContentRv.postDelayed(new Runnable() {
            @Override
            public void run() {
                CHZZPhotoFolderPw.super.dismiss();
            }
        }, ANIM_DURATION);
    }

    public int getCurrentPosition() {
        return mCurrentPosition;
    }

    @Override
    public void onRVItemClick(ViewGroup viewGroup, View view, int position) {
        if (mDelegate != null && mCurrentPosition != position) {
            mDelegate.onSelectedFolder(position);
        }
        mCurrentPosition = position;
        dismiss();
    }

    private class FolderAdapter extends CHZZRecyclerViewAdapter<CHZZImageFolderModel> {
        private int mImageWidth;
        private int mImageHeight;

        public FolderAdapter(RecyclerView recyclerView) {
            super(recyclerView, R.layout.chzz_pp_item_photo_folder);

            mDatas = new ArrayList<>();
            mImageWidth = CHZZPhotoPickerUtil.getScreenWidth(mActivity) / 10;
            mImageHeight = mImageWidth;
        }

        @Override
        protected void fillData(CHZZViewHolderHelper helper, int position, CHZZImageFolderModel model) {
            helper.setText(R.id.tv_item_photo_folder_name, model.name);
            helper.setText(R.id.tv_item_photo_folder_count, String.valueOf(model.getCount()));
            CHZZImage.displayImage(mActivity, helper.getImageView(R.id.iv_item_photo_folder_photo), model.coverPath, R.mipmap.chzz_pp_ic_holder_light, R.mipmap.chzz_pp_ic_holder_light, mImageWidth, mImageHeight, null);
        }
    }

    public interface Delegate {
        void onSelectedFolder(int position);

        void executeDismissAnim();
    }
}