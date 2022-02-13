package com.liangguo.claritypermission.core

import android.content.Context
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.liangguo.claritypermission.isPermissionDeniedPermanently


/**
 * @author ldh
 * 时间: 2021/12/12 15:34
 * 邮箱: 2637614077@qq.com
 */
internal class PermissionFragment(private var mRequestInfo: Array<out String>) : Fragment() {

    /**
     * 对外的权限申请回调
     */
    var permissionCallBack: IPermissionResultCallback? = null


    override fun onAttach(context: Context) {
        super.onAttach(context)
        requireActivity().lifecycle.addObserver(object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                if (event == Lifecycle.Event.ON_CREATE) {
                    requireActivity().lifecycle.removeObserver(this)
                    //注册权限请求回调
                    val requester = registerForActivityResult(
                        ActivityResultContracts.RequestMultiplePermissions(),
                        mPermissionCallback
                    )
                    requester.launch(mRequestInfo)
                }
            }
        })
    }


    /**
     * 注册的Activity申请到的权限的回调
     */
    private val mPermissionCallback = ActivityResultCallback<Map<String, Boolean>> { map ->
        val denied = mutableListOf<String>()
        val deniedPermanently = mutableListOf<String>()
        map.forEach {
            if (!it.value) {
                //被拒绝的权限
                denied.add(it.key)
                if (requireActivity().isPermissionDeniedPermanently(it.key)) {
                    //被永久拒绝的权限
                    deniedPermanently.add(it.key)
                }
            }
        }
        if (denied.isEmpty()) {
            //没有被拒绝的，那就是全部同意了
            permissionCallBack?.onPermissionResult(PermissionResult.Granted)
        } else {
            //有被拒绝的
            permissionCallBack?.onPermissionResult(
                PermissionResult.Denied(
                    denied,
                    deniedPermanently
                )
            )
        }
        removeThis()
    }

    /**
     * 权限申请完了过后就可以移除自己了
     */
    private fun removeThis() {
        parentFragmentManager.beginTransaction().remove(this).commitAllowingStateLoss()
    }

}