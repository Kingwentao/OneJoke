package com.example.baseframework.db.curd

import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.example.baseframework.db.DaoUtil
import com.example.baseframework.db.IDaoSupport

/**
 * author: created by wentaoKing
 * date: created in 2020-03-22
 * description:
 */
class DaoSupport<T>: IDaoSupport<T> {


    companion object{
        private const val TAG = "DaoSupport"
    }
    private lateinit var mSqLiteDatabase: SQLiteDatabase
    private lateinit var mClazz: Class<T>


    override fun init(sqLiteDatabase: SQLiteDatabase, clazz: Class<T>) {
        this.mSqLiteDatabase = sqLiteDatabase
        this.mClazz = clazz


        // 创建表
        /*"create table if not exists Person ("
                + "id integer primary key autoincrement, "
                + "name text, "
                + "age integer, "
                + "flag boolean)";*/

        val sb = StringBuffer()
        sb.append("create table if not exists ")
            .append(DaoUtil.getTableName(mClazz))
            .append("(id integer primary key autoincrement, ")

        val fields = mClazz.declaredFields
        for (field in fields){
            field.isAccessible = true
            val name = field.name
            val type = field.type.simpleName
            //  type转化成sql语句规则的类型
            sb.append(name).append(DaoUtil.getColumnType(type)).append(", ")
        }
        sb.replace(sb.length - 2,sb.length,")")
        val createTableSql = sb.toString()
        Log.d(TAG,"create database sql is --> $createTableSql")
        //创建表
        mSqLiteDatabase.execSQL(createTableSql)
    }

    override fun insert(t: T): Long {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun insert(datas: List<T>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    override fun delete(whereClause: String, vararg whereArgs: String): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun update(obj: T, whereClause: String, vararg whereArgs: String): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}