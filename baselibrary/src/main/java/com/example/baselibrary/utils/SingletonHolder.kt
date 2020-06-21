package com.example.baselibrary.utils

/**
 * author: created by wentaoKing
 * date: created in 2020-06-21
 * description: 封装逻辑来懒惰地在此类中创建和初始化带有参数的单例
 *
 * 父类泛型对象可以赋值给子类泛型对象，用 in；
 * 子类泛型对象可以赋值给父类泛型对象，用 out；
 */

open class SingletonHolder<out T, in A>(creator: (A) -> T) {

    private var creator: ((A) -> T)? = creator
    @Volatile
    private var instance: T? = null

    fun getInstance(arg: A): T {
        val i = instance
        if (i != null) {
            return i
        }

        return synchronized(this) {
            val i2 = instance
            if (i2 != null) {
                i2
            } else {
                val created = creator!!(arg)
                instance = created
                creator = null
                created
            }
        }
    }

}