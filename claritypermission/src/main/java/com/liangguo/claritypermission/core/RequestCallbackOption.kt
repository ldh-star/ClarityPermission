package com.liangguo.claritypermission.core


/**
 * @author ldh
 * 时间: 2022/2/14 14:37
 * 邮箱: 2637614077@qq.com
 */
class RequestCallbackOption {

    private var grantedCallback: ((granted: PermissionResult.Granted) -> Unit)? = null

    private var deniedCallback: ((denied: PermissionResult.Denied) -> Unit)? = null

    private var canceledCallback: ((canceled: PermissionResult.Cancel) -> Unit)? = null

    fun granted(grantedCallback: ((granted: PermissionResult.Granted) -> Unit)? = null) =
        this.apply {
            this.grantedCallback = grantedCallback
        }

    fun denied(deniedCallback: ((denied: PermissionResult.Denied) -> Unit)? = null) =
        this.apply {
            this.deniedCallback = deniedCallback
        }

    fun canceled(canceledCallback: ((canceled: PermissionResult.Cancel) -> Unit)? = null) =
        this.apply {
            this.canceledCallback = canceledCallback
        }

    internal fun invoke(result: PermissionResult) {
        when (result) {
            is PermissionResult.Granted -> grantedCallback?.invoke(result)
            is PermissionResult.Denied -> deniedCallback?.invoke(result)
            is PermissionResult.Cancel -> canceledCallback?.invoke(result)
        }
    }
}