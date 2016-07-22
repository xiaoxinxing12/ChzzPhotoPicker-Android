:running:CHZZPhotoPicker-Android
============


## 主要功能
- [x] 单图选择
- [x] 多图选择
- [x] 拍照选择
- [x] 图片选择预览（支持微博长图）、缩放查看
- [x] 图片预览（支持微博长图）、缩放查看
- [x] 支持 glide、picasso、universal-image-loader、xutils 图片加载库
- [x] 正方形、圆形头像、带边框的圆形头像控件
- [x] 朋友圈列表界面的九宫格图片控件
- [x] 发布朋友圈界面的可拖拽排序的九宫格图片控件
- [x] 覆盖相应的资源文件来定制界面

## 效果图与示例 apk

![PhotoPicker-Demo](http://7xk9dj.com1.z0.glb.clouddn.com/%40%2Fphotopicker%2Fbga-photopicker3.gif?imageView2/2/w/300)



## Gradle 依赖


由于需要支持微博长图预览，该库中已经引入了 [PhotoView](https://github.com/chrisbanes/PhotoView) 的源码并进行了修改，所以你的项目中就不要再重复引入 [PhotoView](https://github.com/chrisbanes/PhotoView) 了

```groovy
dependencies {
    compile 'com.android.support:appcompat-v7:23.1.1'
    compile 'com.android.support:recyclerview-v7:23.1.1'
    compile 'org.chzz:adapter:1.0.0@aar'

   

    // 必须依赖下面四个图片加载库中的某一个
    compile 'com.github.bumptech.glide:glide:3.7.0'
//    compile 'com.squareup.picasso:picasso:2.5.2'
//    compile 'com.nostra13.universalimageloader:universal-image-loader:1.9.5'
//    compile 'org.xutils:xutils:3.3.36'
}
```

## 接口说明

> CHZZPhotoPickerActivity

```java
/**
 * @param context        应用程序上下文
 * @param imageDir       拍照后图片保存的目录。如果传null表示没有拍照功能，如果不为null则具有拍照功能，
 * @param maxChooseCount 图片选择张数的最大值
 * @param selectedImages 当前已选中的图片路径集合，可以传null
 * @return
 */
public static Intent newIntent(Context context, File imageDir, int maxChooseCount, ArrayList<String> selectedImages)

/**
 * 获取已选择的图片集合
 *
 * @param intent
 * @return
 */
public static ArrayList<String> getSelectedImages(Intent intent)
```

> CHZZPhotoPreviewActivity

```java
/**
 * 获取查看多张图片的intent
 *
 * @param context
 * @param saveImgDir      保存图片的目录，如果传null，则没有保存图片功能
 * @param previewImages   当前预览的图片目录里的图片路径集合
 * @param currentPosition 当前预览图片的位置
 * @return
 */
public static Intent newIntent(Context context, File saveImgDir, ArrayList<String> previewImages, int currentPosition)

/**
 * 获取查看单张图片的intent
 *
 * @param context
 * @param saveImgDir 保存图片的目录，如果传null，则没有保存图片功能
 * @param photoPath  图片路径
 * @return
 */
public static Intent newIntent(Context context, File saveImgDir, String photoPath)
```


## Proguard

```java
## ----------------------------------
##      UIL相关
## ----------------------------------
-keep class com.nostra13.universalimageloader.** { *; }
-keepclassmembers class com.nostra13.universalimageloader.** {*;}
-dontwarn com.nostra13.universalimageloader.**

## ----------------------------------
##      Glide相关
## ----------------------------------
-keep class com.bumptech.glide.Glide { *; }
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
-dontwarn com.bumptech.glide.**

## ----------------------------------
##      Picasso相关
## ----------------------------------
-keep class com.squareup.picasso.Picasso { *; }
-dontwarn com.squareup.okhttp.**
-dontwarn com.squareup.picasso.**

## ----------------------------------
##      xUtils3相关
## ----------------------------------
-keepattributes Signature,*Annotation*
-keep public class org.xutils.** {
    public protected *;
}
-keep public interface org.xutils.** {
    public protected *;
}
-keepclassmembers class * extends org.xutils.** {
    public protected *;
}
-keepclassmembers @org.xutils.db.annotation.* class * {*;}
-keepclassmembers @org.xutils.http.annotation.* class * {*;}
-keepclassmembers class * {
    @org.xutils.view.annotation.Event <methods>;
}
-dontwarn org.xutils.**
```
