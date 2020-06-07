package com.example.baseframework.skin

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import com.example.baseframework.skin.attr.SkinAttr
import com.example.baseframework.skin.attr.SkinType

/**
 * author: created by wentaoKing
 * date: created in 2020-06-07
 * description:
 */
class SkinAttrSupport {

    companion object {

        private const val TAG = "SkinAttrSupport"

        /**
         * 获取skin的属性
         */
        fun getSkinAttrs(context: Context, attrs: AttributeSet): MutableList<SkinAttr> {

            val skinAttrs = ArrayList<SkinAttr>()

            val attrLength = attrs.attributeCount
            for (i in 0 until attrLength) {
                val attrName = attrs.getAttributeName(i)
                val attrValue = attrs.getAttributeValue(i)
                val skinType: SkinType? = getSkinType(attrName)

                if (skinType != null) {
                    //获取资源名称 attrValue是个@int类型
                    val resName: String? = getResName(context, attrValue)
                    if (resName == null || resName.isEmpty()) {
                        continue
                    }
                    val skinAttr = SkinAttr(resName, skinType)
                    skinAttrs.add(skinAttr)
                }

                //Log.d(TAG,"attrName = $attrName attrValue = $attrValue")
            }

            return skinAttrs
        }

        private fun getResName(context: Context, attrValue: String): String? {
            var resName: String? = null
            if (attrValue.startsWith("@")) {
                val value = attrValue.substring(1)
                //转换成int类型
                val resId = Integer.parseInt(value)
                //获取资源名字
                resName = context.resources.getResourceEntryName(resId)
            }
            Log.d(TAG, "资源名是 $resName ")
            return resName
        }

        /**
         *  获取需要的类型
         */
        private fun getSkinType(attrName: String): SkinType? {
            val skinTypes = SkinType.values()

            var skinType: SkinType? = null
            for (type in skinTypes) {
                Log.d(TAG, "type name = ${type.mResName}  attrName = $attrName")
                if (type.mResName == attrName) {
                    skinType = type
                }
            }
            return skinType
        }

    }
}