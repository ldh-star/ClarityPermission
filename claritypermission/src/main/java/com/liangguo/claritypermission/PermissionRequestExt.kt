package com.liangguo.claritypermission

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.liangguo.claritypermission.core.IPermissionResultCallback
import com.liangguo.claritypermission.core.PermissionResult
import com.liangguo.claritypermission.core.RequestCallbackOption
import com.liangguo.claritypermission.core.RequestLauncher
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
            else PermissionResult.Cancel
        )
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
    resultCallback: (permissionResult: PermissionResult) -> Unit
) {
    RequestLauncher(this)
        .setCallBack(object : IPermissionResultCallback {
            override fun onPermissionResult(permissionResult: PermissionResult) {
                resultCallback(permissionResult)
            }
        }).launch(permissions)
}

/**
 * 回调函数式申请权限
 * @param resultCallback 返回结果的处理函数，permissionResult见[PermissionResult]
 */
fun Fragment.requestPermissionsWithCallback(
    vararg permissions: String,
    resultCallback: (permissionResult: PermissionResult) -> Unit
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
    val requestCallbackOption = RequestCallbackOption()
    requestPermissionsWithCallback(*permissions) {
        requestCallbackOption.invoke(it)
    }
    return requestCallbackOption
}