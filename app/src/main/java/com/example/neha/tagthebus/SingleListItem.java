package com.example.neha.tagthebus;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

//This class is reponsible for taking pictures for the streets we can add our username and title
//We also set the current time of picture creation as its time.
public class SingleListItem extends AppCompatActivity {

    private static final int CAMERA_PIC_REQUEST = 1888;
    private static final int SELECT_PICTURE = 100;
    public static SQLiteHelper sqLiteHelper;
    String item;
    AppCompatImageView imgView;
    TextView itemName;
    EditText maketitle, username;
    ImageView imageView;
    String date;
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_list_item);
        imageView = (ImageView) findViewById(R.id.imageView);
        Intent intent = getIntent();
        item = intent.getStringExtra("title");
        itemName = (TextView) findViewById(R.id.product_label);
        itemName.setText(item);
        //getting the current time &date
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        date = sdf.format(new Date());
        Button button = (Button) findViewById(R.id.button);
        btn = (Button) findViewById(R.id.btn);
        imageView = (ImageView) findViewById(R.id.imageView);
        maketitle = (EditText) findViewById(R.id.editText);
        username = (EditText) findViewById(R.id.editText2);
        //we create a new database if not existing with the name
        sqLiteHelper = new SQLiteHelper(this, "STREETSDB.sqlite", null, 1);
        sqLiteHelper.queryData("CREATE TABLE IF NOT EXISTS STREET(Id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR, title VARCHAR, image BLOB, time VARCHAR, user VARCHAR)");
    }

    //method to take image from the camera
    public void takeImageFromCamera(View v) {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE_SECURE);
        startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
    }

    //when we get the image from camera capture we pass it to the imageview
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_PIC_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap image = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(image);
        }
    }

    //method to validate the image i.e store it in the database once this is done we go
    //to the activity ImageFinal which Shows finally all the images of the street
    public void validate(View v) {
        try {
            sqLiteHelper.insertData(
                    itemName.getText().toString().trim(),
                    maketitle.getText().toString().trim(),
                    imageViewToByte(imageView), date,
                    username.getText().toString().trim()
            );
            Toast.makeText(getApplicationContext(), "Added successfully!", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Intent in = new Intent(getApplicationContext(), ImageFinal.class);
        in.putExtra("name", item);
        startActivity(in);
    }

    private byte[] imageViewToByte(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }
}
