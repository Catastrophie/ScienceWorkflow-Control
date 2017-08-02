package edu.utah.chpc.crystal.test;

import android.media.MediaPlayer;
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

public class Sensors extends AppCompatActivity {   //Calling class -Sensors- and creating a subclass of the base class being extended AppCompatActivity

    VideoView streamView;   //Variable calling streamView into import class VideoView
    EditText addrField;     //Variable calling addrField into import class EditText
    Button btnConnect;      //Variable calling btnConnect into import class Button
    MediaController mediaController; //Variable calling mediaController into import class MediaController

    private static final String URL1 =("http://155.101.8.208:80/html/cam.jpg"); //Private is an access specifier. Static is class-level variable. Final make this call as final.


    @Override    //Indicates that a method declaration is intended to override the functionality of an existing method.
    protected void onCreate(Bundle savedInstanceState) { //onCreate Required Android method to get activity ready for creation. Protected-access level limit Void-no return value
        super.onCreate(savedInstanceState);    //run code in addition to the existing code in the onCreate() of the parent class.
        setContentView(R.layout.activity_sensors);    //Creates window for UI (R.layout.* -references any layout resource created. .activity_sensors -calls class activity.)

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);    //top bar with title and subtitle
        setSupportActionBar(toolbar);

        streamView = (VideoView)findViewById(R.id.streamview);  //The import class VideoView previously called as variable streamView is in turn registered to the resource id streamview
        addrField = (EditText)findViewById(R.id.addr);   //The import class EditText previously called as variable addrField is in turn registered to the resource id addr
        btnConnect = (Button)findViewById(R.id.connect);   //The import class Button previously called as variable btnConnect is in turn registered to the resource id connect

        btnConnect.setOnClickListener(new OnClickListener() {   //Program is instructed to "listen" for a mouse click at button location

            @Override
            public void onClick(View URL1) {    //When Button has "heard" a click fulfill following
                String s = addrField.getEditableText().toString();    //
                playStream(s);  // where is it calling playStream?
            }
        });

    }



    private void playStream(String URL1) {
        Uri uri = Uri.parse("http://155.101.8.208:80/html/cam.jpg"); // Calling the URL to play
        if (uri == null) {
            Toast.makeText(Sensors.this,
                    "uri == null", Toast.LENGTH_LONG).show();
        }else{
            streamView.setVideoURI(uri);
            //mediaController.setAnchorView(streamView);
            streamView.setMediaController(mediaController);
            streamView.start();


            Toast.makeText(Sensors.this,
                    "Connect: " + "http://155.101.8.208:80/html/cam.jpg",
                    Toast.LENGTH_LONG).show();
        }
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        streamView.stopPlayback();
    }
}