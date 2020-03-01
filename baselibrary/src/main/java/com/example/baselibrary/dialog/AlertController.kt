package com.example.baselibrary.dialog

import android.content.Context
import android.content.DialogInterface
import android.util.Log
import android.util.SparseArray
import android.view.*
import java.lang.Exception

/**
 * author: created by wentaoKing
 * date: created in 2020-02-23
 * description: 对话弹窗的控制类
 */
class AlertController {

    private lateinit var mViewHelper: DialogViewHelper
    private var mWindow: Window
    private var mDialog: AlertDialog

    constructor(dialog: AlertDialog, window: Window) {
        this.mDialog = dialog
        this.mWindow = window
    }

    class AlertParams {

        var mAnimationStyle: Int? = null
        var mHeight: Int = ViewGroup.LayoutParams.WRAP_CONTENT
        var mGravity: Int = Gravity.CENTER
        var mWidth: Int = ViewGroup.LayoutParams.WRAP_CONTENT
        var mContext: Context
        var mThemeResId: Int = 0

        constructor(context: Context, themeResId: Int) {
            this.mContext = context
            this.mThemeResId = themeResId
        }

        var mViewLayoutResId: Int? = null
        var mView: View? = null

        var mCancelable = false
        //dialog 取消监听
        var mOnCancelListener: DialogInterface.OnCancelListener? = null
        //dialog 消失监听
        var mOnDismissListener: DialogInterface.OnDismissListener? = null
        //dialog key监听
        var mOnKeyListener: DialogInterface.OnKeyListener? = null
        //存放字体的修改（It is intended to be more memory efficient
        // * than using a HashMap to map Integers to Objects）
        var mTextArray: SparseArray<CharSequence> = SparseArray()
        //存放点击事件
        var mClickArray: SparseArray<View.OnClickListener> = SparseArray()

        /**
         * 绑定和设置参数
         */
        fun apply(mAlert: AlertController) {

            //设置布局辅助类
            var viewHelper: DialogViewHelper? = null
            if (mViewLayoutResId != null) {
                viewHelper = DialogViewHelper(mContext, mViewLayoutResId!!)
            }

            if (mView != null) {
                viewHelper = DialogViewHelper()
                viewHelper.setContentView(mView!!)
            }

            if (viewHelper == null)
                throw Exception("please set content view")

            //设置布局 - 关键
            mAlert.getDialog().setContentView(viewHelper.getContentView())


            mAlert.setViewHelper(viewHelper)

            //设置文本
            val textArraySize = mTextArray.size()
            for (i in 0 until textArraySize) {
                Log.d("testTemp", "mTextArraySize = $textArraySize id = ${mTextArray.keyAt(i)}")
                viewHelper.setText(mTextArray.keyAt(i), mTextArray.valueAt(i))
            }

            //设置点击事件
            val clickArraySize = mClickArray.size()
            for (i in 0 until clickArraySize) {
                viewHelper.setOnClickListener(mClickArray.keyAt(i), mClickArray.valueAt(i))
            }

            //配置其他参数
            val window = mAlert.getWindow()

            //设置弹窗位置
            window.setGravity(mGravity)

            //设置动画
            if (mAnimationStyle != null) window.setWindowAnimations(mAnimationStyle!!)

            //设置窗口宽高
            val prams = window.attributes
            prams.width = mWidth
            prams.height = mHeight
            window.attributes = prams
        }

    }

    private fun getWindow(): Window {
        return mWindow
    }

    fun getDialog(): AlertDialog {
        return mDialog
    }

    fun setViewHelper(viewHelper: DialogViewHelper) {
        this.mViewHelper = viewHelper
    }

    fun setOnClickListener(viewId: Int, listener: View.OnClickListener) {
        mViewHelper.setOnClickListener(viewId, listener)
    }

}