package com.bixiu.bxmap_flutter.utils;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.bixiu.bxmap_flutter.core.BxMapOptionsSink;
import com.tencent.tencentmap.mapsdk.maps.CameraUpdate;
import com.tencent.tencentmap.mapsdk.maps.CameraUpdateFactory;
import com.tencent.tencentmap.mapsdk.maps.TencentMap;
import com.tencent.tencentmap.mapsdk.maps.TencentMapOptions;
import com.tencent.tencentmap.mapsdk.maps.model.CameraPosition;
import com.tencent.tencentmap.mapsdk.maps.model.LatLng;
import com.tencent.tencentmap.mapsdk.maps.model.LatLngBounds;
import com.tencent.tencentmap.mapsdk.maps.model.MyLocationStyle;

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

    public static void interpretBxMapOptions(Object o, @NonNull BxMapOptionsSink sink) {
        try {
            final Map<?, ?> data = (Map<?, ?>) o;
            final Object mapType = data.get("mapType");
//            if (mapType != null) {
//                sink.setMapType(toLocalMapType(toInt(mapType)));
//            }
//
//            final Object buildingsEnabled = data.get("buildingsEnabled");
//            if (null != buildingsEnabled) {
//                sink.setBuildingsEnabled(toBoolean(buildingsEnabled));
//            }
//
//            final Object customMapStyleOptions = data.get("customStyleOptions");
//            if (null != customMapStyleOptions) {
//                TencentMapOptions customMapStyleOptions1 = toCustomMapStyleOptions(customMapStyleOptions);
//                sink.setCustomMapStyleOptions(customMapStyleOptions1);
//            }
//
//            final Object myLocationStyleData = data.get("myLocationStyle");
//            if (null != myLocationStyleData) {
//                sink.setMyLocationStyle(ConvertUtil.toMyLocationStyle(myLocationStyleData, density));
//            }
//
//            final Object screenAnchor = data.get("screenAnchor");
//            if (null != screenAnchor) {
//                final List<?> anchorData = toList(screenAnchor);
//                sink.setScreenAnchor(toFloat(anchorData.get(0)), toFloat(anchorData.get(1)));
//            }
//
//            final Object compassEnabled = data.get("compassEnabled");
//            if (null != compassEnabled) {
//                sink.setCompassEnabled(toBoolean(compassEnabled));
//            }
//
//            final Object labelsEnabled = data.get("labelsEnabled");
//            if (null != labelsEnabled) {
//                sink.setLabelsEnabled(toBoolean(labelsEnabled));
//            }
//
//            final Object limitBounds = data.get("limitBounds");
//            if (null != limitBounds) {
//                final List<?> targetData = toList(limitBounds);
//                sink.setLatLngBounds(toLatLngBounds(targetData));
//            }
//
//            final Object minMaxZoomPreference = data.get("minMaxZoomPreference");
//            if (null != minMaxZoomPreference) {
//                final List<?> targetData = toList(minMaxZoomPreference);
//                sink.setMinZoomLevel(toFloatWrapperWithDefault(targetData.get(0), 3));
//                sink.setMaxZoomLevel(toFloatWrapperWithDefault(targetData.get(1), 20));
//            }
//
//            final Object scaleEnabled = data.get("scaleEnabled");
//            if (null != scaleEnabled) {
//                sink.setScaleEnabled(toBoolean(scaleEnabled));
//            }
//
//            final Object touchPoiEnabled = data.get("touchPoiEnabled");
//            if (null != touchPoiEnabled) {
//                sink.setTouchPoiEnabled(toBoolean(touchPoiEnabled));
//            }
//
//            final Object trafficEnabled = data.get("trafficEnabled");
//            if (null != trafficEnabled) {
//                sink.setTrafficEnabled(toBoolean(trafficEnabled));
//            }
//
//            final Object rotateGesturesEnabled = data.get("rotateGesturesEnabled");
//            if (null != rotateGesturesEnabled) {
//                sink.setRotateGesturesEnabled(toBoolean(rotateGesturesEnabled));
//            }
//
//            final Object scrollGesturesEnabled = data.get("scrollGesturesEnabled");
//            if (null != scrollGesturesEnabled) {
//                sink.setScrollGesturesEnabled(toBoolean(scrollGesturesEnabled));
//            }
//
//            final Object tiltGesturesEnabled = data.get("tiltGesturesEnabled");
//            if (null != tiltGesturesEnabled) {
//                sink.setTiltGesturesEnabled(toBoolean(tiltGesturesEnabled));
//            }
//
//            final Object zoomGesturesEnabled = data.get("zoomGesturesEnabled");
//            if (null != zoomGesturesEnabled) {
//                sink.setZoomGesturesEnabled(toBoolean(zoomGesturesEnabled));
//            }
        } catch (Throwable e) {
            LogUtil.e(CLASS_NAME, "interpretAMapOptions", e);
        }
    }

    private static MyLocationStyle toMyLocationStyle(Object o, float density) {
        final Map<?, ?> map = toMap(o);
        final MyLocationStyle myLocationStyle = new MyLocationStyle();
        final Object enableData = map.get("enabled");
        if (null != enableData) {
//            myLocationStyle.showMyLocation(toBoolean(enableData));
        }
        //两端差异比较大，Android端设置成跟随但是不移动到中心点模式，与iOS端兼容
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW_NO_CENTER);
//        final Object trackingMode = map.get("trackingMode");
//        if (null != trackingMode) {
//            int trackingModeIndex = toInt(trackingMode);
//            if (trackingModeIndex < LocationTypeMap.length) {
//                myLocationStyle.myLocationType(LocationTypeMap[trackingModeIndex]);
//            }
//        }

        final Object circleFillColorData = map.get("circleFillColor");
        if (null != circleFillColorData) {
//            myLocationStyle.radiusFillColor(toInt(circleFillColorData));
        }
        final Object circleStrokeColorData = map.get("circleStrokeColor");
        if (null != circleStrokeColorData) {
            myLocationStyle.strokeColor(toInt(circleStrokeColorData));
        }

        final Object circleStrokeWidthData = map.get("circleStrokeWidth");
        if (null != circleStrokeWidthData) {
//            myLocationStyle.strokeWidth(toPixels(circleStrokeWidthData));
        }

        final Object iconDta = map.get("icon");
        if (null != iconDta) {
//            myLocationStyle.myLocationIcon(toBitmapDescriptor(iconDta));
        }
        return myLocationStyle;
    }

    public static Float toFloatWrapperWithDefault(Object o, float defaultValue) {
        return (o == null) ? defaultValue : toFloat(o);
    }

    public static LatLngBounds toLatLngBounds(Object o) {
        if (o == null) {
            return null;
        }
        final List<?> data = toList(o);
        return new LatLngBounds(toLatLng(data.get(0)), toLatLng(data.get(1)));
    }

    private static TencentMapOptions toCustomMapStyleOptions(Object o) {
        final Map<?, ?> map = toMap(o);
        final TencentMapOptions customMapStyleOptions = new TencentMapOptions();
        final Object enableData = map.get("enabled");
        if (null != enableData) {
//            customMapStyleOptions.setEnable(toBoolean(enableData));
        }

        final Object styleData = map.get("styleData");
        if (null != styleData) {
//            customMapStyleOptions.setStyleData((byte[]) styleData);
        }
        final Object styleExtraData = map.get("styleExtraData");
        if (null != styleExtraData) {
//            customMapStyleOptions.setStyleExtraData((byte[]) styleExtraData);
        }
        return customMapStyleOptions;
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

    public static boolean toBoolean(Object o) {
        return (Boolean) o;
    }

    public static int toInt(Object o) { return ((Number) o).intValue(); }
}
