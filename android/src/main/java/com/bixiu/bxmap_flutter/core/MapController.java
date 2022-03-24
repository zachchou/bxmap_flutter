package com.bixiu.bxmap_flutter.core;

import android.location.Location;

import androidx.annotation.NonNull;

import com.bixiu.bxmap_flutter.MyMethodCallHandler;
import com.bixiu.bxmap_flutter.utils.Const;
import com.bixiu.bxmap_flutter.utils.LogUtil;
import com.tencent.tencentmap.mapsdk.maps.CameraUpdateFactory;
import com.tencent.tencentmap.mapsdk.maps.MapView;
import com.tencent.tencentmap.mapsdk.maps.TencentMap;
import com.tencent.tencentmap.mapsdk.maps.TextureMapView;
import com.tencent.tencentmap.mapsdk.maps.model.CameraPosition;
import com.tencent.tencentmap.mapsdk.maps.model.LatLng;
import com.tencent.tencentmap.mapsdk.maps.model.MapPoi;
import com.tencent.tencentmap.mapsdk.maps.model.TencentMapGestureListener;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;

public class MapController
        implements
        MyMethodCallHandler,
        BxMapOptionsSink,
        TencentMap.OnMapLoadedCallback,
        TencentMap.OnMyLocationChangeListener,
        TencentMap.OnCameraChangeListener,
        TencentMap.OnMapClickListener,
        TencentMapGestureListener,
        TencentMap.OnMapLongClickListener,
        TencentMap.OnMapPoiClickListener{
    private static final String CLASS_NAME = "MapController";
    private static boolean hasStarted = false;
    private static boolean firstMyLocation = false;
    private final MethodChannel methodChannel;
    private final TextureMapView mapView;
    private final TencentMap tmap;
    private MethodChannel.Result mapReadyResult;
    protected int[] myArray = {};

    private boolean mapLoaded = false;

    public MapController(MethodChannel methodChannel, TextureMapView mapView){
        this.methodChannel = methodChannel;
        this.mapView = mapView;
        tmap = mapView.getMap();

        tmap.addOnMapLoadedCallback(this);
        tmap.setOnMyLocationChangeListener(this);
        tmap.setOnCameraChangeListener(this);
        tmap.setOnMapClickListener(this);
        tmap.setOnMapLongClickListener(this);
        tmap.setOnMapPoiClickListener(this);
        tmap.addTencentMapGestureListener(this);
    }


    @Override
    public String[] getRegisterMethodIdArray() { return Const.METHOD_ID_LIST_FOR_MAP; }

    @Override
    public void doMethodCall(@NonNull MethodCall call, @NonNull MethodChannel.Result result) {
        LogUtil.i(CLASS_NAME, "doMethodCall====>" + call.method);
        if (tmap == null) {
            return;
        }
        switch (call.method) {
            case Const.METHOD_MAP_WAIT_FOR_MAP:
                if (mapLoaded) {
                    result.success(null);
                    return;
                }
                mapReadyResult = result;
                break;
            case Const.METHOD_MAP_CAMERA_MOVE_END:

                break;
            default:
                LogUtil.w(CLASS_NAME, "doMethodCall cannot find mapId: " + call.method);
                break;
        }

    }


    @Override
    public void setCamera(CameraPosition cameraPosition) {
        tmap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    @Override
    public void onMapLoaded() {
        LogUtil.i(CLASS_NAME, "onMapLoaded====>");
        System.out.println(CLASS_NAME + " onMapLoaded====> ");
        try {
            mapLoaded = true;
            if (mapReadyResult != null) {
                mapReadyResult.success(null);
                mapReadyResult = null;
            }
        } catch (Throwable e) {
            LogUtil.e(CLASS_NAME, "onMapLoaded throwable ", e);
            System.out.println(CLASS_NAME + " onMapLoaded====>throwable " + e);
        }
        if (LogUtil.isDebugModel && !hasStarted) {
            hasStarted = true;
//            int index = myArray[0];
        }
    }

    @Override
    public void onMyLocationChange(Location location) {
        System.out.println(CLASS_NAME + " onMyLocationChange " + location);
//        if (!firstMyLocation) {
//            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
//            tmap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
//            firstMyLocation = true;
//        }
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
        System.out.println(CLASS_NAME + " onCameraChange " + cameraPosition);
    }

    @Override
    public void onCameraChangeFinished(CameraPosition cameraPosition) {
        System.out.println(CLASS_NAME + " onCameraChangeFinished " + cameraPosition);
    }

    // 点击地图任意位置
    @Override
    public void onMapClick(LatLng latLng) {
        System.out.println(CLASS_NAME + " onMapClick " + latLng);
        tmap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        System.out.println(CLASS_NAME + " onMapClick " + latLng);
    }

    // 点击POI点
    @Override
    public void onClicked(MapPoi mapPoi) {
        System.out.println(CLASS_NAME + " onClicked " + mapPoi);
        tmap.moveCamera(CameraUpdateFactory.newLatLng(mapPoi.getPosition()));
//        methodChannel.invokeMethod("poi#didTap",);

    }

    @Override
    public boolean onDoubleTap(float v, float v1) {
        return false;
    }

    @Override
    public boolean onSingleTap(float v, float v1) {
        return false;
    }

    @Override
    public boolean onFling(float v, float v1) {
        return false;
    }

    @Override
    public boolean onScroll(float v, float v1) {
        return false;
    }

    @Override
    public boolean onLongPress(float v, float v1) {
        return false;
    }

    @Override
    public boolean onDown(float v, float v1) {
        return false;
    }

    @Override
    public boolean onUp(float v, float v1) {
        return false;
    }

    @Override
    public void onMapStable() {
        System.out.println(CLASS_NAME + " onMapStable ");
    }
}
