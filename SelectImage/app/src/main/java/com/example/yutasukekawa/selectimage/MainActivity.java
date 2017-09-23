package com.example.yutasukekawa.selectimage;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.ParcelFileDescriptor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.FileDescriptor;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private static final int RESULT_PICK_IMAGEFILE = 1001;
    private ImageView imageView = null;
    private Uri selectUri;

    private boolean[] faceBit = new boolean[5];
    private Bitmap icoBmp;
    private Drawable[] drawable = new Drawable[5];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MyDBHelper helper = new MyDBHelper(this);
        final SQLiteDatabase db = helper.getReadableDatabase();

        /** ツールバーの文字列 */
        setTitle("図鑑ページ作成");

        imageView = (ImageView)findViewById(R.id.image_view);

        /** エディットテキストを紐付け */
        final EditText inptTitle = (EditText)findViewById(R.id.edit_title);
        final EditText inptComment = (EditText)findViewById(R.id.edit_comment);

        /** 画像選択ボタンが押された時 */
        Button slctImgBtn = (Button)findViewById(R.id.create_image_button);
        slctImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent;

                if (Build.VERSION.SDK_INT < 19) {
                    intent = new Intent(Intent.ACTION_PICK);
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                } else {
                    intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                }

                intent.setType("image/*");

                startActivityForResult(intent, RESULT_PICK_IMAGEFILE);

            }
        });

        /** それぞれのイメージボタンが押された時 */
        // 嬉しい
        icoBmp = BitmapFactory.decodeResource(getResources(), R.drawable.ico_00);
        drawable[0] = new BitmapDrawable(getResources(), icoBmp);
        ImageButton happy =(ImageButton)findViewById(R.id.happy_face_button);
        drawable[0].setAlpha(128);
        happy.setBackground(drawable[0]);
        happy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                faceBit[0] = !faceBit[0];
                if(faceBit[0] == false) drawable[0].setAlpha(128);
                else drawable[0].setAlpha(255);
            }
        });

        // 楽しい
        icoBmp = BitmapFactory.decodeResource(getResources(), R.drawable.ico_01);
        drawable[1] = new BitmapDrawable(getResources(), icoBmp);
        ImageButton pleasant =(ImageButton)findViewById(R.id.pleasant_face_button);
        drawable[1].setAlpha(128);
        pleasant.setBackground(drawable[1]);
        pleasant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                faceBit[1] = !faceBit[1];
                if(faceBit[1] == false) drawable[1].setAlpha(128);
                else drawable[1].setAlpha(255);

            }
        });

        // 驚き
        icoBmp = BitmapFactory.decodeResource(getResources(), R.drawable.ico_02);
        drawable[2] = new BitmapDrawable(getResources(), icoBmp);
        ImageButton surprise =(ImageButton)findViewById(R.id.surprise_face_button);
        drawable[2].setAlpha(128);
        surprise.setBackground(drawable[2]);
        surprise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                faceBit[2] = !faceBit[2];
                if(faceBit[2] == false) drawable[2].setAlpha(128);
                else drawable[2].setAlpha(255);
            }
        });

        // 悲しい
        icoBmp = BitmapFactory.decodeResource(getResources(), R.drawable.ico_03);
        drawable[3] = new BitmapDrawable(getResources(), icoBmp);
        ImageButton sorrow =(ImageButton)findViewById(R.id.sorrow_face_button);
        drawable[3].setAlpha(128);
        sorrow.setBackground(drawable[3]);
        sorrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                faceBit[3] = !faceBit[3];
                if(faceBit[3] == false) drawable[3].setAlpha(128);
                else drawable[3].setAlpha(255);
            }
        });

        // 怒り
        icoBmp = BitmapFactory.decodeResource(getResources(), R.drawable.ico_04);
        drawable[4] = new BitmapDrawable(getResources(), icoBmp);
        ImageButton anger =(ImageButton)findViewById(R.id.anger_face_button);
        drawable[4].setAlpha(128);
        anger.setBackground(drawable[4]);
        anger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                faceBit[4] = !faceBit[4];
                if(faceBit[4] == false) drawable[4].setAlpha(128);
                else drawable[4].setAlpha(255);
            }
        });

        /** 作成ボタンが押された時 */
        Button cretPgBtn = (Button)findViewById(R.id.create_button);
        cretPgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(selectUri != null) {

                    String titleStr = inptTitle.getText().toString();
                    String commentStr = inptComment.getText().toString();

                    short faceBin = 0;
                    byte i;

                    for(i = 0; i < 5; i++){
                        if(faceBit[i] == true){
                            switch (i){
                                case 0:
                                    faceBin += 1;
                                    break;

                                case 1:
                                    faceBin += 10;
                                    break;

                                case 2:
                                    faceBin += 100;
                                    break;

                                case 3:
                                    faceBin += 1000;
                                    break;

                                case 4:
                                    faceBin += 10000;
                                    break;
                            }
                        }
                    }

                    /** タイトル・コメントが空白の場合 */
                    if(titleStr.length() == 0){
                        titleStr = "Untitled";
                    }

                    if(commentStr.length() == 0){
                        commentStr = "No Comment";
                    }

                    ContentValues insertValues = new ContentValues();

                    insertValues.put("title", titleStr);
                    insertValues.put("uri", selectUri.toString());
                    insertValues.put("comment", commentStr);
                    insertValues.put("faceBin", faceBin);
                    long id = db.insert("mySalviaPage", "", insertValues);

                    Toast.makeText(MainActivity.this, "\"" + titleStr + "\"" + "を作成しました．", Toast.LENGTH_SHORT).show();

                }
                else{
                    Toast.makeText(MainActivity.this, "おっと！画像を選択してください！", Toast.LENGTH_SHORT).show();
                }
            }

        });

        /** リスト表示ボタンが押された時 */
        Button listButtn = (Button)findViewById(R.id.list_button);
        listButtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ShowList.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {

        // The ACTION_OPEN_DOCUMENT intent was sent with the request code
        // READ_REQUEST_CODE. If the request code seen here doesn't match, it's the
        // response to some other intent, and the code below shouldn't run at all.
        if (requestCode == RESULT_PICK_IMAGEFILE && resultCode == Activity.RESULT_OK) {
            // The document selected by the user won't be returned in the intent.
            // Instead, a URI to that document will be contained in the return intent
            // provided to this method as a parameter.
            // Pull that URI using resultData.getData().
            if (resultData != null) {
                selectUri = resultData.getData();
                try {
                    Bitmap getImage = getBitmapFromUri(selectUri);
                    imageView.setImageBitmap(getImage);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
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