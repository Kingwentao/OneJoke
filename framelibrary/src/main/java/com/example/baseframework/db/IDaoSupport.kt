package com.example.baseframework.db

import android.database.sqlite.SQLiteDatabase

/**
 * author: created by wentaoKing
 * date: created in 2020-03-22
 * description: 定义数据库dao层接口，为了后面可扩展
 */
interface IDaoSupport<T> {

    fun init(sqLiteDatabase: SQLiteDatabase, clazz: Class<T>)
    // 插入数据
    fun insert(t: T): Long

    // 批量插入  检测性能
    fun insert(datas: MutableList<T>)

    // 获取专门查询的支持类
  //  fun querySupport(): QuerySupport<T>

    // 查询所有
    fun query(): MutableList<T>


    fun delete(whereClause: String, vararg whereArgs: String): Int

    fun update(obj: T, whereClause: String, vararg whereArgs: String): Int
}