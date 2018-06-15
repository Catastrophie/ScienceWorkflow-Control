package edu.utah.chpc.crystal.test;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import scicoms.Robot;


public class Robotic_Sen_Cont extends AppCompatActivity implements MovementDial.OnAngleChangedListener {

    ImageButton mainmenu;

   // MovementDial movementDial;
    Button buttonConnect;
    //TextView textViewState, textViewRx;
    EditText message;
    float xCoord,yCoord,_theta;

    Robot emerRobo_Cont;
    String Ipaddr;
    int portNum,interval=1, histSize=1;

    UDPHandler sciComs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        message = (EditText) findViewById(R.id.message);
        buttonConnect = (Button) findViewById(R.id.connect);
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




/*    public void getUDPHandler(String addr, int port) {
        UDPHandler connect = new UDPHandler(addr, port);
        this.Ipaddr = connect.getAddress();
        this.portNum = connect.getPortNo();

    }*/


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


    }
/*
    public void sciComsValues(){
         //TODO: is this legal?

        sciComs.setAddress("155.101.8.219");
        sciComs.setPort(8080);
    }
*/


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
        int thetaInt;
        int speed = 0;

        if (theta > 0 && theta <= 45){   //theta is greater than 0 but less than 45
            thetaInt = 315;             // drive forward turning right

        }else if (theta > 45 && theta <= 90){
            thetaInt = 0;               // drive forward

        }else if (theta > 90 && theta <= 135){
            thetaInt = 45;              // drive forward turning left

        }else if (theta > 135 && theta <= 180){
            thetaInt = 90;              // turn right

        }else if (theta > 180 && theta <= 225){
            thetaInt = 135;             // drive reverse turning left

        }else if (theta > 225 && theta <= 270){
            thetaInt = 180;             // drive reverse

        }else if (theta > 270 && theta <= 315){
            thetaInt = 225;             // drive reverse turning right

        }else if (theta > 315 && theta <= 360){
            thetaInt = 270;             // turning right

        } else {
            thetaInt= -1;
        }

        //TODO: Why isn't Robot call working?
        sciComs = new UDPHandler("155.101.8.219", 8080);

        Ipaddr = sciComs.getAddress();
        portNum = sciComs.getPortNo();

        sciComs.setAddress(Ipaddr);
        sciComs.setPort(portNum);

        sciComs.start();


        emerRobo_Cont = new Robot(Ipaddr, portNum, interval, histSize);

       // sciComs.send("DRIVE " + thetaInt + " " + speed);
       // sciCom.send("PAN " + xCoord + " " + yCoord);
/*        sciComs.sendReceive("SENSOR_LIST", new UDPResponseHandler() {
            @Override
            public void handler(String response) {
                System.out.println(response);
            }
        });*/

        emerRobo_Cont.drive(thetaInt, speed);

    }


/*    //???? TODO:
    private abstract class UDPDataReceive implements UDPHandler.UDPDataReceiveHandler{

        @Override
        public void recieveHandler(String handOff) {
            System.out.println(handOff);

        }

    }*/



    @Override
    protected void onDestroy() {
        super.onDestroy();
        emerRobo_Cont.kill();
        sciComs.kill();
    }



}
