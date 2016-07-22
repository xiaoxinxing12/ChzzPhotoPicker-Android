package org.chzz.photo.util;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * 作者:copy 邮件:2499551993@qq.com
 * 创建时间:16/6/24 下午7:22
 * 描述:
 */
public class CHZZSpaceItemDecoration extends RecyclerView.ItemDecoration {
    public static final int SPAN_COUNT = 3;
    private int mSpace;

    public CHZZSpaceItemDecoration(int space) {
        mSpace = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.left = mSpace;
        outRect.right = mSpace;
        outRect.top = mSpace;
        outRect.bottom = mSpace;
    }
}