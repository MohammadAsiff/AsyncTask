package com.example.sys.asynctask;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URL;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class AsynctaskActivity extends AppCompatActivity {
    ProgressDialog progressDialog;
    ImageView imv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_asynctask );
        imv=findViewById( R.id.imageV );
        if (checkPermission())
        {
            imv.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i=new Intent( Intent.ACTION_SEND );
                    Uri u=Uri.parse( "file:///sdcard/name.jpg");
                    i.setType("image/jpeg");
                    i.putExtra(Intent.EXTRA_STREAM, u);
                    startActivity(Intent.createChooser(i, "Share image "));

                }
            } );

        }
        else{
            requestPermission( );
            imv.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i=new Intent( Intent.ACTION_SEND );
                    Uri u=Uri.parse( "file:///sdcard/name.jpg");
                    i.setType("image/jpeg");
                    i.putExtra(Intent.EXTRA_STREAM, u);
                    startActivity(Intent.createChooser(i,"Share image"));

                }
            } );

        }
String url="https://media.zigcdn.com/media/model/2017/Nov/royal-enfield-interceptor-int-650-right_600x300.jpg";
        new DownloadImage().execute( url );

    }

    private boolean checkPermission() {
        int FirstPermissionResult = ContextCompat.checkSelfPermission( AsynctaskActivity.this ,WRITE_EXTERNAL_STORAGE );
        if (FirstPermissionResult== PackageManager.PERMISSION_GRANTED)
            return true;
        else
            return false;
    }

    public void requestPermission()
    {
        ActivityCompat.requestPermissions( AsynctaskActivity.this,new String[]
                {WRITE_EXTERNAL_STORAGE},100 );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 100:
                if (grantResults.length>0&&grantResults[0]== PackageManager.PERMISSION_GRANTED
                        )
                    Log.e( "value","Permission Granted,Now you can use local Drive" );
                else
                    Log.e( "value","Permissions Denied,You cannot use local Drive" );
                break;

        }
    }

    private class DownloadImage extends AsyncTask<String,Void,Bitmap> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog=new ProgressDialog( AsynctaskActivity.this );
            progressDialog.setTitle( "Downloading" );
            progressDialog.setMessage( "Loading.." );
            progressDialog.setIndeterminate( false );
            progressDialog.show();
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            String imageURL = strings[0];
            Bitmap bitmap = null;

            try {
                InputStream input = new URL( imageURL ).openStream();
                bitmap = BitmapFactory.decodeStream( input );
                input.close();
                File storagePath = Environment.getExternalStorageDirectory();
                 OutputStream bytes = new FileOutputStream( new File( storagePath, "name.jpg" ));

                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                bytes.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
         /*   InputStream input = null;
            OutputStream output = null;
            try {
                byte aReasonable = 0;
                byte[] buffer = new byte[aReasonable];
                int bytesRead = 0;
                while ((bytesRead = input.read( buffer, 0, buffer.length )) >= 0) {
                    output.write( buffer, 0, bytesRead );
                }


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    input.close();
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }*/
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
          imv.setImageBitmap( bitmap );
          progressDialog.dismiss();
        }
    }
}
