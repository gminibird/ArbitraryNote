package com.zrj.arbitrarynote.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.zrj.arbitrarynote.R;
import com.zrj.arbitrarynote.util.Util;

public class ArbitraryNoteActivity extends AppCompatActivity implements View.OnClickListener{

    private final int CHOOSE_PHOTO = 0;
    private final int READ_EX_STORAGE = 1;

    private ScrollView scrollView;
    private EditText editText ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arbitrary_note);
        scrollView = (ScrollView) findViewById(R.id.scroll_view_arbitrary_note);
        editText = (EditText) findViewById(R.id.edit_arbitrary_note);
        ImageView insertImage = (ImageView) findViewById(R.id.gallery_arbitrary_note);
        setTitle("随心记");
        ActionBar actionBar = getSupportActionBar();
        if (actionBar!=null){
            actionBar.setElevation(0);
        }
        insertImage.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.gallery_arbitrary_note:
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                if (Build.VERSION.SDK_INT >= 23){
                    if (ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED){
                        ActivityCompat.requestPermissions(
                                this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},READ_EX_STORAGE);
                    }
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_GRANTED){
                        startActivityForResult(intent,CHOOSE_PHOTO);
                    }
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK){
            switch (requestCode){
                case CHOOSE_PHOTO:
                    String imagePath = Util.getRealImagePath(this,data);
                    insertImage(imagePath);
                    break;
            }
        }
    }

    private void insertImage(String imagePath){
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
        SpannableString spannableString = new SpannableString(imagePath);
        ImageSpan imageSpan = new ImageSpan(this,bitmap,ImageSpan.ALIGN_BASELINE);
        spannableString.setSpan(imageSpan,0,
                imagePath.length(),SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
        );
        Editable edit = editText.getEditableText();
        edit.insert(editText.getSelectionStart(),spannableString);

    }
}












