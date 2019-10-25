package com.james.motion.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageButton;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.james.motion.R;
import com.james.motion.commmon.utils.Conn;
import com.james.motion.commmon.utils.MySp;
import com.james.motion.commmon.utils.UIHelper;
import com.james.motion.commmon.utils.Utils;
import com.james.motion.ui.BaseFragment;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 描述: 验证码快速登录
 * 作者: james
 * 日期: 2019/1/4 18:15
 * 类名: FastLoginFragment
 */
public class FastLoginFragment extends BaseFragment {

    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_code)
    EditText etCode;
    @BindView(R.id.btClear)
    ImageButton btClear;
    @BindView(R.id.chronometer)
    Chronometer chronometer;

    private String code = "-1";

    @Override
    public int getLayoutId() {
        return R.layout.fragment_fastlogin;
    }

    @Override
    public void initData(Bundle savedInstanceState) {

        String loginname = SPUtils.getInstance().getString(MySp.PHONE);
        String password = SPUtils.getInstance().getString(MySp.PASSWORD);
        if (!TextUtils.isEmpty(loginname)) {
            btClear.setVisibility(View.VISIBLE);
            etPhone.setText(loginname);
        }

        chronometer.setText("获取验证码");
    }

    @Override
    public void initListener() {
        chronometer.setOnChronometerTickListener(chronometer -> {
            long time = (Long) chronometer.getTag() - SystemClock.elapsedRealtime() / 1000;
            if (time > 0) {
                chronometer.setText(UIHelper.getString(R.string.chronometer_time, time));
            } else {
                chronometer.setText("重新获取");
                chronometer.stop();
                chronometer.setEnabled(true);
            }
        });
        etPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                btClear.setVisibility(s.length() > 0 ? View.VISIBLE : View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @OnClick({R.id.chronometer, R.id.btClear})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.chronometer:
                String phone = etPhone.getText().toString();
                if (TextUtils.isEmpty(phone)) {
                    ToastUtils.showShort("请输入11位手机号码");
                    return;
                }
                if (!Utils.isMobile(phone)) {
                    ToastUtils.showShort("请输入正确的手机号码");
                    return;
                }
                // 先影藏输入法
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(etPhone.getWindowToken(), 0);

                yanZhengMa();

                break;
            case R.id.btClear:
                etPhone.setText("");
                break;
            default:
                break;
        }
    }

    public void yzmStart() {
        chronometer.setTag(SystemClock.elapsedRealtime() / 1000 + 60);
        chronometer.setText("(60)重新获取");
        chronometer.start();
        chronometer.setEnabled(false);

        getActivity().getWindow()
                .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

    /**
     * 获取验证码
     */
    public void yanZhengMa() {
        showLoadingView();
        new Handler().postDelayed(() -> {
            dismissLoadingView();
            int numcode = (int) ((Math.random() * 9 + 1) * 100000);
            code = numcode + "";
            yzmStart();
            ToastUtils.showShort("验证获取成功！");
            etCode.setText(code);
        }, Conn.Delayed);
    }

    public void checkAccount(CallBack callBack) {
        if (null != etPhone && null != etCode) {
            if (isInput(etPhone, etCode)) {
                String account = etPhone.getText().toString();
                String codes = etCode.getText().toString();
                if (!Utils.isMobile(account)) {
                    ToastUtils.showShort("请输入正确的手机号码");
                } else if (!TextUtils.equals(codes, code)) {
                    ToastUtils.showShort("请输入正确的验证码!");
                } else {
                    callBack.getResult(account, codes);
                }
            }
        }
    }

    public interface CallBack {
        void getResult(String account, String psd);
    }

    private boolean isInput(EditText... e) {
        for (EditText anE : e) {
            if (TextUtils.isEmpty(anE.getText())) {
                ToastUtils.showShort(anE.getHint().toString());
                return false;
            }
        }
        return true;
    }

}
