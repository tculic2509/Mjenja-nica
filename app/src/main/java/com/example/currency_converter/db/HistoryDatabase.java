package com.example.currency_converter.db;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.currency_converter.dao.Dao;
import com.example.currency_converter.entities.HistoryData;

import java.io.File;
import java.util.concurrent.Executors;

@Database(entities = {HistoryData.class}, exportSchema = false, version = 1)
public abstract class HistoryDatabase extends RoomDatabase {


    private static final String DATABASE_NAME = "db_history";
    private static HistoryDatabase historyDatabase;
    public static synchronized HistoryDatabase getInstance(final Context context){
        if(historyDatabase == null){
            historyDatabase = Room
                    .databaseBuilder(context.getApplicationContext(),HistoryDatabase.class ,DATABASE_NAME)

                    .addCallback(new Callback() {
                        @Override
                        public void onCreate(@NonNull SupportSQLiteDatabase db) {
                            super.onCreate(db);
                            Executors.newSingleThreadExecutor().execute(new Runnable() {
                                @Override
                                public void run() {
                                    getInstance(context).dao();

                                }
                            });
                        }
                    }).build();
        }
        return historyDatabase;
    }
    public abstract Dao dao();

}


