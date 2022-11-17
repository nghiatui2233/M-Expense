package com.jovanovic.stefan.sqlitetutorial;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Detail extends AppCompatActivity {
    TextView tvTripDt, input_trip_name, date_start_input, date_end_input, place_from, place_to;
    TextView expense_input, no_data;
    ImageView empty_imageview;
    Button add_button;
    Trip selectedTrip;
    List<Expense> expenses;
    RecyclerView rcvExpense;
    MyDatabaseHelper myDB;
    ExpenseAdapter expenseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_trip);

        tvTripDt = findViewById(R.id.tvTripDt);
        input_trip_name = findViewById(R.id.tvTripNameDt);
        date_start_input = findViewById(R.id.dateStartDt);
        date_end_input = findViewById(R.id.dateEndDt);
        place_from = findViewById(R.id.tvPlaceFromDt);
        place_to = findViewById(R.id.tvPlaceToDt);
        expense_input = findViewById(R.id.tvTotalEx);
        rcvExpense = findViewById(R.id.rcvExpense);
        empty_imageview = findViewById(R.id.empty_imageview);
        no_data = findViewById(R.id.no_data);
        add_button = findViewById(R.id.btnAddExpenseDt);



        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Detail.this, AddExpense.class);
                intent.putExtra("id",selectedTrip.getId());
                startActivity(intent);
            }
        });

        myDB = new MyDatabaseHelper(Detail.this);
        Intent intent=getIntent();
        selectedTrip = (Trip) intent.getSerializableExtra("trip");


        expenses = new ArrayList<>();

        displayOrNot();
        //First we call this
        getAndDisplayInfo_Trip();



        expenseAdapter = new ExpenseAdapter(Detail.this,this, expenses);
        rcvExpense.setAdapter(expenseAdapter);
        rcvExpense.setLayoutManager(new LinearLayoutManager(Detail.this));
    }

    private void displayOrNot() {
        expenses = myDB.getListExpenseByTripID(selectedTrip.getId());
        if(expenses.size() == 0){
            empty_imageview.setVisibility(View.VISIBLE);
            no_data.setVisibility(View.VISIBLE);
        }else{
            empty_imageview.setVisibility(View.GONE);
            no_data.setVisibility(View.GONE);
        }
    }


    public void getAndDisplayInfo_Trip() {
        Intent intent = getIntent();
        selectedTrip = (Trip) intent.getSerializableExtra("trip");

        //display in textview
        tvTripDt.setText(String.valueOf(selectedTrip.getId()));
        input_trip_name.setText(selectedTrip.getTrip_name());
        date_start_input.setText(selectedTrip.getDate_start());
        date_end_input.setText(selectedTrip.getDate_end());
        place_from.setText(selectedTrip.getPlace_from());
        place_to.setText(selectedTrip.getPlace_to());
        expense_input.setText(String.valueOf(myDB.getTotalExpense(selectedTrip.getId())));

        //display in action bar
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setTitle(selectedTrip.getTrip_name());
        }
    }

}
