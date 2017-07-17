package edu.utah.chpc.crystal.test;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

public class Sensors extends AppCompatActivity {


    String URL = "http://155.101.8.208:80/html/cam.jpg";
    VideoView streamView = (VideoView)findViewById(R.id.streamview);
    EditText addrField = (EditText)findViewById(R.id.addr);
    Button btnConnect = (Button)findViewById(R.id.connect);

    MediaController mediaController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensors);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        btnConnect.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                String s = addrField.getEditableText().toString();
                playStream(s);
            }
        });
    }

    MediaController streamControl = new MediaController(this);

    private void playStream(String src) {
        streamView.setVideoURI(Uri.parse(URL));
        if (URL == null) {
            Toast.makeText(Sensors.this,
                    "URL == null", Toast.LENGTH_LONG).show();
        }else{

            streamControl.setAnchorView(streamView);
            mediaController = new MediaController(this);
            streamView.setMediaController(mediaController);
            streamView.start();


            Toast.makeText(Sensors.this,
                    "Connect: " + src,
                    Toast.LENGTH_LONG).show();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        streamView.stopPlayback();
    }
}