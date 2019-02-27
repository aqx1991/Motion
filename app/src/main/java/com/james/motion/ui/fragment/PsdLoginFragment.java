package com.james.motion.ui.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.blankj.utilcode.util.SPUtils;
import com.james.motion.R;
import com.james.motion.commmon.utils.MySp;
import com.james.motion.ui.BaseFragment;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 描述: 密码登录
 * 作者: james
 * 日期: 2019/1/4 18:15
 * 类名: PsdLoginFragment
 */
public class PsdLoginFragment extends BaseFragment {

    @BindView(R.id.et_username)
    EditText etUsername;
    @BindView(R.id.et_psd)
    EditText etPsd;
    @BindView(R.id.btClear)
    ImageButton btClear;
    @BindView(R.id.btPsd)
    ImageButton btPsd;

    private boolean iscleartext = false;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_psdlogin;
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        String loginname = SPUtils.getInstance().getString(MySp.PHONE);
        String password = SPUtils.getInstance().getString(MySp.PASSWORD);
        if (!TextUtils.isEmpty(loginname)) {
            btClear.setVisibility(View.VISIBLE);
            etUsername.setText(loginname);
        }
    }

    @Override
    public void initListener() {
        etUsername.addTextChangedListener(new TextWatcher() {
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

    @OnClick({R.id.btPsd, R.id.btClear, R.id.tvForget})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btPsd:
                if (TextUtils.isEmpty(etPsd.getText())) {
                    showToast("请先输入密码!");
                    return;
                }
                if (!iscleartext) {
                    etPsd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    btPsd.setImageResource(R.mipmap.icon_psd_h);
                } else {
                    etPsd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    btPsd.setImageResource(R.mipmap.icon_psd_s);
                }
                iscleartext = !iscleartext;
                etPsd.setSelection(etPsd.getText().length());
                break;
            case R.id.btClear:
                etUsername.setText("");
                break;
            case R.id.tvForget:
                showToast("功能开发中...");
                break;
            default:
                break;
        }
    }

    public void checkAccount(CallBack callBack) {
        if (isInput(etUsername, etPsd)) {
            String account = etUsername.getText().toString();
            String psd = etPsd.getText().toString();
            if (psd.length() < 4) {
                showToast("请输入正确的密码!");
            } else {
                callBack.getResult(account, psd);
            }
        }
    }


    public interface CallBack {
        void getResult(String account, String psd);
    }

    private boolean isInput(EditText... e) {
        for (EditText anE : e) {
            if (TextUtils.isEmpty(anE.getText())) {
                showToast(anE.getHint().toString());
                return false;
            }
        }
        return true;
    }
}
