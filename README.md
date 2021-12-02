# PermissionX

[![](https://jitpack.io/v/com.gitee.liu_wanshun/PermissionX.svg)](https://jitpack.io/#com.gitee.liu_wanshun/PermissionX)

**Step 1.** Add it in your root build.gradle at the end of repositories:

```groovy
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```

**Step 2.** Add the dependency

```groovy
dependencies {
    ...
    implementation 'com.gitee.liu_wanshun:PermissionX:0.0.6'
    //需要AndroidX依赖
}
```

(Please replace `Tag`  with the latest version numbers: [![](https://jitpack.io/v/com.gitee.liu_wanshun/PermissionX.svg)](https://jitpack.io/#com.gitee.liu_wanshun/PermissionX)



## 功能介绍

1. 单个权限请求

```java
	PermissionX.init(this)
          //单个权限permission
                .permission(Manifest.permission.CAMERA)
                .onRequestRationale("解释请求权限的原因")
                .request(result -> {
                    Log.e("ssss", "onResult: " + result);
                });
```

2. 多个权限请求

```java
	PermissionX.init(this)
          //多个权限permissions
                .permissions(Manifest.permission.CAMERA,Manifest.permission.READ_CONTACTS)
                .onRequestRationale("解释请求权限的原因")
                .request(result -> {
                    Log.e("ssss", "onResult: " + result);
                });
```

3. 自定义设置

```java
    PermissionX.getDefaultConfig()
            //设置权限解释弹窗AlertDialog主题(可选,默认为宿主Activity的主题中的AlertDialogTheme)
      			//可在主题中自定义布局界面
            .setAlertDialogTheme(R.style.Theme_Material3_DayNight_Dialog_Alert)
            //设置权限解释弹窗位置(可选,默认为Gravity.CENTER)
            .setGravity(Gravity.BOTTOM)


	//拒绝权限处理
	        PermissionX.init(this)
                .permission(Manifest.permission.CAMERA)
                .onRequestRationale("解释请求权限的原因")
                //在拒绝权限时进行解释（可选）
                .onDeniedRationale("拒绝了权限，进行解释，同意后将再次请求权限", () -> {
                    Log.e("ssss", "不认可拒绝解释，可以退出");
                })
                //在永久拒绝权限时进行解释（可选）
                .onDeniedForeverRationale("永久拒绝权限解释，同意将跳转设置界面让用户自己开启权限", () -> {
                    Log.e("ssss", "不认可永久拒绝解释,可以退出");
                })
                .request(result -> {
                    Log.e("ssss", "onResult: $result");
                });

```

