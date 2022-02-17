package com.liangguo.claritypermissionsample

import android.Manifest.permission.*
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.liangguo.claritypermission.requestPermissions
import com.liangguo.claritypermission.requestPermissionsWithCoroutine
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun click1(view: View) {
        lifecycleScope.launch {
            val result = withContext(Dispatchers.IO) {
                requestPermissionsWithCoroutine(WRITE_EXTERNAL_STORAGE)
            }
            Toast.makeText(this@MainActivity, result.javaClass.simpleName, Toast.LENGTH_LONG).show()
        }
    }

    fun click2(view: View) {
        requestPermissions(CAMERA, RECORD_AUDIO, READ_CONTACTS, WRITE_CONTACTS).granted {
            //权限被同意
            Toast.makeText(this@MainActivity, it.javaClass.simpleName, Toast.LENGTH_LONG).show()

        }.denied {
            //权限被拒绝
            Toast.makeText(
                this@MainActivity,
                "拒绝:" + it.deniedPermissions + "  永久拒绝:" + it.deniedPermissionsPermanently,
                Toast.LENGTH_LONG
            ).show()

        }
    }
}