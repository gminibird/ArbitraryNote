package com.zrj.abitrarynote.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.zrj.abitrarynote.R;
import com.zrj.abitrarynote.activity.LoginActivity;
import com.zrj.abitrarynote.activity.MainActivity;
import com.zrj.abitrarynote.activity.SignUpActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by a on 2017/4/24.
 */

public class VerificationCodeFragment extends Fragment implements View.OnClickListener {

    private TextView title;
    private EditText phoneNumber;
    private EditText verificationCode;
    private EditText password;
    private EditText passwordConfirmed;
    private Button requestSMSCode;
    private Button back;
    private Button submit;
    private Context mContext;
    private final String APP_ID = "55d899d33f17878859b67be3d5f02401";

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        Bmob.initialize(context, APP_ID);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_verification, container, false);
        title = (TextView) view.findViewById(R.id.title_verification);
        phoneNumber = (EditText) view.findViewById(R.id.phone_number_verification);
        verificationCode = (EditText) view.findViewById(R.id.SMSCode_verification);
        password = (EditText) view.findViewById(R.id.password_new);
        passwordConfirmed = (EditText) view.findViewById(R.id.confirm_password);
        requestSMSCode = (Button) view.findViewById(R.id.request_verification_code);
        submit = (Button) view.findViewById(R.id.submit_verification);
        back = (Button) view.findViewById(R.id.back_verification);
        back.setOnClickListener(this);
        submit.setOnClickListener(this);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        addEditTextChangedListener();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.request_verification_code:
                BmobSMS.requestSMSCode(phoneNumber.getText().toString(), "随心记", new QueryListener<Integer>() {
                    @Override
                    public void done(Integer integer, BmobException e) {
                        if (e == null) {
                            Toast.makeText(mContext, "获取验证码成功", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(mContext, "获取验证码失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
            case R.id.back_verification:
                ((Activity) mContext).finish();
                break;
            case R.id.submit_verification:
                submitVerification();
                break;
        }
    }

    private void addEditTextChangedListener() {
        if (phoneNumber != null) {
            phoneNumber.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s.length() == 11) {
                        requestSMSCode.setTextColor(getResources().getColor(R.color.colorBuleLight));
                        requestSMSCode.setOnClickListener(VerificationCodeFragment.this);
                    } else {
                        requestSMSCode.setTextColor(getResources().getColor(R.color.colorEditHint));
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

        }
    }

    private void submitVerification() {
        if (phoneNumber != null && verificationCode != null && password != null && passwordConfirmed != null) {
            String password = VerificationCodeFragment.this.password.getText().toString();
            String passwordConfirmed = VerificationCodeFragment.this.passwordConfirmed.getText().toString();
            if (password.equals(passwordConfirmed)) {
                String REGEX = "[^0-9][^a-z][^A-Z][^~`!@#$%^&*-=+_()?.,:;'\"]";
                Pattern pattern = Pattern.compile(REGEX);
                Matcher matcher = pattern.matcher(password);
                if (matcher.find()) {
                    String illegalText = "使用非法字符“" + password.substring(matcher.start(), matcher.end()) + "”";
                    Toast.makeText(mContext, illegalText, Toast.LENGTH_SHORT).show();
                } else {
                    BmobSMS.verifySmsCode(phoneNumber.getText().toString(), verificationCode.getText().toString(), new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                Intent intent = new Intent(mContext, MainActivity.class);
                                startActivity(intent);
                                ((Activity) mContext).finish();
                            } else {
                                if (mContext instanceof SignUpActivity) {
                                    Toast.makeText(mContext, "注册失败", Toast.LENGTH_SHORT).show();
                                } else if (mContext instanceof LoginActivity) {
                                    Toast.makeText(mContext, "重置密码失败", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });

                }
            } else {
                Toast.makeText(mContext, "两次输入的密码不一致", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void setTitle(String title) {
        if (this.title != null) {
            this.title.setText(title);
        }
    }

    public void setEditTextHint(@Nullable String passwordHint, @Nullable String confirmHint) {
        if (password != null) {
            password.setHint(passwordHint);
        }
        if (passwordConfirmed != null) {
            passwordConfirmed.setHint(confirmHint);
        }
    }
}
