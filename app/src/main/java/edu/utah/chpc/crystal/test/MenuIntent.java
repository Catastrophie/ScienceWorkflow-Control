package edu.utah.chpc.crystal.test;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by crystal on 11/28/2017.
 */
public class MenuIntent extends View {

    View.OnClickListener onClickListener = null;

    public MenuIntent(Context context) {
        super(context);


       // setContentView(R.drawable.mainmenu);

        final Button MenuL = (Button) findViewById(R.menu.menu_main);
        MenuL.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            }
        });
    }

    public class OnClickListener {

    }


   //public MenuIntent(Robotic_Sen_Cont robotic_sen_cont) { //TODO:???
   // }

/*    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        });
    }*/
}
