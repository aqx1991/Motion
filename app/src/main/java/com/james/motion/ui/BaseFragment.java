package com.james.motion.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.james.motion.commmon.utils.Utils;
import com.james.motion.ui.weight.CustomProgressDialog;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 描述: Fragment基类
 * 作者: james
 * 日期: 2019/2/25 15:42
 * 类名: BaseFragment
 */
public abstract class BaseFragment extends Fragment {

    protected String TAG = BaseFragment.class.getSimpleName();

    private Unbinder bind;

    protected Context context;

    private Toast mToast = null;

    private static final int DISMISS = 1001;
    private static final int SHOW = 1002;
    private CustomProgressDialog progressDialog = null;

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


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View inflate = inflater.inflate(getLayoutId(), container, false);

        bind = ButterKnife.bind(this, inflate);

        context = getActivity();

        initData(savedInstanceState);

        initListener();

        mToast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
        if (progressDialog == null) {
            progressDialog = new CustomProgressDialog(context);
        }
        return inflate;
    }

    public abstract int getLayoutId();

    public abstract void initData(Bundle savedInstanceState);

    public abstract void initListener();

    @Override
    public void onResume() {
        super.onResume();

        if (mToast == null)
            mToast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
        if (progressDialog == null) {
            progressDialog = new CustomProgressDialog(context);
        }
    }

    /**
     * 显示加载视图
     *
     * @param isTouchAble true:可点击 false:不可点击 默认是可点击
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


    public void showToast(String s) {
        if (!Utils.isString(s))
            return;
        if (mToast != null) {
            mToast.cancel();
            mToast = null;
        }
        if (null != context) {
            mToast = Toast.makeText(context, s, Toast.LENGTH_SHORT);
            mToast.show();
        }
    }

    @Override
    public void onPause() {

        if (mToast != null) {
            mToast.cancel();
            mToast = null;
        }
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        progressDialog = null;

        super.onPause();
    }

    @Override
    public void onDestroy() {
        if (bind != null) {
            bind.unbind();
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
        super.onDestroy();
    }

}
