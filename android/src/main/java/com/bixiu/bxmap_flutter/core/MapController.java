package com.bixiu.bxmap_flutter.core;

import android.location.Location;

import androidx.annotation.NonNull;

import com.bixiu.bxmap_flutter.MyMethodCallHandler;
import com.tencent.tencentmap.mapsdk.maps.MapView;
import com.tencent.tencentmap.mapsdk.maps.TencentMap;
import com.tencent.tencentmap.mapsdk.maps.TextureMapView;
import com.tencent.tencentmap.mapsdk.maps.model.CameraPosition;
import com.tencent.tencentmap.mapsdk.maps.model.LatLng;
import com.tencent.tencentmap.mapsdk.maps.model.MapPoi;

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
        TencentMap.OnMapLongClickListener,
        TencentMap.OnMapPoiClickListener{
    private static final String CLASS_NAME = "MapController";
    private static boolean hasStarted = false;
    private final MethodChannel methodChannel;
    private final TextureMapView mapView;
    private final TencentMap tmap;

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
    }


    @Override
    public void doMethodCall(@NonNull MethodCall call, @NonNull MethodChannel.Result result) {

    }

    @Override
    public String[] getRegisterMethodIdArray() {
        return new String[0];
    }

    @Override
    public void setCamera(CameraPosition cameraPosition) {

    }

    @Override
    public void onMapLoaded() {

    }

    @Override
    public void onMyLocationChange(Location location) {

    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {

    }

    @Override
    public void onCameraChangeFinished(CameraPosition cameraPosition) {

    }

    @Override
    public void onMapClick(LatLng latLng) {

    }

    @Override
    public void onMapLongClick(LatLng latLng) {

    }

    @Override
    public void onClicked(MapPoi mapPoi) {

    }
}
