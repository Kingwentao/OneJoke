package com.example.baseframework

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.baselibrary.navgationbar.AbsNavigationBar

/**
 * author: created by wentaoKing
 * date: created in 2020-03-08
 * description: 默认的导航栏
 */
class DefaultNavigationBar(mParams: Builder.Params) :
    AbsNavigationBar<DefaultNavigationBar.Builder.Params>(mParams) {

    override fun bindLayoutId(): Int {
        return R.layout.title_bar
    }

    override fun applyView() {
        //绑定数据
        if (getParams().mTitle != null){
            setText(R.id.title, getParams().mTitle!!)
        }

        if (getParams().mRightText != null){
            setText(R.id.right_text, getParams().mRightText!!)
        }


        if (getParams().mRightClickListener != null)
            setOnClickListener<TextView>(R.id.right_text, getParams().mRightClickListener!!)
        // 左边 要写一个默认的  finishActivity
        setOnClickListener<TextView>(R.id.back, getParams().mLeftClickListener)
    }


    class Builder(mContext: Context, mParent: ViewGroup) :
        AbsNavigationBar.Builder(mContext, mParent) {

        override val P: Params = Params(mContext, mParent)


        class Params(mContext: Context, mParent: ViewGroup) :
            AbsNavigationBar.Builder.AbsNavigationParams(mContext, mParent) {


            var mRightText: String? = null
            var mTitle: String? = null

            var mRightClickListener: View.OnClickListener? = null
            var mLeftClickListener = View.OnClickListener { (mContext as Activity).finish() }

        }

        fun builder(): DefaultNavigationBar {
            return DefaultNavigationBar(P)
        }

        fun setTitle(title: String): Builder {
            P.mTitle = title
            return this
        }

        fun setRightText(text: String): Builder {
            P.mRightText = text
            return this
        }

        /**
         * 设置右边的点击事件
         */
        fun setRightClickListener(rightListener: View.OnClickListener): DefaultNavigationBar.Builder {
            P.mRightClickListener = rightListener
            return this
        }

        /**
         * 设置左边的点击事件
         */
        fun setLeftClickListener(rightListener: View.OnClickListener): DefaultNavigationBar.Builder {
            P.mLeftClickListener = rightListener
            return this
        }


    }


}
