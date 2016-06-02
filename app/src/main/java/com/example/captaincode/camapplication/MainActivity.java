package com.example.captaincode.camapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private Button btnsend, btntake;
    private ImageView img;
    private EditText etname;
    private String name;
    private String directory;
    private static final int REQUEST_IMAGE_CAPTURE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        this.btnsend = (Button) findViewById(R.id.btnsend);
        this.btntake = (Button) findViewById(R.id.btntake);
        this.img = (ImageView) findViewById(R.id.ivImage);
        this.etname = (EditText) findViewById(R.id.etname);
        this.btnsend.setOnClickListener(this);
        this.btntake.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_IMAGE_CAPTURE){
            if(resultCode ==  RESULT_OK){
                this.img.setImageURI(Uri.parse(this.directory + "/" + this.name + ".jpg"));
            }else if(resultCode == RESULT_CANCELED)
                Toast.makeText(getApplicationContext(), "Ha cancelado la captura de la imagen", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(getApplicationContext(), "Se tuvo un error al capturar la imagen", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if(R.id.btntake == v.getId()) {
            this.name = this.etname.getText().toString();
            this.directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "/pics";

            File newDir = new File(this.directory);
            System.out.println(this.directory);

            if (!newDir.exists())
                newDir.mkdirs();

            File newImage = new File(directory + "/" + this.name + ".jpg");

            try {
                newImage.createNewFile();
            } catch (IOException ex) {
                Log.d("Error", ex.getMessage());
            }

            Uri uri = Uri.fromFile(newImage);

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            this.img.setImageDrawable(null);

            if (intent.resolveActivity(getPackageManager()) != null)
                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);


        }
        else if(this.btnsend.getId() == v.getId()){
            //send by email
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("image/jpg");
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"villalobos.salmeron.1982@gmail.com"});
            File downloadedPicture = new File(this.directory+"/"+this.name+".jpg");
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(downloadedPicture));
            intent.putExtra(Intent.EXTRA_SUBJECT, "Foto de tomada por Diego De Santiago");
            intent.putExtra(Intent.EXTRA_TEXT, "Le adjunto la foto tomada desde mi LG Lion");
            startActivity(intent);
            this.img.setImageDrawable(null);
            this.etname.setText("");
        }

    }
}
