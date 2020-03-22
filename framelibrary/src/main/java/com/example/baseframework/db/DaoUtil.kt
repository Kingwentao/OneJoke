package com.example.baseframework.db

import android.text.TextUtils
import java.util.*

/**
 * author: created by wentaoKing
 * date: created in 2020-03-22
 * description:
 */
class DaoUtil {


    private fun DaoUtil() {
        throw UnsupportedOperationException("cannot be instantiated")
    }


    companion object {

        fun getTableName(clazz: Class<*>): String {
            return clazz.simpleName
        }

        fun getColumnType(type: String): String? {
            var value: String? = null
            when {
                type.contains("String") -> value = " text"
                type.contains("int") -> value = " integer"
                type.contains("boolean") -> value = " boolean"
                type.contains("float") -> value = " float"
                type.contains("double") -> value = " double"
                type.contains("char") -> value = " varchar"
                type.contains("long") -> value = " long"
            }
            return value
        }

        fun capitalize(string: String?): String? {
            if (!TextUtils.isEmpty(string)) {
                return string!!.substring(0, 1).toUpperCase(Locale.US) + string.substring(1)
            }
            return if (string == null) null else ""
        }

    }

}