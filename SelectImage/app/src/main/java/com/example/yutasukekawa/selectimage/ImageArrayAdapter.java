package com.example.yutasukekawa.selectimage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by yutasukekawa on 11/29/16.
 */

public class ImageArrayAdapter extends ArrayAdapter<ListItem> {

    private int resourceId;
    private List<ListItem> items;
    private LayoutInflater inflater;
    private Context c;

    public ImageArrayAdapter(Context context, int resourceId, List<ListItem> items) {
        super(context, resourceId, items);

        c = context;
        this.resourceId = resourceId;
        this.items = items;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        View view;

        if (convertView != null) {
            view = convertView;
        } else {
            view = this.inflater.inflate(this.resourceId, null);
        }

        ListItem item = this.items.get(position);

        /** テキストをセット */
        TextView appInfoText = (TextView)view.findViewById(R.id.item_text);
        appInfoText.setText(item.getText());

        /** サムネイルをセット */
        ImageView appInfoImage = (ImageView)view.findViewById(R.id.item_image);

        InputStream in = null;
        try {
            in = c.getContentResolver().openInputStream(item.getImageUri());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(in, null, options);

        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        float scaleX = options.outWidth / (options.outWidth / 4);
        float scaleY = options.outHeight / (options.outHeight / 4);
        options.inSampleSize = (int) Math.floor(Float.valueOf(Math.max(scaleX, scaleY)).doubleValue());

        options.inJustDecodeBounds = false;
        options.inPreferredConfig = Bitmap.Config.ARGB_4444;
        try {
            in = c.getContentResolver().openInputStream(item.getImageUri());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Bitmap bitmap = BitmapFactory.decodeStream(in, null, options);

        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        appInfoImage.setImageBitmap(bitmap);

        /** コメントをセット */
        TextView appInfoComment = (TextView)view.findViewById(R.id.item_comment);
        appInfoComment.setText(item.getComment());

        return view;
    }

}
