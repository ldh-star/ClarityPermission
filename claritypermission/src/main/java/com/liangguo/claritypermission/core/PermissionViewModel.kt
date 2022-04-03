package com.liangguo.claritypermission.core

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


/**
 * @author ldh
 * 时间: 2022/4/3 9:25
 * 邮箱: 2637614077@qq.com
 */
class PermissionViewModel: ViewModel() {

    val requestInfo = MutableLiveData<Array<out String>>()

}