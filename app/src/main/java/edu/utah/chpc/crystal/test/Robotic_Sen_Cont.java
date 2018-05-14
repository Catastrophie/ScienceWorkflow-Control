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

    MovementDial movementDial;
    Button buttonConnect;
    TextView textViewState, textViewRx;
    EditText message;
    float xCoord,yCoord,_theta;

    Robot emerRobo_Cont;
    EditText editTextAddress, editTextPort;

    UDPHandler udpHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        message = (EditText) findViewById(R.id.message);
        buttonConnect = (Button) findViewById(R.id.connect);
        textViewState = (TextView)findViewById(R.id.state);
        textViewRx = (TextView)findViewById(R.id.received);
      //  buttonConnect.setOnClickListener(buttonConnectOnClickListener);
        editTextAddress = (EditText) findViewById(R.id.address);
        editTextPort = (EditText) findViewById(R.id.port);




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

    private String Robot() {
        return "";
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




    /*
Mapping theta values
*/
  /*  public int mapTheta(float _theta){
        int theta;


        if(_theta > 0){
            theta = map(_theta,0,3.14,0,180);
            Log.i ("Touch", "Touch point 1 changed to: " + theta);
            return theta;

        }
        if(_theta < 0) {
            theta = map(_theta,0,-3.14,0,-180);
            theta = ((180 + theta) +180);
            Log.i ("Touch", "Touch point 2 changed to: " + theta);
            return theta;

            }
            return 0;
    }*/

    @Override
    public void onAngleChanged(float theta) {
       // int direction = mapTheta(theta);

        String dCommand = "DRIVE";
       // Robot() = dCommand + direction;

    }





    @Override
    protected void onDestroy() {
        super.onDestroy();
    }



}
