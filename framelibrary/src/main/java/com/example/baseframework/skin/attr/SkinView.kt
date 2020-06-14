package com.example.baseframework.skin.attr

import android.util.Log
import android.view.View

/**
 * author: created by wentaoKing
 * date: created in 2020-06-07
 * description: 对view进行换肤的类
 */
class SkinView(private val mView: View, private val mAttrs: List<SkinAttr>) {

    fun skin() {
        for (attr in mAttrs){
            Log.d("SkinView","attr: $attr")
            attr.skin(mView)
        }
    }
}