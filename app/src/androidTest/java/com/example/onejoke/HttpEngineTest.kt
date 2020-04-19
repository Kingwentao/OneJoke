package com.example.onejoke

import android.content.Context
import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.baseframework.http.HttpCallback
import com.example.baselibrary.http.HttpUtils
import com.example.onejoke.model.DiscoverListResult
import org.junit.Test
import org.junit.runner.RunWith
import java.lang.Exception

/**
 * author: created by wentaoKing
 * date: created in 2020-04-19
 * description:
 */
@RunWith(AndroidJUnit4::class)
class HttpEngineTest {

    companion object {
        private const val TAG = "HttpEngineTest"
    }

    @Test
    private fun testHttpEngine(context: Context) {

        //
        HttpUtils.with(context).url("http://is.snssdk.com/2/essay/discovery/v3/")
            .addParams("iid", "6152551759").addParams("aid", "7")
            .execute(object : HttpCallback<DiscoverListResult>() {
                override fun onSuccess(result: DiscoverListResult) {
                    Log.d(TAG, "请求成功 $result")
                }

                override fun onError(e: Exception) {
                    Log.d(TAG, "请求失败 ${e.printStackTrace()}")
                }

            })
    }
}