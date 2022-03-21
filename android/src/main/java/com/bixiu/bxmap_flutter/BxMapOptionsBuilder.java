package com.bixiu.bxmap_flutter;

import com.bixiu.bxmap_flutter.core.BxMapOptionsSink;
import com.tencent.tencentmap.mapsdk.maps.TencentMapOptions;
import com.tencent.tencentmap.mapsdk.maps.model.CameraPosition;

public class BxMapOptionsBuilder implements BxMapOptionsSink {
    private static final String CLASS_NAME = "BxMapOptionsBuilder";
    private final TencentMapOptions options = new TencentMapOptions();
    private float minZoomLevel = 3;
    private float maxZoomLevel = 20;


    @Override
    public void setCamera(CameraPosition cameraPosition) {

    }
}
