package com.liangguo.claritypermission

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.liangguo.claritypermission.core.*
import kotlinx.coroutines.*
import kotlin.coroutines.resume


/**
 * @author ldh
 * 时间: 2021/12/12 17:27
 * 邮箱: 2637614077@qq.com
 */

/**
 * 用协程的方式请求权限
 * @return 权限请求回调的结果，见[PermissionResult]
 */
suspend fun Fragment.requestPermissionsWithCoroutine(vararg permissions: String) =
    requireActivity().requestPermissionsWithCoroutine(*permissions)


/**
 * 用协程的方式请求权限
 * @return 权限请求回调的结果，见[PermissionResult]
 */
suspend fun FragmentActivity.requestPermissionsWithCoroutine(vararg permissions: String) =
    doSuspendRequest(this, *permissions)

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