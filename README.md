# RadarView

Android自定义功能雷达图

![](http://upload-images.jianshu.io/upload_images/2746415-516b1d97ca9ad87a.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)



## Installing

Users of your library will need add the jitpack.io repository:

```gradle
allprojects {
 repositories {
    jcenter()
    maven { url "https://jitpack.io" }
 }
}
```

and:

```gradle
dependencies {
    compile 'com.github.zhouchupen:RadarView:v1.0'
}
```

Note: do not add the jitpack.io repository under `buildscript` 

## Adding a sample app 

If you add a sample app to the same repo then your app needs to depend on the library. To do this in your app/build.gradle add a dependency in the form:

```gradle
dependencies {
    compile project(':library')
}
```

where 'library' is the name of your library module.

## Using

You may need this to use the radarview.  Put this into your xml file:
```xml
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <com.scnu.zhou.widget.RadarView
        android:id="@+id/radarView"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_centerInParent="true"
        app:regionColor="#2680ef"/>
</RelativeLayout>
```
And put this into your activity file:
```java
RadarView radarView = (RadarView) findViewById(R.id.radarView);

RadarView.RadarData data1 = new RadarView.RadarData(80, 100, "Java");
RadarView.RadarData data2 = new RadarView.RadarData(80, 100, "C++");
RadarView.RadarData data3 = new RadarView.RadarData(70, 100, "JavaScript");
RadarView.RadarData data4 = new RadarView.RadarData(67, 100, "Android");
RadarView.RadarData data5 = new RadarView.RadarData(55, 100, "iOS");
RadarView.RadarData data6 = new RadarView.RadarData(45, 100, "C#");
List<RadarView.RadarData> mData = new ArrayList<>();
mData.add(data1);
mData.add(data2);
mData.add(data3);
mData.add(data4);
mData.add(data5);
mData.add(data6);

radarView.setRadarData(mData);
```
