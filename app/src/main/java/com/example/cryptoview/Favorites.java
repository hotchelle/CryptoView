package com.example.cryptoview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.internal.NavigationMenu;

import java.util.ArrayList;

public class Favorites extends AppCompatActivity {

    Button homePage, favPage, newsPage, profilePage;
    private EditText cryptoBar;
    private RecyclerView cryptoList;
    private FavDB favDB;
    ArrayList<FavItem> favoriteCrypto = new ArrayList<>();
    RecyclerView recyclerView;
    private favoriteListAdapter favAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        favDB= new FavDB(this);
        setContentView(R.layout.activity_favorites);
        cryptoBar = findViewById(R.id.CryptoBar);
        cryptoList = findViewById(R.id.FavRecycler);
        homePage = findViewById(R.id.home);
        favPage = findViewById(R.id.fav);
        newsPage = findViewById(R.id.news);
        profilePage = findViewById(R.id.profile);

        favAdapter = new favoriteListAdapter(favoriteCrypto,this);
        cryptoList.setLayoutManager(new LinearLayoutManager(this));
        cryptoList.setAdapter(favAdapter);
        loadData();

        homePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        newsPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), News.class);
                startActivity(intent);
                finish();
            }
        });
        profilePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Profile.class);
                startActivity(intent);
                finish();
            }
        });

        cryptoBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filterCryptos(editable.toString());
            }
        });
    }


    private void filterCryptos(String curr){
        ArrayList<FavItem> filteredList = new ArrayList<>();
        for(FavItem item : favoriteCrypto){
            System.out.println(item.getCName());
            if (item.getCName().toLowerCase().contains(curr.toLowerCase())){
                filteredList.add(item);
            }
        }

        if(filteredList.isEmpty()){
            Toast.makeText(this, "No crypto currencies were found", Toast.LENGTH_SHORT).show();
        } else{
            favoriteListAdapter.filterList(filteredList);
        }
    }


    private void loadData() {
        if (favoriteCrypto != null) {
            favoriteCrypto.clear();
        }
        SQLiteDatabase db = favDB.getReadableDatabase();
        Cursor cursor = favDB.select_all_favorite_list();
        try {
            while (cursor.moveToNext()) {
                String name = cursor.getString(cursor.getColumnIndex(favDB.C_Name));
                String symbol = cursor.getString(cursor.getColumnIndex(favDB.C_Symbol));
                double price = Double.parseDouble(cursor.getString(cursor.getColumnIndex(favDB.C_Price)));
                FavItem favItem = new FavItem(name,symbol,price );
                favoriteCrypto.add(favItem);
            }
        } finally {
            if (cursor != null && cursor.isClosed())
                cursor.close();
            db.close();
        }



    }
}
