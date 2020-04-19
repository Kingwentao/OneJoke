package com.example.baselibrary.http

import android.content.Context
import java.util.*

/**
 * author: created by wentaoKing
 * date: created in 2020-03-15
 * description: http 请求引擎规范
 */
interface IHttpEngine {

    /**
     * Get请求
     */
    fun get(isCache: Boolean,context: Context,url: String, params: Map<String, Any>,callback: EngineCallback)

    /**
     * Post请求
     */
    fun post(isCache: Boolean,context: Context,url: String, params: Map<String, Any>,callback: EngineCallback)
}