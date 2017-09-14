package com.example.neha.tagthebus;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class DelPicture extends AppCompatActivity {

    private ImageView img;
    private String user;
    private String time;
    private EditText editText;
    private byte[] StreetSelectedimage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_del_picture);

        //Prints the name of the street on the screen
        editText = (EditText) findViewById(R.id.editText3);

        //getting the user name and time of the selected image
        Intent intent = getIntent();
        user = intent.getStringExtra("user");
        time = intent.getStringExtra("time");

        //selecting all the details of the image selected by the time of creation
        Cursor cursor = SingleListItem.sqLiteHelper.getData("select * from STREET where time='" + time + "'", null);
        ArrayList<Streets> list = new ArrayList<>();
        list.clear();
        StreetListAdapter adapter;
        adapter = new StreetListAdapter(this, R.layout.streetitem, list);
        ListView listView = (ListView) findViewById(R.id.listView1);
        listView.setAdapter(adapter);
        img = (ImageView) findViewById(R.id.imageView4);

        //creating a list to show the image selected along with the other details
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String title = cursor.getString(2);
            StreetSelectedimage = cursor.getBlob(3);
            String time1 = cursor.getString(4);
            list.add(new Streets(title, StreetSelectedimage, time1, id));
        }
        adapter.notifyDataSetChanged();
        Bitmap bitmap = BitmapFactory.decodeByteArray(StreetSelectedimage, 0, StreetSelectedimage.length);
        img.setImageBitmap(bitmap);
    }

    //function to delete selected image but first verifying the username
    //if the user has created it
    public void deleteitems(View v) {

        String userinput = editText.getText().toString().trim();
        if (userinput.equalsIgnoreCase(user)) {
            Toast.makeText(this, "AUTHORISED TO DELETE DATA", Toast.LENGTH_LONG).show();
            SingleListItem.sqLiteHelper.delete("delete from STREET where time='" + time + "'");
            Toast.makeText(this, "DELETED DATA SUCCESSFULLY", Toast.LENGTH_LONG).show();

        } else {
            Toast.makeText(this, "NOT AUTHORISED TO DELETE DATA", Toast.LENGTH_LONG).show();
        }
    }

    //method to share image to other applications
    public void shareimage(View v) {
        // Get access to bitmap image from view
        // Get access to the URI for the bitmap  img is the image view of the selected image
        Uri bmpUri = getLocalBitmapUri(img);
        if (bmpUri != null) {
            // Construct a ShareIntent with link to image
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
            shareIntent.setType("image/*");
            // Launch sharing dialog for image
            startActivity(Intent.createChooser(shareIntent, "Share Image"));
        } else {
            System.out.println("THERE WAS NO IMAGE ");
        }
    }

    // Returns the URI path to the Bitmap displayed in specified ImageView
    public Uri getLocalBitmapUri(ImageView imageView) {
        // Extract Bitmap from ImageView drawable
        Drawable drawable = imageView.getDrawable();
        Bitmap bmp;
        if (drawable instanceof BitmapDrawable) {
            bmp = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        } else {
            return null;
        }
        // Store image to default external storage directory
        Uri bmpUri = null;
        try {

            File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image_" + System.currentTimeMillis() + ".png");
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            bmpUri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }
}
