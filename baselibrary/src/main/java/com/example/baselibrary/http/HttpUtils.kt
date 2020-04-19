package com.example.baselibrary.http

import android.content.Context
import android.util.ArrayMap
import java.lang.Exception
import java.lang.reflect.ParameterizedType

/**
 * author: created by wentaoKing
 * date: created in 2020-03-15
 * description: http请求的工具类
 */
class HttpUtils {

    private var mContext: Context
    private var mParams: ArrayMap<String, Any>
    private var mUrl: String? = null
    // 是否读取缓存
    private var mCache = false

    private constructor(context: Context) {
        mContext = context
        mParams = ArrayMap()
    }

    companion object {

        private const val GET_TYPE = 0x0011
        private const val POST_TYPE = 0x0022

        private var mType = GET_TYPE

        //默认okhttp引擎
        private lateinit var mHttpEngine: IHttpEngine

        fun init(httpEngine: IHttpEngine) {
            mHttpEngine = httpEngine
        }

        fun exchangeHttpEngine(httpEngine: IHttpEngine) {
            mHttpEngine = httpEngine
        }

        fun with(context: Context): HttpUtils {
            return HttpUtils(context)
        }

        /**
         * 拼接参数
         */
        fun jointParams(url: String, params: Map<String, Any>?): String {

            if (params == null || params.isEmpty()) {
                return url
            }

            val stringBuffer = StringBuffer(url)
            if (!url.contains("?")) {
                stringBuffer.append("?")
            } else {
                if (!url.endsWith("?")) {
                    stringBuffer.append("&")
                }
            }

            for ((key, value) in params) {
                stringBuffer.append("$key=$value&")
            }

            stringBuffer.deleteCharAt(stringBuffer.length - 1)

            return stringBuffer.toString()
        }

        /**
         * 解析一个类上面的class信息
         */
        fun analysisClazzInfo(any: Any): Class<*> {
            val genType = any.javaClass.genericSuperclass
            val params = (genType as ParameterizedType).actualTypeArguments
            return params[0] as Class<*>
        }
    }

    fun get(): HttpUtils {
        mType = GET_TYPE
        return this
    }

    fun post(): HttpUtils {
        mType = POST_TYPE
        return this
    }

    fun url(url: String): HttpUtils {
        this.mUrl = url
        return this
    }

    // 是否配置缓存
    fun cache(isCache: Boolean): HttpUtils {
        mCache = isCache
        return this
    }

    //添加参数
    fun addParams(key: String, value: Any): HttpUtils {
        mParams[key] = value
        return this
    }

    fun addParams(params: Map<String, Any>): HttpUtils {
        mParams.putAll(params)
        return this
    }

    fun execute(engineCallback: EngineCallback?) {

        var callback = engineCallback

        callback?.onPreExecute(mContext, mParams)

        if (engineCallback == null) {
            callback = EngineCallback.DEFAULT_ENGINE_CALLBACK
        }

        if (mUrl == null) throw Exception("please invoke url method to add the url")

        when (mType) {
            GET_TYPE -> {
                get(mUrl!!, mParams, callback!!)
            }
            POST_TYPE -> {
                post(mUrl!!, mParams, callback!!)
            }
        }

    }

    fun execute() {
        execute(null)
    }

    private fun get(url: String, params: Map<String, Any>, callback: EngineCallback) {
        mHttpEngine.get(mCache, mContext, url, params, callback)
    }

    private fun post(url: String, params: Map<String, Any>, callback: EngineCallback) {
        mHttpEngine.post(mCache, mContext, url, params, callback)
    }


}