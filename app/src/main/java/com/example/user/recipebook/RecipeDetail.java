package com.example.user.recipebook;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.recipebook.dataBase.Contract;
import com.example.user.recipebook.dataBase.RecipeWidget;
import com.example.user.recipebook.models.ItemDetail;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.user.recipebook.dataBase.Contract.FavRecipeData.COLUMN_AREA;
import static com.example.user.recipebook.dataBase.Contract.FavRecipeData.COLUMN_BACKGROUND_IMAGE;
import static com.example.user.recipebook.dataBase.Contract.FavRecipeData.COLUMN_CATEGORY;
import static com.example.user.recipebook.dataBase.Contract.FavRecipeData.COLUMN_INSTRUCTIONS;
import static com.example.user.recipebook.dataBase.Contract.FavRecipeData.COLUMN_MEALID;
import static com.example.user.recipebook.dataBase.Contract.FavRecipeData.COLUMN_SOURCE;
import static com.example.user.recipebook.dataBase.Contract.FavRecipeData.COLUMN_TITLE;
import static com.example.user.recipebook.dataBase.Contract.FavRecipeData.COLUMN_YOUTUBEKEY;

public class RecipeDetail extends AppCompatActivity {
    ArrayList<String> s=new ArrayList<>();
    static TextView category_type;
    static TextView area;
    static TextView instructions;
    static TextView extraLink;
    static ImageView item_image;
    static ImageView video_image;
    static CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.id_favorite) ImageView favorites;
    boolean check_fav=false;
    final String title="TITLE";
    final String instructionswidget="INSTRUCTIONS";
    AdView mAdView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        Toast.makeText(this, R.string.fabwidget, Toast.LENGTH_SHORT).show();
        collapsingToolbarLayout=findViewById(R.id.collapsebar);
        category_type=findViewById(R.id.category_type);
        area=findViewById(R.id.area);
        extraLink=findViewById(R.id.extraLink);
        instructions=findViewById(R.id.instructions);
        item_image=findViewById(R.id.item_image);
        video_image=findViewById(R.id.vedio_image);
        ButterKnife.bind(this);
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        AdView mAdView;
        mAdView=findViewById(R.id.bannerone);
        MobileAds.initialize(this,"ca-app-pub-6336523906622902~6817819328");
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("C3528C0925D3AB8424433F9070BD2A5F")
                .build();
        mAdView.loadAd(adRequest);

        s=getIntent().getStringArrayListExtra("details");
        collapsingToolbarLayout.setTitle(s.get(0));
        Picasso.with(this).load(s.get(1)).error(R.drawable.blue).into(RecipeDetail.item_image);
        RecipeDetail.category_type.setText((s.get(2)));
        RecipeDetail.area.setText(s.get(3));
        RecipeDetail.instructions.setText(s.get(4));
        final Intent intent=new Intent(this,VideoActivity.class);
        intent.putExtra("key",s.get(5));
        RecipeDetail.video_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent);
            }
        });
        extraLink.setText(s.get(6));
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabbutton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecipeWidget rw=new RecipeWidget();
                String widgettext=title+"\n"+s.get(0)+"\n";
                widgettext+=instructionswidget+"\n"+s.get(4);
                rw.recipeDetailsInfo=widgettext;
                Toast.makeText(RecipeDetail.this, R.string.addedtowidget, Toast.LENGTH_SHORT).show();
            }
        });
        Cursor cursor=getContentResolver().query(Uri.parse(Contract.FavRecipeData.CONTENT_URI+"/*"),null, COLUMN_TITLE+" LIKE ?",new String[]{s.get(0)},null);
        check_fav=cursor.getCount() > 0;
        if (cursor.getCount()>0){
            favorites.setImageResource(R.mipmap.fav);
        }
        else {
            favorites.setImageResource(R.mipmap.un_fav);
        }
        favorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor cursor=getContentResolver().query(Uri.parse(Contract.FavRecipeData.CONTENT_URI+"/*"),null, COLUMN_TITLE+" LIKE ?",new String[]{s.get(0)},null);
                if (cursor.getCount()>0) {
                    getContentResolver().delete(Uri.parse(Contract.FavRecipeData.CONTENT_URI + "/*"), Contract.FavRecipeData.COLUMN_TITLE + " LIKE ?",new  String[]{s.get(0)});
                    check_fav=false;
                    favorites.setImageResource(R.mipmap.un_fav);
                }
                else {
                    ContentValues recipeDetailValues = new ContentValues();
                    recipeDetailValues.put(COLUMN_MEALID,s.get(7));
                    recipeDetailValues.put(COLUMN_TITLE,s.get(0));
                    recipeDetailValues.put(COLUMN_CATEGORY,s.get(2));
                    recipeDetailValues.put(COLUMN_AREA,s.get(3));
                    recipeDetailValues.put(COLUMN_BACKGROUND_IMAGE,s.get(1));
                    recipeDetailValues.put(COLUMN_INSTRUCTIONS,s.get(4));
                    recipeDetailValues.put(COLUMN_YOUTUBEKEY,s.get(5));
                    recipeDetailValues.put(COLUMN_SOURCE, s.get(6));
                    getContentResolver().insert(Uri.parse(Contract.FavRecipeData.CONTENT_URI+""), recipeDetailValues);
                    check_fav=true;
                    favorites.setImageResource(R.mipmap.fav);

                }

            }
        });
    }
}
