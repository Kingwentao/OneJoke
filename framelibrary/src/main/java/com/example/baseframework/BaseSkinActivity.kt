package com.example.baseframework

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewParent
import androidx.appcompat.R
import androidx.appcompat.app.AppCompatViewInflater
import androidx.appcompat.widget.VectorEnabledTintResources
import androidx.core.view.LayoutInflaterCompat
import androidx.core.view.ViewCompat
import com.example.baseframework.skin.SkinAttrSupport
import com.example.baseframework.skin.SkinManager
import com.example.baseframework.skin.attr.SkinAttr
import com.example.baseframework.skin.attr.SkinView
import com.example.baseframework.skin.support.SkinAppCompatViewInflater
import com.example.baselibrary.base.BaseActivity
import org.xmlpull.v1.XmlPullParser

/**
 * author: created by wentaoKing
 * date: created in 2020-05-24
 * description: 插件化换肤
 */
open class BaseSkinActivity : BaseActivity() {

    companion object {
        private const val TAG = "BaseSkinActivity"
    }

    private var mAppCompatViewInflater: SkinAppCompatViewInflater? = null
    private val IS_PRE_LOLLIPOP = Build.VERSION.SDK_INT < 21

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("BaseSkinActivity", "onCreate")


        val layoutInflater = LayoutInflater.from(this)
        LayoutInflaterCompat.setFactory2(layoutInflater, this)

//
//
//        val layoutInflater = LayoutInflater.from(this)
//        LayoutInflaterCompat.setFactory2(layoutInflater, object : LayoutInflater.Factory2 {
//            override fun onCreateView(
//                parent: View?,
//                name: String?,
//                context: Context?,
//                attrs: AttributeSet?
//            ): View? {
//                Log.d("BaseSkinActivity", "拦截到view的创建")
//                if (name == "Button") {
//                    Log.d("BaseSkinActivity", "拦截到Button的创建")
//                    val tv = TextView(this@BaseSkinActivity)
//                    tv.text = "拦截"
//                    return tv
//                }
//                return null
//            }
//
//            override fun onCreateView(
//                name: String?,
//                context: Context?,
//                attrs: AttributeSet?
//            ): View? {
//
//                return null
//            }
//
//        })

        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        parent: View?, name: String, context: Context, attrs: AttributeSet
    ): View? {

        //1. 拦截view的创建
        val view = createView(parent, name, context, attrs)

        //2. 解析属性（src、textColor、background...）
        Log.e(TAG, "view = $view")
        //2.1 一个 activity对应多个这样的SkinView
        if (view != null) {
            val skinArrAttrs: MutableList<SkinAttr> = SkinAttrSupport.getSkinAttrs(context, attrs)
            val skinView = SkinView(view, skinArrAttrs)
            // 3. 统一交给SkinManager处理
            managerSKinView(skinView)

            //4. 判断是否需要换肤
            SkinManager.getInstance(view.context).checkChangeSkin(skinView)
        }

        return view
    }

    /**
     * 统一管理SkinView
     */
    private fun managerSKinView(skinView: SkinView) {

        var skinViews = SkinManager.getInstance(this).getSkinViews(this)

        if (skinViews == null) {
            skinViews = mutableListOf()
            SkinManager.getInstance(this).register(this, skinViews)
        }
        skinViews.add(skinView)

    }


    fun createView(parent: View?, name: String, context: Context, attrs: AttributeSet): View? {
        if (mAppCompatViewInflater == null) {
            val a = obtainStyledAttributes(R.styleable.AppCompatTheme)
            val viewInflaterClassName = a.getString(R.styleable.AppCompatTheme_viewInflaterClass)
            if (viewInflaterClassName == null || AppCompatViewInflater::class.java.name == viewInflaterClassName) {
                // Either default class name or set explicitly to null. In both cases
                // create the base inflater (no reflection)
                mAppCompatViewInflater = SkinAppCompatViewInflater()
            } else {
                try {
                    val viewInflaterClass = Class.forName(viewInflaterClassName!!)
                    mAppCompatViewInflater = viewInflaterClass.getDeclaredConstructor()
                        .newInstance() as SkinAppCompatViewInflater
                } catch (t: Throwable) {
                    Log.i(
                        TAG, "Failed to instantiate custom view inflater "
                                + viewInflaterClassName + ". Falling back to default.", t
                    )
                    mAppCompatViewInflater = SkinAppCompatViewInflater()
                }

            }
        }

        var inheritContext = false
        if (IS_PRE_LOLLIPOP) {
            inheritContext =
                if (attrs is XmlPullParser)
                    (attrs as XmlPullParser).depth > 1
                else
                    shouldInheritContext(parent as ViewParent?)// If we have a XmlPullParser, we can detect where we are in the layout
            // Otherwise we have to use the old heuristic
        }

        return mAppCompatViewInflater?.createView(
            parent, name, context, attrs, inheritContext,
            IS_PRE_LOLLIPOP, /* Only read android:theme pre-L (L+ handles this anyway) */
            true, /* Read read app:theme as a fallback at all times for legacy reasons */
            VectorEnabledTintResources.shouldBeUsed() /* Only tint wrap the context if enabled */
        )
    }

    private fun shouldInheritContext(parent: ViewParent?): Boolean {

        if (parent == null) return false

        while (true) {
            if (parent == null) {
                return true
            } else if (parent !is View
                || ViewCompat.isAttachedToWindow((parent as View?)!!)
            ) {
                return false
            }

        }
    }
}