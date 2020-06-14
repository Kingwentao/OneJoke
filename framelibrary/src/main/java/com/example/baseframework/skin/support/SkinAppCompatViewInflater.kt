package com.example.baseframework.skin.support

import android.content.Context
import android.content.ContextWrapper
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.InflateException
import android.view.View
import androidx.appcompat.R
import androidx.appcompat.view.ContextThemeWrapper
import androidx.appcompat.widget.*
import androidx.collection.ArrayMap
import androidx.core.view.ViewCompat
import java.lang.reflect.Constructor
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method

/**
 * author: created by wentaoKing
 * date: created in 2020-06-07
 * description: copy google source of AppCompatViewInflater class
 *
 * This class is responsible for manually inflating our tinted widgets.
 *
 * This class two main responsibilities: the first is to 'inject' our tinted views in place of
 * the framework versions in layout inflation; the second is backport the `android:theme`
 * functionality for any inflated widgets. This include theme inheritance from its parent.
 */
class SkinAppCompatViewInflater{

    private val mConstructorArgs = arrayOfNulls<Any>(2)

    fun createView(
        parent: View?, name: String, context: Context,
        attrs: AttributeSet, inheritContext: Boolean,
        readAndroidTheme: Boolean, readAppTheme: Boolean, wrapContext: Boolean
    ): View? {
        var context = context
        val originalContext = context

        // We can emulate Lollipop's android:theme attribute propagating down the view hierarchy
        // by using the parent's context
        if (inheritContext && parent != null) {
            context = parent.context
        }
        if (readAndroidTheme || readAppTheme) {
            // We then apply the theme on the context, if specified
            context = themifyContext(context, attrs, readAndroidTheme, readAppTheme)
        }
        if (wrapContext) {
            context = TintContextWrapper.wrap(context)
        }

        var view: View? = null

        // We need to 'inject' our tint aware Views in place of the standard framework versions
        when (name) {
            "TextView" -> {
                view = createTextView(context, attrs)
                verifyNotNull(view, name)
            }
            "ImageView" -> {
                view = createImageView(context, attrs)
                verifyNotNull(view, name)
            }
            "Button" -> {
                view = createButton(context, attrs)
                verifyNotNull(view, name)
            }
            "EditText" -> {
                view = createEditText(context, attrs)
                verifyNotNull(view, name)
            }
            "Spinner" -> {
                view = createSpinner(context, attrs)
                verifyNotNull(view, name)
            }
            "ImageButton" -> {
                view = createImageButton(context, attrs)
                verifyNotNull(view, name)
            }
            "CheckBox" -> {
                view = createCheckBox(context, attrs)
                verifyNotNull(view, name)
            }
            "RadioButton" -> {
                view = createRadioButton(context, attrs)
                verifyNotNull(view, name)
            }
            "CheckedTextView" -> {
                view = createCheckedTextView(context, attrs)
                verifyNotNull(view, name)
            }
            "AutoCompleteTextView" -> {
                view = createAutoCompleteTextView(context, attrs)
                verifyNotNull(view, name)
            }
            "MultiAutoCompleteTextView" -> {
                view = createMultiAutoCompleteTextView(context, attrs)
                verifyNotNull(view, name)
            }
            "RatingBar" -> {
                view = createRatingBar(context, attrs)
                verifyNotNull(view, name)
            }
            "SeekBar" -> {
                view = createSeekBar(context, attrs)
                verifyNotNull(view, name)
            }
            "ToggleButton" -> {
                view = createToggleButton(context, attrs)
                verifyNotNull(view, name)
            }
            else ->
                // The fallback that allows extending class to take over view inflation
                // for other tags. Note that we don't check that the result is not-null.
                // That allows the custom inflater path to fall back on the default one
                // later in this method.
                view = createView(context, name, attrs)
        }

        if (view == null && originalContext !== context) {
            // If the original context does not equal our themed context, then we need to manually
            // inflate it using the name so that android:theme takes effect.
            view = createViewFromTag(context, name, attrs)
        }

        if (view != null) {
            // If we have created a view, check its android:onClick
            checkOnClickListener(view, attrs)
        }

        return view
    }

    protected fun createTextView(context: Context, attrs: AttributeSet): AppCompatTextView {
        return AppCompatTextView(context, attrs)
    }

    protected fun createImageView(context: Context, attrs: AttributeSet): AppCompatImageView {
        return AppCompatImageView(context, attrs)
    }

    protected fun createButton(context: Context, attrs: AttributeSet): AppCompatButton {
        return AppCompatButton(context, attrs)
    }

    protected fun createEditText(context: Context, attrs: AttributeSet): AppCompatEditText {
        return AppCompatEditText(context, attrs)
    }

    protected fun createSpinner(context: Context, attrs: AttributeSet): AppCompatSpinner {
        return AppCompatSpinner(context, attrs)
    }

    protected fun createImageButton(context: Context, attrs: AttributeSet): AppCompatImageButton {
        return AppCompatImageButton(context, attrs)
    }

    protected fun createCheckBox(context: Context, attrs: AttributeSet): AppCompatCheckBox {
        return AppCompatCheckBox(context, attrs)
    }

    protected fun createRadioButton(context: Context, attrs: AttributeSet): AppCompatRadioButton {
        return AppCompatRadioButton(context, attrs)
    }

    protected fun createCheckedTextView(
        context: Context,
        attrs: AttributeSet
    ): AppCompatCheckedTextView {
        return AppCompatCheckedTextView(context, attrs)
    }

    protected fun createAutoCompleteTextView(
        context: Context,
        attrs: AttributeSet
    ): AppCompatAutoCompleteTextView {
        return AppCompatAutoCompleteTextView(context, attrs)
    }

    protected fun createMultiAutoCompleteTextView(
        context: Context,
        attrs: AttributeSet
    ): AppCompatMultiAutoCompleteTextView {
        return AppCompatMultiAutoCompleteTextView(context, attrs)
    }

    protected fun createRatingBar(context: Context, attrs: AttributeSet): AppCompatRatingBar {
        return AppCompatRatingBar(context, attrs)
    }

    protected fun createSeekBar(context: Context, attrs: AttributeSet): AppCompatSeekBar {
        return AppCompatSeekBar(context, attrs)
    }

    protected fun createToggleButton(context: Context, attrs: AttributeSet): AppCompatToggleButton {
        return AppCompatToggleButton(context, attrs)
    }

    private fun verifyNotNull(view: View?, name: String) {
        if (view == null) {
            throw IllegalStateException(
                this.javaClass.name
                        + " asked to inflate view for <" + name + ">, but returned null"
            )
        }
    }

    protected fun createView(context: Context, name: String, attrs: AttributeSet): View? {
        return null
    }

    private fun createViewFromTag(context: Context, name: String, attrs: AttributeSet): View? {
        var name = name
        if (name == "view") {
            name = attrs.getAttributeValue(null, "class")
        }

        try {
            mConstructorArgs[0] = context
            mConstructorArgs[1] = attrs

            if (-1 == name.indexOf('.')) {
                for (i in sClassPrefixList.indices) {
                    val view = createViewByPrefix(context, name, sClassPrefixList[i])
                    if (view != null) {
                        return view
                    }
                }
                return null
            } else {
                return createViewByPrefix(context, name, null)
            }
        } catch (e: Exception) {
            // We do not want to catch these, lets return null and let the actual LayoutInflater
            // try
            return null
        } finally {
            // Don't retain references on context.
            mConstructorArgs[0] = null
            mConstructorArgs[1] = null
        }
    }

    /**
     * android:onClick doesn't handle views with a ContextWrapper context. This method
     * backports new framework functionality to traverse the Context wrappers to find a
     * suitable target.
     */
    private fun checkOnClickListener(view: View, attrs: AttributeSet) {
        val context = view.context

        if ((context !is ContextWrapper || (Build.VERSION.SDK_INT >= 15 && !ViewCompat.hasOnClickListeners(
                view
            )))
        ) {
            // Skip our compat functionality if: the Context isn't a ContextWrapper, or
            // the view doesn't have an OnClickListener (we can only rely on this on API 15+ so
            // always use our compat code on older devices)
            return
        }

        val a = context.obtainStyledAttributes(attrs, sOnClickAttrs)
        val handlerName = a.getString(0)
        if (handlerName != null) {
            view.setOnClickListener(DeclaredOnClickListener(view, handlerName!!))
        }
        a.recycle()
    }

    @Throws(ClassNotFoundException::class, InflateException::class)
    private fun createViewByPrefix(context: Context, name: String, prefix: String?): View? {
        var constructor = sConstructorMap[name]

        try {
            if (constructor == null) {
                // Class not found in the cache, see if it's real, and try to add it
                val clazz = Class.forName(
                    if (prefix != null) (prefix!! + name) else name,
                    false,
                    context.classLoader
                ).asSubclass(View::class.java!!)

                constructor = clazz.getConstructor(*sConstructorSignature)
                sConstructorMap[name] = constructor
            }
            constructor!!.isAccessible = true
            return constructor!!.newInstance(*mConstructorArgs)
        } catch (e: Exception) {
            // We do not want to catch these, lets return null and let the actual LayoutInflater
            // try
            return null
        }

    }

    /**
     * An implementation of OnClickListener that attempts to lazily load a
     * named click handling method from a parent or ancestor context.
     */
    private class DeclaredOnClickListener(
        private val mHostView: View,
        private val mMethodName: String
    ) :
        View.OnClickListener {

        private var mResolvedMethod: Method? = null
        private var mResolvedContext: Context? = null

        override fun onClick(v: View) {
            if (mResolvedMethod == null) {
                resolveMethod(mHostView.context, mMethodName)
            }

            try {
                mResolvedMethod!!.invoke(mResolvedContext, v)
            } catch (e: IllegalAccessException) {
                throw IllegalStateException(
                    "Could not execute non-public method for android:onClick", e
                )
            } catch (e: InvocationTargetException) {
                throw IllegalStateException(
                    "Could not execute method for android:onClick", e
                )
            }

        }

        private fun resolveMethod(context: Context?, name: String) {
            var context = context
            while (context != null) {
                try {
                    if (!context!!.isRestricted) {
                        val method = context!!.javaClass.getMethod(mMethodName, View::class.java)
                        if (method != null) {
                            mResolvedMethod = method
                            mResolvedContext = context
                            return
                        }
                    }
                } catch (e: NoSuchMethodException) {
                    // Failed to find method, keep searching up the hierarchy.
                }

                if (context is ContextWrapper) {
                    context = (context as ContextWrapper).baseContext
                } else {
                    // Can't search up the hierarchy, null out and fail.
                    context = null
                }
            }

            val id = mHostView.id
            val idText = if (id == View.NO_ID)
                ""
            else
                (" with id '"
                        + mHostView.context.resources.getResourceEntryName(id) + "'")
            throw IllegalStateException(
                ("Could not find method " + mMethodName
                        + "(View) in a parent or ancestor Context for android:onClick "
                        + "attribute defined on view " + mHostView.javaClass + idText)
            )
        }
    }

    companion object {

        private val sConstructorSignature = arrayOf(Context::class.java, AttributeSet::class.java)
        private val sOnClickAttrs = intArrayOf(android.R.attr.onClick)

        private val sClassPrefixList =
            arrayOf("android.widget.", "android.view.", "android.webkit.")

        private val LOG_TAG = "AppCompatViewInflater"

        private val sConstructorMap = ArrayMap<String, Constructor<out View>>()

        /**
         * Allows us to emulate the `android:theme` attribute for devices before L.
         */
        private fun themifyContext(
            context: Context, attrs: AttributeSet,
            useAndroidTheme: Boolean, useAppTheme: Boolean
        ): Context {
            var context = context
            val a = context.obtainStyledAttributes(attrs, R.styleable.View, 0, 0)
            var themeId = 0
            if (useAndroidTheme) {
                // First try reading android:theme if enabled
                themeId = a.getResourceId(R.styleable.View_android_theme, 0)
            }
            if (useAppTheme && themeId == 0) {
                // ...if that didn't work, try reading app:theme (for legacy reasons) if enabled
                themeId = a.getResourceId(R.styleable.View_theme, 0)

                if (themeId != 0) {
                    Log.i(
                        LOG_TAG,
                        ("app:theme is now deprecated. " + "Please move to using android:theme instead.")
                    )
                }
            }
            a.recycle()

            if ((themeId != 0 && ((context !is ContextThemeWrapper || (context as ContextThemeWrapper).themeResId != themeId)))) {
                // If the context isn't a ContextThemeWrapper, or it is but does not have
                // the same theme as we need, wrap it in a new wrapper
                context = ContextThemeWrapper(context, themeId)
            }
            return context
        }
    }
}
