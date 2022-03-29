package com.bixiu.bxmap_flutter;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;

import com.bixiu.bxmap_flutter.core.MapController;
import com.bixiu.bxmap_flutter.utils.LogUtil;
import com.tencent.map.geolocation.TencentLocation;
import com.tencent.map.geolocation.TencentLocationListener;
import com.tencent.map.geolocation.TencentLocationManager;
import com.tencent.map.geolocation.TencentLocationManagerOptions;
import com.tencent.map.geolocation.TencentLocationRequest;
import com.tencent.tencentmap.mapsdk.maps.CameraUpdateFactory;
import com.tencent.tencentmap.mapsdk.maps.LocationSource;
import com.tencent.tencentmap.mapsdk.maps.TencentMap;
import com.tencent.tencentmap.mapsdk.maps.TencentMapOptions;
import com.tencent.tencentmap.mapsdk.maps.TextureMapView;
import com.tencent.tencentmap.mapsdk.maps.model.BitmapDescriptor;
import com.tencent.tencentmap.mapsdk.maps.model.BitmapDescriptorFactory;
import com.tencent.tencentmap.mapsdk.maps.model.CameraPosition;
import com.tencent.tencentmap.mapsdk.maps.model.LatLng;
import com.tencent.tencentmap.mapsdk.maps.model.MyLocationStyle;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.platform.PlatformView;

public class BxMapPlatformView
    implements
        DefaultLifecycleObserver,
        ActivityPluginBinding.OnSaveInstanceStateListener,
        MethodChannel.MethodCallHandler,
        LocationSource,
        TencentLocationListener,
        PlatformView {

    private static final String CLASS_NAME = "BxMapPlatformView";
    private final MethodChannel methodChannel;
    private OnLocationChangedListener locationChangedListener;
    private TencentLocationManager locationManager;
    private TencentLocationRequest locationRequest;
    private MapController mapController;
    private TextureMapView mapView;
    private Context mContext;
    private ImageView centerIV;
    private ImageView backIV;
    private Location userLocation;
    private boolean disposed = false;
    private static final int CENTER_IV_WIDTH = 48;

    private final Map<String, MyMethodCallHandler> myMethodCallHandlerMap;

    BxMapPlatformView(int id,
                      Context context,
                      FlutterPlugin.FlutterAssets flutterAssets,
                      BinaryMessenger binaryMessenger,
                      LifecycleProvider lifecycleProvider,
                      TencentMapOptions options,
                      CameraPosition cameraPosition,
                      FlutterPlugin.FlutterPluginBinding binding) {
        methodChannel = new MethodChannel(binaryMessenger, "bxmap_flutter_map_" + id);
        methodChannel.setMethodCallHandler(this);
        myMethodCallHandlerMap = new HashMap<String, MyMethodCallHandler>(8);

        try {
            mapView = new TextureMapView(context, options);
            final TencentMap map = mapView.getMap();
            map.getUiSettings().setLogoPosition(
                    TencentMapOptions.LOGO_POSITION_BOTTOM_LEFT
            );
            mContext = context;
            mapController = new MapController(methodChannel, mapView);
            mapController.setCamera(cameraPosition);
            backIV = new ImageView(context);
            backIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (userLocation != null) {
                        LatLng latLng = new LatLng(userLocation.getLatitude(), userLocation.getLongitude());
                        map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                    }
                }
            });
            centerIV = new ImageView(context);
            centerIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    System.out.println("view");
                }
            });

            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams
                    (FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
            params.width = 152 / 3 * 2;
            params.height = 152 / 3 * 2;
            params.bottomMargin = 50;
            params.gravity = Gravity.CENTER;
            mapView.addView(centerIV, params);

            AssetManager assetManager = binding.getApplicationContext().getAssets();
            String key = flutterAssets.getAssetFilePathByName("packages/bxmap_flutter/images/center_location.png");
            InputStream is = assetManager.open(key);
//            AssetFileDescriptor fd = assetManager.openFd(key);
            centerIV.setImageBitmap(BitmapFactory.decodeStream(is));

            FrameLayout.LayoutParams layoutParamsBack = new FrameLayout.LayoutParams
                    (FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
            layoutParamsBack.gravity = Gravity.BOTTOM | Gravity.END;
            layoutParamsBack.rightMargin = 20;
            layoutParamsBack.bottomMargin = 20;
            layoutParamsBack.width = 196 / 3 * 2;
            layoutParamsBack.height = 196 / 3 * 2;
            backIV.setLayoutParams(layoutParamsBack);
            mapView.addView(backIV, layoutParamsBack);

            String keyBack = flutterAssets.getAssetFilePathByName("packages/bxmap_flutter/images/back_location.png");
            InputStream isBack = assetManager.open(keyBack);
            backIV.setImageBitmap(BitmapFactory.decodeStream(isBack));


            MyLocationStyle locationStyle = new MyLocationStyle();
            String keyLocation = flutterAssets.getAssetFilePathByName("packages/bxmap_flutter/images/mine_location.png");
            InputStream isLocation = assetManager.open(keyLocation);
            BitmapDescriptor bitmapDescriptor =
                    BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeStream(isLocation));
            locationStyle.icon(bitmapDescriptor);
            map.setMyLocationStyle(locationStyle);

            initMyMethodCallHandlerMap();
            initLocation(context);
            lifecycleProvider.getLifecycle().addObserver(this);
            map.setLocationSource(this);
            map.setMyLocationEnabled(true);
        } catch (Throwable e) {
            LogUtil.e(CLASS_NAME, "<init> throwable ", e);
        }
    };

    /**
     * 定位的一些初始化设置
     */
    private void initLocation(Context context){
        //用于访问腾讯定位服务的类, 周期性向客户端提供位置更新
        locationManager = TencentLocationManager.getInstance(mContext);

        //创建定位请求
        locationRequest = TencentLocationRequest.create();
        //设置定位周期（位置监听器回调周期）为3s
        locationRequest.setInterval(3000);
    }


    private void initMyMethodCallHandlerMap() {
        String[] methodIdArray = mapController.getRegisterMethodIdArray();
        if (methodIdArray != null && methodIdArray.length > 0) {
            for (String methodId: methodIdArray) {
                myMethodCallHandlerMap.put(methodId, mapController);
            }
        }
    }

    public MapController getMapController() {return mapController;}

    @Override
    public void onMethodCall(@NonNull MethodCall call, @NonNull MethodChannel.Result result) {
        LogUtil.i(CLASS_NAME, "onMethodCall==>" + call.method + ", arguments==> " + call.arguments);
        String methodId = call.method;
        if (myMethodCallHandlerMap.containsKey(methodId)) {
            myMethodCallHandlerMap.get(methodId).doMethodCall(call, result);
        } else {
            LogUtil.w(CLASS_NAME, "onMethodCall, the methodId: " + call.method + ", not implemented");
            result.notImplemented();
        }
    }

    @Override
    public void onCreate(@NonNull LifecycleOwner owner) {
        LogUtil.i(CLASS_NAME, "onCreate===>");
        try {
            if (disposed) {
                return;
            }
            if (mapView == null) {
//                mapView.getMap();
            }
        } catch (Throwable e) {
            LogUtil.e(CLASS_NAME, "onCreate ", e);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle bundle) {
    }

    @Override
    public void onRestoreInstanceState(@Nullable Bundle bundle) {
    }

    @Override
    public void onStart(@NonNull LifecycleOwner owner) {
    }

    @Override
    public void onResume(@NonNull LifecycleOwner owner) {
        LogUtil.i(CLASS_NAME, "onResume==>");
        try {
            if (disposed) {
                return;
            }
            if (null != mapView) {
                mapView.onResume();
            }
        } catch (Throwable e) {
            LogUtil.e(CLASS_NAME, "onResume", e);
        }
    }

    @Override
    public void onPause(@NonNull LifecycleOwner owner) {
        LogUtil.i(CLASS_NAME, "onPause==>");
        try {
            if (disposed) {
                return;
            }
            mapView.onPause();
        } catch (Throwable e) {
            LogUtil.e(CLASS_NAME, "onPause", e);
        }
    }

    @Override
    public void onStop(@NonNull LifecycleOwner owner) {
        LogUtil.i(CLASS_NAME, "onDestroy==>");
        try {
            if (disposed) {
                return;
            }
//            destroyMapViewIfNecessary();
        } catch (Throwable e) {
            LogUtil.e(CLASS_NAME, "onDestroy", e);
        }
    }

    @Override
    public void onDestroy(@NonNull LifecycleOwner owner) {
        LogUtil.i(CLASS_NAME, "onDestroy==>");
        try {
            if (disposed) {
                return;
            }
            destroyMapViewIfNecessary();
        } catch (Throwable e) {
            LogUtil.e(CLASS_NAME, "onDestroy", e);
        }
    }

    @Override
    public View getView() {
        return mapView;
    }

    @Override
    public void dispose() {
        LogUtil.i(CLASS_NAME, "dispose==>");
        try {
            if (disposed) {
                return;
            }
            methodChannel.setMethodCallHandler(null);
            destroyMapViewIfNecessary();
            disposed = true;
        } catch (Throwable e) {
            LogUtil.e(CLASS_NAME, "dispose", e);
        }
    }


    private void destroyMapViewIfNecessary() {
        if (mapView == null) {
            return;
        }
        mapView.onDestroy();
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        //这里我们将地图返回的位置监听保存为当前 Activity 的成员变量
        locationChangedListener = onLocationChangedListener;
        //开启定位
        int err = locationManager.requestSingleFreshLocation(
                locationRequest, this, Looper.myLooper());
        switch (err) {
            case 1:
//                Toast.makeText(this,
//                        "设备缺少使用腾讯定位服务需要的基本条件",
//                        Toast.LENGTH_SHORT).show();
                break;
            case 2:
//                Toast.makeText(this,
//                        "manifest 中配置的 key 不正确", Toast.LENGTH_SHORT).show();
                break;
            case 3:
//                Toast.makeText(this,
//                        "自动加载libtencentloc.so失败", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    @Override
    public void deactivate() {

    }

    @Override
    public void onLocationChanged(TencentLocation tencentLocation, int i, String s) {
        //其中 locationChangeListener 为 LocationSource.active 返回给用户的位置监听器
        //用户通过这个监听器就可以设置地图的定位点位置
        if(i == TencentLocation.ERROR_OK && locationChangedListener != null){
            Location location = new Location(tencentLocation.getProvider());
            userLocation = location;
            //设置经纬度
            location.setLatitude(tencentLocation.getLatitude());
            location.setLongitude(tencentLocation.getLongitude());
            //设置精度，这个值会被设置为定位点上表示精度的圆形半径
            location.setAccuracy(tencentLocation.getAccuracy());
            //设置定位标的旋转角度，注意 tencentLocation.getBearing() 只有在 gps 时才有可能获取
            location.setBearing((float) tencentLocation.getBearing());
            //将位置信息返回给地图
            locationChangedListener.onLocationChanged(location);

        }
    }

    @Override
    public void onStatusUpdate(String s, int i, String s1) {

    }
}
