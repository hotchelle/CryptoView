package com.example.cryptoview;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Crypto {
//    private String cChart;
    private String CName;
    private String CSymbol;
    private double CPrice;
    private boolean status;

    public Crypto(String CName, String CSymbol, double CPrice) {
        this.CName = CName;
        this.CSymbol = CSymbol;
        this.CPrice = CPrice;
        this.status = false;
    }
    public Crypto(String cName)
    {
        this.CName = cName;
    }


    public String getCName() {
        return CName;
    }

    public void setCName(String CName) {
        this.CName = CName;
    }

    public String getCSymbol() {
        return CSymbol;
    }

    public void setCSymbol(String CSymbol) {
        this.CSymbol = CSymbol;
    }

    public double getCPrice() {
        return CPrice;
    }

    public void setCPrice(double CPrice) {
        this.CPrice = CPrice;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

}
