package com.example.cryptoview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    private EditText cryptoBar;
    private RecyclerView cryptoList;
    private ProgressBar loadingSymbol;
    private ArrayList<Crypto> cryptoArrayList;
    private CryptoListAdapter cryptoAdapter;
    Button homePage, favPage, newsPage, profilePage;
    @Override

    // This method overrided to initialize all the required components with inputs from all the crypto objects
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cryptoBar = findViewById(R.id.CryptoBar);
        cryptoList = findViewById(R.id.CryptoRecycler);
        loadingSymbol = findViewById(R.id.LoadingSymbol);
        cryptoArrayList = new ArrayList<>();
        cryptoAdapter = new CryptoListAdapter(cryptoArrayList,this);
        cryptoList.setLayoutManager(new LinearLayoutManager(this));
        cryptoList.setAdapter(cryptoAdapter);

        getCryptoData();

        homePage = findViewById(R.id.home);
        favPage = findViewById(R.id.fav);
        newsPage = findViewById(R.id.news);
        profilePage = findViewById(R.id.profile);


        favPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Favorites.class);
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

    // This method filters the crypto currencies on the screen to display the one that matches the search selection inputted by the user
    private void filterCryptos(String curr){
        ArrayList<Crypto> filteredList = new ArrayList<>();
        for(Crypto item : cryptoArrayList){
            System.out.println(item.getCName());
            if (item.getCName().toLowerCase().contains(curr.toLowerCase())){
                filteredList.add(item);
            }
        }

        if(filteredList.isEmpty()){
            Toast.makeText(this, "No crypto currencies were found", Toast.LENGTH_SHORT).show();
        } else{
            CryptoListAdapter.filterList(filteredList);
        }
    }

    //Build favorites

    // This method fetches the data from cryptocapmarket api
    // and stores this data as a JSON object and parse this string to obtain
    // 1- name of the crypto
    // 2- symbol of the crypto
    // 3- price of the crypto in USD
    private void getCryptoData() {
        loadingSymbol.setVisibility(View.VISIBLE);
        String url = "https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/latest";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                loadingSymbol.setVisibility(View.GONE);
                try{
                    JSONArray dataArray = response.getJSONArray("data");
                    for(int i = 0; i < dataArray.length(); i++){
                        JSONObject dataObj = dataArray.getJSONObject(i);
                        String name = dataObj.getString("name");
                        String symbol = dataObj.getString("symbol");
                        JSONObject quote = dataObj.getJSONObject("quote");
                        JSONObject usd = quote.getJSONObject("USD");
                        double price = usd.getDouble("price");
                        cryptoArrayList.add(new Crypto(name, symbol, price));
                    }

                    cryptoAdapter.notifyDataSetChanged();

                }catch(JSONException e){
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "failed to extract json data", Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingSymbol.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, "failed to fetch data", Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("X-CMC_PRO_API_KEY",apikey);
                return headers;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }
}