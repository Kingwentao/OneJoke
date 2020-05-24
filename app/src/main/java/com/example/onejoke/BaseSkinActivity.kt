package com.example.onejoke

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.core.view.LayoutInflaterCompat
import androidx.core.view.LayoutInflaterFactory

/**
 * author: created by wentaoKing
 * date: created in 2020-05-24
 * description: 插件化换肤
 */
open class BaseSkinActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("BaseSkinActivity", "onCreate")


        val layoutInflater = LayoutInflater.from(this)
        LayoutInflaterCompat.setFactory2(layoutInflater, object : LayoutInflater.Factory2 {
            override fun onCreateView(
                parent: View?,
                name: String?,
                context: Context?,
                attrs: AttributeSet?
            ): View? {
                Log.d("BaseSkinActivity", "拦截到view的创建")
                if (name == "Button") {
                    Log.d("BaseSkinActivity", "拦截到Button的创建")
                    val tv = TextView(this@BaseSkinActivity)
                    tv.text = "拦截"
                    return tv
                }
                return null
            }

            override fun onCreateView(
                name: String?,
                context: Context?,
                attrs: AttributeSet?
            ): View? {

                return null
            }

        })

        super.onCreate(savedInstanceState)
    }

}