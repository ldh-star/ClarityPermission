package com.liangguo.claritypermission.core


/**
 * @author ldh
 * 时间: 2021/12/12 14:48
 * 邮箱: 2637614077@qq.com
 *
 * 权限请求回调的结果
 */
sealed class PermissionResult {
    /**
     * 全部通过
     */
    object Granted : PermissionResult()

    /**
     * 其中有被拒绝的权限
     * @param deniedPermissions 所有被拒绝的权限，包括被永久拒绝的，一定不是空的集合
     * @param deniedPermissionsPermanently 被永久拒绝的权限
     */
    class Denied(val deniedPermissions: List<String>, val deniedPermissionsPermanently: List<String>) : PermissionResult()

    /**
     * 已取消
     * @param causedByActivityFinished 是否是因为Activity结束而导致的取消请求
     */
    class Cancel(val causedByActivityFinished: Boolean = false) : PermissionResult()


}
