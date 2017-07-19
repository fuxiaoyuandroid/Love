package com.fxy.love;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.fxy.love.base.BaseActivity;
import com.fxy.love.utils.NetUtil;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setBaseContentView(R.layout.activity_main);
        //设置title文本
        setTitle("新Title");
        //设置返回拦截
        setBaseBack(new OnClickBackCallBack() {
            @Override
            public void clickBack() {
                showToast("111");
            }
        });

        setBaseRightIcon1(R.mipmap.more, "更多", new OnClickRightIcon1CallBack() {
            @Override
            public void clickRightIcon1() {
                showLoadDialog();
            }
        });
        boolean netConnect = this.isHasNet();
        Log.d("main", "onCreate: "+netConnect);
    }

    @Override
    public void onNetChanged(int netState) {
        super.onNetChanged(netState);
        if (netState == NetUtil.NETWORK_NONE){
            Log.d("main", "onCreate: "+netState);
        }else {
            Log.d("main", "onCreate: "+netState);
        }
    }
}
