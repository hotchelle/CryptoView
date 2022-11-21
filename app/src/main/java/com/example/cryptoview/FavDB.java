package com.example.cryptoview;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class FavDB extends SQLiteOpenHelper {

    private static int DB_VERSION = 1;
    private static String DATABASE_NAME = "FavoritesDB";
    private static String TABLE_NAME = "favoriteTable";
    public static String C_Name="CName";
    public static String C_Symbol = "CSymbol";
    public static String C_Price = "CPrice"; // should be double(causes error)
    public static String FAVORITE_STATUS = "fStatus";
    private ArrayList<Crypto> cryptoArrayList;
    private String user;

    private static String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
            + C_Name + " TEXT," + C_Symbol+ " TEXT,"
            + C_Price + " TEXT," + FAVORITE_STATUS+" TEXT)";

    public FavDB(Context context, ArrayList<Crypto> cryptoArrayList)
    {
        super(context,DATABASE_NAME,null,DB_VERSION);
        this.cryptoArrayList = cryptoArrayList;
    }

    public FavDB(Context context)
    {
        super(context,DATABASE_NAME,null,DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1)
    {
        sqLiteDatabase.execSQL("drop table if exists favoriteTable");
    }

    // create empty table
    public void insertEmpty() {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        // enter your value
        for (int x = 0; x < cryptoArrayList.size(); x++) {
            cv.put(C_Name,cryptoArrayList.get(x).getCName());
            cv.put(C_Symbol,cryptoArrayList.get(x).getCSymbol());
            cv.put(C_Price,(cryptoArrayList.get(x).getCPrice()));
            cv.put(FAVORITE_STATUS, "0");

            db.insert(TABLE_NAME,null, cv);
        }
    }

    // insert data into database
    public Boolean insertIntoTheDatabase(String Cname, String Csymbol, double Cprice, String fav_status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(C_Name, Cname);
        cv.put(C_Symbol, Csymbol);
        cv.put(C_Price, Cprice);
        cv.put(FAVORITE_STATUS, fav_status);
        long result = db.insert(TABLE_NAME,null, cv);
        Log.d("FavDB Status", Cname + ", favstatus - "+fav_status+" - . " + cv);

        if(result == -1)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    // read all data
    public Cursor read_all_data(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "select * from " + TABLE_NAME + " where " + C_Name+"="+name+"";
        return db.rawQuery(sql,null,null);
    }

    // remove line from database
    public void remove_fav(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "UPDATE " + TABLE_NAME + " SET  "+ FAVORITE_STATUS+" = 0 WHERE "+C_Name+"="+name;
        String sql1 = "UPDATE favoriteTable SET fStatus = 0 WHERE CName = '"+name+"'";
        //db.rawQuery("SELECT _id FROM  Meeting   where meet='"+rid+"'" , null);
        db.execSQL(sql1);
        Log.d("remove", name.toString());

    }

    // select all favorite list
    public Cursor select_all_favorite_list() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM "+TABLE_NAME+" WHERE "+FAVORITE_STATUS+" ='1'";
        return db.rawQuery(sql,null,null);
    }

}
