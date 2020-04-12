package com.example.onejoke

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.baseframework.DefaultNavigationBar
import com.example.baseframework.db.DaoSupportFactory
import com.example.baseframework.db.IDaoSupport
import com.example.baseframework.db.curd.DaoSupport
import com.example.baseframework.http.HttpCallback
import com.example.baselibrary.dialog.AlertDialog
import com.example.baselibrary.http.HttpUtils
import com.example.onejoke.model.DiscoverListResult
import com.example.onejoke.model.Person
import kotlinx.android.synthetic.main.activity_main.*
import pub.devrel.easypermissions.EasyPermissions
import java.lang.Exception


class MainActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks {


    companion object {
        private const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //初始化bar
        initTitleBar()

        //请求权限
        requestSomePermission()

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

        // 测试网络框架
        testHttpEngine()

        //测试创建数据库
        testCreateDataBase()
    }


    private fun requestSomePermission() {
        val perms =
            arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )

        val per = arrayListOf<String>(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        if (EasyPermissions.hasPermissions(this, *perms)) {
            // Already have permission, do the thing
            // ...
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(
                this, "请求啦啦啦啦啦啦",
                101, *perms
            )
        }
    }

    private fun testCreateDataBase() {

        val person = Person("jwt", 123)
        DaoSupportFactory.init()
        val daoSupport = DaoSupportFactory.getDao(Person::class.java)
        testInsertDataBase(daoSupport)
    }

    private fun testInsertDataBase(daoSupport: IDaoSupport<Person>) {
        val persons = mutableListOf<Person>()
        for (i in 0..10) {
            val person = Person("wentaoking${i+1}", i + 1)
            persons.add(person)
        }
        daoSupport.insert(persons)
    }

    private fun testHttpEngine() {
        HttpUtils.with(this).url("http://is.snssdk.com/2/essay/discovery/v3/")
            .addParams("iid", "6152551759").addParams("aid", "7")
            .execute(object : HttpCallback<DiscoverListResult>() {
                override fun onSuccess(result: DiscoverListResult) {
                    Log.d(TAG, "请求成功 $result")
                }

                override fun onError(e: Exception) {
                    Log.d(TAG, "请求失败 ${e.printStackTrace()}")
                }

            })
    }

    private fun initTitleBar() {

        val navigationBar =
            DefaultNavigationBar.Builder(this, ltTitle)
                .setTitle("投稿")
                .setRightText("右边的文本")
                .builder()

    }


    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        Log.d(TAG, "权限申请失败")
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        Log.d(TAG, "权限申请成功")
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)


        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
}
