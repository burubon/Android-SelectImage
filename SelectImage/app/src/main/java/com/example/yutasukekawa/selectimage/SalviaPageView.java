package com.example.yutasukekawa.selectimage;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.FileDescriptor;
import java.io.IOException;

/**
 * Created by yutasukekawa on 10/25/16.
 */

public class SalviaPageView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.salvia_page_view);

        Intent intent = getIntent();

        int nowCursor = intent.getExtras().getInt("nowCursor");

        /** データベース */
        MyDBHelper helper = new MyDBHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();

        Cursor cursor = db.query("mySalviaPage", new String[] {"title", "uri", "comment", "faceBin"}, null, null, null, null, null);
        cursor.move(nowCursor);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.salvia);

        Uri uri = Uri.parse(cursor.getString(1));

        try {
            bitmap = getBitmapFromUri(uri);
        } catch (IOException e) {
            e.printStackTrace();
        }

        setTitle(cursor.getString(0));

        /** ImageViewに紐つけ */
        ImageView mainImageView = (ImageView)findViewById(R.id.mainImageView);
        mainImageView.setImageBitmap(bitmap);

        /** TextViewに紐付け */
        TextView titleText = (TextView)findViewById(R.id.salvia_title);
        TextView commentText = (TextView)findViewById(R.id.textView2);

        titleText.setText(cursor.getString(0));
        commentText.setText(cursor.getString(2));

        titleText.setTypeface(Typeface.createFromAsset(getAssets(), "rounded-mplus-1c-bold.ttf"));
        commentText.setTypeface(Typeface.createFromAsset(getAssets(), "rounded-mplus-1c-bold.ttf"));

        /** ImageViewに紐付け（顔アイコン） */
        ImageView[] faceImage = new ImageView[5];
        faceImage[0] = (ImageView)findViewById(R.id.face_1);
        faceImage[1] = (ImageView)findViewById(R.id.face_2);
        faceImage[2] = (ImageView)findViewById(R.id.face_3);
        faceImage[3] = (ImageView)findViewById(R.id.face_4);
        faceImage[4] = (ImageView)findViewById(R.id.face_5);

        /** faceBin 解析 */
        boolean[] faceBit = new boolean[5];
        int i, j;
        short faceBin = cursor.getShort(3);
        for(i = 0; i < 5; i++){

            boolean breakFlag = false;

            faceBit[i] = false;

            short num = (short) Math.pow(10, i);

            switch (i){

                // 1ビット目
                case 0:
                    if(faceBin < 1){
                        breakFlag = true;
                        break;
                    }
                    else if(faceBin / 1 % 10 == 1){
                        faceBit[i] = true;
                        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.ico_00);
                        faceImage[i].setImageBitmap(bmp);
                    }

                    break;

                // 2ビット目
                case 1:
                    if(faceBin < 10){
                        breakFlag = true;
                        break;
                    }
                    else if(faceBin / 10 % 10 == 1){
                        faceBit[i] = true;
                        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.ico_01);
                        for(j = 0; j <= i; j++){
                            if(faceBit[j] == false) {
                                faceBit[i] = false;
                                faceBit[j] = true;
                                faceImage[j].setImageBitmap(bmp);
                                break;
                            }
                        }
                    }
                    break;

                // 3ビット目
                case 2:
                    if(faceBin < 100){
                        breakFlag = true;
                        break;
                    }
                    else if(faceBin / 100 % 10 == 1){
                        faceBit[i] = true;
                        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.ico_02);
                        for(j = 0; j <= i; j++){
                            if(faceBit[j] == false) {
                                faceBit[i] = false;
                                faceBit[j] = true;
                                faceImage[j].setImageBitmap(bmp);
                                break;
                            }
                        }
                    }
                    break;

                // 4ビット目
                case 3:
                    if(faceBin < 1000){
                        breakFlag = true;
                        break;
                    }
                    else if(faceBin / 1000 % 10 == 1){
                        faceBit[i] = true;
                        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.ico_03);
                        for(j = 0; j <= i; j++){
                            if(faceBit[j] == false) {
                                faceBit[i] = false;
                                faceBit[j] = true;
                                faceImage[j].setImageBitmap(bmp);
                                break;
                            }
                        }
                    }
                    break;

                // 5ビット目
                case 4:
                    if(faceBin < 10000){
                        breakFlag = true;
                        break;
                    }
                    else if(faceBin / 10000 % 10 == 1){
                        faceBit[i] = true;
                        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.ico_04);
                        for(j = 0; j <= i; j++){
                            if(faceBit[j] == false) {
                                faceBit[i] = false;
                                faceBit[j] = true;
                                faceImage[j].setImageBitmap(bmp);
                                break;
                            }
                        }
                    }
                    break;
            }

            if(breakFlag == true)
                break;

        }

        db.close();
        cursor.close();

    }

    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }

}
