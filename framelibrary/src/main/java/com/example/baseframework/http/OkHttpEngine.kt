package com.example.baseframework.http

import android.content.Context
import android.util.Log
import com.example.baselibrary.http.EngineCallback
import com.example.baselibrary.http.HttpUtils
import com.example.baselibrary.http.IHttpEngine
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.IOException
import java.net.URLConnection

/**
 * author: created by wentaoKing
 * date: created in 2020-03-15
 * description: okhttp 引擎实现
 */
class OkHttpEngine : IHttpEngine {

    private val mOkHttpClient = OkHttpClient()

    override fun get(
        isCache: Boolean,
        context: Context,
        url: String,
        params: Map<String, Any>,
        callback: EngineCallback
    ) {

        val joinUrl = HttpUtils.jointParams(url, params)
        Log.e("Get请求路径：", joinUrl)

        //判断是否从缓存中加载
        if (isCache){
            val resultJson = CacheDataUtil.getCacheResultJson(joinUrl)
            if (!resultJson.isNullOrEmpty()){
                // 需要缓存，而且数据库有缓存,直接就去执行，里面执行成功
                callback.onSuccess(resultJson)
            }
        }

        val requestBuilder = Request.Builder().url(joinUrl).tag(context)
        //可以省略，默认是GET请求
        val request = requestBuilder.build()

        mOkHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback.onError(e)
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val resultJson = response.body!!.string()

                if (isCache){
                    //判断缓存内容是否一致
                    val cacheResultJson = CacheDataUtil.getCacheResultJson(joinUrl)
                    if (!cacheResultJson.isNullOrEmpty()){
                        if (cacheResultJson == resultJson){
                            return
                        }
                    }
                }

                callback.onSuccess(resultJson)
                Log.e("Get返回结果：", resultJson)

                if (isCache){
                    // 2.3 缓存数据
                    CacheDataUtil.cacheData(joinUrl, resultJson)
                }
            }
        })
    }

    /**
     * OkHttp的post请求
     */
    override fun post(
        isCache: Boolean,
        context: Context,
        url: String,
        params: Map<String, Any>,
        callback: EngineCallback
    ) {

        val jointUrl =
            HttpUtils.jointParams(url, params)  //打印
        Log.e("Post请求路径：", jointUrl)

        val requestBody = appendBody(params)
        val request = Request.Builder()
            .url(url)
            .tag(context)
            .post(requestBody)
            .build()

        mOkHttpClient.newCall(request).enqueue(
            object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    callback.onError(e)
                }

                @Throws(IOException::class)
                override fun onResponse(call: Call, response: Response) {
                    // 这个 两个回掉方法都不是在主线程中
                    val result = response.body!!.string()
                    Log.e("Post返回结果：", jointUrl)
                    callback.onSuccess(result)
                }
            }
        )
    }

    /**
     * 组装post请求参数body
     */
    private fun appendBody(params: Map<String, Any>): RequestBody {
        val builder = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
        addParams(builder, params)
        return builder.build()
    }

    // 添加参数
    private fun addParams(builder: MultipartBody.Builder, params: Map<String, Any>?) {
        if (params != null && !params.isEmpty()) {
            for (key in params.keys) {
                builder.addFormDataPart(key, (params[key] ?: error("")).toString() + "")
                val value = params[key]
                when (value) {
                    is File -> {
                        // 处理文件 --> Object File
                        val file = value as File?
                        builder.addFormDataPart(
                            key, file!!.name, file
                                .asRequestBody(
                                    guessMimeType(file.absolutePath)
                                        .toMediaTypeOrNull()
                                )
                        )
                    }
                    is List<*> -> // 代表提交的是 List集合
                        try {
                            val listFiles = value as List<File>?
                            for (i in listFiles!!.indices) {
                                // 获取文件
                                val file = listFiles[i]
                                builder.addFormDataPart(
                                    key + i, file.name, file
                                        .asRequestBody(
                                            guessMimeType(file.absolutePath)
                                                .toMediaTypeOrNull()
                                        )
                                )
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    else -> builder.addFormDataPart(key, value!!.toString() + "")
                }
            }
        }
    }

    /**
     * 猜测文件类型
     */
    private fun guessMimeType(path: String): String {
        val fileNameMap = URLConnection.getFileNameMap()
        var contentTypeFor: String? = fileNameMap.getContentTypeFor(path)
        if (contentTypeFor == null) {
            contentTypeFor = "application/octet-stream"
        }
        return contentTypeFor
    }

}