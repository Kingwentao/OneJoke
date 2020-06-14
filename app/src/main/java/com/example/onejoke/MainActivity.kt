package com.example.onejoke

import android.Manifest
import android.content.Intent
import android.content.res.AssetManager
import android.content.res.Resources
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.baseframework.BaseSkinActivity
import com.example.baseframework.DefaultNavigationBar
import com.example.baseframework.skin.SkinManager
import kotlinx.android.synthetic.main.activity_main.*
import pub.devrel.easypermissions.EasyPermissions
import java.io.File
import java.lang.Exception


class MainActivity : BaseSkinActivity(), EasyPermissions.PermissionCallbacks, View.OnClickListener {


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

        initListener()

//        btnComment.setOnClickListener {
//
//            testSkin()
//
//            val dialog = AlertDialog.Builder(this)
//                .setContentView(R.layout.detail_comment_dialog)
//                .setText(R.id.btnSubmit, "发送")
//                .fromTop()
//                .setFullWidth()
//                .show()
//
//            //
//            dialog.setOnClickListener(R.id.account_icon_weibo, View.OnClickListener {
//                Toast.makeText(this, "分享到微博", Toast.LENGTH_LONG).show()
//            })
//        }

    }

    private fun initListener() {
        btnChangeSkin.setOnClickListener(this)
        btnDefaultSkin.setOnClickListener(this)
        btnSkip.setOnClickListener(this)
    }

    private fun initTitleBar() {

        val navigationBar =
            DefaultNavigationBar.Builder(this, ltTitle)
                .setTitle("投稿")
                .setRightText("右边的文本")
                .builder()
    }

    //测试更换皮肤
    fun testSkin() {

        Toast.makeText(this, "换肤中...", Toast.LENGTH_LONG).show()
        //获取resource对象
        val resources = resources
        //创建AssetManager
        val asset = AssetManager::class.java.newInstance()
        //皮肤资源的路径
        val skinSrcPath =
            Environment.getExternalStorageDirectory().absolutePath.plus(File.separator)
                .plus("red.skin")
        try {
            //添加本地的资源皮肤
            val method =
                AssetManager::class.java.getDeclaredMethod("addAssetPath", String::class.java)
            method.invoke(asset, skinSrcPath)
            val resource = Resources(asset, resources.displayMetrics, resources.configuration)
            //获取皮肤资源中的id（资源name/资源类型/资源所在的包名）
            val resourceId =
                resource.getIdentifier("image_src", "drawable", "com.example.skinplugin")
            val drawable = resource.getDrawable(resourceId, null)
            //ivSkin.setImageDrawable(drawable)
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d(TAG, "换肤异常")
        }

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


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnChangeSkin -> {
                val skinPath = Environment.getExternalStorageDirectory().absolutePath +
                        File.separator + "new.skin"
                if (File(skinPath).exists()){
                    Log.d(TAG,"file is exist!")
                }
                SkinManager.getInstance().init(this)
                SkinManager.getInstance().loadSkin(skinPath)

            }
            R.id.btnDefaultSkin -> {
                SkinManager.getInstance().recoverDefaultSkin()
            }
            R.id.btnSkip -> {
                val intent = Intent(this,MainActivity::class.java)
                startActivity(intent)
            }
        }
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
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }
}
