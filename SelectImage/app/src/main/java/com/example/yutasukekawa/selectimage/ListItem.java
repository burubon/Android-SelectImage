package com.example.yutasukekawa.selectimage;

import android.net.Uri;

/**
 * Created by yutasukekawa on 11/29/16.
 */

public class ListItem {

    private Uri imageUri;
    private String title;
    private String comment;

    /** サムネイルのURI */
    public Uri getImageUri(){
        return imageUri;
    }

    public void setImageUri(Uri imageUri){
        this.imageUri = imageUri;
    }

    /** タイトル */
    public String getText(){
        return title;
    }

    public void setText(String text){
        this.title = text;
    }

    /** コメント */
    public String getComment(){
        return comment;
    }

    public void setComment(String comment){
        this.comment = comment;
    }

}
