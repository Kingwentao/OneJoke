package com.example.baseframework.http

import android.util.Log
import com.example.baseframework.db.DaoSupportFactory
import com.example.baselibrary.utils.MD5Util

/**
 * author: created by wentaoKing
 * date: created in 2020-04-19
 * description: 缓存数据工具类
 */
class CacheDataUtil {

    companion object {

        fun getCacheResultJson(finalUrl: String): String? {
            val dataDaoSupport = DaoSupportFactory.getDao(CacheData::class.java)
            val finalUrls = arrayOf(MD5Util.string2MD5(finalUrl))
            val cacheData = dataDaoSupport.querySupport()
                // finalUrl http:w 报错  finalUrl -> MD5处理
                .selection("mUrlKey = ?").selectionArgs(finalUrls).query()

            if (cacheData.isNotEmpty()) {
                val data: CacheData = cacheData[0]
                return data.mResultJson
            }
            return null
        }

        fun cacheData(joinUrl: String, resultJson: String): Long {
            val dataDaoSupport = DaoSupportFactory.getDao(CacheData::class.java)
            dataDaoSupport.delete("mUrlKey=?", MD5Util.string2MD5(joinUrl))
            val number = dataDaoSupport.insert(CacheData(MD5Util.string2MD5(joinUrl), resultJson))
            Log.e("TAG", "number --> $number")
            return number
        }

    }
}