package com.example.user.recipebook.dataBase;

import android.net.Uri;
import android.provider.BaseColumns;


public class Contract {
    public static final String AUTHORITY = "com.example.user.recipebook";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_TASKS = "RecipeTable";

    public static final class FavRecipeData implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_TASKS).build();
        public static final String TABLE_NAME = "RecipeTable";
        public static final String ROW_ID="RowId";
        public static final String COLUMN_MEALID = "MealId";
        public static final String COLUMN_TITLE = "Title";
        public static final String COLUMN_CATEGORY = "Category";
        public static final String COLUMN_AREA = "Area";
        public static final String COLUMN_INSTRUCTIONS = "Instructions";
        public static final String COLUMN_BACKGROUND_IMAGE = "BackgroundImage";
        public static final String COLUMN_YOUTUBEKEY = "YoutubeKey";
        public static final String COLUMN_SOURCE = "Source";

    }
}
