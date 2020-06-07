package com.example.baseframework.skin.attr

/**
 * author: created by wentaoKing
 * date: created in 2020-06-07
 * description: 皮肤的类型
 */
enum class SkinType(val mResName: String) {

    TEXT_COLOR("textColor") {
        override fun skin() {

        }
    },BACKGROUND("background") {
        override fun skin() {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
    },SRC("src") {
        override fun skin() {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
    };


    abstract fun skin()
}