package com.bixiu.bxmap_flutter.utils;

import android.text.TextUtils;

import com.tencent.tencentmap.mapsdk.maps.CameraUpdate;
import com.tencent.tencentmap.mapsdk.maps.CameraUpdateFactory;
import com.tencent.tencentmap.mapsdk.maps.TencentMap;
import com.tencent.tencentmap.mapsdk.maps.model.CameraPosition;
import com.tencent.tencentmap.mapsdk.maps.model.LatLng;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConvertUtil {
    private static final String CLASS_NAME = "ConverUtil";

    public static float density;
    private static String apiKey;

    public static void checkApiKey(Object object) {
        if (object == null) {
            return;
        }

        Map<?, ?> kepMap = toMap(object);
        Object keyObject = kepMap.get("androidKey");
        if (keyObject != null) {
            final String bKey = toString(keyObject);
            if (TextUtils.isEmpty(apiKey) || !bKey.equals(apiKey)) {
                apiKey = bKey;
            }
        }
    }

    public static int toLocalMapType(int dartMapIndex) {
        int[] localTypeArray = {TencentMap.MAP_TYPE_NORMAL, TencentMap.MAP_TYPE_SATELLITE, TencentMap.MAP_TYPE_DARK};
        if (dartMapIndex > localTypeArray.length) {
            return  localTypeArray[0];
        }
        return localTypeArray[dartMapIndex];
    }

    public static CameraUpdate toCameraUpdate(Object o) {
        final List<?> data = toList(o);
        switch (toString(data.get(0))) {
            case "newCameraPosition":
                return CameraUpdateFactory.newCameraPosition(toCameraPosition(data.get(1)));
            default:
                throw new IllegalArgumentException("Connot interpret" + o + " as CameraUpdate");
        }
    }

    public static CameraPosition toCameraPosition(Object o) {
        final Map<?, ?> data = (Map<?, ?>) o;
        final CameraPosition.Builder builder = CameraPosition.builder();
        builder.bearing(toFloat(data.get("bearing")));
        builder.target(toLatLng(data.get("target")));
        builder.zoom(toFloat(data.get("zoom")));
        builder.tilt(toFloat(data.get("tilt")));
        return  builder.build();
    }

    public  static Object cameraPositonToMap(CameraPosition position) {
        if (position == null) {
            return null;
        }
        final Map<String, Object> data = new HashMap<>();
        data.put("bearing", position.bearing);
        data.put("target", latLngToList(position.target));
        data.put("zoom", position.zoom);
        data.put("tilt", position.tilt);
        return  data;
    }

    public static Object latLngToList(LatLng latLng) {
        if (latLng == null) {
            return null;
        }
        return Arrays.asList(latLng.latitude, latLng.longitude);
    }

    public static LatLng toLatLng(Object o) {
        final List<?> data = (List<?>) o;
        return new LatLng((Double) data.get(0), (Double) data.get(1));
    }

    public static Map<?, ?> toMap(Object o) { return (Map<?, ?>) o; }

    public static List<?> toList(Object o) {return (List<?>) o; }

    public static String toString(Object o) { return (String) o; }

    public static float toFloat(Object o) { return ((Number) o).floatValue(); }
}
