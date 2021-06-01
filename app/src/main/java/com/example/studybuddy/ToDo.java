package com.example.studybuddy;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import ticker.views.com.ticker.widgets.circular.timer.view.CircularView;

public class ToDo extends AppCompatActivity {

    private static final String TAG = "ToDoActivity";
    DatabaseHelper mDatabaseHelper;
    private ListView mListView;
    Button btnAdd;
    TextView addtask;
    EditText editText;
    TextView customDialog_TextView ;
    EditText customDialog_EditText;
    Button customDialog_Update;
    static final int CUSTOM_DIALOG_ID = 0;
    Button ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do);
ref=(Button) findViewById(R.id.btnSave);
ref.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        displaytasks();
    }
});
        mListView = (ListView) findViewById(R.id.listView);
        mDatabaseHelper = new DatabaseHelper(this);
        addtask=(TextView) findViewById(R.id.addtask);
        btnAdd = (Button) findViewById(R.id.btnplus);

        displaytasks();
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDialog(CUSTOM_DIALOG_ID);

                if (addtask.length() != 0) {
                   // AddData();
                    String newEntry = addtask.getText().toString();
                    boolean insertData = mDatabaseHelper.addData(newEntry);
                    displaytasks();
                   /* if (insertData) {
                        Toast.makeText(ToDo.this,"Yes",Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(ToDo.this,"No",Toast.LENGTH_LONG).show();
                    }*/
                } else {

               }
            }
        });


    }


    void AddData(){


        String newEntry = addtask.getText().toString();
        boolean insertData = mDatabaseHelper.addData(newEntry);

    }
    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        Dialog dialog = null;;

        switch(id) {
            case CUSTOM_DIALOG_ID:
                dialog = new Dialog(ToDo.this);

                dialog.setContentView(R.layout.customtodoadd);
                dialog.setTitle("Custom Dialog");

                customDialog_EditText = (EditText)dialog.findViewById(R.id.dialogedittext);

                customDialog_Update = (Button)dialog.findViewById(R.id.dialogupdate);

                customDialog_Update.setOnClickListener(customDialog_UpdateOnClickListener);

                break;
        }
        return dialog;
    }
    private Button.OnClickListener customDialog_UpdateOnClickListener
            = new Button.OnClickListener(){

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            //       customDialog_TextView.setText(customDialog_EditText.getText().toString());
            displaytasks();
            String result = customDialog_EditText.getText().toString();
            addtask.setText(result);


            dismissDialog(CUSTOM_DIALOG_ID);
        }
    };

    void displaytasks(){

        //get the data and append to a list
        Cursor data = mDatabaseHelper.getData();
        ArrayList<String> listData = new ArrayList<>();
        while(data.moveToNext()){
            //get the value from the database in column 1
            //then add it to the ArrayList
            listData.add(data.getString(1));
        }
        //create the list adapter and set the adapter
        ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listData);
        mListView.setAdapter(adapter);

        //set an onItemClickListener to the ListView
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String name = adapterView.getItemAtPosition(i).toString();
                Log.d(TAG, "onItemClick: You Clicked on " + name);

                Cursor data = mDatabaseHelper.getItemID(name); //get the id associated with that name
                int itemID = -1;
                while(data.moveToNext()){
                    itemID = data.getInt(0);
                }
                if(itemID > -1){
                  Log.d(TAG, "onItemClick: The ID is: " + itemID);
                    Intent editScreenIntent = new Intent(ToDo.this, EditDataActivity.class);
                     editScreenIntent.putExtra("id",itemID);
                    editScreenIntent.putExtra("name",name);
                    startActivity(editScreenIntent);

                }
                else{
                    Toast.makeText(ToDo.this,"No ID associated with that name",Toast.LENGTH_LONG).show();
                }
            }
        });
    }


}
