package com.example.save_money_app;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DataBase {
    private static DataBase instance;
    private SQLiteDatabase dataBase;

    private String path = "/data/data/com.example.save_money_app/databases/saveMoneyDB.db";

    private DataBase(Context context) {
        try {

            this.dataBase = SQLiteDatabase.openOrCreateDatabase(this.path, null);

            this.dataBase.execSQL("CREATE TABLE IF NOT EXISTS lists(" +
                    " id INTEGER PRIMARY KEY AUTOINCREMENT" +
                    " , nome VARCHAR) ");

            this.dataBase.execSQL("CREATE TABLE IF NOT EXISTS itens(" +
                    " id INTEGER PRIMARY KEY AUTOINCREMENT" +
                    " , nome VARCHAR" +
                    " , codigoBarra VARCHAR)");

            this.dataBase.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static DataBase getInstance(Context context) {
        if (instance == null) {
            instance = new DataBase(context);
        }

        return instance;
    }

    public List<String> getLists() {
        try{
            this.dataBase = SQLiteDatabase.openOrCreateDatabase(this.path, null);

            Cursor dbCursor = this.dataBase.rawQuery("SELECT id, nome FROM lists", null);

            List<String> lists = new ArrayList<String>();

            dbCursor.moveToFirst();

            while( dbCursor != null ){
                lists.add(dbCursor.getString(1));

                if(dbCursor.isLast()){
                    break;
                }

                dbCursor.moveToNext();
            }

            this.dataBase.close();

            return lists;

        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    public int insertList(String listName) {
        try {

            this.dataBase = SQLiteDatabase.openOrCreateDatabase(this.path, null);

            String sql = "INSERT INTO lists (nome) VALUES (?)";
            SQLiteStatement stmt = this.dataBase.compileStatement(sql);

            stmt.bindString(1, listName);
            stmt.executeInsert();

            this.dataBase.close();

        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }

        return 0;
    }

}
