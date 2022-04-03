package com.liangguo.claritypermission.core

import android.content.Context
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import com.liangguo.claritypermission.isPermissionDeniedPermanently


/**
 * @author ldh
 * 时间: 2021/12/12 15:34
 * 邮箱: 2637614077@qq.com
 */
internal class PermissionFragment : Fragment() {

    /**
     * 对外的权限申请回调
     */
    var permissionCallBack: PermissionResultInterface? = null

    /**
     * 移除自己后的回调
     */
    var destroyedCallback: (() -> Unit)? = null

    private val mViewModel by lazy {
        ViewModelProvider(requireActivity())[PermissionViewModel::class.java]
    }

    private val mActivityLifecycleObserver = object : LifecycleEventObserver {
        override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
            if (event == Lifecycle.Event.ON_CREATE) {
                requireActivity().lifecycle.removeObserver(this)
                //注册权限请求回调
                val requester = registerForActivityResult(
                    ActivityResultContracts.RequestMultiplePermissions(),
                    mPermissionCallback
                )

                mViewModel.requestInfo.observe(this@PermissionFragment) {
                    requester.launch(it)
                }
            }
        }
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
            permissionCallBack?.invoke(PermissionResult.Granted)
        } else {
            //有被拒绝的
            permissionCallBack?.invoke(
                PermissionResult.Denied(
                    denied,
                    deniedPermanently
                )
            )
        }
        removeThis()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        requireActivity().lifecycle.addObserver(mActivityLifecycleObserver)
    }

    /**
     * 权限申请完了过后就可以移除自己了
     */
    private fun removeThis() {
        requireActivity().lifecycle.removeObserver(mActivityLifecycleObserver)
        parentFragmentManager.beginTransaction().remove(this).commitAllowingStateLoss()
        mainThreadHandler.post {
            destroyedCallback?.invoke()
        }
    }

}