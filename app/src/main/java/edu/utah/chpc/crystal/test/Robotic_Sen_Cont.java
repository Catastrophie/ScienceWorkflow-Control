package edu.utah.chpc.crystal.test;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Robotic_Sen_Cont extends AppCompatActivity implements MovementDial.OnAngleChangedListener {

    ImageButton mainmenu;

    EditText editTextAddress, editTextPort;
    Button buttonConnect;
    TextView textViewState, textViewRx;
    EditText message;
    UDPHandler udpHandler;
    UDPThread udpThread;

/**/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        message = (EditText) findViewById(R.id.message);
        editTextAddress = (EditText) findViewById(R.id.address);
        editTextPort = (EditText) findViewById(R.id.port);
        buttonConnect = (Button) findViewById(R.id.connect);
        textViewState = (TextView)findViewById(R.id.state);
        textViewRx = (TextView)findViewById(R.id.received);

        buttonConnect.setOnClickListener(buttonConnectOnClickListener);

       udpHandler = new UDPHandler(this);

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
    View.OnClickListener buttonConnectOnClickListener =
            new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {

                    udpThread = new UDPThread(
                            editTextAddress.getText().toString(),
                            Integer.parseInt(editTextPort.getText().toString()),
                            udpHandler,
                            getMessage());
                    udpThread.start();

                    buttonConnect.setEnabled(false);
                }
            };
    private void updateState(String state){
        textViewState.setText(state);
    }

    private void updateRxMsg(String rxmsg){
        textViewRx.append(rxmsg + "\n");
    }

    private void clientEnd(){
        udpThread = null;
        textViewState.setText("clientEnd()");
        buttonConnect.setEnabled(true);

    }

    public String getMessage() {
        return message.getText().toString();
    }
    public static class UDPHandler extends Handler {
        public static final int UPDATE_STATE = 0;
        public static final int UPDATE_MSG = 1;
        public static final int UPDATE_END = 2;
        private MainActivity parent;

        public UDPHandler(Robotic_Sen_Cont parent) {
            super();
            this.parent = parent;
        }

        @Override
        public void handleMessage(Message msg) {

            switch (msg.what){
                case UPDATE_STATE:
                    parent.updateState((String)msg.obj);
                    break;
                case UPDATE_MSG:
                    parent.updateRxMsg((String)msg.obj);
                    break;
                case UPDATE_END:
                    parent.clientEnd();
                    break;
                default:
                    super.handleMessage(msg);
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

        /*
    Maps a value from one number range onto another @author : Aaron Pabst
    */
        private int map(double x, double in_min, double in_max, double out_min, double out_max)
        {
            int mapVal = (int) ((int)(x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min);
            return mapVal;
        }

    /*
    Mapping theta values
     */
        public int mapTheta(int _theta) {

            int theta;

            if (_theta > 0){
                theta = map(_theta,0,3.14,0,180);

                return theta;
            } else {
                theta = map(_theta,0,-3.14,0,-180);
                theta = ((180 + theta) +180);
                Log.i ("Touch", "Touch point changed to: " + theta);
                return theta;

            }

        }
    @Override
    public void onAngleChanged(int theta) {
        int direction = theta; //conversion on theta for volume
        direction = mapTheta(theta);

    }





    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}
