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
    private final Context context;

//This is a constructor that takes in an array list of all the crypto currencies
    // and a context to wrap around the program
    public CryptoListAdapter(ArrayList<Crypto> cryptoArrayList, Context context) {
        CryptoListAdapter.cryptoArrayList = cryptoArrayList;
        this.context = context;
    }

    // This method filters the array by creating another filtered list to check for matching when typing in the search bar
    public static void filterList(ArrayList<Crypto> filteredList){
        cryptoArrayList = filteredList;

    }

    @NonNull
    @Override
    public CryptoListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.crypto_list, parent, false);
        return new CryptoListAdapter.ViewHolder(view);
    }

    // This method fills up the recycler view with the information of each crypto currency
    @Override
    public void onBindViewHolder(@NonNull CryptoListAdapter.ViewHolder holder, int position) {
        Crypto crypto = cryptoArrayList.get(position);
        holder.currencyName.setText(crypto.getCName());
        holder.currencySymbol.setText(crypto.getCSymbol());
        holder.currencyPrice.setText("$ " + cdf.format(crypto.getCPrice()));

    }

    // This method returns an integer total of the size of how many crypto currencies we have in the recycler view
    @Override
    public int getItemCount() {
        return cryptoArrayList.size();
    }

    // This method initializes the ViewHolder to store the crypto currencies inside text views.
    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView currencySymbol;
        private final TextView currencyName;
        private final TextView currencyPrice;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            currencySymbol = itemView.findViewById(R.id.currSymbol);
            currencyName = itemView.findViewById(R.id.currName);
            currencyPrice = itemView.findViewById(R.id.currPrice);
        }
    }
}
