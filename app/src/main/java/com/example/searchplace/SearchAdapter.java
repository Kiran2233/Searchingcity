package com.example.searchplace;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.libraries.places.api.model.AutocompletePrediction;

import java.util.List;


public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.MyHolder> {
    private List<AutocompletePrediction> resultsList;
    private Context context;

    public SearchAdapter(List<AutocompletePrediction> resultsList, Context context) {
        this.resultsList = resultsList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_row,viewGroup,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(MyHolder viewHolder, int i) {
        viewHolder.textView.setText(resultsList.get(i).getPrimaryText(null));
    }

    @Override
    public int getItemCount() {
        return resultsList.size();
    }
    public static class  MyHolder extends RecyclerView.ViewHolder {
        TextView textView;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.address);
        }
    }
}
