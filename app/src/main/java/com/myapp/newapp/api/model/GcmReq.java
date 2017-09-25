package com.myapp.newapp.api.model;

/**
 * Created by MOSMI on 9/24/2017.
 */

public class GcmReq {
    private String DeviceToken;
    private String gcm;

    public String getDeviceToken() {
        return DeviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        DeviceToken = deviceToken;
    }

    public String getGcm() {
        return gcm;
    }

    public void setGcm(String gcm) {
        this.gcm = gcm;
    }
}
