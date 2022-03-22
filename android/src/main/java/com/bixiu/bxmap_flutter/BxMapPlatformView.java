package com.bixiu.bxmap_flutter;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;

import com.bixiu.bxmap_flutter.core.MapController;
import com.bixiu.bxmap_flutter.utils.LogUtil;
import com.tencent.tencentmap.mapsdk.maps.TencentMap;
import com.tencent.tencentmap.mapsdk.maps.TencentMapOptions;
import com.tencent.tencentmap.mapsdk.maps.TextureMapView;

import java.util.HashMap;
import java.util.Map;

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
        PlatformView {

    private static final String CLASS_NAME = "BxMapPlatformView";
    private final MethodChannel methodChannel;

    private MapController mapController;
    private TextureMapView mapView;
    private boolean disposed = false;

    private final Map<String, MyMethodCallHandler> myMethodCallHandlerMap;

    BxMapPlatformView(int id,
                      Context context,
                      BinaryMessenger binaryMessenger,
                      LifecycleProvider lifecycleProvider,
                      TencentMapOptions options) {
        methodChannel = new MethodChannel(binaryMessenger, "bxmap_flutter_map_" + id);
        methodChannel.setMethodCallHandler(this);
        myMethodCallHandlerMap = new HashMap<String, MyMethodCallHandler>(8);

        try {
            mapView = new TextureMapView(context, options);
            TencentMap map = mapView.getMap();
            mapController = new MapController(methodChannel, mapView);
        } catch (Throwable e) {
            LogUtil.e(CLASS_NAME, "<init> throwable ", e);
        }
    };

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
                mapView.getMap();
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
    }

    @Override
    public void onPause(@NonNull LifecycleOwner owner) {
    }

    @Override
    public void onStop(@NonNull LifecycleOwner owner) {
    }

    @Override
    public void onDestroy(@NonNull LifecycleOwner owner) {
    }

    @Override
    public View getView() {
        return mapView;
    }

    @Override
    public void dispose() {

    }

}
