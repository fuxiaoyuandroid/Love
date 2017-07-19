package com.fxy.love.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.fxy.love.MainActivity;
import com.fxy.love.R;
import com.fxy.love.event.NetBroadCastReceiver;
import com.fxy.love.utils.NetUtil;
import com.fxy.love.widget.CustomProgressDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;


public class BaseActivity extends AppCompatActivity implements View.OnClickListener,NetBroadCastReceiver.NetEvent{
    //网络状态监听者
    public static NetBroadCastReceiver.NetEvent event;
    /**
     * 网络类型
     */
    private int netClass;

    public Activity activity;
    public Context context;
    /**
     * 是否输出日志信息
     */
    private boolean isDebug = true;
    /**
     * 是否重置返回按钮点击事件
     */
    private boolean isResetBack = false;
    /**
     * title控件
     */
    private ImageView baseBack,baseRightIcon2,baseRightIcon1;
    private TextView baseTitle,baseRightText;
    /**
     * 点击回调方法
     */
    private OnClickRightIcon1CallBack onClickRightIcon1;
    private OnClickRightIcon2CallBack onClickRightIcon2;
    private OnClickRightTextCallBack onClickRightText;
    private OnClickBackCallBack onClickBack;

    /**
     * 当前打开Activity存储List
     */
    private static List<Activity> activities = new ArrayList<>();
    /**
     * 加载提示框
     */
    private CustomProgressDialog customProgressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        //加入的Activity不是MainActivity
        if (!(this instanceof MainActivity)){
            activities.add(this);
        }
        ButterKnife.bind(this);
        context = getApplicationContext();//指的是Application
        activity = this;
        customProgressDialog = new CustomProgressDialog(activity,R.style.progress_dialog_loading,"玩命加载中......");
        event = this;
        initView();
        //判断初始化网络状态
        isPrimaryNet();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        baseRightIcon1 = (ImageView) findViewById(R.id.base_right_icon1);
        baseRightIcon2 = (ImageView) findViewById(R.id.base_right_icon2);
        baseBack = (ImageView) findViewById(R.id.base_back);
        baseTitle = (TextView) findViewById(R.id.base_title);
        baseRightText = (TextView) findViewById(R.id.base_right_text);
        baseBack.setOnClickListener(this);
    }

    /**
     * 获取返回键
     */
    public ImageView getBaseBack() {
        return baseBack;
    }

    /**
     * 隐藏返回键
     */
    private void hideBack() {
        baseBack.setVisibility(View.GONE);
    }


    /**
     * 设置标题
     *
     * @param title 标题的文本
     */
    public void setTitle(String title) {
        baseTitle.setText(title);
    }

    public void setBaseBack(OnClickBackCallBack onClickBack){
        this.onClickBack = onClickBack;
        isResetBack = true;
    }
    /**
     * 设置右侧图片1（最右侧）
     *
     * @param resId             图片的资源id
     * @param alertText         提示文本
     * @param onClickRightIcon1 点击处理接口
     */
    public void setBaseRightIcon1(int resId, String alertText, OnClickRightIcon1CallBack onClickRightIcon1) {
        this.onClickRightIcon1 = onClickRightIcon1;
        baseRightIcon1.setImageResource(resId);
        baseRightIcon1.setVisibility(View.VISIBLE);
        baseRightIcon1.setOnClickListener(this);
        //语音辅助提示的时候读取的信息
        baseRightIcon1.setContentDescription(alertText);
    }

    /**
     * 设置右侧图片2（右数第二个图片）
     *
     * @param resId     图片的资源id
     * @param alertText 提示文本
     */
    public void setBaseRightIcon2(int resId, String alertText, OnClickRightIcon2CallBack onClickRightIcon2) {
        this.onClickRightIcon2 = onClickRightIcon2;
        baseRightIcon2.setImageResource(resId);
        baseRightIcon2.setVisibility(View.VISIBLE);
        baseRightIcon2.setOnClickListener(this);
        //语音辅助提示的时候读取的信息
        baseRightIcon2.setContentDescription(alertText);
    }

    /**
     * 设置右侧文本信息
     *
     * @param text 所需要设置的文本
     */
    public void setBaseRightText(String text, OnClickRightTextCallBack onClickRightText) {
        this.onClickRightText = onClickRightText;
        baseRightText.setText(text);
        baseRightText.setVisibility(View.VISIBLE);
        baseRightText.setOnClickListener(this);
    }

    /**
     * 引用头部布局
     *
     * @param layoutId 布局id
     */
    public void setBaseContentView(int layoutId) {
        //当子布局高度值不足ScrollView时，用这个方法可以充满ScrollView，防止布局无法显示
        ((ScrollView) findViewById(R.id.activity_base)).setFillViewport(true);
        LinearLayout layout = (LinearLayout) findViewById(R.id.base_main_layout);

        //获取布局，并在BaseActivity基础上显示
        final View view = getLayoutInflater().inflate(layoutId, null);
        //关闭键盘
        hideKeyBoard();
        //给EditText的父控件设置焦点，防止键盘自动弹出
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        layout.addView(view, params);
    }

    /**
     * 隐藏键盘
     */
    public void hideKeyBoard() {
        View view = activity.getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //返回按键
            case R.id.base_back:
                if (isResetBack){
                    onClickBack.clickBack();
                }else {
                    finish();
                }
                break;

            //图片1
            case R.id.base_right_icon1:
                onClickRightIcon1.clickRightIcon1();
                break;

            //图片2
            case R.id.base_right_icon2:
                onClickRightIcon2.clickRightIcon2();
                break;

            //右侧文本
            case R.id.base_right_text:
                onClickRightText.clickRightText();
                break;

            default:
                break;
        }

    }

    /**
     * 无携带信息跳转页面
     * @param cls 所跳转的目标Activity类
     */
    public void startActivity(Class<?> cls){
        startActivity(new Intent(this,cls));
    }

    /**
     * 携带信息跳转页面
     * @param cls  所跳转的目标Activity类
     * @param bundle 携带的信息
     */
    public void startActivity(Class<?> cls,Bundle bundle){
        Intent intent = new Intent(this,cls);
        if (bundle!=null){
            intent.putExtra("bundle",bundle);
        }
        startActivity(intent);
    }

    /**
     * 无携带参数跳转页面
     * @param cls 所跳转的Activity
     * @param requestCode 请求码
     */
    public void startActivityForResult(Class<?> cls,int requestCode){
        startActivityForResult(new Intent(this,cls),requestCode);
    }

    /**
     * 携带参数跳转页面
     * @param cls 所跳转的Activity
     * @param requestCode 请求码
     * @param bundle 携带的参数
     */
    public void startActivityForResult(Class<?> cls,int requestCode,Bundle bundle){
        Intent intent = new Intent(this,cls);
        if (bundle != null){
            intent.putExtra("bundle",bundle);
        }
        startActivityForResult(intent,requestCode);
    }

    /**
     * 消息提示框
     * @param message 提示消息
     */
    public void showToast(String message){
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
    }

    /**
     * 输出日志
     *
     * @param msg 日志信息
     */
    public void log(String msg) {
        if (isDebug) {
            Log.d("debug", msg);
        }
    }

    /**
     * 关闭所有Activity（除MainActivity以外）
     */
    public void finishActivity(){
        for (Activity activity:activities){
            activity.finish();
        }
    }

    public void goTo(Class<?> cls){
        if (cls.equals(MainActivity.class)){
            finishActivity();
        }else {
            for (int i = activities.size()-1;i>=0;i--){
                if (cls.equals(activities.get(i).getClass())){
                    break;
                }else {
                    activities.get(i).finish();
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        activities.remove(this);
        event = null;
        onClickRightIcon1 = null;
        onClickRightIcon2 = null;
        onClickRightText = null;
    }

    @Override
    public void onNetChanged(int netState) {
        this.netClass = netState;
        isHasNet();
    }
    /**
     * 初始化时判断有没有网络
     */
    public boolean isPrimaryNet(){
        this.netClass = NetUtil.getNetWorkState(context);
        Log.d("base", "isPrimaryNet: "+netClass);
        return isHasNet();
    }

    /**
     * 判断有无网络
     */
    public boolean isHasNet(){
        if (netClass == 1){
            Log.d("base", "isHasNet: 1");
            return true;
        }else if (netClass == 0){
            Log.d("base", "isHasNet: 0");
            return true;
        }else if (netClass == -1){
            Log.d("base", "isHasNet: -1");
            return false;
        }
        return false;
    }

    /**
     * 显示加载提示框
     */
    public void showLoadDialog() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                customProgressDialog.show();
            }
        });
    }

    /**
     * 隐藏加载提示框
     */
    public void hideLoadDialog() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (customProgressDialog != null && customProgressDialog.isShowing()) {
                    customProgressDialog.dismiss();
                }
            }
        });
    }


    /**
     * 图片一点击回调接口
     */
    public interface OnClickRightIcon1CallBack{
        void clickRightIcon1();
    }
    /**
     * 图片二点击回调接口
     */
    public interface OnClickRightIcon2CallBack{
        void clickRightIcon2();
    }
    /**
     * 右侧文字点击回调接口
     */
    public interface OnClickRightTextCallBack{
        void clickRightText();
    }

    /**
     * 返回键点击回调接口
     */
    public interface OnClickBackCallBack {
        void clickBack();
    }
}
