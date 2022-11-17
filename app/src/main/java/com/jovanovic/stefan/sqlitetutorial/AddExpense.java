package com.jovanovic.stefan.sqlitetutorial;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddExpense extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    EditText total_expense, input_notes, date_time;
    Button add_button, Btn_cancel;
    String category="None";
    Trip trip;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_expense);


        total_expense = findViewById(R.id.total_expense);
        date_time = findViewById(R.id.date_time);
        input_notes = findViewById(R.id.input_notes);
        add_button = findViewById(R.id.add_button);
        Btn_cancel = findViewById(R.id.btnCancel);

        Spinner spinner = (Spinner) findViewById(R.id.input_category);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.category, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        Intent intent=getIntent();
        Integer trip_id  = intent.getIntExtra("id",1);

        date_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimeDialog(date_time);
            }

            private void showDateTimeDialog(EditText date_time) {
                Calendar calendar =Calendar.getInstance();
                DatePickerDialog.OnDateSetListener dateSetListener= new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dateOfMonth) {
                        calendar.set(Calendar.YEAR,year);
                        calendar.set(Calendar.MONTH,month);
                        calendar.set(Calendar.DAY_OF_MONTH,dateOfMonth);

                        TimePickerDialog.OnTimeSetListener timeSetListener=new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                calendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
                                calendar.set(Calendar.MINUTE,minute);

                                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm");
                                date_time.setText(simpleDateFormat.format(calendar.getTime()));
                            }
                        };
                        new TimePickerDialog(AddExpense.this,timeSetListener,calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),false).show();
                    }
                };
                new DatePickerDialog(AddExpense.this,dateSetListener,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });



        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyDatabaseHelper myDB = new MyDatabaseHelper(AddExpense.this);
                Expense expense = new Expense();
                expense.setTotal_expense(Integer.parseInt(total_expense.getText().toString().trim()));
                expense.setUsed_time(date_time.getText().toString().trim());
                expense.setNotes(input_notes.getText().toString().trim());
                long result = myDB.addExpense(expense,category, trip_id);
                validate();
                if (result == -1 && category.equals("None")) {
                    Toast.makeText(getBaseContext(), "Failed", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AddExpense.this, "Added Successfully!", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_CANCELED);
                    AddExpense.this.finish();
                }
            }
            private void validate() {
                if (total_expense.getText().length() == 0 || input_notes.getText().length() == 0 ) {
                    Toast.makeText(getApplicationContext(), "You must enter all the information", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_CANCELED);
                AddExpense.this.finish();
            }
        });



    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        category = adapterView.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
