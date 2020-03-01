package com.example.onejoke

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.baselibrary.dialog.AlertDialog
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(){

    companion object{
        private const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        btnComment.setOnClickListener {

            //TODO 测试自己写的dialog
            val dialog = AlertDialog.Builder(this)
                .setContentView(R.layout.detail_comment_dialog)
                .setText(R.id.btnSubmit,"发送")
                .fromTop()
                .setFullWidth()
                .show()

            //
            dialog.setOnClickListener(R.id.account_icon_weibo, View.OnClickListener {
                Toast.makeText(this,"分享到微博",Toast.LENGTH_LONG).show()
            })

        }

    }
}
