package com.example.currency_converter.dao;


import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.currency_converter.entities.HistoryData;

import java.util.List;

@androidx.room.Dao
public interface Dao {
    @Query("select * from historyData")
    List<HistoryData> getAllData();

    @Query("select * from historyData where date = :date")
    List<HistoryData> getAllDataByDate(String date);

    @Insert
    void insertData(HistoryData data);

    @Delete
    void deleteData(HistoryData data);

    @Query("delete from historyData where id = :id")
    void delete(int id);
}
