package com.example.currency_converter;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.currency_converter.db.HistoryDatabase;
import com.example.currency_converter.entities.HistoryData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;

public class HistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private HistoryAdapter historyAdapter;
    private HistoryDatabase historyDatabase;
    private EditText datePickerEditText;
    private Button searchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        datePickerEditText = findViewById(R.id.date_picker);
        searchButton = findViewById(R.id.search_button);

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("Povijest");

        //back arrow
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        //prikazuje kalendar
        datePickerEditText.setOnClickListener(v -> showDatePickerDialog());

        //pretraživanje po datumu
        searchButton.setOnClickListener(v -> {
            String selectedDate = datePickerEditText.getText().toString();
            if (!selectedDate.isEmpty()) {
                searchByDate(selectedDate);
            } else {
                Toast.makeText(HistoryActivity.this, "Odaberi datum", Toast.LENGTH_SHORT).show();
            }
        });

        //asinkrono dohvaća podatke
        final Handler handler = new Handler();
        Executors.newSingleThreadExecutor()
                .execute(() -> {
                    historyDatabase = HistoryDatabase.getInstance(HistoryActivity.this);
                    List<HistoryData> historyDataList = historyDatabase.dao().getAllData();
                    handler.post(() -> display(historyDataList));
                });




    }

    private void searchByDate(String date) {


        final Handler handler = new Handler();
        Executors.newSingleThreadExecutor()
                .execute(() -> {
                    historyDatabase = HistoryDatabase.getInstance(HistoryActivity.this);
                    List<HistoryData> filteredData = historyDatabase.dao().getAllDataByDate(date);
                    if(filteredData.isEmpty()){

                        handler.post(() -> Toast.makeText(HistoryActivity.this, "Ne postoje podatci na taj datum", Toast.LENGTH_SHORT).show());

                    }else{
                        handler.post(() -> {
                        recyclerView = findViewById(R.id.recycler_view);
                        recyclerView.setLayoutManager(new LinearLayoutManager(this));
                        historyAdapter = new HistoryAdapter(this, filteredData);
                        recyclerView.setAdapter(historyAdapter);
                        });
                    }
                });
    }

    //pomoću ove metode se prikazuje kalendar i onda formatira datum
    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year1, month1, dayOfMonth) -> {
                    String selectedDate = dayOfMonth + "-" + (month1 + 1) + "-" + year1;
                    datePickerEditText.setText(selectedDate);
                }, year, month, day);

        datePickerDialog.show();
    }
    //prikaz podataka RecycleView-om koristeći LinearLayoutManager
    public void display(List<HistoryData> historyDataList){

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        historyAdapter = new HistoryAdapter(this, historyDataList);
        recyclerView.setAdapter(historyAdapter);
    }
}