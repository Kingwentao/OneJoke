package com.example.baselibrary.dialog

import android.content.Context
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import java.lang.Exception
import java.lang.ref.WeakReference

/**
 * author: created by wentaoKing
 * date: created in 2020-02-23
 * description: dialog view的辅助类
 */
class DialogViewHelper {


    private lateinit var mContentView: View
    //缓存view，避免重复去findView，软引用避免内存泄露
    private var mViews: SparseArray<WeakReference<View>>

    constructor() {
        mViews = SparseArray()
    }

    constructor(context: Context, resLayoutId: Int) {
        mViews = SparseArray()
        mContentView = LayoutInflater.from(context).inflate(resLayoutId, null)
    }

    fun setContentView(mView: View) {
        mContentView = mView
    }

    fun setText(viewId: Int, text: CharSequence?) {
        val tv: TextView? = getView(viewId)
        tv?.text = text
    }

    fun setOnClickListener(viewId: Int, listener: View.OnClickListener?) {
        val view: View? = getView(viewId)
        view?.setOnClickListener(listener)
    }

    /**
     * 从缓存里面去拿view
     */
    private fun <T : View> getView(viewId: Int): T? {

        val viewReference = mViews.get(viewId)
        var view: View? = null
        if (viewReference != null) {
            view = viewReference.get()
        }

        if (view == null) {
            view = mContentView.findViewById<T>(viewId)
            if (view != null){
                mViews.put(viewId, WeakReference(view))
            }else{
                throw Exception("view($viewId) is can't find")
            }

        }

        return view as T?
    }


    /**
     * 获取contentView
     */
    fun getContentView(): View {
        return mContentView
    }


}