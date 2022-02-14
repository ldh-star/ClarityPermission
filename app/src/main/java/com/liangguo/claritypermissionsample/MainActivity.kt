package com.liangguo.claritypermissionsample

import android.Manifest
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.liangguo.claritypermission.requestPermissionsWithCallback
import com.liangguo.claritypermission.requestPermissionsWithCoroutine
import com.liangguo.claritypermission.core.PermissionResult
import com.liangguo.claritypermission.requestPermissions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun click1(view: View) {
        CoroutineScope(Dispatchers.Main).launch {
            val result = requestPermissionsWithCoroutine(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            Toast.makeText(this@MainActivity, result.javaClass.simpleName, Toast.LENGTH_LONG).show()
        }
    }

    fun click2(view: View) {
        requestPermissionsWithCallback(Manifest.permission.CAMERA) {
            if (it is PermissionResult.Denied) {
                it.deniedPermissions
            }
            Toast.makeText(this@MainActivity, it.javaClass.simpleName, Toast.LENGTH_LONG).show()
        }

        requestPermissions(Manifest.permission.RECORD_AUDIO).granted {
            //权限被同意
        }.denied {
            //权限被拒绝
        }

    }
}