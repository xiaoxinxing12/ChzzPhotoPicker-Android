package org.chzz.photo.widget;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import org.chzz.adapter.CHZZOnItemChildClickListener;
import org.chzz.adapter.CHZZOnRVItemClickListener;
import org.chzz.adapter.CHZZRecyclerViewAdapter;
import org.chzz.adapter.CHZZRecyclerViewHolder;
import org.chzz.adapter.CHZZViewHolderHelper;
import org.chzz.photo.R;
import org.chzz.photo.imageloader.CHZZImage;
import org.chzz.photo.util.CHZZPhotoPickerUtil;
import org.chzz.photo.util.CHZZSpaceItemDecoration;

import java.util.ArrayList;

/**
 * 作者:copy 邮件:2499551993@qq.com
 * 创建时间:16/7/8 下午4:51
 * 描述:
 */
public class CHZZSortableNinePhotoLayout extends RecyclerView implements CHZZOnItemChildClickListener, CHZZOnRVItemClickListener {
    private static final int MAX_ITEM_COUNT = 9;
    private static final int MAX_SPAN_COUNT = 3;
    private PhotoAdapter mPhotoAdapter;
    private ItemTouchHelper mItemTouchHelper;
    private Delegate mDelegate;
    private GridLayoutManager mGridLayoutManager;
    private boolean mIsPlusSwitchOpened = true;
    private boolean mIsSortable = true;
    private Activity mActivity;

    public CHZZSortableNinePhotoLayout(Context context) {
        this(context, null);
    }

    public CHZZSortableNinePhotoLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CHZZSortableNinePhotoLayout(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        setOverScrollMode(OVER_SCROLL_NEVER);

        initAttrs(context, attrs);

        mItemTouchHelper = new ItemTouchHelper(new ItemTouchHelperCallback());
        mItemTouchHelper.attachToRecyclerView(this);

        mGridLayoutManager = new GridLayoutManager(context, MAX_SPAN_COUNT);
        setLayoutManager(mGridLayoutManager);
        addItemDecoration(new CHZZSpaceItemDecoration(getResources().getDimensionPixelSize(R.dimen.chzz_pp_size_photo_divider)));

        mPhotoAdapter = new PhotoAdapter(this);
        mPhotoAdapter.setOnItemChildClickListener(this);
        mPhotoAdapter.setOnRVItemClickListener(this);
        setAdapter(mPhotoAdapter);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CHZZSortableNinePhotoLayout);
        final int N = typedArray.getIndexCount();
        for (int i = 0; i < N; i++) {
            initAttr(typedArray.getIndex(i), typedArray);
        }
        typedArray.recycle();
    }

    private void initAttr(int attr, TypedArray typedArray) {
        if (attr == R.styleable.CHZZSortableNinePhotoLayout_bga_snpl_isPlusSwitchOpened) {
            mIsPlusSwitchOpened = typedArray.getBoolean(attr, mIsPlusSwitchOpened);
        } else if (attr == R.styleable.CHZZSortableNinePhotoLayout_bga_snpl_isSortable) {
            mIsSortable = typedArray.getBoolean(attr, mIsSortable);
        }
    }

    public void init(Activity activity) {
        mActivity = activity;
    }

    /**
     * 设置是否可拖拽排序
     *
     * @param isSortable
     */
    public void setIsSortable(boolean isSortable) {
        mIsSortable = isSortable;
    }

    /**
     * 设置图片路径数据集合
     *
     * @param photos
     */
    public void setDatas(ArrayList<String> photos) {
        if (mActivity == null) {
            throw new RuntimeException("请先调用init方法进行初始化");
        }

        mPhotoAdapter.setData(photos);
        updateHeight();
    }

    private void updateHeight() {
        if (mPhotoAdapter.getItemCount() > 0 && mPhotoAdapter.getItemCount() < MAX_SPAN_COUNT) {
            mGridLayoutManager.setSpanCount(mPhotoAdapter.getItemCount());
        } else {
            mGridLayoutManager.setSpanCount(MAX_SPAN_COUNT);
        }
        int itemWidth = CHZZPhotoPickerUtil.getScreenWidth(getContext()) / (MAX_SPAN_COUNT + 1);
        int width = itemWidth * mGridLayoutManager.getSpanCount();
        int height = 0;
        if (mPhotoAdapter.getItemCount() != 0) {
            int rowCount = (mPhotoAdapter.getItemCount() - 1) / mGridLayoutManager.getSpanCount() + 1;
            height = itemWidth * rowCount;
        }
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        layoutParams.width = width;
        layoutParams.height = height;
        setLayoutParams(layoutParams);
    }

    /**
     * 获取图片路劲数据集合
     *
     * @return
     */
    public ArrayList<String> getDatas() {
        return (ArrayList<String>) mPhotoAdapter.getData();
    }

    /**
     * 删除指定索引位置的图片
     *
     * @param position
     */
    public void removeItem(int position) {
        mPhotoAdapter.removeItem(position);
        updateHeight();
    }

    /**
     * 获取图片总数
     *
     * @return
     */
    public int getItemCount() {
        return mPhotoAdapter.getData().size();
    }

    @Override
    public void onItemChildClick(ViewGroup parent, View childView, int position) {
        if (mDelegate != null) {
            mDelegate.onClickDeleteNinePhotoItem(this, childView, position, mPhotoAdapter.getItem(position), (ArrayList<String>) mPhotoAdapter.getData());
        }
    }

    @Override
    public void onRVItemClick(ViewGroup parent, View itemView, int position) {
        if (mPhotoAdapter.isPlusItem(position)) {
            if (mDelegate != null) {
                mDelegate.onClickAddNinePhotoItem(this, itemView, position, (ArrayList<String>) mPhotoAdapter.getData());
            }
        } else {
            if (mDelegate != null && ViewCompat.getScaleX(itemView) <= 1.0f) {
                mDelegate.onClickNinePhotoItem(this, itemView, position, mPhotoAdapter.getItem(position), (ArrayList<String>) mPhotoAdapter.getData());
            }
        }
    }

    public void setIsPlusSwitchOpened(boolean isPlusSwitchOpened) {
        mIsPlusSwitchOpened = isPlusSwitchOpened;
        updateHeight();
    }

    public void setDelegate(Delegate delegate) {
        mDelegate = delegate;
    }

    private class PhotoAdapter extends CHZZRecyclerViewAdapter<String> {
        private int mImageWidth;
        private int mImageHeight;

        public PhotoAdapter(RecyclerView recyclerView) {
            super(recyclerView, R.layout.chzz_pp_item_nine_photo,null,null,null,null);
            mImageWidth = CHZZPhotoPickerUtil.getScreenWidth(recyclerView.getContext()) / 6;
            mImageHeight = mImageWidth;
        }

        @Override
        protected void setItemChildListener(CHZZViewHolderHelper helper) {
            helper.setItemChildClickListener(R.id.iv_item_nine_photo_flag);
        }

        @Override
        public int getItemCount() {
            if (mIsPlusSwitchOpened && super.getItemCount() < MAX_ITEM_COUNT) {
                return super.getItemCount() + 1;
            }

            return super.getItemCount();
        }

        @Override
        public String getItem(int position) {
            if (isPlusItem(position)) {
                return null;
            }

            return super.getItem(position);
        }

        public boolean isPlusItem(int position) {
            return mIsPlusSwitchOpened && super.getItemCount() < MAX_ITEM_COUNT && position == getItemCount() - 1;
        }

        @Override
        protected void fillData(CHZZViewHolderHelper helper, int position, String model) {
            if (isPlusItem(position)) {
                helper.setVisibility(R.id.iv_item_nine_photo_flag, View.GONE);
                helper.setImageResource(R.id.iv_item_nine_photo_photo, R.mipmap.chzz_pp_ic_plus);
            } else {
                helper.setVisibility(R.id.iv_item_nine_photo_flag, View.VISIBLE);
                CHZZImage.displayImage(mActivity, helper.getImageView(R.id.iv_item_nine_photo_photo), model, R.mipmap.chzz_pp_ic_holder_light, R.mipmap.chzz_pp_ic_holder_light, mImageWidth, mImageHeight, null);
            }
        }
    }

    private class ItemTouchHelperCallback extends ItemTouchHelper.Callback {

        @Override
        public boolean isLongPressDragEnabled() {
            return mIsSortable && mPhotoAdapter.getData().size() > 1;
        }

        @Override
        public boolean isItemViewSwipeEnabled() {
            return false;
        }

        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            if (mPhotoAdapter.isPlusItem(viewHolder.getAdapterPosition())) {
                return ItemTouchHelper.ACTION_STATE_IDLE;
            }

            int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.START | ItemTouchHelper.END;
            int swipeFlags = dragFlags;
            return makeMovementFlags(dragFlags, swipeFlags);
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder source, RecyclerView.ViewHolder target) {
            if (source.getItemViewType() != target.getItemViewType() || mPhotoAdapter.isPlusItem(target.getAdapterPosition())) {
                return false;
            }
            mPhotoAdapter.moveItem(source.getAdapterPosition(), target.getAdapterPosition());
            return true;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        }

        @Override
        public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }

        @Override
        public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
            if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
                ViewCompat.setScaleX(viewHolder.itemView, 1.2f);
                ViewCompat.setScaleY(viewHolder.itemView, 1.2f);
                ((CHZZRecyclerViewHolder) viewHolder).getViewHolderHelper().getImageView(R.id.iv_item_nine_photo_photo).setColorFilter(getResources().getColor(R.color.chzz_pp_photo_selected_mask));
            }
            super.onSelectedChanged(viewHolder, actionState);
        }

        @Override
        public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            ViewCompat.setScaleX(viewHolder.itemView, 1.0f);
            ViewCompat.setScaleY(viewHolder.itemView, 1.0f);
            ((CHZZRecyclerViewHolder) viewHolder).getViewHolderHelper().getImageView(R.id.iv_item_nine_photo_photo).setColorFilter(null);
            super.clearView(recyclerView, viewHolder);
        }
    }

    public interface Delegate {
        void onClickAddNinePhotoItem(CHZZSortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, ArrayList<String> models);

        void onClickDeleteNinePhotoItem(CHZZSortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, String model, ArrayList<String> models);

        void onClickNinePhotoItem(CHZZSortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, String model, ArrayList<String> models);
    }
}