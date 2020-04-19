package com.example.baseframework.db.curd

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.ArrayMap
import android.util.Log
import com.example.baseframework.db.DaoUtil
import com.example.baseframework.db.IDaoSupport
import java.lang.Exception
import java.lang.reflect.Method
import java.util.*

/**
 * author: created by wentaoKing
 * date: created in 2020-03-22
 * description:
 */
class DaoSupport<T> : IDaoSupport<T> {


    companion object {
        private const val TAG = "DaoSupport"
        //缓存参数
        private val mPutMethodArgs = arrayOfNulls<Any>(2)
        //缓存方法
        private val mPutMethod = ArrayMap<String, Method>()
    }

    private lateinit var mSqLiteDatabase: SQLiteDatabase
    private lateinit var mClazz: Class<T>
    private var mQuerySupport: QuerySupport<T>? = null

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
        for (field in fields) {
            field.isAccessible = true
            val name = field.name
            val type = field.type.simpleName
            //  type转化成sql语句规则的类型
            sb.append(name).append(DaoUtil.getColumnType(type)).append(", ")
        }
        sb.replace(sb.length - 2, sb.length, ")")
        val createTableSql = sb.toString()
        Log.d(TAG, "create database sql is --> $createTableSql")
        //创建表
        mSqLiteDatabase.execSQL(createTableSql)
    }

    /**
     * 插入数据
     * @param t insert object
     * @return Long  inserted line
     */
    override fun insert(t: T): Long {
        // 使用的其实还是  原生的使用方式，只是我们是封装一下而已
        val values: ContentValues = contentValuesByObject(t)
        //速度比第三方的快一倍左右
        return mSqLiteDatabase.insert(DaoUtil.getTableName(mClazz), null, values)
    }

    /**
     * 转换成ContentValues对象
     */
    private fun contentValuesByObject(obj: T): ContentValues {
        val values = ContentValues()
        //通过反射获取变量
        val fields = mClazz.declaredFields
        for (field in fields) {
            field.isAccessible = true
            val key = field.name
            val value = field.get(obj)

            mPutMethodArgs[0] = key
            mPutMethodArgs[1] = value


            try {
                val filedTypeName = field.type.name
                var putMethod = mPutMethod[filedTypeName]
                //先判断缓存有没有
                if (putMethod == null) {
                    //使用反射获取put方法
                    putMethod = ContentValues::class.java.getDeclaredMethod(
                        "put", String::class.java
                        , value.javaClass
                    )
                    Log.d("jwtjwt","arg1  = ${mPutMethodArgs[0]} arg2  = ${mPutMethodArgs[1]}")
                    mPutMethod[filedTypeName] = putMethod
                }
                //通过反射执行put方法
                putMethod.invoke(values, mPutMethodArgs)
            } catch (e: Exception) {
                Log.d(TAG, "appear exception ${e.message}")
            } finally {
                mPutMethodArgs[0] = null
                mPutMethodArgs[1] = null
            }
        }
        return values
    }

    override fun insert(datas: MutableList<T>) {
        // 批量插入采用 事务 ： 可提升效率
        mSqLiteDatabase.beginTransaction()
        for (data in datas) {
            val result = insert(data)
            Log.d(TAG, "insert data success $result")
        }
        mSqLiteDatabase.setTransactionSuccessful()
        mSqLiteDatabase.endTransaction()
    }


    override fun delete(whereClause: String, vararg whereArgs: String): Int {
        return mSqLiteDatabase.delete(DaoUtil.getTableName(mClazz), whereClause, whereArgs)
    }

    override fun update(obj: T, whereClause: String, vararg whereArgs: String): Int {
        val values = contentValuesByObject(obj)
        return mSqLiteDatabase.update(
            DaoUtil.getTableName(mClazz),
            values, whereClause, whereArgs
        )
    }

    // 查询目前直接查询所有,希望单独写一个类做到按条件查询:age = 22  name = darren
    override fun query(): MutableList<T> {
        val cursor =
            mSqLiteDatabase.query(DaoUtil.getTableName(mClazz), null, null, null, null, null, null)
        return cursorToList(cursor)
    }

    /**
     *
     */
    private fun cursorToList(cursor: Cursor?): MutableList<T> {
        val list = ArrayList<T>()
        if (cursor != null && cursor.moveToFirst()) {
            // 不断的从游标里面获取数据
            do {
                try {
                    // 通过反射new对象
                    val instance = mClazz.newInstance()
                    val fields = mClazz.declaredFields


                    for (field in fields) {
                        // 遍历属性
                        field.isAccessible = true
                        val name = field.name
                        // 获取角标  获取在第几列
                        val index = cursor.getColumnIndex(name)
                        if (index == -1) {
                            continue
                        }

                        // 通过反射获取 游标的方法  field.getType() -> 获取的类型
                        val cursorMethod = cursorMethod(field.type)
                        if (cursorMethod != null) {
                            // 通过反射获取了 value
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

                            // 通过反射注入
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

    // 获取游标的方法
    @Throws(Exception::class)
    private fun cursorMethod(type: Class<*>): Method {
        val methodName = getColumnMethodName(type)
        // type String getString(index); int getInt; boolean getBoolean
        return Cursor::class.java.getMethod(methodName, Int::class.javaPrimitiveType!!)
    }

    private fun getColumnMethodName(fieldType: Class<*>): String {
        val typeName: String?
        if (fieldType.isPrimitive) {
            typeName = DaoUtil.capitalize(fieldType.name)
        } else {
            typeName = fieldType.simpleName
        }

        if (typeName == null) throw Exception("fileType is null")

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

    override fun querySupport(): QuerySupport<T> {
        if (mQuerySupport == null) {
            mQuerySupport = QuerySupport(mSqLiteDatabase, mClazz)
        }
        return mQuerySupport!!
    }
}