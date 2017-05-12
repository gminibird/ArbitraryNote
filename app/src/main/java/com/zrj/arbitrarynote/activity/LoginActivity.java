package com.zrj.arbitrarynote.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.zrj.arbitrarynote.R;
import com.zrj.arbitrarynote.bmob.BmobUtil;
import com.zrj.arbitrarynote.bmob.User;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText userAccount;
    private EditText password;
    private TextView forgotPass;
    private TextView signUp;
    private Button login;
    private CheckBox rememberPass;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        userAccount = (EditText) findViewById(R.id.user_acount);
        password = (EditText) findViewById(R.id.user_password);
        signUp = (TextView) findViewById(R.id.to_sign_up);
        forgotPass = (TextView) findViewById(R.id.forgot_pass);
        login = (Button) findViewById(R.id.button_login_to);
        rememberPass = (CheckBox) findViewById(R.id.remember_pass);
        signUp.setOnClickListener(this);
        forgotPass.setOnClickListener(this);
        login.setOnClickListener(this);
        rememberPass.setOnClickListener(this);
        Bmob.initialize(this, BmobUtil.APP_ID);
        initControlsProperties();
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.to_sign_up:
                intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
                break;
            case R.id.forgot_pass:
                intent = new Intent(LoginActivity.this, ResetPasswordActivity.class);
                startActivity(intent);
                break;
            case R.id.button_login_to:
                login();
                break;

        }
    }

    private void login() {
        showProgressDialog();
        final SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this).edit();
        User user = new User();
        user.setUsername(userAccount.getText().toString());
        user.setPassword(password.getText().toString());
        user.login(new SaveListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                    }
                });
                if (e == null) {
                    Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                    editor.putString("userAccount", userAccount.getText().toString());
                    if (rememberPass.isChecked()) {
                        editor.putString("password", password.getText().toString());
                        editor.putBoolean("remember_password", true);
                    } else {
                        editor.putBoolean("remember_password", false);
                        editor.remove("password");
                    }
                    editor.apply();
                    editor.putString("userAccount", userAccount.getText().toString());
                    if (rememberPass.isChecked()) {
                        editor.putString("password", password.getText().toString());
                        editor.putBoolean("remember_password", true);
                    } else {
                        editor.putBoolean("remember_password", false);
                        editor.remove("password");
                    }
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    BmobUtil.handleException(LoginActivity.this, e, "登录失败");
                    editor.remove("password");
                }
                editor.apply();
            }
        });
    }

    private void initControlsProperties() {
        Intent intent = getIntent();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String userAccountText = intent.getStringExtra("user_account");
        String passwordText = intent.getStringExtra("password");
        if (userAccountText != null) {
            this.userAccount.setText(userAccountText);
            password.setText(passwordText);
        } else {
            userAccountText = preferences.getString("userAccount", null);
            passwordText = preferences.getString("password", null);
            this.userAccount.setText(userAccountText);
            password.setText(passwordText);
        }
        boolean isRemembered = preferences.getBoolean("remember_password", false);
        rememberPass.setChecked(isRemembered);
        if (!userAccount.getText().toString().equals("")) {
            password.requestFocus();
            password.setSelection(password.getText().length());
        }
    }

    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(LoginActivity.this);
            progressDialog.setMessage("登录中...");
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
