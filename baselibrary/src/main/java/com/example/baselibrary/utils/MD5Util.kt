package com.example.baselibrary.utils

import java.io.UnsupportedEncodingException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

/**
 * author: created by wentaoKing
 * date: created in 2020-04-19
 * description: md5工具类
 */
object MD5Util {


    /**利用MD5进行加密
     * @param inStr  待加密的字符串
     * @return  加密后的字符串
     * @throws NoSuchAlgorithmException  没有这种产生消息摘要的算法
     * @throws UnsupportedEncodingException
     */
    fun string2MD5(inStr: String): String {
        var md5: MessageDigest? = null
        try {
            md5 = MessageDigest.getInstance("MD5")
        } catch (e: Exception) {
            println(e.toString())
            e.printStackTrace()
            return ""
        }

        val charArray = inStr.toCharArray()
        val byteArray = ByteArray(charArray.size)

        for (i in charArray.indices)
            byteArray[i] = charArray[i].toByte()
        val md5Bytes = md5!!.digest(byteArray)
        val hexValue = StringBuffer()
        for (i in md5Bytes.indices) {
            val vab = md5Bytes[i].toInt() and 0xff
            if (vab < 16)
                hexValue.append("0")
            hexValue.append(Integer.toHexString(vab))
        }
        return hexValue.toString()
    }


}