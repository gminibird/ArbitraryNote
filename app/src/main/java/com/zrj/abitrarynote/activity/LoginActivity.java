package com.zrj.abitrarynote.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.zrj.abitrarynote.R;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText userName;
    private EditText password;
    private TextView forgotPass;
    private TextView signUp;
    private Button login;
    private CheckBox rememberPass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        userName = (EditText)findViewById(R.id.phone_number_verification);
        password = (EditText)findViewById(R.id.user_password);
        signUp = (TextView)findViewById(R.id.to_sign_up);
        forgotPass = (TextView)findViewById(R.id.forgot_pass);
        login = (Button) findViewById(R.id.button_login_to);
        rememberPass = (CheckBox)findViewById(R.id.remember_pass);

        signUp.setOnClickListener(this);
        forgotPass.setOnClickListener(this);
        login.setOnClickListener(this);
        rememberPass.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case  R.id.to_sign_up:
                intent = new Intent(LoginActivity.this,SignUpActivity.class);
                startActivity(intent);
                break;
            case R.id.forgot_pass:
                intent = new Intent(LoginActivity.this,ResetPasswordActivity.class);
                startActivity(intent);
                break;
            case R.id.button_login_to:
                break;
            case R.id.remember_pass:
                break;
        }
    }
}
