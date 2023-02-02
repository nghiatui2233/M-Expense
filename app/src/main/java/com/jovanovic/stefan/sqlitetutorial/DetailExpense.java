package com.jovanovic.stefan.sqlitetutorial;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class DetailExpense extends AppCompatActivity {
    TextView category, total_expense, notes;
    Button delete_button, btnCancel;
    ImageView ivShare;
    Expense selectedExpense;
    private int index = -1;
    ArrayList<String> listFile = new ArrayList<>();
    private static final String FILE_NAME = "jsonfile.txt";


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delete_expense);

        category= findViewById(R.id.category_2);
        total_expense = findViewById(R.id.total_expense_2);
        notes = findViewById(R.id.notes_2);
        delete_button = findViewById(R.id.delete_button_2);
        btnCancel = findViewById(R.id.btnCancel);
        ivShare = findViewById(R.id.ivShare);
        getAndDisplayInfo_Expense();


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1000);
        }


        //Set actionbar title after getAndDisplayInfo_Trip method
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setTitle(selectedExpense.getCategory());
        }

        ivShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listFile.clear();
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                String json = gson.toJson( selectedExpense);
                listFile.add(json);
                saveTextFile(String.valueOf(listFile));
                try {
                    ReadFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                setResult(RESULT_CANCELED);
                DetailExpense.this.finish();
            }
        });
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
                DetailExpense.this.finish();
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
                Database myDB = new Database(DetailExpense.this);
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

    private void ReadFile() throws IOException {
        FileInputStream fileInputStream = getApplicationContext().openFileInput(FILE_NAME);
        if (fileInputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String lineData = bufferedReader.readLine();
            while (lineData != null) {
                listFile.add(lineData);
                lineData = bufferedReader.readLine();
            }
        }
    }

    private void saveTextFile(String filename) {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(System.currentTimeMillis());
        try {
            File path = Environment.getExternalStorageDirectory();
            File dir = new File(path + "/Download/");
            dir.mkdirs();
            String fileName = "MyFile_" + timeStamp + ".txt";
            File file = new File(dir, fileName);
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(filename);
            bw.close();
            Toast.makeText(DetailExpense.this, "Download file Successfully!", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(DetailExpense.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1000:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(DetailExpense.this, "PERMISSION GRANTED!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(DetailExpense.this, "PERMISSION NOT GRANTED!", Toast.LENGTH_LONG).show();
                    finish();
                }
        }
    }
}
