package com.example.currency_converter;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.currency_converter.db.HistoryDatabase;
import com.example.currency_converter.entities.HistoryData;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    private List<HistoryData> historyDataList;
    private Context context;
    private HistoryDatabase db;

    public HistoryAdapter(Context context, List<HistoryData> historyDataList) {
        this.context = context;
        this.historyDataList = historyDataList;
        db = HistoryDatabase.getInstance(context);
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_row, parent, false);
        return new HistoryViewHolder(view);
    }
    public static class HistoryViewHolder extends RecyclerView.ViewHolder {
        TextView amountTextView;
        TextView fromTextView;
        TextView toTextView;
        TextView rateTextView;
        TextView dateTextView;


        Button deleteButton;

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            amountTextView = itemView.findViewById(R.id.amount);
            fromTextView = itemView.findViewById(R.id.from);
            toTextView = itemView.findViewById(R.id.to);
            rateTextView = itemView.findViewById(R.id.rate);
            dateTextView = itemView.findViewById(R.id.date);

            deleteButton = itemView.findViewById(R.id.delete_button);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        HistoryData currentHistoryData = historyDataList.get(position);
        holder.amountTextView.setText(String.valueOf(currentHistoryData.getAmount()));
        holder.fromTextView.setText(currentHistoryData.getFromCurrency());
        holder.toTextView.setText(currentHistoryData.getToCurrency());
        holder.rateTextView.setText(String.valueOf(currentHistoryData.getRate()));
        holder.dateTextView.setText(currentHistoryData.getDate());
        holder.deleteButton.setOnClickListener(v -> {
            delete(currentHistoryData.getId());
            historyDataList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, historyDataList.size());
        });
    }

    private void delete(int id){

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Handler handler = new Handler();
        executorService.execute(() -> {
            db.dao().delete(id);
            handler.post(() -> Toast.makeText( context, "Data deleted", Toast.LENGTH_SHORT).show());
        });
    }

    @Override
    public int getItemCount() {
        return historyDataList.size();
    }


}

