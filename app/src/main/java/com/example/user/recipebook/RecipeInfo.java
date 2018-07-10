package com.example.user.recipebook;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;


public class RecipeInfo extends AppCompatActivity {
    public static final String filterUrl="https://www.themealdb.com/api/json/v1/1/filter.php?";
    String filterName;
    static RecyclerView recyclerView2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_info);
        recyclerView2=findViewById(R.id.recycler2);
        filterName=getIntent().getStringExtra("filter");//Taking data from adapter
        String filter1Url=filterUrl+filterName;
        MyAsyncTask myAsyncTask=new MyAsyncTask(this,3);
        myAsyncTask.execute(filter1Url);
    }
}
