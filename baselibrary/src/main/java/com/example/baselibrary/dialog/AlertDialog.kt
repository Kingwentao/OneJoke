package com.example.baselibrary.dialog

import android.app.Dialog
import android.content.Context

import android.view.View
import androidx.appcompat.view.ContextThemeWrapper
import com.example.baselibrary.R

/**
 * author: created by wentaoKing
 * date: created in 2020-02-23
 * description:
 */
class AlertDialog(context: Context, themeResId: Int) : Dialog(context, themeResId) {


    private lateinit var mAlert: AlertController



    class Builder {

        private lateinit var P: AlertController.AlertParams


        constructor(context: Context, themeResId: Int=R.style.DialogStyle) {
            P = AlertController.AlertParams(context,themeResId)
        }


        fun setContentView(view: View): Builder {
            P.mView = view
            P.mViewLayoutResId = 0
            return this
        }

        fun setContentView(layoutResId: Int): Builder {
            P.mView = null
            P.mViewLayoutResId = layoutResId
            return this
        }

        fun setText(viewId: Int,text: CharSequence): Builder{
            P.mTextArray?.put(viewId,text)
            return this
        }

        fun setOnClickListener(viewId: Int,listener: View.OnClickListener):Builder{
            P.mClickArray?.put(viewId,listener)
            return this
        }

        fun create(): AlertDialog {
            // Context has already been wrapped with the appropriate theme.
            val dialog = AlertDialog(P.mContext, P.mThemeResId)
            P.apply(dialog.mAlert)
            dialog.setCancelable(P.mCancelable)
            if (P.mCancelable) {
                dialog.setCanceledOnTouchOutside(true)
            }
            dialog.setOnCancelListener(P.mOnCancelListener)
            dialog.setOnDismissListener(P.mOnDismissListener)
            if (P.mOnKeyListener != null) {
                dialog.setOnKeyListener(P.mOnKeyListener)
            }
            return dialog
        }
    }




}