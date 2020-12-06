package com.jarry.scrollcard

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import androidx.core.view.get
import androidx.recyclerview.widget.RecyclerView

class ScrollCardBehavior :
    CoordinatorLayout.Behavior<CardItemLayout> {
    //默认，及在xml中设置
    constructor() : super()
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    companion object {
        const val TAG = "ScrollCardBehavior"
    }

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
        val res = (axes and ViewCompat.SCROLL_AXIS_VERTICAL) != 0 && (child == directTargetChild) &&
                !(child[0] as RecyclerView).canScrollVertically(-1)
        Log.e(TAG, "onStartNestedScroll :: return $res")
        return res
    }

    override fun onNestedPreScroll(
        parent: CoordinatorLayout,
        child: CardItemLayout,
        target: View,
        dx: Int,
        dy: Int,
        consumed: IntArray,
        type: Int
    ) {
        consumed[1] =
            scroll(child, dy, mInitialOffset, mInitialOffset + child.height - child.titleH)
        //控制上边和下边child的移动
        shiftScroll(consumed[1], parent, child)
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

        val offset = scroll(
            child,
            dyUnconsumed,
            mInitialOffset,
            mInitialOffset + child.height - child.titleH
        )
        //控制上边、下边的滑动
        shiftScroll(offset, coordinatorLayout, child)


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

    //兼顾滚动上边和下边
    private fun shiftScroll(offset: Int, parent: CoordinatorLayout, child: CardItemLayout) {
        if (offset == 0) return
        if (offset > 0) {//向上
            var curr = child
            var prevCard = getPreviousChild(parent, curr)
            while (prevCard != null) {
                val offset = getHeaderOverlap(prevCard, curr)
                if (offset > 0) {
                    prevCard.offsetTopAndBottom(-offset)
                }
                curr = prevCard
                prevCard = getPreviousChild(parent, curr)
            }
        } else {//向下
            var curr = child
            var nextCard = getNextChild(parent, curr)
            while (nextCard != null) {
                val offset = getHeaderOverlap(curr, nextCard)
                if (offset > 0) {
                    nextCard.offsetTopAndBottom(offset)
                }
                curr = nextCard
                nextCard = getNextChild(parent, curr)
            }

        }
    }

    private fun getHeaderOverlap(curr: CardItemLayout, nextCard: CardItemLayout): Int {
        return curr.top + curr.titleH - nextCard.top
    }

    private fun getNextChild(parent: CoordinatorLayout, child: CardItemLayout): CardItemLayout? {
        val index = parent.indexOfChild(child)

        for (i in index + 1 until parent.childCount) {
            if (parent[i] is CardItemLayout) {
                return parent[i] as CardItemLayout
            }
        }

        return null
    }

    private fun scroll(child: CardItemLayout, dy: Int, minOffset: Int, maxOffset: Int): Int {
        val initialOffset = child.top
        val offset = clamp(initialOffset - dy, minOffset, maxOffset) - initialOffset
        child.offsetTopAndBottom(offset)
        return -offset//往上是正，往下是负
    }

    private fun clamp(i: Int, min: Int, max: Int): Int {
        return if (i < min) min else if (i > max) max else i
    }

}