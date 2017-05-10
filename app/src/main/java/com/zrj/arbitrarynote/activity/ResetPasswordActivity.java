package com.zrj.arbitrarynote.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.zrj.arbitrarynote.R;
import com.zrj.arbitrarynote.fragment.VerificationCodeFragment;

public class ResetPasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        initializeFragment();
    }

    private void initializeFragment() {
        VerificationCodeFragment fragment = (VerificationCodeFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_reset_password);
        fragment.setTitle("重置密码");
        fragment.setEditTextHint("新密码", "确认密码");
    }
}
