package com.example.baseframework.skin.attr

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.example.baseframework.skin.SkinManager
import com.example.baseframework.skin.SkinResource

/**
 * author: created by wentaoKing
 * date: created in 2020-06-07
 * description: 皮肤的类型
 */
enum class SkinType(val mResName: String) {

    TEXT_COLOR("textColor") {
        override fun skin(view: View, resName: String) {
            val skinResource = getSkinResource(view.context) ?: return
            val color = skinResource.getColorByName(resName) ?: return
            val textView = view as TextView
            textView.setTextColor(color.defaultColor)
        }
    },
    BACKGROUND("background") {
        override fun skin(view: View, resName: String) {
            val skinResource = getSkinResource(view.context) ?: return

            //背景如果是图片
            val drawable = skinResource.getDrawableByName(resName)
            if (drawable != null) {
                val imageView = view as ImageView
                imageView.background = drawable
                return
            }

            //背景如果是颜色
            val color = skinResource.getColorByName(resName)
            if (color != null) {
                val textView = view as TextView
                textView.setTextColor(color.defaultColor)
            }

        }
    },
    SRC("src") {
        override fun skin(view: View, resName: String) {
            val skinResource = getSkinResource(view.context) ?: return
            //背景如果是图片
            val drawable = skinResource.getDrawableByName(resName)
            if (drawable != null) {
                val imageView = view as ImageView
                imageView.setImageDrawable(drawable)
            }

        }
    };

    fun getSkinResource(context: Context): SkinResource? {
        return SkinManager.getInstance(context).getSkinResource()
    }

    abstract fun skin(view: View, resName: String)
}