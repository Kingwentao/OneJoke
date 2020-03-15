package com.example.baselibrary.navgationbar

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

/**
 * author: created by wentaoKing
 * date: created in 2020-03-08
 * description: 头部导航的基类
 */
open class AbsNavigationBar<T :AbsNavigationBar.Builder.AbsNavigationParams>
    (private val mParams: T): INavigationBar{


    private lateinit var mNavigationView: View

    init {
        createAndBindView()
    }

    /**
     * 创建并绑定View
     */
    fun createAndBindView(){

        //获取根布局
        if (mParams.mViewGroup != null){
//            val activityRoot =
//                (mParams.mContext as Activity).findViewById<ViewGroup>(android.R.id.content)

            //根据源码去拿decorView作为根布局，这样再去加载内容到跟布局就可以不受传入布局的影响
            val activityRoot =
                (mParams.mContext as Activity).window.decorView as ViewGroup
            mParams.mViewGroup = activityRoot.getChildAt(0) as ViewGroup
        }


        if (mParams.mViewGroup == null ) return

        //创建view
        mNavigationView = LayoutInflater.from(mParams.mContext).inflate(bindLayoutId(),
            mParams.mViewGroup,false)
        //添加创建的view,放在头部
        mParams.mViewGroup!!.addView(mNavigationView,0)
        //
        applyView()
    }

    fun setText(viewId: Int,text: String){
        val tv = findViewById<TextView>(viewId)
        if (text.isNotEmpty()){
            tv.visibility = View.VISIBLE
            tv.text = text
        }
    }


    /**
     * 设置点击事件
     */
    protected fun<T: View>setOnClickListener(viewId: Int,listener: View.OnClickListener){
        findViewById<T>(viewId).setOnClickListener(listener)
    }

    /**
     * 寻找viewId
     */
    public fun<T: View> findViewById(viewId: Int): T{
       return mNavigationView.findViewById(viewId) as T
    }


    override fun bindLayoutId(): Int {
        return -1
    }

    override fun applyView() {

    }

    fun getParams(): T{
        return mParams
    }


    abstract class Builder(mContext: Context, viewGroup: ViewGroup){

        open val P = AbsNavigationParams(mContext,viewGroup)

        open class AbsNavigationParams(val mContext: Context, var mViewGroup: ViewGroup?){

        }

       // abstract fun<T: AbsNavigationParams> builder(): AbsNavigationBar<T>

    }


}