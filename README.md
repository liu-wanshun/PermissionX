# PermissionX

[![jitpack](https://jitpack.io/v/liu-wanshun/PermissionX.svg)](https://jitpack.io/#liu-wanshun/PermissionX)
[![license](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg?style=flat)](https://www.apache.org/licenses/LICENSE-2.0)

	一个简单易用的Android 运行时权限请求框架。

## Gradle

**1.** 在项目根目录下的 `build.gradle` 文件中加入

```groovy
allprojects {
    repositories {
        maven { url = "https://jitpack.io" }
    }
}
```

**2.** 在 app 模块`build.gradle`添加依赖

```groovy
dependencies {
    //仅支持AndroidX
    implementation("com.github.liu-wanshun:PermissionX:latest_version")
}
```

## 用法

**1.** 权限请求

  ```kotlin
 PermissionX.with(this)
    .permissions(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE)
    .onRequestRationale("授权通过后，方便在聊天中，提供发送手机相册中的媒体内容给对方的能力")
    .request { result ->
        Log.e("ssss", "request   onResult: isAllGranted  ->  ${result.isAllGranted}")
        Log.e("ssss", "request   onResult: Granted -> ${result.getGrantedList()}")
        Log.e("ssss", "request   onResult: Denied  -> ${result.getDeniedList()}")
    }
  ```

2.自定义弹框样式

```kotlin
//设置自定义弹框
PermissionX.setRationaleFactory(CustomRationaleFactory())

//取消设置自定义弹框（使用默认弹框）
PermissionX.setRationaleFactory(null)
```

3.提示

1. 限制：需要在 FragmentActivity / Fragment 环境中使用。
2. 建议 FragmentActivity 主题使用AppCompat的Theme，如果没有使用AppCompat的Theme，则可以使用以下2种方式
    1. 重写 style：`PermissionDialogTheme` 使用AppCompat
    2. 通过 `PermissionX.setRationaleFactory(CustomRationaleFactory())`自定义弹框
    3. 建议仅在点击时请求权限，禁止在生命周期`onCreate`,`onResume`中请求,也尽量避免在`onStart`中请求

## 效果演示

| 请求时效果                                        | 永久拒绝时效果                                             |
|----------------------------------------------|-----------------------------------------------------|
| <img src="picture/request.png" width="360"/> | <img src="picture/denied_forever.png" width="360"/> |



