package com.liangguo.claritypermission.core

import android.util.Log
import android.widget.Toast
import androidx.fragment.app.FragmentActivity


/**
 * @author ldh
 * 时间: 2021/12/12 16:59
 * 邮箱: 2637614077@qq.com
 */
internal object RequestLauncher {

    private const val FRAGMENT_TAG = "PermissionFragmentTag"

    /**
     * 调用此函数应直接传入还没有得到的权限
     */
    fun launch(
        permissions: Array<out String>,
        activity: FragmentActivity,
        callback: PermissionResultInterface
    ): RequestLauncher {
        if (activity.isFinishing || activity.isDestroyed) {
            callback(PermissionResult.Cancel(true))
            return this
        }

        val oldFragment =
            activity.supportFragmentManager.findFragmentByTag(FRAGMENT_TAG) as PermissionFragment?
        oldFragment?.let {
            //如果旧的Fragment已经存在，就等它消失之后再进行权限申请
            oldFragment.destroyedCallback = {
                launch(permissions, activity, callback)
            }
        } ?: launchFragment(permissions, activity, callback)
        return this
    }


    /**
     * 启动Fragment来进行权限申请
     */
    private fun launchFragment(
        permissions: Array<out String>,
        activity: FragmentActivity,
        callback: PermissionResultInterface
    ) {
        val newFragment = PermissionFragment(permissions)
        newFragment.permissionCallBack = callback
        activity.supportFragmentManager
            .beginTransaction()
            .add(newFragment, FRAGMENT_TAG)
            .commitNowAllowingStateLoss()
    }

}