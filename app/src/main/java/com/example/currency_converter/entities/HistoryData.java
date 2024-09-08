package com.example.currency_converter.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "historyData")
public class HistoryData {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private Integer id;
    @NonNull
    private Double amount;
    @NonNull
    private String fromCurrency;
    @NonNull
    private String toCurrency;
    @NonNull
    private String date;
    @NonNull
    private String time;
    @NonNull
    private Double rate;

    public HistoryData(@NonNull Double amount, @NonNull String fromCurrency, @NonNull String toCurrency,
                       @NonNull String date, @NonNull String time, @NonNull Double rate) {
        this.amount = amount;
        this.fromCurrency = fromCurrency;
        this.toCurrency = toCurrency;
        this.date = date;
        this.time = time;
        this.rate = rate;
    }

    @NonNull
    public Integer getId() {
        return id;
    }

    public void setId(@NonNull Integer id) {
        this.id = id;
    }

    @NonNull
    public Double getAmount() {
        return amount;
    }

    public void setAmount(@NonNull Double amount) {
        this.amount = amount;
    }

    @NonNull
    public String getFromCurrency() {
        return fromCurrency;
    }

    public void setFromCurrency(@NonNull String fromCurrency) {
        this.fromCurrency = fromCurrency;
    }

    @NonNull
    public String getToCurrency() {
        return toCurrency;
    }

    public void setToCurrency(@NonNull String toCurrency) {
        this.toCurrency = toCurrency;
    }

    @NonNull
    public String getDate() {
        return date;
    }

    public void setDate(@NonNull String date) {
        this.date = date;
    }

    @NonNull
    public Double getRate() {
        return rate;
    }

    public void setRate(@NonNull Double rate) {
        this.rate = rate;
    }

    @NonNull
    public String getTime() {
        return time;
    }

    public void setTime(@NonNull String time) {
        this.time = time;
    }
}
