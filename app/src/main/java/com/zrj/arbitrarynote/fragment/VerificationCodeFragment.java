package com.zrj.arbitrarynote.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.zrj.arbitrarynote.R;
import com.zrj.arbitrarynote.activity.LoginActivity;
import com.zrj.arbitrarynote.activity.ResetPasswordActivity;
import com.zrj.arbitrarynote.activity.SignUpActivity;
import com.zrj.arbitrarynote.bmob.BmobUtil;
import com.zrj.arbitrarynote.bmob.User;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
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
    private ProgressDialog progressDialog;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        Bmob.initialize(context, BmobUtil.APP_ID);
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
        submit = (Button) view.findViewById(R.id.commit_verification);
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
                requestSMSCode();
                break;
            case R.id.back_verification:
                ((Activity) mContext).finish();
                break;
            case R.id.commit_verification:
                commit();
                break;
        }
    }

    private void requestSMSCode() {
        showProgressDialog("正在获取...");
        BmobSMS.requestSMSCode(phoneNumber.getText().toString(), "随心记", new QueryListener<Integer>() {
            @Override
            public void done(Integer integer, BmobException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                    }
                });
                if (e == null) {
                    Toast.makeText(mContext, "获取验证码成功", Toast.LENGTH_SHORT).show();
                } else {
                    BmobUtil.handleException(mContext, e, "获取验证码失败");
                }
            }
        });
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

    private void commit() {
        if (phoneNumber != null && verificationCode != null && password != null && passwordConfirmed != null) {
            final String userAccount = phoneNumber.getText().toString();
            final String password = VerificationCodeFragment.this.password.getText().toString();
            String passwordConfirmed = VerificationCodeFragment.this.passwordConfirmed.getText().toString();
            if (!password.equals("") && password.equals(passwordConfirmed)) {
                String REGEX = "[^0-9][^a-z][^A-Z][^~`!@#$%^&*-=+_()?.,:;'\"]";
                Pattern pattern = Pattern.compile(REGEX);
                Matcher matcher = pattern.matcher(password);
                if (matcher.find()) {
                    String illegalText = "使用非法字符“" + password.substring(matcher.start(), matcher.end()) + "”";
                    Toast.makeText(mContext, illegalText, Toast.LENGTH_SHORT).show();
                } else {
                    showProgressDialog("请稍后...");
                    BmobSMS.verifySmsCode(userAccount, verificationCode.getText().toString(), new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                if (mContext instanceof SignUpActivity) {
                                    User user = new User();
                                    user.setUsername(userAccount);
                                    user.setPassword(password);
                                    user.signUp(new SaveListener<User>() {
                                        @Override
                                        public void done(User user, BmobException e) {
                                            getActivity().runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    closeProgressDialog();
                                                }
                                            });
                                            if (e == null) {
                                                setDialog("注册成功");
                                            } else {
                                                BmobUtil.handleException(mContext, e, "注册失败");
                                            }
                                        }
                                    });
                                } else if (mContext instanceof ResetPasswordActivity) {
                                    BmobQuery<User> query = new BmobQuery<>();
                                    query.addWhereEqualTo("username", userAccount)
                                            .findObjects(new FindListener<User>() {
                                                @Override
                                                public void done(List<User> list, BmobException e) {
                                                    getActivity().runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            closeProgressDialog();
                                                        }
                                                    });
                                                    if (e == null) {
                                                        User user = list.get(0);
                                                        user.setPassword(password);
                                                        user.update(user.getObjectId(), null);
                                                        setDialog("密码重置成功!");
                                                    } else {
                                                        BmobUtil.handleException(mContext, e, "重置失败");
                                                    }
                                                }
                                            });
                                }
                            } else {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        closeProgressDialog();
                                    }
                                });
                                BmobUtil.handleException(mContext, e, null);
                            }
                        }
                    });

                }
            } else if (userAccount.equals("")) {
                Toast.makeText(mContext, "手机号不能为空", Toast.LENGTH_SHORT).show();
            } else if (!password.equals(passwordConfirmed)) {
                Toast.makeText(mContext, "两次输入的密码不一致", Toast.LENGTH_SHORT).show();
            } else if (password.equals("")) {
                Toast.makeText(mContext, "密码不能为空", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void setDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("提示")
                .setMessage(message)
                .setPositiveButton("立即登录", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(mContext, LoginActivity.class);
                        intent.putExtra("user_account", phoneNumber.getText().toString());
                        intent.putExtra("password", password.getText().toString());
                        startActivity(intent);
                        ((Activity) mContext).finish();
                    }
                })
                .show();

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

    private void showProgressDialog(String msg) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(mContext);
            progressDialog.setMessage(msg);
            progressDialog.setCancelable(true);
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    private void closeProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }
}
