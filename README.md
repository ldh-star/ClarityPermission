
# 超简单的权限申请-ClarityPermission
![](https://api.bintray.com/packages/li-xiaojun/jrepo/xpopup/images/download.svg)  
![](https://img.shields.io/badge/author-ldh-brightgreen.svg) ![](https://img.shields.io/badge/compileSdkVersion-32-orange.svg) ![](https://img.shields.io/badge/minSdkVersion-21-orange.svg) ![](https://img.shields.io/hexpm/l/plug.svg)


### 使用

#### 1.在 build.gradle 中添加依赖


```gradle
implementation 'com.github.ldh-star:ClarityPermission:1.0.0'
```

#### 2.使用

##### 以协程的方式使用
```kotlin
CoroutineScope(Dispatchers.Main).launch {
            val result = requestPermissionsWithCoroutine(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
```

##### 以回调函数的方式使用
```kotlin
requestPermissionsWithCallback(Manifest.permission.CAMERA) { result ->

}
```

##### 返回结果 result

```kotlin
 when (result) {
                is PermissionResult.Granted -> {
                    //权限全部同意
                }
                is PermissionResult.Denied -> {
                    result.deniedPermissions//被拒绝的权限
                    result.deniedPermissionsPermanently//被永久拒绝的权限
                }
            }
```