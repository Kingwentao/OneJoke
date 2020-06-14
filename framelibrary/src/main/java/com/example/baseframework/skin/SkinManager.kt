package com.example.baseframework.skin

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.baseframework.skin.attr.SkinView

/**
 * author: created by wentaoKing
 * date: created in 2020-06-07
 * description: 皮肤管理
 */
class SkinManager {

    private var mContext: Context? = null
    private var mSkinList: MutableMap<Activity, MutableList<SkinView>> = HashMap()
    private var mSkinResource: SkinResource ?= null

    companion object {

        private const val TAG = "SkinManager"
        private var mSkinManager: SkinManager = SkinManager()


        fun getInstance(): SkinManager {
            return mSkinManager
        }
    }

    fun loadSkin(skinPath: String): Int {

        if (mContext == null) {
            Log.d(TAG, "context is null , need to init it")
            return -1
        }

        //初始化资源
        val skinResource = SkinResource(mContext!!, skinPath)
        mSkinResource = skinResource

        for (skinList in mSkinList.entries) {
            val skinViewList = skinList.value
            for (skinView in skinViewList) {
                Log.d(TAG, "skinview : $skinView")
                skinView.skin()
            }
        }

        return 0
    }

    fun init(context: Context) {
        mContext = context.applicationContext
    }

    fun getSkinViews(activity: Activity): MutableList<SkinView>? {
        return mSkinList[activity]
    }

    /**
     * 注册activity 和 skinView
     */
    fun register(activity: AppCompatActivity, skinViews: MutableList<SkinView>) {
        mSkinList[activity] = skinViews
    }

    /**
     * 获取档
     */
    fun getSkinResource(): SkinResource?{
        return mSkinResource
    }

    fun recoverDefaultSkin() {

    }


}