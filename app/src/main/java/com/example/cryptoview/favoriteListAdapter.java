package com.example.cryptoview;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class favoriteListAdapter extends RecyclerView.Adapter<favoriteListAdapter.ViewHolder>
{
    private static final DecimalFormat cdf = new DecimalFormat( "#.##");
    private static ArrayList<FavItem> favCryptoArrayList;
    private final Context context;
    private FavDB favDB;

    //This is a constructor that takes in an array list of all the crypto currencies
    // and a context to wrap around the program
    public favoriteListAdapter(ArrayList<FavItem> cryptoArrayList, Context context) {
        favoriteListAdapter.favCryptoArrayList = cryptoArrayList;
        this.context = context;
    }


    // This method filters the array by creating another filtered list to check for matching when typing in the search bar
    public static void filterList(ArrayList<FavItem> filteredList){
        favCryptoArrayList = filteredList;
    }


    @NonNull
    @Override
    public favoriteListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.crypto_list, parent, false);
        favDB = new FavDB(context);

        return new favoriteListAdapter.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull favoriteListAdapter.ViewHolder holder, int position) {
        FavItem favItem = favCryptoArrayList.get(position);
        holder.currencyName.setText(favItem.getCName());
        holder.currencySymbol.setText(favItem.getCSymbol());
        holder.currencyPrice.setText("$ " + cdf.format(favItem.getCPrice()));
    }

    @Override
    public int getItemCount() {
        return favCryptoArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView currencySymbol;
        private final TextView currencyName;
        private final TextView currencyPrice;
        private final Button favBtn;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            currencySymbol = itemView.findViewById(R.id.currSymbol);
            currencyName = itemView.findViewById(R.id.currName);
            currencyPrice = itemView.findViewById(R.id.currPrice);
            favBtn = itemView.findViewById(R.id.favBtn);

            //add to fav btn
            favBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    final FavItem crypto = favCryptoArrayList.get(position);
                    favDB.remove_fav(crypto.getCName());
                    removeItem(position);
                }
            });


        }
        private void removeItem(int position) {
            favCryptoArrayList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position,favCryptoArrayList.size());
        }
    }
}
