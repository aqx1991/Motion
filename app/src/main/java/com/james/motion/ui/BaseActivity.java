package com.james.motion.ui;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.gyf.barlibrary.ImmersionBar;
import com.james.motion.MyApplication;
import com.james.motion.ui.permission.PermissionActivity;
import com.james.motion.commmon.utils.LogUtils;
import com.james.motion.commmon.utils.Utils;
import com.james.motion.ui.weight.CustomProgressDialog;

import java.util.List;

import butterknife.ButterKnife;

/**
 * 描述: Activity基类
 * 作者: james
 * 日期: 2019/2/25 15:42
 * 类名: BaseActivity
 */
public abstract class BaseActivity extends PermissionActivity {

    protected String TAG = BaseActivity.class.getSimpleName();
    protected boolean isActive = true; //是否活跃

    private InputMethodManager imm;
    private Toast mToast = null;

    private static final int DISMISS = 1001;
    private static final int SHOW = 1002;
    private CustomProgressDialog progressDialog = null;

    protected ProgressDialog mProgressDialog;

    protected InputMethodManager inputMethodManager;

    protected Context context;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SHOW:
                    if (progressDialog != null) {
                        progressDialog.setTouchAble((Boolean) msg.obj);
                        progressDialog.show();
                    }
                    break;
                case DISMISS:
                    if (progressDialog != null && progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(getLayoutId());

        context = this;

        ButterKnife.bind(this);

        MyApplication.addActivity(this);

        initData(savedInstanceState);
        initListener();

        //初始化沉浸式
        if (isImmersionBarEnabled())
            initImmersionBar();

        mToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);

        progressDialog = new CustomProgressDialog(this);
        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    public abstract int getLayoutId();

    public abstract void initData(Bundle savedInstanceState);

    public abstract void initListener();


    /**
     * 是否可以使用沉浸式
     * Is immersion bar enabled boolean.
     *
     * @return the boolean
     */
    protected boolean isImmersionBarEnabled() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }

    protected void initImmersionBar() {
        //在BaseActivity里初始化
        ImmersionBar.with(this).init();
    }

    public void hideSoftKeyBoard() {
        View localView = getCurrentFocus();
        if (this.imm == null) {
            this.imm = ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE));
        }
        if ((localView != null) && (this.imm != null)) {
            this.imm.hideSoftInputFromWindow(localView.getWindowToken(), 2);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        this.imm = null;
        if (isImmersionBarEnabled()) {
            ImmersionBar.with(this).destroy();
        }

        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        progressDialog = null;

        if (null != mHandler) {
            mHandler.removeMessages(DISMISS);
            mHandler.removeMessages(SHOW);
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }

        MyApplication.removeActivity(this);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
//        if (OSUtils.isEMUI3_x() && isImmersionBarEnabled()) {
//            ImmersionBar.with(this).init();
//        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        hideProgress();
        mProgressDialog = null;

        if (!isAppOnForeground()) {
            //app 进入后台
            //全局变量 记录当前已经进入后台
            isActive = false;
            LogUtils.i(TAG, "进入后台");
        }
    }

    @SuppressLint("ShowToast")
    @Override
    public void onResume() {
        super.onResume();

//        if (OSUtils.isEMUI3_x() && isImmersionBarEnabled()) {
//            ImmersionBar.with(this).init();
//        }

        if (mToast == null)
            mToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);

        if (!isActive) {
            //app 从后台唤醒，进入前台
            isActive = true;
            LogUtils.i(TAG, "进入前台");
        }
    }

    public void showToast(String s) {
        if (!Utils.isString(s))
            return;
        if (mToast != null) {
            mToast.cancel();
            mToast = null;
        }
        mToast = Toast.makeText(context, s, Toast.LENGTH_SHORT);
        mToast.show();
    }

    @Override
    public void onPause() {

        if (mToast != null) {
            mToast.cancel();
            mToast = null;
        }
        hideSoftKeyBoard();

        super.onPause();

    }

    /**
     * 显示加载视图
     * <p>
     * true:可点击 false:不可点击 默认是可点击
     */
    protected void showLoadingView(boolean isTouchAble) {
        if (null != mHandler && !isShowingLoadingView()) {
            Message m = mHandler.obtainMessage(SHOW, isTouchAble);
            mHandler.sendMessage(m);
        }
    }

    protected void showLoadingView() {
        if (!isShowingLoadingView())
            showLoadingView(true);
    }

    /**
     * 关闭加载视图
     */
    protected void dismissLoadingView() {
        if (null != mHandler)
            mHandler.sendEmptyMessage(DISMISS);
    }

    protected boolean isShowingLoadingView() {
        if (progressDialog != null) {
            return progressDialog.isShowing();
        } else {
            return false;
        }
    }


    public ProgressDialog showProgress(String title, String message) {
        return showProgress(title, message, -1);
    }

    public ProgressDialog showProgress(String title, String message, int theme) {
        if (mProgressDialog == null) {
            if (theme > 0)
                mProgressDialog = new ProgressDialog(this, theme);
            else
                mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            mProgressDialog.setCanceledOnTouchOutside(false);// 不能取消
            mProgressDialog.setIndeterminate(true);// 设置进度条是否不明确
        }

        if (!TextUtils.isEmpty(title))
            mProgressDialog.setTitle(title);
        mProgressDialog.setMessage(message);
        mProgressDialog.show();
        return mProgressDialog;
    }

    public void hideProgress() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    public void back(View v) {
        hideSoftKeyBoard();
        MyApplication.removeActivity(this);
        finish();
    }

    @Override
    public void finish() {
        super.finish();
        hideSoftKeyBoard();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        LogUtils.i("memory -- info -->", level + "");
    }

    /**
     * 程序是否在前台运行
     *
     * @return
     */
    public boolean isAppOnForeground() {
        // Returns a list of application processes that are running on the
        // device

        ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = getApplicationContext().getPackageName();

        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        if (appProcesses == null)
            return false;

        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            // The name of the process that this object is associated with.
            if (appProcess.processName.equals(packageName)
                    && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }

        return false;
    }
}
