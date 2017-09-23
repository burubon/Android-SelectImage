package com.example.yutasukekawa.selectimage;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yutasukekawa on 11/26/16.
 */

public class ShowList extends AppCompatActivity {

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_page_list);

        /** ツールバーの文字列 */
        setTitle("図鑑一覧");

        List<ListItem> list = new ArrayList<ListItem>();

        /** データベース */
        MyDBHelper helper = new MyDBHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();

        /** query メソッドの実行 */
        Cursor cursor = db.query("mySalviaPage", new String[] {"title", "uri", "comment"}, null, null, null, null, null);

        boolean mov = cursor.moveToFirst();

        while(mov){
            ListItem item = new ListItem();
            item.setText(cursor.getString(0));
            item.setImageUri(Uri.parse(cursor.getString(1)));
            item.setComment(cursor.getString(2));
            list.add(item);
            mov = cursor.moveToNext();
        }

        cursor.close();
        db.close();

        /** adapterのインスタンスを作成 */
        ImageArrayAdapter adapter = new ImageArrayAdapter(this, R.layout.list_design, list);

        listView = (ListView) findViewById(R.id.listview);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent,
                                    View view, int pos, long id) {

                Intent intent = new Intent(ShowList.this, SalviaPageView.class);

                intent.putExtra("nowCursor", pos + 1);

                startActivity(intent);

            }
        });

    }

}
