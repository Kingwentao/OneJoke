package com.example.baselibrary.http

import java.lang.Exception

/**
 * author: created by wentaoKing
 * date: created in 2020-03-15
 * description: 请求回掉接口
 */
interface EngineCallback {

    companion object{

        //实现一个默认的callBack
         val DEFAULT_ENGINE_CALLBACK = object : EngineCallback{
             override fun onError(e: Exception) {
             }

             override fun onSuccess(message: String) {
             }

         }
    }


    fun onError(e: Exception)

    fun onSuccess(message: String)


}