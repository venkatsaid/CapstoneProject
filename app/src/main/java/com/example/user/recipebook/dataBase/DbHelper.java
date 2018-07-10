package com.example.user.recipebook.dataBase;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {
    public static final int version=1;
    public Context c;
    public DbHelper(Context context) {
        super(context, Contract.FavRecipeData.TABLE_NAME, null, version);
        c = context;
    }

    public Void showFavoriteMeals() {

        String queryselected = "SELECT * FROM " + Contract.FavRecipeData.TABLE_NAME;
        SQLiteDatabase database = getReadableDatabase();
        Cursor mealdetails = database.rawQuery(queryselected, null);
        String[] data = new String[10];
        String p = "";
        if (mealdetails.moveToFirst()) {
            do {
                data[0] = String.valueOf(mealdetails.getInt(0));
                data[1] = mealdetails.getString(1);
                data[2] = mealdetails.getString(2);
                data[3] = mealdetails.getString(3);
                data[4] = mealdetails.getString(4);
                p = p + data[1] + "\n" + data[2] + "\n" + data[3] + "\n" + data[4] + "\n" + data[5] + "\n" + data[6] + "\n";
            } while (mealdetails.moveToNext());
        }
        database.close();
        return null;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String query = "CREATE TABLE " + Contract.FavRecipeData.TABLE_NAME + "("
                + Contract.FavRecipeData.ROW_ID + " INTEGER PRIMARY KEY AUTOINCREMENT " + ", "
                + Contract.FavRecipeData.COLUMN_MEALID + " TEXT" + ", "
                + Contract.FavRecipeData.COLUMN_TITLE + " TEXT" + ", "
                + Contract.FavRecipeData.COLUMN_CATEGORY + " TEXT" + ", "
                + Contract.FavRecipeData.COLUMN_AREA + " TEXT" + ", "
                + Contract.FavRecipeData.COLUMN_BACKGROUND_IMAGE + " TEXT"+", "
                + Contract.FavRecipeData.COLUMN_INSTRUCTIONS + " TEXT"+", "
                + Contract.FavRecipeData.COLUMN_YOUTUBEKEY + " TEXT"+", "
                + Contract.FavRecipeData.COLUMN_SOURCE + " TEXT" + ")";
        db.execSQL(query);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Contract.FavRecipeData.TABLE_NAME);
        onCreate(db);
    }
}
