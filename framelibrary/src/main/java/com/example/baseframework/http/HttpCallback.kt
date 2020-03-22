package com.example.baseframework.http

import android.content.Context
import com.example.baselibrary.http.EngineCallback
import com.example.baselibrary.http.HttpUtils
import com.google.gson.Gson

/**
 * author: created by wentaoKing
 * date: created in 2020-03-22
 * description: http请求的回调
 */
abstract class HttpCallback<T>: EngineCallback {

    override fun onPreExecute(context: Context, params: MutableMap<String, Any>){
        // 添加与项目业务逻辑有关的参数
        params["app_name"] = "joke_essay"
        params["version_name"] = "5.7.0"
        params["ac"] = "wifi"
        params["device_id"] = "30036118478"
        params["device_brand"] = "Xiaomi"
        params["update_version_code"] = "5701"
        params["manifest_version_code"] = "570"
        params["longitude"] = "113.000366"
        params["latitude"] = "28.171377"
        params["device_platform"] = "android"

        onPreExecute()
    }

    private fun onPreExecute(){

    }

    override fun onSuccess(message: String) {
        val gson = Gson()
        val objResult: T = gson.fromJson(message,HttpUtils.analysisClazzInfo(this)) as T
        onSuccess(objResult)
    }

    abstract fun onSuccess(result: T)

}