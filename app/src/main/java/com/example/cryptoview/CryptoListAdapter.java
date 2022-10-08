package com.example.cryptoview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.ArrayList;

//in this class we set the data of each item in our recycler view.
public class CryptoListAdapter extends RecyclerView.Adapter<CryptoListAdapter.ViewHolder> {
    private static final DecimalFormat cdf = new DecimalFormat( "#.##");
    private static ArrayList<Crypto> cryptoArrayList;
    private Context context;


    public CryptoListAdapter(ArrayList<Crypto> cryptoArrayList, Context context) {
        this.cryptoArrayList = cryptoArrayList;
        this.context = context;
    }
    public static void filterList(ArrayList<Crypto> filteredList){
        cryptoArrayList = filteredList;

    }

    @NonNull
    @Override
    public CryptoListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.crypto_list, parent, false);
        return new CryptoListAdapter.ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull CryptoListAdapter.ViewHolder holder, int position) {
        Crypto crypto = cryptoArrayList.get(position);
        holder.currencyName.setText(crypto.getCName());
        holder.currencySymbol.setText(crypto.getCSymbol());
        holder.currencyPrice.setText("$ " + cdf.format(crypto.getCPrice()));

    }

    @Override
    public int getItemCount() {
        return cryptoArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private  TextView currencySymbol;
        private TextView currencyName;
        private TextView currencyPrice;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            currencySymbol = itemView.findViewById(R.id.currSymbol);
            currencyName = itemView.findViewById(R.id.currName);
            currencyPrice = itemView.findViewById(R.id.currPrice);
        }
    }
}
