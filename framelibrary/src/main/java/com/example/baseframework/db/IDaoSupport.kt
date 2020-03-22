package com.example.baseframework.db

import android.database.sqlite.SQLiteDatabase
import com.example.baseframework.db.curd.QuerySupport

/**
 * author: created by wentaoKing
 * date: created in 2020-03-22
 * description:
 */
interface IDaoSupport<T> {

    fun init(sqLiteDatabase: SQLiteDatabase, clazz: Class<T>)
    // 插入数据
    fun insert(t: T): Long

    // 批量插入  检测性能
    fun insert(datas: List<T>)

    // 获取专门查询的支持类
  //  fun querySupport(): QuerySupport<T>

    // 按照语句查询


    fun delete(whereClause: String, vararg whereArgs: String): Int

    fun update(obj: T, whereClause: String, vararg whereArgs: String): Int
}