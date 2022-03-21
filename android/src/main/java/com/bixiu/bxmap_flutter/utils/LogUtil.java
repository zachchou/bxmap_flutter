package com.bixiu.bxmap_flutter.utils;

import android.util.Log;

public class LogUtil {
    public static boolean isDebugModel = false;
    private static final String TAG = "BxMapFlutter_";
    public static void i(String className, String message) {
        if (isDebugModel) {
            Log.i(TAG+className, message);
        }
    }

    public static void d(String className, String message) {
        if (isDebugModel) {
            Log.d(TAG+className, message);
        }
    }

    public static void w(String className, String message) {
        if (isDebugModel) {
            Log.w(TAG+className, message);
        }
    }

    public static void e(String className, String methodName, Throwable e) {
        if (isDebugModel) {
            Log.e(TAG+className, methodName + " exception!!", e);
        }
    }
}
