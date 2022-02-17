package com.liangguo.claritypermission

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.liangguo.claritypermission.core.*
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume


/**
 * @author ldh
 * 时间: 2021/12/12 17:27
 * 邮箱: 2637614077@qq.com
 */
/**
 * 对内的方法
 */
internal fun CancellableContinuation<PermissionResult>.doRequest(
    activity: FragmentActivity,
    vararg permissions: String
) {
    activity.requestPermissionsWithCallback(*permissions) {
        resume(
            if (isActive) it
            else PermissionResult.Cancel()
        )
    }
}

/**
 *
 */
private fun requestPermissionsCore(
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

/**
 * 用协程的方式请求权限
 * @return 权限请求回调的结果，见[PermissionResult]
 */
suspend fun Fragment.requestPermissionsWithCoroutine(vararg permissions: String) =
    suspendCancellableCoroutine<PermissionResult> {
        it.doRequest(requireActivity(), *permissions)
    }

/**
 * 用协程的方式请求权限
 * @return 权限请求回调的结果，见[PermissionResult]
 */
suspend fun FragmentActivity.requestPermissionsWithCoroutine(vararg permissions: String) =
    suspendCancellableCoroutine<PermissionResult> {
        it.doRequest(this, *permissions)
    }

/**
 * 回调函数式申请权限
 * @param resultCallback 返回结果的处理函数，permissionResult见[PermissionResult]
 */
fun FragmentActivity.requestPermissionsWithCallback(
    vararg permissions: String,
    resultCallback: PermissionResultInterface
) {
    requestPermissionsCore(this, *permissions) {
        resultCallback(it)
    }
}

/**
 * 回调函数式申请权限
 * @param resultCallback 返回结果的处理函数，permissionResult见[PermissionResult]
 */
fun Fragment.requestPermissionsWithCallback(
    vararg permissions: String,
    resultCallback: PermissionResultInterface
) {
    requireActivity().requestPermissionsWithCallback(*permissions) {
        resultCallback(it)
    }
}

fun FragmentActivity.requestPermissionsWithCallback(
    vararg permissions: String,
    deniedCallback: (denied: PermissionResult.Denied) -> Unit = {},
    grantedCallback: (granted: PermissionResult.Granted) -> Unit = {}
) {
    requestPermissionsWithCallback(*permissions) {
        if (it is PermissionResult.Granted) {
            grantedCallback(it)
        }
        if (it is PermissionResult.Denied) {
            deniedCallback(it)
        }
    }
}

/**
 * 回调函数式申请权限
 * @return 返回的是[RequestCallbackOption]，通过这个对象来设置回调
 */
fun Fragment.requestPermissions(
    vararg permissions: String
) = requireActivity().requestPermissions(*permissions)

/**
 * 回调函数式申请权限
 * @return 返回的是[RequestCallbackOption]，通过这个对象来设置回调
 */
fun FragmentActivity.requestPermissions(
    vararg permissions: String
): RequestCallbackOption {
    return RequestCallbackOption().apply {
        mainThreadHandler.post {
            requestPermissionsWithCallback(*permissions) {
                invoke(it)
            }
        }
    }
}