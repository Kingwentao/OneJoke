package com.example.onejoke

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.baselibrary.dialog.AlertDialog

class MainActivity : AppCompatActivity(), View.OnClickListener {

    override fun onClick(v: View?) {
        //TODO 测试自己写的dialog
       AlertDialog.Builder(this).create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
