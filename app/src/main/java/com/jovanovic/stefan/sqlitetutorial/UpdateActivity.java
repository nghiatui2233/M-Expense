package com.jovanovic.stefan.sqlitetutorial;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class UpdateActivity extends AppCompatActivity {

    EditText input_trip_name, date_start_input, date_end_input, place_from, place_to;
    Button update_button, delete_button;
    Trip selectedTrip;
    String risk ="Yes";
    Expense selectedExpense;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_trip);
        input_trip_name = findViewById(R.id.input_trip_name2);
        date_start_input = findViewById(R.id.date_start_input2);
        date_end_input = findViewById(R.id.date_end_input2);
        place_from = findViewById(R.id.place_from2);
        place_to = findViewById(R.id.place_to2);
        update_button = findViewById(R.id.update_button);
        delete_button = findViewById(R.id.delete_button);


        //First we call this
        getAndDisplayInfo_Trip();

        //Set actionbar title after getAndDisplayInfo_Trip method
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setTitle(selectedTrip.getTrip_name());
        }




        update_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyDatabaseHelper myDB = new MyDatabaseHelper(UpdateActivity.this);

                selectedTrip.setTrip_name(input_trip_name.getText().toString().trim());
                selectedTrip.setDate_start(date_start_input.getText().toString().trim());
                selectedTrip.setDate_end(date_end_input.getText().toString().trim());
                selectedTrip.setPlace_from(place_from.getText().toString().trim());
                selectedTrip.setPlace_to(place_to.getText().toString().trim());
                long result = myDB.updateTrip(selectedTrip,risk);
                if (result == -1) {
                    Toast.makeText(getBaseContext(), "Failed", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getBaseContext(), "Edit Successfully!", Toast.LENGTH_SHORT).show();
                }

            }
        });
        delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteAction_Trip();
            }
        });
        Calendar calendar= Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        date_start_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        UpdateActivity.this, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        month = month+1;
                        String date = day + "/" + month + "/" + year;
                        date_start_input.setText(date);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });
        date_end_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        UpdateActivity.this, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        month = month+1;
                        String date = day + "/" + month + "/" + year;
                        date_end_input.setText(date);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });

    }

     void getAndDisplayInfo_Trip() {
        Intent intent = getIntent();
        selectedTrip = (Trip) intent.getSerializableExtra("selectedTrip");

        //display in textview
        input_trip_name.setText(selectedTrip.getTrip_name());
        date_start_input.setText(selectedTrip.getDate_start());
        date_end_input.setText(selectedTrip.getDate_end());
        place_from.setText(selectedTrip.getPlace_from());
        place_to.setText(selectedTrip.getPlace_to());

        //display in action bar
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setTitle(selectedTrip.getTrip_name());
        }
    }

    void deleteAction_Trip() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete " + selectedTrip.getTrip_name() + " ?");
        builder.setMessage("Are you sure you want to delete " + selectedTrip.getTrip_name() + " ?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                MyDatabaseHelper myDB = new MyDatabaseHelper(UpdateActivity.this);
                long result = myDB.deleteTrip(Long.parseLong(String.valueOf(selectedTrip.getId())));
                if (result == -1) {
                    Toast.makeText(getBaseContext(), "Failed", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getBaseContext(), "Delete Successfully!", Toast.LENGTH_SHORT).show();
                }
                onBackPressed();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.create().show();
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();
        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.rY:
                if (checked)
                    risk="Yes";
                break;
            case R.id.rN:
                if (checked)
                    risk="No";
                break;
        }
    }
}
