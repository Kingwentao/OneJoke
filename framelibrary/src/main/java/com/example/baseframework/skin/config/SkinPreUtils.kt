package com.example.baseframework.skin.config

import android.content.Context
import com.example.baselibrary.utils.SingletonHolder

/**
 * author: created by wentaoKing
 * date: created in 2020-06-21
 * description: 皮肤信息缓存的工具
 */
class SkinPreUtils private constructor(context: Context) {

    private var mContext = context

    companion object : SingletonHolder<SkinPreUtils, Context>(::SkinPreUtils)

    init {

    }

    /**
     * 保存当前皮肤路径
     */
    fun saveSkinPath(skinPath: String) {
        mContext.getSharedPreferences(SkinConfig.SKIN_INFO_NAME, Context.MODE_PRIVATE).edit()
            .putString(SkinConfig.SKIN_PATH_NAME, skinPath)
            .apply()
    }

    /**
     * 获取保存的o皮肤路径
     */
    fun getSkinPath(): String {
        return mContext.getSharedPreferences(SkinConfig.SKIN_INFO_NAME, Context.MODE_PRIVATE)
            .getString(SkinConfig.SKIN_PATH_NAME, "")!!
    }

    /**
     * 清空保存的信息
     */
    fun clearInfo() {
        mContext.getSharedPreferences(SkinConfig.SKIN_INFO_NAME, Context.MODE_PRIVATE)
            .edit()
            .clear()
            .apply()
    }

}