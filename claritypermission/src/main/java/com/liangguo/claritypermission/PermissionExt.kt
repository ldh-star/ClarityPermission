package com.liangguo.claritypermission

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


/**
 * @author ldh
 * 时间: 2022/2/13 9:59
 * 邮箱: 2637614077@qq.com
 */

/**
 * 判断某项权限是否已拥有
 */
fun Context.isPermissionGranted(permission: String) =
    ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED

/**
 * 判断某项权限是否被永久拒绝
 */
fun Activity.isPermissionDeniedPermanently(permission: String) =
    ActivityCompat.shouldShowRequestPermissionRationale(
        this,
        permission
    )

/**
 * 把没有授权的权限过滤出来
 */
fun Context.filterDeniedPermissions(permissions: Array<out String>) = permissions.filter {
    ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
}.toTypedArray()
