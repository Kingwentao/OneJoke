package com.example.baselibrary.dialog

import android.app.Dialog
import android.content.Context
import android.util.Log
import android.view.Gravity

import android.view.View
import android.view.ViewGroup
import com.example.baselibrary.R

/**
 * author: created by wentaoKing
 * date: created in 2020-02-23
 * description: 自定义的对话弹窗
 */
class AlertDialog(context: Context, themeResId: Int) : Dialog(context, themeResId) {


    private var mAlert: AlertController = AlertController(this, window)


    class Builder(context: Context, themeResId: Int = R.style.DialogStyle) {

        private var P: AlertController.AlertParams =
            AlertController.AlertParams(context, themeResId)


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

        fun setText(viewId: Int, text: CharSequence): Builder {
            P.mTextArray.put(viewId, text)
            return this
        }

        fun setOnClickListener(viewId: Int, listener: View.OnClickListener): Builder {
            P.mClickArray.put(viewId, listener)
            return this
        }


        /**
         * 全屏模式
         */
        fun setFullWidth(): Builder {
            P.mWidth = ViewGroup.LayoutParams.MATCH_PARENT
            return this
        }

        /**
         * 从底部弹出
         */
        fun fromButton(): Builder {
            P.mGravity = Gravity.BOTTOM
            return this
        }

        fun fromTop(): Builder{
            P.mGravity = Gravity.TOP
            return this
        }

        fun setWidthAndHeight(width: Int, height: Int): Builder {
            P.mWidth = width
            P.mHeight = height
            return this
        }

        fun setAnimation(animationStyle: Int): Builder {
            P.mAnimationStyle = animationStyle
            return this
        }


        fun create(): AlertDialog {
            // Context has already been wrapped with the appropriate theme.
            Log.d("testTemp", "${P.mThemeResId}")
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

        fun show(): AlertDialog {
            val dialog = create()
            dialog.show()
            return dialog
        }
    }

    fun setOnClickListener(viewId: Int, listener: View.OnClickListener) {
        mAlert.setOnClickListener(viewId, listener)
    }

}