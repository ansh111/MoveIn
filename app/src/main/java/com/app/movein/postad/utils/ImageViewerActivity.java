package com.app.movein.postad.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import com.app.movein.R;

public class ImageViewerActivity extends Activity {
    private ImageView mImageViewerActivity;
    private Button mBtnCloseImage, mBtnRemoveImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        setContentView(R.layout.imagevieweractivity);
        mImageViewerActivity = (ImageView) findViewById(R.id.imageviewerimage);
        mBtnCloseImage = (Button) findViewById(R.id.imageviewerclose);
        mBtnRemoveImage = (Button) findViewById(R.id.imageviewerremove);
        savedInstanceState = getIntent().getExtras();
        String mImageViewerFilePath = savedInstanceState.getString("FilePath");
        Bitmap myBitmap = BitmapFactory.decodeFile(mImageViewerFilePath);
        mImageViewerActivity.setImageBitmap(myBitmap);
        mBtnCloseImage.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                ImageViewerActivity.this.finish();
                // TODO Auto-generated method stub

            }
        });
        mBtnRemoveImage.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

            }
        });

        super.onCreate(savedInstanceState);
    }

}
