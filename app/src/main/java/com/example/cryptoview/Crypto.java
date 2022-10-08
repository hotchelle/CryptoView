package com.example.cryptoview;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Crypto extends RecyclerView.Adapter {
//    private String cChart;
    private static String CName;
    private static String CSymbol;
    private static double CPrice;

    public Crypto(String CName, String CSymbol, double CPrice) {
        this.CName = CName;
        this.CSymbol = CSymbol;
        this.CPrice = CPrice;
    }


    public Crypto(ArrayList<Crypto> cryptoArrayList, MainActivity mainActivity) {
    }

    public static String getCName() {
        return CName;
    }

    public void setCName(String CName) {
        this.CName = CName;
    }

    public static String getCSymbol() {
        return CSymbol;
    }

    public void setCSymbol(String CSymbol) {
        this.CSymbol = CSymbol;
    }

    public static double getCPrice() {
        return CPrice;
    }

    public void setCPrice(double CPrice) {
        this.CPrice = CPrice;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
