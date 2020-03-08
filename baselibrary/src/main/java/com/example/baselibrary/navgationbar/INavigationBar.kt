package com.example.baselibrary.navgationbar

/**
 * author: created by wentaoKing
 * date: created in 2020-03-08
 * description: navgation bar 的规范接口
 */
interface INavigationBar {

    /**
     * 绑定布局id
     */
    fun bindLayoutId(): Int

    /**
     * 绑定头部的参数
     */
    fun applyView()

}