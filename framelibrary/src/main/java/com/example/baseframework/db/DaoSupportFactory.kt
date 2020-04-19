package com.example.baseframework.db

import android.database.sqlite.SQLiteDatabase
import android.os.Environment
import com.example.baseframework.db.curd.DaoSupport
import java.io.File

/**
 * author: created by wentaoKing
 * date: created in 2020-03-22
 * description:
 */
object DaoSupportFactory {

    private var mFactory: DaoSupportFactory? = null
    // 持有外部数据库的引用
    private lateinit var mSqLiteDatabase: SQLiteDatabase



    init {
        val dbRoot = File(
            Environment.getExternalStorageDirectory()
                .absolutePath + File.separator + "nhdz" + File.separator + "database"
        )

        if (!dbRoot.exists()) {
            dbRoot.mkdirs()
        }
        val dbFile = File(dbRoot, "nhdz.db")

        // 打开或者创建一个数据库
        mSqLiteDatabase = SQLiteDatabase.openOrCreateDatabase(dbFile, null)
    }


    fun <T> getDao(clazz: Class<T>): IDaoSupport<T> {
        val daoSupport: IDaoSupport<T> = DaoSupport()
        daoSupport.init(mSqLiteDatabase, clazz)
        return daoSupport
    }
}