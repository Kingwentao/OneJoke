package com.example.onejoke

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.baseframework.DefaultNavigationBar
import com.example.baselibrary.dialog.AlertDialog
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //初始化bar
        initTitleBar()

        btnComment.setOnClickListener {

            val dialog = AlertDialog.Builder(this)
                .setContentView(R.layout.detail_comment_dialog)
                .setText(R.id.btnSubmit, "发送")
                .fromTop()
                .setFullWidth()
                .show()

            //
            dialog.setOnClickListener(R.id.account_icon_weibo, View.OnClickListener {
                Toast.makeText(this, "分享到微博", Toast.LENGTH_LONG).show()
            })


        }

    }

    private fun initTitleBar() {

        val navigationBar =
            DefaultNavigationBar.Builder(this, ltTitle)
                .setTitle("投稿")
                .setRightText("右边的文本")
                .builder()

    }
}
