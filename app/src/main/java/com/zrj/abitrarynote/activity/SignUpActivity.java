package com.zrj.abitrarynote.activity;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.zrj.abitrarynote.R;
import com.zrj.abitrarynote.fragment.VerificationCodeFragment;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
       initializeFragment();
    }

    private void initializeFragment(){
        VerificationCodeFragment fragment = (VerificationCodeFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_sign_up);
        fragment.setTitle("注册");
        fragment.setEditTextHint("密码","确认密码");
    }
}
