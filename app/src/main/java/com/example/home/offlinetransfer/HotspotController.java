package com.example.home.offlinetransfer;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.lang.reflect.Method;

/**
 * Created by Home on 05-05-2017.
 */
public class HotspotController {



    public static void turnoffWifiAccessPoint(Context context) {
        WifiManager wifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
        if(wifiManager.isWifiEnabled())
        {
            wifiManager.setWifiEnabled(false);
        }


        WifiConfiguration netConfig = new WifiConfiguration();

        try {
            Method setWifiApMethod = wifiManager.getClass().getMethod("setWifiApEnabled",  WifiConfiguration.class, boolean.class);
            setWifiApMethod.invoke(wifiManager, netConfig,false);

        } catch (Exception e) {
            e.printStackTrace();
        }



    }


    public static void createWifiAccessPoint(String name,Context context) {
        WifiManager wifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
        if(wifiManager.isWifiEnabled())
        {
            wifiManager.setWifiEnabled(false);
        }


        WifiConfiguration netConfig = new WifiConfiguration();

        netConfig.SSID = name;
        netConfig.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
        netConfig.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
        netConfig.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
        netConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);

        try {
            Method setWifiApMethod = wifiManager.getClass().getMethod("setWifiApEnabled",  WifiConfiguration.class, boolean.class);
            setWifiApMethod.invoke(wifiManager, netConfig,true);

            Method isWifiApEnabledmethod = wifiManager.getClass().getMethod("isWifiApEnabled");
            while(!(Boolean)isWifiApEnabledmethod.invoke(wifiManager)){};
            Method getWifiApStateMethod = wifiManager.getClass().getMethod("getWifiApState");
            getWifiApStateMethod.invoke(wifiManager);
            Method getWifiApConfigurationMethod = wifiManager.getClass().getMethod("getWifiApConfiguration");
            netConfig=(WifiConfiguration)getWifiApConfigurationMethod.invoke(wifiManager);
            Log.e("CLIENT", "\nSSID:"+netConfig.SSID+"\nPassword:"+netConfig.preSharedKey+"\n");
        } catch (Exception e) {
            e.printStackTrace();
        }



    }
}
