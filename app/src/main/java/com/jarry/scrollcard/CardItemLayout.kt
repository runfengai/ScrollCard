package com.jarry.scrollcard

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import kotlinx.android.synthetic.main.card_item.view.*

class CardItemLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var simpleAdapter: SimpleAdapter

    init {
        LayoutInflater.from(this.context).inflate(R.layout.card_item, this)
        val typedArray =
            context.obtainStyledAttributes(attrs, R.styleable.CardItemLayout, defStyleAttr, 0)
        val title = typedArray.getText(R.styleable.CardItemLayout_android_text)
        if (!TextUtils.isEmpty(title)) {
            tvTitle.text = title
        }
        val bg = typedArray.getColor(
            R.styleable.CardItemLayout_titleBackgroundColor,
            context.getCompatColor(R.color.colorAccent)
        )
        val textColor = typedArray.getColor(
            R.styleable.CardItemLayout_android_textColor,
            context.getCompatColor(android.R.color.white)
        )
        tvTitle.setTextColor(textColor)
        tvTitle.setBackgroundColor(bg)

        simpleAdapter = SimpleAdapter(mutableListOf())
        rv.adapter = simpleAdapter
        typedArray.recycle()
    }

    fun setData(entity: Entity) {
        tvTitle.text = entity.title
        simpleAdapter.noti(entity.list)
    }
}