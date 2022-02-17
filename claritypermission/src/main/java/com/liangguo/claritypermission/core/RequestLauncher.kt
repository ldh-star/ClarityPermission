package com.liangguo.claritypermission.core

import androidx.fragment.app.FragmentActivity
import com.liangguo.claritypermission.filterDeniedPermissions
import java.lang.ref.WeakReference


/**
 * @author ldh
 * 时间: 2021/12/12 16:59
 * 邮箱: 2637614077@qq.com
 */
internal class RequestLauncher(private val mActivityReference: WeakReference<FragmentActivity>) {

    companion object {
        private const val FRAGMENT_TAG = "FRAGMENT_TAG"
    }

    private var mRequestCallback: IPermissionResultCallback? = null

    fun setCallBack(requestCallback: IPermissionResultCallback?): RequestLauncher {
        this.mRequestCallback = requestCallback
        return this
    }

    /**
     * 基础版，只设置了请求哪些权限
     */
    fun launch(permissions: Array<out String>): RequestLauncher {
        val activity = mActivityReference.get()
        if (activity == null || activity.isFinishing) {
            return this
        }

        //先检查一遍，把已经拥有的权限过滤掉
        val deniedPermissions = activity.filterDeniedPermissions(permissions)
        if (deniedPermissions.isNotEmpty()) {
            //剩下的这些就是还没有的权限，需要申请
            val oldFragment =
                activity.supportFragmentManager.findFragmentByTag(FRAGMENT_TAG) as PermissionFragment?
            oldFragment?.let {
                it.permissionCallBack = mRequestCallback
            } ?: doRequest(deniedPermissions, activity)
        } else {
            //全部权限都有，直接回调
            mRequestCallback?.onPermissionResult(PermissionResult.Granted)
        }
        return this
    }


    /**
     * 真正开始申请权限是从这里开始
     */
    private fun doRequest(permissions: Array<out String>, activity: FragmentActivity) {
        val newFragment = PermissionFragment(permissions)
        newFragment.permissionCallBack = mRequestCallback
        activity.supportFragmentManager
            .beginTransaction()
            .add(newFragment, FRAGMENT_TAG)
            .commitNowAllowingStateLoss()
    }

}