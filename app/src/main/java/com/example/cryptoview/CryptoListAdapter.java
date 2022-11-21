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

//in this class we set the data of each item in our recycler view.
public class CryptoListAdapter extends RecyclerView.Adapter<CryptoListAdapter.ViewHolder> {
    private static final DecimalFormat cdf = new DecimalFormat( "#.##");
    private static ArrayList<Crypto> cryptoArrayList;
    private final Context context;
    private FavDB favDB;

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
        favDB = new FavDB(context,cryptoArrayList);
        SharedPreferences prefs = context.getSharedPreferences("prefs", Context.MODE_PRIVATE);
        boolean firstStart = prefs.getBoolean("firstStart", true);
        if (firstStart) {
            createTableOnFirstStart();
        }
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
                    Crypto crypto = cryptoArrayList.get(position);

                    if(!crypto.isStatus())
                    {
                        crypto.setStatus(true);
                        favDB.insertIntoTheDatabase(""+crypto.getCName(),""+crypto.getCSymbol(), crypto.getCPrice(), "1");
                        favBtn.setBackgroundResource(R.drawable.ic_red_favorite_24);
                    }
                    else
                    {
                        crypto.setStatus(false);
                        favDB.remove_fav(crypto.getCName());
                        favBtn.setBackgroundResource(R.drawable.ic_shadow_favorite_24);
                    }
                }
            });


        }
    }

    private void createTableOnFirstStart() {
        favDB.insertEmpty();

        SharedPreferences prefs = context.getSharedPreferences("prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("firstStart", false);
        editor.apply();
    }

    private void readCursorData(Crypto crypto, ViewHolder viewHolder) {
        Cursor cursor = favDB.read_all_data(crypto.getCName());
        SQLiteDatabase db = favDB.getReadableDatabase();
        try {
            while (cursor.moveToNext()) {
                String item_fav_status = cursor.getString(cursor.getColumnIndex(FavDB.FAVORITE_STATUS));
                if (item_fav_status == "0")
                {
                    crypto.setStatus(false);
                }
                else
                {
                    crypto.setStatus(true);
                }

                //check fav status
                if (item_fav_status != null && item_fav_status.equals("1")) {
                    viewHolder.favBtn.setBackgroundResource(R.drawable.ic_red_favorite_24);
                } else if (item_fav_status != null && item_fav_status.equals("0")) {
                    viewHolder.favBtn.setBackgroundResource(R.drawable.ic_shadow_favorite_24);
                }
            }
        } finally {
            if (cursor != null && cursor.isClosed())
                cursor.close();
            db.close();
        }

    }

}