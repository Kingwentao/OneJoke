package com.example.baseframework.skin.attr

import android.view.View

/**
 * author: created by wentaoKing
 * date: created in 2020-06-07
 * description: skin的属性
 */
class SkinAttr(private var mResName: String, private val mType: SkinType) {

    /**
     * 换肤
     * @param view 换肤的view
     */
    fun skin(view: View) {
        mType.skin(view, mResName)
    }
}