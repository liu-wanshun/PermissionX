# PermissionX

[![](https://jitpack.io/v/com.gitee.liu_wanshun/PermissionX.svg)](https://jitpack.io/#com.gitee.liu_wanshun/PermissionX)

​	一个简单易用的Android 运行时权限请求框架。

## Gradle

**1.** 在项目根目录下的 `build.gradle` 文件中加入

```groovy
allprojects {
	repositories {
		maven { url 'https://jitpack.io' }
	}
}
```

**2.** 在 app 模块`build.gradle`添加依赖

```groovy
dependencies {
  //仅支持AndroidX
  implementation 'com.gitee.liu_wanshun:PermissionX:0.1.0'
}
```



## 用法

**1.** 权限使用目的解释弹窗样式配置（可选）

```java
PermissionX.getDefaultConfig()
  //设置权限解释弹窗AlertDialog主题(可选,默认为宿主Activity的主题中的AlertDialogTheme)，可在主题中自定义布局界面，参考demo
  .setAlertDialogTheme(R.style.Theme_Material3_DayNight_Dialog_Alert)
  //设置权限解释弹窗位置(可选,默认为Gravity.CENTER)
  .setGravity(Gravity.BOTTOM)
```
**2.** 权限请求

  ```java
PermissionX.init(this)
  .permissions(Manifest.permission.CAMERA)
  //以下解释中的字符串推荐使用String资源
  .onRequestRationale("这里解释权限请求的原因，因合规化需求，推荐进行解释。【可选方法，如不需要可不写该方法】")
  .onDeniedRationale("因用户拒绝了权限，这里可以进行解释，同意后将再次请求权限。【可选方法，如不需要可不写该方法】", () -> {
    Log.e("PermissionX", "此时用户不认可解释，仍然拒绝，可以自定义操作，比如退出，或者不进行需要权限的操作");
  })
  .onDeniedForeverRationale("因用户永久拒绝权限解释，同意将跳转设置界面引导用户自己开启权限。【可选方法，如不需要可不写该方法】", () -> {
    Log.e("PermissionX", "此时用户不认可解释，仍然拒绝，可以自定义操作，比如退出，或者不进行需要权限的操作");
  })
  .request(result -> {
    //此时可根据权限请求的结果执行对应的操作
    Log.e("PermissionX", "权限请求的结果: " + result);
  });
  ```


## 更新日志

[Releases](https://gitee.com/liu_wanshun/PermissionX/releases)


## License

```
Copyright (C) 2021. liuwanshun

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
