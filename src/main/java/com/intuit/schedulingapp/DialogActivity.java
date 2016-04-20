package com.intuit.schedulingapp;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;

public class DialogActivity extends AppCompatActivity {
    final Context context = this;
    Calendar mcurrentTime;
    String SelectedTime;
    Calendar newTime;
    int waitMinutes;
    int selectedhour,selectedminute;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.prompts, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final EditText userInput = (EditText) promptsView
                .findViewById(R.id.editTextDialogUserInput);

        userInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mcurrentTime = Calendar.getInstance();
                Log.d("current time",mcurrentTime.getTime()+"");
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);

                TimePickerDialog mTimePicker;

                mTimePicker = new TimePickerDialog(DialogActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        selectedhour=selectedHour;
                        selectedminute=selectedMinute;
                        mcurrentTime.add(Calendar.MINUTE, selectedMinute);
                        mcurrentTime.add(Calendar.HOUR, selectedMinute);
                        userInput.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                newTime=Calendar.getInstance();
                waitMinutes= (selectedhour-hour)*60+Math.abs(selectedminute-minute);


                newTime.setTime(mcurrentTime.getTime());
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // get user input and set it to result
                                // edit text
                                Date d = new Date();
                                d.setHours(Integer.parseInt(userInput.getText().toString().split(":")[0]));
                                d.setMinutes(Integer.parseInt(userInput.getText().toString().split(":")[1]));
                                newTime.setTime(d);
                                Log.d("new time", newTime.getTime() + "");

                                TimeService.NOTIFY_INTERVAL=waitMinutes;
                                Intent scheduledTask=new Intent(getApplicationContext(), TimeService.class);
                                scheduledTask.putExtra("Time",waitMinutes);
                                TimeService.scheduled_Date=newTime.getTime();
                                Log.d("scheduledDate",newTime.getTime().toString());
                                startService(scheduledTask);
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();

    }

}
