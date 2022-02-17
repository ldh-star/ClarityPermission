package com.liangguo.claritypermission.core

import android.os.Handler
import android.os.Looper

/**
 * @author ldh
 * 时间: 2022/2/17 11:45
 * 邮箱: 2637614077@qq.com
 */

/**
 * 权限请求结果的回调
 */
internal typealias PermissionResultInterface = (permissionResult: PermissionResult) -> Unit

/**
 * 用来post函数
 */
internal val mainThreadHandler by lazy {
    Handler(Looper.getMainLooper())
}

