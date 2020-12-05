package com.jarry.scrollcard

import android.util.Log
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import androidx.core.view.get

class ScrollCardBehavior : CoordinatorLayout.Behavior<CardItemLayout>() {

    //记录垂直方向的偏移量
    private var mInitialOffset: Int = 0

    /**
     * 测量child的宽高
     */
    override fun onMeasureChild(
        parent: CoordinatorLayout,
        child: CardItemLayout,
        parentWidthMeasureSpec: Int,
        widthUsed: Int,
        parentHeightMeasureSpec: Int,
        heightUsed: Int
    ): Boolean {
        //计算当前控件高度：=父容器H-上边和下边几个child的高度
//        offset=上边和下边几个child的高度
        val offset = getChildMeasureOffset(parent, child)
        val height = View.MeasureSpec.getSize(parentHeightMeasureSpec) - offset
        //计算孩子的宽高
        child.measure(
            parentWidthMeasureSpec,
            View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY)
        )
        return true
    }

    override fun onLayoutChild(
        parent: CoordinatorLayout,
        child: CardItemLayout,
        layoutDirection: Int
    ): Boolean {
        //所有child先布局上去
        parent.onLayoutChild(child, layoutDirection)
        //给child做偏移量
        //拿到上边的child,获取每个child的高度再相加
        val previous = getPreviousChild(parent, child)
        if (previous != null) {
            val offset = previous.top + previous.titleH
            child.offsetTopAndBottom(offset)
        }
        mInitialOffset = child.top
        return true
    }

    private fun getPreviousChild(
        parent: CoordinatorLayout,
        child: CardItemLayout
    ): CardItemLayout? {
        val index = parent.indexOfChild(child)
        if (index > 0) {
            for (i in index - 1 downTo 0) {
                if (parent[i] is CardItemLayout) {
                    return parent[i] as CardItemLayout
                }
            }
        }
        return null
    }

    /**
     *
     */
    private fun getChildMeasureOffset(parent: CoordinatorLayout, child: CardItemLayout): Int {
        val size = parent.childCount
        var offset = 0
        for (i in 0 until size) {
            val curr = parent[i]
            if (curr == child) continue
            if (curr is CardItemLayout) {
                offset += curr.titleH
            }
        }
        return offset
    }

    override fun onStartNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: CardItemLayout,
        directTargetChild: View,
        target: View,
        axes: Int,
        type: Int
    ): Boolean {
        return (axes and ViewCompat.SCROLL_AXIS_VERTICAL) != 0 && (child == directTargetChild)
    }

    override fun onNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: CardItemLayout,
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        type: Int,
        consumed: IntArray
    ) {
        //控制自己的滑动


        //控制上边、下边的滑动


        super.onNestedScroll(
            coordinatorLayout,
            child,
            target,
            dxConsumed,
            dyConsumed,
            dxUnconsumed,
            dyUnconsumed,
            type,
            consumed
        )
    }

}