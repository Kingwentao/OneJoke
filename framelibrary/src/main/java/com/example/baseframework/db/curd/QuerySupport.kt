package com.example.baseframework.db.curd

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.example.baseframework.db.DaoUtil
import java.lang.reflect.Method
import java.util.*

/**
 * author: created by wentaoKing
 * date: created in 2020-03-22
 * description: 查询数据库支持类，支持拼装参数
 */
class QuerySupport<T> {

    // 查询的列
    private var mQueryColumns: Array<String>? = null
    // 查询的条件
    private var mQuerySelection: String? = null
    // 查询的参数
    private var mQuerySelectionArgs: Array<String>? = null
    // 查询分组
    private var mQueryGroupBy: String? = null
    // 查询对结果集进行过滤
    private var mQueryHaving: String? = null
    // 查询排序
    private var mQueryOrderBy: String? = null
    // 查询可用于分页
    private var mQueryLimit: String? = null

    private val mClass: Class<T>
    private val mSQLiteDatabase: SQLiteDatabase

    constructor(sqLiteDatabase: SQLiteDatabase, clazz: Class<T>) {
        this.mClass = clazz
        this.mSQLiteDatabase = sqLiteDatabase
    }

    fun columns(columns: Array<String>): QuerySupport<*> {
        this.mQueryColumns = columns
        return this
    }

    fun selectionArgs(selectionArgs: Array<String>): QuerySupport<T> {
        this.mQuerySelectionArgs = selectionArgs
        return this
    }

    fun having(having: String): QuerySupport<*> {
        this.mQueryHaving = having
        return this
    }

    fun orderBy(orderBy: String): QuerySupport<*> {
        this.mQueryOrderBy = orderBy
        return this
    }

    fun limit(limit: String): QuerySupport<*> {
        this.mQueryLimit = limit
        return this
    }

    fun groupBy(groupBy: String): QuerySupport<*> {
        this.mQueryGroupBy = groupBy
        return this
    }

    fun selection(selection: String): QuerySupport<T> {
        this.mQuerySelection = selection
        return this
    }

    fun query(): List<T> {
        val cursor = mSQLiteDatabase.query(
            DaoUtil.getTableName(mClass), mQueryColumns, mQuerySelection,
            mQuerySelectionArgs, mQueryGroupBy, mQueryHaving, mQueryOrderBy, mQueryLimit
        )
        clearQueryParams()
        return cursorToList(cursor)
    }

    fun queryAll(): List<T> {
        val cursor =
            mSQLiteDatabase.query(DaoUtil.getTableName(mClass), null, null, null, null, null, null)
        return cursorToList(cursor)
    }

    /**
     * 清空参数
     */
    private fun clearQueryParams() {
        mQueryColumns = null
        mQuerySelection = null
        mQuerySelectionArgs = null
        mQueryGroupBy = null
        mQueryHaving = null
        mQueryOrderBy = null
        mQueryLimit = null
    }

    /**
     * 通过Cursor封装成查找对象
     * @return 对象集合列表
     */
    private fun cursorToList(cursor: Cursor?): List<T> {
        val list = ArrayList<T>()
        if (cursor != null && cursor.moveToFirst()) {
            do {
                try {
                    val instance = mClass.newInstance()
                    val fields = mClass.declaredFields
                    for (field in fields) {
                        // 遍历属性
                        field.isAccessible = true
                        val name = field.name
                        // 获取角标
                        val index = cursor.getColumnIndex(name)
                        if (index == -1) {
                            continue
                        }
                        // 通过反射获取 游标的方法
                        val cursorMethod = cursorMethod(field.type)
                        if (cursorMethod != null) {
                            var value: Any? = cursorMethod.invoke(cursor, index) ?: continue

                            // 处理一些特殊的部分
                            if (field.type == Boolean::class.javaPrimitiveType || field.type == Boolean::class.java) {
                                if ("0" == value.toString()) {
                                    value = false
                                } else if ("1" == value.toString()) {
                                    value = true
                                }
                            } else if (field.type == Char::class.javaPrimitiveType || field.type == Char::class.java) {
                                value = (value as String)[0]
                            } else if (field.type == Date::class.java) {
                                val date = (value as Long?)!!
                                if (date <= 0) {
                                    value = null
                                } else {
                                    value = Date(date)
                                }
                            }
                            field.set(instance, value)
                        }
                    }
                    // 加入集合
                    list.add(instance)
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            } while (cursor.moveToNext())
        }
        cursor!!.close()
        return list
    }

    @Throws(Exception::class)
    private fun cursorMethod(type: Class<*>): Method {
        val methodName = getColumnMethodName(type)
        return Cursor::class.java.getMethod(methodName, Int::class.javaPrimitiveType!!)
    }

    private fun getColumnMethodName(fieldType: Class<*>): String {
        val typeName: String
        if (fieldType.isPrimitive) {
            typeName = DaoUtil.capitalize(fieldType.name)
        } else {
            typeName = fieldType.simpleName
        }
        var methodName = "get$typeName"
        if ("getBoolean" == methodName) {
            methodName = "getInt"
        } else if ("getChar" == methodName || "getCharacter" == methodName) {
            methodName = "getString"
        } else if ("getDate" == methodName) {
            methodName = "getLong"
        } else if ("getInteger" == methodName) {
            methodName = "getInt"
        }
        return methodName
    }
}