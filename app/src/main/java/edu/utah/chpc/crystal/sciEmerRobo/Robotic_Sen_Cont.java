package edu.utah.chpc.crystal.sciEmerRobo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import SciComs.Robot;
import edu.utah.chpc.crystal.sciEmerRobo.MovementDial.OnAngleChangedListener;
import edu.utah.chpc.crystal.test.R;


public class Robotic_Sen_Cont extends AppCompatActivity implements OnAngleChangedListener {

    //controller

    ImageButton mainmenu;

   // MovementDial movementDial;
    Button buttonConnect;
    //TextView textViewState, textViewRx;
    EditText message;
    float xCoord,yCoord,_theta;

    Robot emerRobo_Cont;
    String Ipaddr;
    int portNum, interval=1, histSize=100, thetaInt, speed = 0;
    double distanceTraveled;

    UDPHandler sciComs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       // message = (EditText) findViewById(R.id.message);
        //buttonConnect = (Button) findViewById(R.id.connect);
/*        textViewState = (TextView)findViewById(R.id.state);
        textViewRx = (TextView)findViewById(R.id.received);*/
      //  buttonConnect.setOnClickListener(buttonConnectOnClickListener);

       // final Video camera = new Video(this); TODO: Implement image feed under sensor readings

        LinearLayout rootLayout = new LinearLayout(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       // getSupportActionBar().setDisplayHomeAsUpEnabled(true);


       // rootLayout.addView(MenuL, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 0));


        final MovementDial Direction = new MovementDial(this);
        Direction.setOnAngleChangedListener(this);



        rootLayout.addView(Direction, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 0));
        setContentView(rootLayout);
       // setContentView(R.layout.activity_robotic__sen__cont);

     //  mainmenu = (ImageButton) findViewById(R.id.menuIcon);

      /* mainmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Robotic_Sen_Cont.this, MainActivity.class);
                startActivity(intent);
            }
        });*/
    }
/*    View.OnClickListener buttonConnectOnClickListener = new View.OnClickListener() {
*/

    public String getMessage() {
        return message.getText().toString();
    }

    @Override
    public void onResume() {
        super.onResume();

        Log.i("Touch", "Touch occurred");

        sciComs = new UDPHandler("155.101.8.219", 8080);

        Ipaddr = sciComs.getAddress();
        portNum = sciComs.getPortNo();

        sciComs.setAddress(Ipaddr);
        sciComs.setPort(portNum);

        sciComs.start();

        emerRobo_Cont = new Robot(Ipaddr, portNum, interval, histSize);

    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    public void xAndY_Axis(){
        MovementDial coords = new MovementDial(getApplicationContext());
            xCoord = coords.x;
            yCoord = coords.y;
            _theta = coords._theta;
            coords.setDistance(distanceTraveled);



    }

    /*
        Maps a value from one number range onto another @author : Aaron Pabst
        */

    // Drive is 0 forward, 90 left, 180 reverse, and 270 right.

        private int map(double x, double in_min, double in_max, double out_min, double out_max)
        {
            int mapVal = (int) ((int)(x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min);
            return mapVal;
        }


    @Override
    public void onAngleChanged(float theta) {


        if (theta > 22.5 && theta <= 67.5){   //theta is greater than 0 but less than 45
            thetaInt = 315;             // drive forward turning right

        }else if (theta > 67.5 && theta <= 112.5){
            thetaInt = 0;               // drive forward

        }else if (theta > 112.5 && theta <= 157.5){
            thetaInt = 45;              // drive forward turning left

        }else if (theta > 157.5 && theta <= 202.5){
            thetaInt = 90;              // turn right

        }else if (theta > 202.5 && theta <= 247.5){
            thetaInt = 135;             // drive reverse turning left

        }else if (theta > 247.5 && theta <= 292.5){
            thetaInt = 180;             // drive reverse

        }else if (theta > 292.5 && theta <= 337.5){
            thetaInt = 225;             // drive reverse turning right

        }else if (theta > 337.5 && theta <= 360 || theta > 0 && theta <= 22.5){
            thetaInt = 270;             // turning right

        } else {
            thetaInt= -1;               //error check
        }

        emerRobo_Cont.onTouch();
        emerRobo_Cont.drive(thetaInt, speed);

    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        emerRobo_Cont.kill();
        sciComs.kill();
    }

}

