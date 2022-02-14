package com.liangguo.claritypermission.core

import android.os.Handler
import android.os.Looper


/**
 * @author ldh
 * 时间: 2022/2/14 16:44
 * 邮箱: 2637614077@qq.com
 */
internal object MainExecutor {

    private val mHandler = Handler(Looper.getMainLooper())

    fun post(func: () -> Unit) {
        mHandler.post(func)
    }

}