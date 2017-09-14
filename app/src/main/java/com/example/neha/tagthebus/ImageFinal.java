package com.example.neha.tagthebus;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

//This clss is the method responsible for seeing the list updated list of photos of a street
public class ImageFinal extends AppCompatActivity {
    String item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_final);
        //we get as intent the name of the selected street from SingleListItemClass
        Intent intent = getIntent();
        item = intent.getStringExtra("name");
        //We make an array which stores the street photos and other details
        ArrayList<Streets> list;
        StreetListAdapter adapter;
        final ListView listView = (ListView) findViewById(R.id.listView);
        list = new ArrayList<>();
        adapter = new StreetListAdapter(this, R.layout.streetitem, list);
        listView.setAdapter(adapter);
        //this shows the name of the streets
        TextView txt = (TextView) findViewById(R.id.textView);
        txt.setText(item);
        // get all data from sqlite for the particular street
        Cursor cursor = SingleListItem.sqLiteHelper.getData("select * from STREET where name='" + item + "'", null);
        list.clear();
        String title;
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            title = cursor.getString(2);
            byte[] image = cursor.getBlob(3);
            String time = cursor.getString(4);
            String user = cursor.getString(5);
            list.add(new Streets(name, title, image, time, user, id));
        }
        adapter.notifyDataSetChanged();

        // listening to single list item on click we pass the image details to class
        //DelPicture where we may want to share or delete the picture
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = new Intent(getApplicationContext(), DelPicture.class);
                String u = (String) ((TextView) view.findViewById(R.id.txtuser)).getText();
                String t = (String) ((TextView) view.findViewById(R.id.txttime)).getText();
                intent.putExtra("time", t);
                intent.putExtra("user", u);
                startActivity(intent);
            }
        });
    }

}
