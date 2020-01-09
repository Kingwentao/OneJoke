package com.example.baselibrary

import android.content.Context
import dalvik.system.BaseDexClassLoader
import java.io.File
import java.io.FileNotFoundException
import java.lang.reflect.Array

/**
 * author: created by wentaoKing
 * date: created in 2020-01-08
 * description:
 */
class FixDexManager() {

    private lateinit var mContext: Context
    private lateinit var mDexDir: File

    constructor(context: Context) : this() {
        this.mContext = context
        //获取应用可以访问的dex目录
        mDexDir = context.getDir("odex",Context.MODE_PRIVATE)

    }

    /**
     * 修复dex包
     */
    fun fixDex(path: String) {

        //1. 拿到已经运行的dexElements
        val applicationClassLoader = mContext.classLoader
        var applicationDexElements = getDexElementsByClassLoader(applicationClassLoader)

        //2. 获取我们下载好的补丁dexElement

        //2.1 移动补丁到系统可以访问的
        val srcFile = File(path)
        if (!srcFile.exists()){
            throw  FileNotFoundException()
        }

        val destFile = File(mDexDir,srcFile.name)

        //目标文件存在于dex目录直接return
        if (destFile.exists()) return

        copyFile(srcFile,destFile)

        //2.2 classLoader 读取fileDex 路径
        val fixDexFiles = ArrayList<File>()
        fixDexFiles.add(destFile)

        val optimizedDirectory = File(mDexDir,"odex")
        if (!optimizedDirectory.exists()){
            optimizedDirectory.mkdirs()
        }


        //修复文件
        for(fixDexFile in fixDexFiles){
            val fixDexClassLoader = BaseDexClassLoader(
                fixDexFile.absolutePath,
                optimizedDirectory.absoluteFile,
                null,
                applicationClassLoader
                )
            val fixDexElements = getDexElementsByClassLoader(fixDexClassLoader)

            //3. 把补丁插入到DexElements的第一个元素
           applicationDexElements = combainArray(fixDexElements,applicationDexElements)
        }

        //把合并完的数组注入到原来的  classLoader
        injectDexElement(applicationClassLoader,applicationDexElements)


    }

    /**
     * 注入到原来的类中
     */
    private fun injectDexElement(classLoader: ClassLoader?, dexElements: Any) {
        //获取pathList
        val pathListField = BaseDexClassLoader::class.java.getDeclaredField("pathList")
        pathListField.isAccessible = true
        val pathList = pathListField.get(classLoader)

        // pathList 里面的 dexElements
        val dexElementField = pathListField.javaClass.getDeclaredField("dexElements")
        dexElementField.isAccessible = true

        dexElementField.set(pathList,dexElements)
    }

    private fun copyFile(srcFile: File, destFile: File) {

    }

    /**
     * 合并两个数组
     */
    private fun combainArray(arrayLhs: Any,arrayRhs: Any):Any{
        val loadClass = arrayLhs.javaClass.componentType
        val i = Array.getLength(arrayLhs)
        val j = i + Array.getLength(arrayRhs)
        val result = Array.newInstance(loadClass,j)
        for ( k in 0..j){
            if (k < i){
                Array.set(result,k,Array.get(arrayLhs,k))
            }else{
                Array.set(result,k,Array.get(arrayRhs,k - i))
            }
        }
        return result
    }

    /**
     * 通过反射从classLoader获取dexElements
     */
    private fun getDexElementsByClassLoader(classLoader: ClassLoader?): Any {
        //获取pathList
        val pathListField = BaseDexClassLoader::class.java.getDeclaredField("pathList")
        pathListField.isAccessible = true
        val pathList = pathListField.get(classLoader)

        // pathList 里面的 dexElements
        val dexElementField = pathListField.javaClass.getDeclaredField("dexElements")
        dexElementField.isAccessible = true
        return dexElementField.get(pathList)

    }
}