package edu.utah.chpc.crystal.test;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class Robotic_Sen_Cont extends AppCompatActivity implements MovementDial.OnAngleChangedListener {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout rootLayout = new LinearLayout(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

      //  getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final MovementDial Direction = new MovementDial(this);
        Direction.setOnAngleChangedListener(this);
        rootLayout.addView(Direction, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 0));

        setContentView(R.layout.activity_robotic__sen__cont);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onAngleChanged(float theta) {
        float volume = theta; //conversion on theta for volume
        Log.i ("TAG", "Volume changed to: " + volume);
    }
}
