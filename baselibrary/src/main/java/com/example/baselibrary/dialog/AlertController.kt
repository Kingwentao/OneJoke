package com.example.baselibrary.dialog

import android.content.Context
import android.content.DialogInterface
import android.util.SparseArray
import android.view.View

/**
 * author: created by wentaoKing
 * date: created in 2020-02-23
 * description:
 */
class AlertController {






    class AlertParams {

        var mContext: Context
        var mThemeResId:Int = 0

        constructor(context: Context,themeResId: Int){
            this.mContext = context
            this.mThemeResId = themeResId
        }


        var mViewLayoutResId: Int?=null
        var mView: View?=null



        var mCancelable = false
        //dialog 取消监听
        var mOnCancelListener: DialogInterface.OnCancelListener? = null
        //dialog 消失监听
        var mOnDismissListener: DialogInterface.OnDismissListener? = null
        //dialog key监听
        var mOnKeyListener: DialogInterface.OnKeyListener? = null
        //存放字体的修改（It is intended to be more memory efficient
        // * than using a HashMap to map Integers to Objects）
        var mTextArray: SparseArray<CharSequence>?=SparseArray()
        //存放点击事件
        var mClickArray: SparseArray<View.OnClickListener>?=SparseArray()

        fun AlertParams(context: Context, themeResId: Int) {
            this.mContext = context
            this.mThemeResId = themeResId
        }

        fun apply(mAlert: AlertController){

        }

    }
}