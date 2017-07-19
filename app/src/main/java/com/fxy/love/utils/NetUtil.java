package com.fxy.love.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Administrator on 2017/7/13 0013.
 */

public class NetUtil {
    /**
     * 没有连接网络
     */
    public static final int NETWORK_NONE = -1;
    /**
     * 移动网络
     */
    public static final int NETWORK_MOBILE = 0;
    /**
     * 无线网络
     */
    public static final int NETWORK_WIFI = 1;


    public static int getNetWorkState(Context context){
        //得到连接管理器的对象
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activityNetWorkInfo = connectivityManager.getActiveNetworkInfo();
        if (activityNetWorkInfo != null&&activityNetWorkInfo.isConnected()){
            if (activityNetWorkInfo.getType() == ConnectivityManager.TYPE_WIFI){
                return NETWORK_WIFI;
            } else if (activityNetWorkInfo.getType() == ConnectivityManager.TYPE_MOBILE){
                return NETWORK_MOBILE;
            }
        }else {
            return NETWORK_NONE;
        }
        return NETWORK_NONE;
    }
}
