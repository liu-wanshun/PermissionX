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
    implementation 'com.gitee.liu_wanshun:PermissionX:Tag'
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
	//设置AlertDialog自定义主题
	PermissionDialogConfig.setDefaultDialogTheme(R.style.alertdialog);

	//设置是否使用AndroidX的AlertDialog（默认值 true）
	PermissionDialogConfig.setAlertDialogAndroidX(true);


	//使用自定义Callback
	PermissionX.init(this)
            .permission(Manifest.permission.CAMERA)
  	    .onRequestRationale("解释请求权限的原因")
  	    //CustomCallback为自定义Callback，参考demo
  	    .transform(CustomCallback::new)
  	    .request((Granted, denyForever) -> {
    	         Log.e("ssss", "onResult: " + Granted + "  " + denyForever);
   	    });

```


