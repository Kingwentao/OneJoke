package com.example.baseframework.skin

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.text.TextUtils
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.baseframework.skin.attr.SkinView
import com.example.baseframework.skin.config.SkinPreUtils
import com.example.baselibrary.utils.SingletonHolder
import java.io.File

/**
 * author: created by wentaoKing
 * date: created in 2020-06-07
 * description: 皮肤管理
 */
class SkinManager private constructor(context: Context) {


    private var mSkinList: MutableMap<Activity, MutableList<SkinView>> = HashMap()
    private var mSkinResource: SkinResource? = null

    companion object : SingletonHolder<SkinManager, Context>(::SkinManager) {
        const val TAG = "SkinManager"
    }

    private var mContext = context.applicationContext

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

        //保存皮肤
        saveSkinStatus(skinPath)

        return 0
    }

    init {
        //判断皮肤是否存在，来更新缓存保存的信息
        val currentSkinPath = SkinPreUtils.getInstance(mContext).getSkinPath()
        val result = checkSkinInfo(currentSkinPath)
        if (result) {
            mSkinResource = SkinResource(mContext, currentSkinPath)
        }
    }

    /**
     * 验证皮肤信息
     */
    private fun checkSkinInfo(skinPath: String): Boolean {

        val skinFile = File(skinPath)
        if (!skinFile.exists()) {
            SkinPreUtils.getInstance(mContext).clearInfo()
            return false
        }

        //验证一下包名是否可以获取到
        val pckName = mContext.packageManager
            .getPackageArchiveInfo(skinPath, PackageManager.GET_ACTIVITIES)?.packageName
        if (TextUtils.isEmpty(pckName)) {
            SkinPreUtils.getInstance(mContext).clearInfo()
            return false
        }

        //TODO： 做一些验证签名
        return true
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
    fun getSkinResource(): SkinResource? {
        return mSkinResource
    }

    fun recoverDefaultSkin() {

    }

    /**
     * 检查并更换皮肤
     */
    fun checkChangeSkin(skinView: SkinView) {

        //如果当前有皮肤，直接更换
        val currentSkinPath = SkinPreUtils.getInstance(mContext).getSkinPath()
        Log.d(TAG, "current skinPath is $currentSkinPath")
        if (!TextUtils.isEmpty(currentSkinPath)) {
            skinView.skin()
        } else {
            Log.d(TAG, "current skin path is null")
        }

    }

    /**
     * 保存皮肤状态
     */
    private fun saveSkinStatus(skinPath: String) {
        SkinPreUtils.getInstance(mContext!!).saveSkinPath(skinPath)
    }

}