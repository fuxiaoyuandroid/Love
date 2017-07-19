package com.fxy.love.event;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.util.Log;

import com.fxy.love.base.BaseActivity;
import com.fxy.love.utils.NetUtil;

/**
 * Created by Administrator on 2017/7/13 0013.
 */

public class NetBroadCastReceiver extends BroadcastReceiver {
    public NetEvent event = BaseActivity.event;
    @Override
    public void onReceive(Context context, Intent intent) {
        // 如果相等的话就说明网络状态发生了变化
        if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)){
            int netState = NetUtil.getNetWorkState(context);
            // 接口回调传过去状态的类型
            event.onNetChanged(netState);
        }
    }
    //自定义接口
    public interface NetEvent{
        void onNetChanged(int netState);
    }
}
