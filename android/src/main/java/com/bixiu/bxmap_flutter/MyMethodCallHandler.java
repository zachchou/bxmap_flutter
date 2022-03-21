package com.bixiu.bxmap_flutter;

import androidx.annotation.NonNull;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;

public interface MyMethodCallHandler {

    public void doMethodCall(@NonNull MethodCall call, @NonNull MethodChannel.Result result);

    public abstract String[] getRegisterMethodIdArray();
}
