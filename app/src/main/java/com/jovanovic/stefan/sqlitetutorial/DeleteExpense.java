package com.jovanovic.stefan.sqlitetutorial;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class DeleteExpense extends AppCompatActivity {
    TextView category, total_expense, notes;
    Button delete_button, btnCancel;

    Expense selectedExpense;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delete_expense);
        category= findViewById(R.id.category_2);
        total_expense = findViewById(R.id.total_expense_2);
        notes = findViewById(R.id.notes_2);
        delete_button = findViewById(R.id.delete_button_2);
        btnCancel = findViewById(R.id.btnCancel);
        getAndDisplayInfo_Expense();

        //Set actionbar title after getAndDisplayInfo_Trip method
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setTitle(selectedExpense.getCategory());
        }
        delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteAction_Expense();
            }

        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_CANCELED);
                DeleteExpense.this.finish();
            }
        });

    }

    void getAndDisplayInfo_Expense() {
        Intent intent = getIntent();
        selectedExpense = (Expense) intent.getSerializableExtra("selectedExpense");

        //display in textview
        category.setText(selectedExpense.getCategory());
        total_expense.setText(String.valueOf(selectedExpense.getTotal_expense()));
        notes.setText(selectedExpense.getNotes());

        //display in action bar
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setTitle(selectedExpense.getCategory());
        }
    }

    void deleteAction_Expense() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete " + selectedExpense.getCategory() + " ?");
        builder.setMessage("Are you sure you want to delete " + selectedExpense.getCategory() + " ?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                MyDatabaseHelper myDB = new MyDatabaseHelper(DeleteExpense.this);
                long result = myDB.deleteExpense(String.valueOf(selectedExpense.getId()));
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
    

}
