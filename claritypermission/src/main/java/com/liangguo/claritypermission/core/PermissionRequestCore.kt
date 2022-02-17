package com.liangguo.claritypermission.core

import androidx.fragment.app.FragmentActivity
import com.liangguo.claritypermission.filterDeniedPermissions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.isActive
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume


/**
 * @author ldh
 * 时间: 2022/2/17 19:25
 * 邮箱: 2637614077@qq.com
 */

/**
 * 在协程中进行权限申请的核心入口
 */
internal suspend fun doSuspendRequest(
    activity: FragmentActivity,
    vararg permissions: String
) = withContext(Dispatchers.Main) {
    val scope = suspendCancellableCoroutine<PermissionResult> { cancellableCoroutine ->
        requestPermissionsCore(activity, *permissions) {
            cancellableCoroutine.resume(it)
        }
    }
    return@withContext if (isActive) scope
    else PermissionResult.Cancel()
}

/**
 * 启动请求的核心方法
 */
internal fun requestPermissionsCore(
    activity: FragmentActivity,
    vararg permissions: String,
    resultCallback: PermissionResultInterface
) {
    //提前先检查一遍，如果所有权限都是已授权的，那就不去启动那个fragment了，直接进行回调
    //过滤出被拒绝的权限
    val deniedPermissions = activity.filterDeniedPermissions(permissions)
    if (deniedPermissions.isEmpty()) {
        //权限全都有那就直接回调了
        resultCallback(PermissionResult.Granted)
    } else {
        //有些权限是没有的，那就去申请
        RequestLauncher.launch(deniedPermissions, activity, resultCallback)
    }
}