package com.example.save_money_app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import java.util.ArrayList;
import java.util.List;

public class DataBase {
    private static DataBase instance;
    private SQLiteDatabase dataBase;

    public List<String> lists = new ArrayList<String>();
    public List<Integer> listsIds = new ArrayList<Integer>();


    private String path;

    private DataBase(Context context) {
        try {
            this.path = context.getDataDir().toString() + "/saveMoney.db";



            this.dataBase = SQLiteDatabase.openOrCreateDatabase(this.path, null);

            this.dataBase.execSQL("CREATE TABLE IF NOT EXISTS lists(" +
                    " id INTEGER PRIMARY KEY AUTOINCREMENT" +
                    " , nome VARCHAR) ");

            this.dataBase.execSQL("CREATE TABLE IF NOT EXISTS itens(" +
                    " id INTEGER PRIMARY KEY AUTOINCREMENT" +

                    " , nome VARCHAR" +
                    " , quantidade INTEGER" +
                    " , listaID INTEGER" +
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
        try {
            this.dataBase = SQLiteDatabase.openOrCreateDatabase(this.path, null);

            Cursor dbCursor = this.dataBase.rawQuery("SELECT id, nome FROM lists", null);

            if (!dbCursor.moveToFirst()) {
                return null;
            }

            lists.clear();
            listsIds.clear();

            while (dbCursor != null) {
                lists.add(dbCursor.getString(1));
                listsIds.add(dbCursor.getInt(0));

                if (dbCursor.isLast()) {
                    break;
                }

                dbCursor.moveToNext();
            }

            this.dataBase.close();

            return lists;

        } catch (Exception e) {
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

    public int getListID(int index) {
        if (index < listsIds.size()) {
            return listsIds.get(index).intValue();
        }

        return -1;
    }

    public List<Integer> getListItemsIds(int ListID) {
        try {
            this.dataBase = SQLiteDatabase.openOrCreateDatabase(this.path, null);

            Cursor dbCursor = this.dataBase.rawQuery("SELECT id FROM itens WHERE listaID =" + ListID, null);

            dbCursor.moveToFirst();

            List<Integer> itemsIds = new ArrayList<Integer>();

            while (dbCursor != null) {

                itemsIds.add(dbCursor.getInt(0));

                if (dbCursor.isLast()) {
                    break;
                }

                dbCursor.moveToNext();
            }

            this.dataBase.close();

            return itemsIds;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public String getItemName(int itemID) {
        try {
            this.dataBase = SQLiteDatabase.openOrCreateDatabase(this.path, null);

            Cursor dbCursor = this.dataBase.rawQuery("SELECT nome FROM itens WHERE id =" + itemID, null);

            if (!dbCursor.moveToFirst()) {
                return null;
            }

            dbCursor.moveToFirst();

            String itemName = dbCursor.getString(0);

            this.dataBase.close();

            return itemName;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public int setItemOnList(String codBarras, String itemName, int listID, int quantity) {

        try {
            this.dataBase = SQLiteDatabase.openOrCreateDatabase(this.path, null);

            Cursor dbCursor = this.dataBase.rawQuery("SELECT id, listaID FROM itens WHERE codigoBarra =" + codBarras, null);

            if (dbCursor.moveToFirst()) {
                while (dbCursor != null) {

                    if (dbCursor.getInt(1) == listID) {
                        this.dataBase.execSQL("UPDATE itens SET quantidade=" + quantity + " WHERE id=" + dbCursor.getInt(0));
                        this.dataBase.close();
                        return 0;
                    }

                    if (dbCursor.isLast()) {
                        break;
                    }

                    dbCursor.moveToNext();
                }
            }

            ContentValues values = new ContentValues();

            values.put("nome", itemName);
            values.put("quantidade", quantity);
            values.put("listaID", listID);
            values.put("codigoBarra", codBarras);

            long response = this.dataBase.insert("itens", null, values);

            this.dataBase.close();

            return 0;

        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }

    }

    public int setItemQtyOnList(int itemID, int quantity) {

        try {
            this.dataBase = SQLiteDatabase.openOrCreateDatabase(this.path, null);

            if (quantity != 0) {
                this.dataBase.execSQL("UPDATE itens SET quantidade=" + quantity + " WHERE id=" + itemID);
            } else {
                this.dataBase.delete("itens", "id=" + itemID, null);
            }

            this.dataBase.close();

            return 0;

        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }

    }

    public int getItemQty(int itemID) {
        try {
            this.dataBase = SQLiteDatabase.openOrCreateDatabase(this.path, null);

            Cursor dbCursor = this.dataBase.rawQuery("SELECT quantidade FROM itens WHERE id =" + itemID, null);

            if (!dbCursor.moveToFirst()) {
                return -1;
            }

            int itemQty = dbCursor.getInt(0);

            this.dataBase.close();

            return itemQty;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1;
    }

    public int deleteList(int listID) {
        try {
            this.dataBase = SQLiteDatabase.openOrCreateDatabase(this.path, null);

            this.dataBase.delete("itens", "listaID=" + listID, null);
            this.dataBase.delete("lists", "id=" + listID, null);

            this.dataBase.close();

            return 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1;
    }

    public String getListName(int listID) {
        try {
            this.dataBase = SQLiteDatabase.openOrCreateDatabase(this.path, null);

            Cursor dbCursor = this.dataBase.rawQuery("SELECT nome FROM lists WHERE id =" + listID, null);

            if (!dbCursor.moveToFirst()) {
                return null;
            }

            String listName = dbCursor.getString(0);

            this.dataBase.close();

            return listName;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<String> getListBarCodes(int listID) {
        try {
            this.dataBase = SQLiteDatabase.openOrCreateDatabase(this.path, null);

            Cursor dbCursor = this.dataBase.rawQuery("SELECT codigoBarra FROM itens WHERE listaID =" + listID, null);

            if (!dbCursor.moveToFirst()) {
                return null;
            }

            List<String> barCodes = new ArrayList<String>();

            do {
                barCodes.add(dbCursor.getString(0));
            } while (dbCursor.moveToNext());

            this.dataBase.close();

            return barCodes;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}
