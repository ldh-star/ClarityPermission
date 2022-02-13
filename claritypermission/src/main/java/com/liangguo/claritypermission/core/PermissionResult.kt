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
     * @param deniedPermissions 所有被拒绝的权限，包括被永久拒绝的
     * @param deniedPermissionsPermanently 被永久拒绝的权限
     */
    class Denied(val deniedPermissions: List<String>, val deniedPermissionsPermanently: List<String>) : PermissionResult()

    /**
     * 已取消
     */
    object Cancel : PermissionResult()

}
