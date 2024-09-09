package com.example.currency_converter;

//import static com.example.currency_converter.db.HistoryDatabase.historyDatabase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.currency_converter.db.HistoryDatabase;
import com.example.currency_converter.entities.HistoryData;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    String from = "";
    String to = "";
    Double rate = 0.0;
    private HistoryDatabase historyDatabase;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Spinner spinnerFromCurrency = findViewById(R.id.spinner_from_currency);
        Spinner spinnerToCurrency = findViewById(R.id.spinner_to_currency);
        Button buttonConvert = findViewById(R.id.button_convert);
        TextView textViewConvertedValue = findViewById(R.id.converted_value);
        EditText amount = findViewById(R.id.edit_text_amount);

        //header
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("Mjenjačnica");


        final Handler handler = new Handler();
        Executors.newSingleThreadExecutor()
                .execute(() -> {
                    historyDatabase = HistoryDatabase.getInstance(MainActivity.this);
                //asinkrono dohvaćanje iz baze
                });


        //dropdown za valute
        ArrayAdapter<CharSequence>adapter= ArrayAdapter.createFromResource(this, R.array.currencies, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinnerFromCurrency.setAdapter(adapter);
        spinnerToCurrency.setAdapter(adapter);
        spinnerToCurrency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String text = parent.getItemAtPosition(position).toString();
                to = text;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                final Handler handler = new Handler();
                handler.post(() -> Toast.makeText(MainActivity.this, "Niste odabrali nijednu valutu!!", Toast.LENGTH_SHORT).show());
            }
        });


        spinnerFromCurrency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            //u slucaju odabira valute poziva se ova metoda
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String text = parent.getItemAtPosition(position).toString();
                from = text;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                final Handler handler = new Handler();
                handler.post(() -> Toast.makeText(MainActivity.this, "Niste odabrali nijednu valutu!!", Toast.LENGTH_SHORT).show());
            }
        });

    }
    //prikaz menija u headeru
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.action_history) {
            Intent intent = new Intent(this, HistoryActivity.class);
            startActivity(intent);
            return true;
        } else {

            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        // Save the selected currency from and to values
        outState.putString("from_currency", from);
        outState.putString("to_currency", to);

        // Save the input amount if available
        EditText amount = findViewById(R.id.edit_text_amount);
        if (!amount.getText().toString().isEmpty()) {
            outState.putString("amount", amount.getText().toString());
        }

        // Save the converted rate if available
        outState.putDouble("rate", rate);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState != null) {
            // Restore the selected currency values
            from = savedInstanceState.getString("from_currency", "");
            to = savedInstanceState.getString("to_currency", "");

            // Restore the input amount
            EditText amount = findViewById(R.id.edit_text_amount);
            String savedAmount = savedInstanceState.getString("amount", "");
            if (!savedAmount.isEmpty()) {
                amount.setText(savedAmount);
            }

            // Restore the converted rate
            rate = savedInstanceState.getDouble("rate", 0.0);
        }
    }
    //konvertiranje valuta u EUR
    public double convertToEur(){
        EditText amount = findViewById(R.id.edit_text_amount);
        double value = Double.parseDouble(amount.getText().toString());
        double convertedValue = 0.0;

        switch (from){
            case "EUR":
                convertedValue = value;
                rate = 0.0;
                break;
            case "USD":
                convertedValue = value * 0.89;
                rate = 0.89;
                break;
            case "AUD":
                convertedValue = value * 0.61;
                rate = 0.61;
                break;
            case "CHF":
                convertedValue = value * 1.06;
                rate = 1.06;
                break;
            case "GBP":
                convertedValue = value * 1.19;
                rate = 1.19;
                break;
            case "CAD":
                convertedValue = value * 0.67;
                rate = 0.67;
                break;
            case "JPY":
                convertedValue = value * 0.0067;
                rate = 0.0067;
                break;
            case "SEK":
                convertedValue = value * 0.093;
                rate = 0.093;
                break;
            case "NOK":
                convertedValue = value * 0.096;
                rate = 0.096;
                break;
            case "CNY":
                convertedValue = value * 0.14;
                rate = 0.14;
                break;

        }
        return convertedValue;

    }
    //konvertiranje valuta iz EUR
    public double convertFromEur(){
        double convertedValue = convertToEur();
        double value = 0.0;
        switch (to){
            case "EUR":
                value = convertedValue;
                rate = 0.0;
                break;
            case "USD":
                value = convertedValue * 1.12;
                rate = 1.12;
                break;
            case "AUD":
                value = convertedValue * 1.65;
                rate = 1.65;
                break;
            case "CHF":
                value = convertedValue * 0.94;
                rate = 0.94;
                break;
            case "GBP":
                value = convertedValue * 0.84;
                rate = 0.84;
                break;
            case "CAD":
                value = convertedValue * 1.50;
                rate = 1.50;
                break;
            case "JPY":
                value = convertedValue * 130.0;
                rate = 130.0;
                break;
            case "SEK":
                value = convertedValue * 11.0;
                rate = 11.0;
                break;
            case "NOK":
                value = convertedValue * 11.5;
                rate = 11.5;
                break;
            case "CNY":
                value = convertedValue * 7.8;
                rate = 7.8;
                break;
        }
        return value;
    }

    //metoda za pretvorbu
    public void convert_action(View view) {

        EditText amount = findViewById(R.id.edit_text_amount);
        amount.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);


        TextView textViewConvertedValue = findViewById(R.id.converted_value);

        // Dohvati uneseni tekst
        String amountText = amount.getText().toString().trim();


        // Provjera da li je polje prazno
        if (amountText.isEmpty()) {
            Toast.makeText(this, "Unesite broj!!!", Toast.LENGTH_SHORT).show();
            return; // Prekini ako je polje prazno
        }
        // Provjera da li su valute jednake
        if (from.equals(to)) {
            Toast.makeText(this, "Promijenite valutu!!!", Toast.LENGTH_SHORT).show();
            return; // Prekini ako su valute jednake
        }
            // Pokušaj parsiranja unosa u double
            double amountValue = Double.parseDouble(amountText);

            // Provjera da li su varijable 'to' i 'from' ispravno postavljene
            if (to.equals(null) || from.equals(null)) {
                Toast.makeText(this, "Valute nisu pravilno postavljene!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (to.equals("EUR")) {
                double convertedValue = convertToEur();
                textViewConvertedValue.setText(String.format("%.3f", convertedValue));
                addDataInBackground(createDataLog(amountValue));
            } else if (to.equals(from)) {
                textViewConvertedValue.setText(String.format("%.3f", amountValue));
                addDataInBackground(createDataLog(amountValue));
            } else {
                double convertedValue = convertFromEur();
                textViewConvertedValue.setText(String.format("%.3f", Math.ceil(convertedValue * 1000) / 1000));
                addDataInBackground(createDataLog(amountValue));
            }
    }

    //pozadinska operacija koja dodaje u bazu podatke asinkrono
    private void addDataInBackground(HistoryData data){

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executorService.execute(() -> {
            try {
                historyDatabase.dao().insertData(data);
            }catch (Exception e){
                String message = "Error";
                handler.post(() -> Toast.makeText(this, message, Toast.LENGTH_SHORT).show());
            }
        });
    }
    //metoda koja sprema podatke u objekt, gdje se na frontendu ispisuje
    private HistoryData createDataLog(Double amount){

        SimpleDateFormat sdfDate = new SimpleDateFormat("d-M-yyyy");
        SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm:ss");
        String date = sdfDate.format(new Date());
        String time = sdfTime.format(new Date());

        HistoryData data = new HistoryData(amount, from, to, date, time, rate );
        return data;

    }

}
