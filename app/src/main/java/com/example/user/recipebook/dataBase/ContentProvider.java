package com.example.user.recipebook.dataBase;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class ContentProvider extends android.content.ContentProvider {

    public static final int FAVMEALDB = 55;
    public static final int FAVMEALDB_WITH_ID = 102;
    private static final UriMatcher favUriMatcher = buildUriMatcher();
    private DbHelper mealDbHelper;

    private static UriMatcher buildUriMatcher() {
        final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(Contract.AUTHORITY, Contract.PATH_TASKS, FAVMEALDB);
        uriMatcher.addURI(Contract.AUTHORITY, Contract.PATH_TASKS + "/*", FAVMEALDB_WITH_ID);
        return uriMatcher;
    }

    public ContentProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase sqDatabase = mealDbHelper.getWritableDatabase();
        int match = favUriMatcher.match(uri);
        int favMoviedeleted;
        if (selection == null) {
            selection = "1";
        }
        switch (match) {
            case FAVMEALDB:
                favMoviedeleted = sqDatabase.delete(Contract.FavRecipeData.TABLE_NAME, selection, selectionArgs);
                break;

            case FAVMEALDB_WITH_ID:
                favMoviedeleted = sqDatabase.delete(Contract.FavRecipeData.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri:" + uri);
        }
        if (favMoviedeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return favMoviedeleted;
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mealDbHelper.getWritableDatabase();
        long id;
        int match = favUriMatcher.match(uri);
        Uri uriMatched = null;
        switch (match) {
            case FAVMEALDB:
                id = db.insert(Contract.FavRecipeData.TABLE_NAME, null, values);
                if (id > 0) {
                    uriMatched = ContentUris.withAppendedId(Contract.FavRecipeData.CONTENT_URI, id);
                }
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri" + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return uriMatched;
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        mealDbHelper = new DbHelper(context);
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        final SQLiteDatabase db = mealDbHelper.getReadableDatabase();
        int match = favUriMatcher.match(uri);
        Cursor cursor = null;
        switch (match) {
            case FAVMEALDB:
                cursor = db.query(Contract.FavRecipeData.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case FAVMEALDB_WITH_ID:
                cursor = db.query(Contract.FavRecipeData.TABLE_NAME, projection, selection, selectionArgs, null, null, null);
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri" + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
