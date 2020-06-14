package com.example.baseframework.skin

import android.content.Context
import android.content.pm.PackageManager
import android.content.res.AssetManager
import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.os.Environment
import android.util.Log
import java.io.File
import java.lang.Exception

/**
 * author: created by wentaoKing
 * date: created in 2020-06-07
 * description: 皮肤资源
 */
class SkinResource(private val mContext: Context, private val skinPath: String) {

    companion object {
        private const val TAG = "SkinResource"
    }

    private var mSkinResource: Resources? = null
    private var mResPkgName: String? = null

    init {

        val resources = mContext.resources
        //创建AssetManager
        val asset = AssetManager::class.java.newInstance()

        try {
            //添加本地的资源皮肤
            val method =
                AssetManager::class.java.getDeclaredMethod("addAssetPath", String::class.java)
            method.invoke(asset, skinPath)
            val resource = Resources(asset, resources.displayMetrics, resources.configuration)
            mSkinResource = resource

            //获取包名
            val packInfo = mContext.packageManager.getPackageArchiveInfo(
                skinPath,
                PackageManager.GET_ACTIVITIES
            )
            val pkgName = packInfo?.packageName
            mResPkgName = pkgName
            Log.d(TAG, "资源包名: $pkgName")

        } catch (e: Exception) {
            e.printStackTrace()
            Log.d(TAG, "换肤异常")
        }
    }

    /**
     * 通过资源名获取drawable
     */
    fun getDrawableByName(resName: String): Drawable? {
        try {
            val resId = mSkinResource?.getIdentifier(
                resName,
                "drawable", mResPkgName
            )
            if (resId != null) {
                return mSkinResource?.getDrawable(resId, null)
            }
            return null

        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    /**
     * 通过资源名获取颜色
     */
    fun getColorByName(resName: String): ColorStateList? {
        try {
            val resId = mSkinResource?.getIdentifier(
                resName,
                "color", mResPkgName
            )
            if (resId != null) {
                return mSkinResource?.getColorStateList(resId, null)
            }
            return null

        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }
}