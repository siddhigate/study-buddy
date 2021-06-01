package com.example.studybuddy;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Calendar;

public class Study extends AppCompatActivity {

    public DBManager dbManager;
    TextView studynow,todo,score,hours;
    ImageView todoimg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study);

        dbManager = new DBManager(Study.this);
        dbManager.open();

        todo=(TextView) findViewById(R.id.todo);
        score=(TextView) findViewById(R.id.score);
        hours=(TextView) findViewById(R.id.hours);
        studynow=(TextView)findViewById(R.id.btnstudynow);
        todoimg =(ImageView) findViewById(R.id.todoimg);

        updateValues();

        studynow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(MainActivity.this, "Clicked", Toast.LENGTH_SHORT).show();

                Intent i = new Intent(Study.this, StudyNow.class);
                startActivity(i);
            }
        });


        todo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                todo();
            }
        });


        todoimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                todo();
            }
        });
    }

    void todo(){

        DatabaseHelper dbh = new DatabaseHelper(this);
        Cursor c=dbh.getData();
        if(c.getCount()==0){
        Intent i = new Intent(Study.this, TodoEmpty.class);
        startActivity(i);
        }
        else{
            Intent i = new Intent(Study.this, ToDo.class);
            startActivity(i);

        }
    }

    void updateValues(){


        DBManager dbM = new DBManager(Study.this);
        dbM.open();

        float h = dbM.getHours();
        int s = dbM.getScore();

        String hh=String.valueOf(h);
        String ss=String.valueOf(s);

        hours.setText(hh);
        score.setText(ss);
    }

    @Override
    protected void onPause() {

        DBManager dbM = new DBManager(Study.this);
        dbM.open();

        float h = dbM.getHours();
        int s = dbM.getScore();

        String hh=String.valueOf(h);
        String ss=String.valueOf(s);

        hours.setText(hh);
        score.setText(ss);
        super.onPause();
    }

    @Override
    protected void onResume() {

        DBManager dbM = new DBManager(Study.this);
        dbM.open();

        float h = dbM.getHours();
        int s = dbM.getScore();

        if(h>60){

            String hh=String.valueOf(h);
            String ss=String.valueOf(s);

            hours.setText(hh);
            score.setText(ss);

        }
        else{

            String hh=String.valueOf(h);
            String ss=String.valueOf(s);

            hours.setText(hh);
            score.setText(ss);

            TextView t =(TextView) findViewById(R.id.texthours);
            t.setText("Number of Minutes");

        }

        super.onResume();
    }



// Calendar

    public void AddCalendarEvent(View view) {
        Calendar calendarEvent = Calendar.getInstance();
        Intent i = new Intent(Intent.ACTION_EDIT);
        i.setType("vnd.android.cursor.item/event");
        i.putExtra("beginTime", calendarEvent.getTimeInMillis());
        i.putExtra("allDay", true);
        i.putExtra("rule", "FREQ=YEARLY");
        i.putExtra("endTime", calendarEvent.getTimeInMillis() + 60 * 60 * 1000);
        i.putExtra("title", "Calendar Event");
        startActivity(i);
    }


}
