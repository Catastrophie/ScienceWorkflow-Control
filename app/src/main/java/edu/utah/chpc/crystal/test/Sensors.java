package edu.utah.chpc.crystal.test;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;

public class Sensors extends AppCompatActivity {

    ImageView streamView;
    Button btnConnect;

    private static final String URL1 =("http://155.101.8.183:80/html/cam.jpg");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensors);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        streamView = (ImageView)findViewById(R.id.streamview);
        btnConnect = (Button)findViewById(R.id.connect);


        btnConnect.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {

                playStream(URL1);
            }
        });

    }


    private void playStream(String url) {
        final String finalURL = url;
        if (url == null) {
            Toast.makeText(Sensors.this,
                    "url == null", Toast.LENGTH_LONG).show();
        }else{
            Timer tT = new Timer(true);
            TimerTask task = new TimerTask() {
                public void run() {
                    new DownloadImageTask(streamView).execute (finalURL);
                }
            };
            tT.schedule(task, 1, 1);


            Toast.makeText(Sensors.this,
                    "Connect: " + url,
                    Toast.LENGTH_LONG).show();
        }
    }


    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);


        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}

