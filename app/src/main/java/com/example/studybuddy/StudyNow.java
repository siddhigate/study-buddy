package com.example.studybuddy;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import ticker.views.com.ticker.widgets.circular.timer.callbacks.CircularViewCallback;
import ticker.views.com.ticker.widgets.circular.timer.view.CircularView;

public class StudyNow extends AppCompatActivity {

    //Variables

    int TIMERFINISH=1;
    DBManager dbManager;

    Study study;
    static final int CUSTOM_DIALOG_ID = 0;

    CircularView.OptionsBuilder builderWithTimer;
    TextView customDialog_TextView, nextsession;
    EditText customDialog_EditText;
    Button customDialog_Update, customDialog_Dismiss;

    String result = "";
    TextView textReturned;
    TextView time;
    Context context = this;

    CircularView circularViewWithTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_now);

        dbManager = new DBManager(this);
        dbManager.open();

        //Initializing
        time =(TextView) findViewById(R.id.time);
        nextsession=(TextView) findViewById(R.id.btnnextsession) ;
        circularViewWithTimer = findViewById(R.id.circular_view);

        // timer
        builderWithTimer =
                new CircularView.OptionsBuilder()
                        .shouldDisplayText(true)
                        .setCounterInSeconds(10)
                        .setCircularViewCallback(new CircularViewCallback() {

                            public void onTimerFinish() {

                                TIMERFINISH=1;
                                // Will be called if times up of countdown timer
                                //Toast.makeText(StudyNow.this, "CircularCallback: Timer Finished ", Toast.LENGTH_SHORT).show();


                                AlertDialog.Builder alertDialog = new AlertDialog.Builder(StudyNow.this);
                                alertDialog.setMessage("You studided without getting distracted by the phone and with complete focus. 100 POINTS ARE AWARDED TO YOU");
                                AlertDialog alert = alertDialog.create();
                                alert.show();

                                updateData(Integer.parseInt(time.getText().toString()),100);
        //                        study.updateValues();


                            }

                            public void onTimerCancelled() {
                                TIMERFINISH=1;

                                // Will be called if stopTimer is called
                                //Toast.makeText(StudyNow.this, "CircularCallback: Timer Cancelled ", Toast.LENGTH_SHORT).show();


                                AlertDialog.Builder alertDialog = new AlertDialog.Builder(StudyNow.this);
                                alertDialog.setMessage("You left your work in the middle. NO POINTS WILL BE GIVEN TO YOU");
                                AlertDialog alert = alertDialog.create();
                                alert.show();

                                updateData(0,0);
        //                        study.updateValues();
                            }
                        });
        builderWithTimer.setCounterInSeconds(30);


        //Dialog set time

        textReturned = (TextView)findViewById(R.id.time);

        Button buttonStartDialog = (Button)findViewById(R.id.setTime);
        buttonStartDialog.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                showDialog(CUSTOM_DIALOG_ID);
            }});

        nextsession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(CUSTOM_DIALOG_ID);
            }
        });

    }

    //Dialog operations
    private Button.OnClickListener customDialog_UpdateOnClickListener
            = new Button.OnClickListener(){

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            //       customDialog_TextView.setText(customDialog_EditText.getText().toString());

            result = customDialog_EditText.getText().toString();
            time.setText(result);

int numeric=0;
            String timestring = time.getText().toString();
            if(time != null){
                try{
                     Integer.parseInt(timestring);
                     numeric=1;
                }
                catch (Exception e){

                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(StudyNow.this);
                    alertDialog.setMessage("GIVE NUMBER AS AN INPUT");
                    AlertDialog alert = alertDialog.create();
                    alert.show();
                    numeric=0;
                }
                if(numeric==1) {
                    int minutes = Integer.parseInt(timestring);
                    if(minutes>0) {
                        long seconds = minutes * 60;
                        builderWithTimer.setCounterInSeconds(seconds);
                    }
                    else{

                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(StudyNow.this);
                        alertDialog.setMessage("GIVE POSITIVE NUMBER AS AN INPUT");
                        AlertDialog alert = alertDialog.create();
                        alert.show();
                    }
                    }
            }
            circularViewWithTimer.setOptions(builderWithTimer);

            dismissDialog(CUSTOM_DIALOG_ID);
        }
    };


    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        Dialog dialog = null;;

        switch(id) {
            case CUSTOM_DIALOG_ID:
                dialog = new Dialog(StudyNow.this);

                dialog.setContentView(R.layout.customlayout);
                dialog.setTitle("Custom Dialog");

                customDialog_EditText = (EditText)dialog.findViewById(R.id.dialogedittext);

                customDialog_Update = (Button)dialog.findViewById(R.id.dialogupdate);

                customDialog_Update.setOnClickListener(customDialog_UpdateOnClickListener);

                break;
        }
        return dialog;
    }


    // Timer operations
    public void btn_start(View view) {
        circularViewWithTimer.startTimer();
        TIMERFINISH=0;

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(StudyNow.this);
        alertDialog.setMessage("You left the timer. You didn't focus on your work. 100 POINTS ARE DEDUCTED");
//        alertDialog.setMessage("Once you start don't leave this application or this screen. It will result in deduction of score. All the Best");
        AlertDialog alert = alertDialog.create();
        alert.show();

    }

    public void btn_stop(View view) {
        circularViewWithTimer.stopTimer();

    }

    public void btn_pause(View view) {
        circularViewWithTimer.pauseTimer();
    }

    public void btn_resume(View view) {
        circularViewWithTimer.resumeTimer();
    }

    // If the student leaves the app or the timer this will get invoked
    // This implies that the student is using the phone
    // And not focusing on his/her studies
    @Override
    protected void onPause() {

        if(TIMERFINISH!=1) {
            circularViewWithTimer.stopTimer();

            updateData(0, -100);

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(StudyNow.this);
            alertDialog.setMessage("You left the timer. You didn't focus on your work. 100 POINTS ARE DEDUCTED");
            AlertDialog alert = alertDialog.create();
            alert.show();
        }
        super.onPause();

    }

    void updateData(float hours,int score){
      dbManager.insertDailyStatus(hours,score);
    }
}
