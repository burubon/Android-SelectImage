package com.example.yutasukekawa.selectimage;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_PICK_UP_IMAGE = 101;

    private Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create Intent to pick up an image.
        Intent i = new Intent();
        i.setType("image/*");
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(i, REQUEST_PICK_UP_IMAGE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_PICK_UP_IMAGE) {
            uri = data.getData();
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            ImageView my_img_view = (ImageView) findViewById(R.id.image_view);
            my_img_view.setImageBitmap(bitmap);

        }
    }
}
