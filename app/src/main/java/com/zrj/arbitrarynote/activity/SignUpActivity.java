package com.zrj.arbitrarynote.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.zrj.arbitrarynote.R;
import com.zrj.arbitrarynote.fragment.LogOrSignFragment;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
       initializeFragment();
    }

    private void initializeFragment(){
        LogOrSignFragment fragment = (LogOrSignFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_sign_up);
        fragment.setTitle("注册");
        fragment.setEditTextHint("密码","确认密码");
    }
}
