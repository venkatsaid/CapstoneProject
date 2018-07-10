package com.example.user.recipebook;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.user.recipebook.models.ItemInfo;
import com.example.user.recipebook.models.MealArea;
import com.example.user.recipebook.models.MealCategory;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MealAdapter extends RecyclerView.Adapter<MealAdapter.MyViewHolder> {
    Context ct;
    ArrayList<MealCategory> meal = new ArrayList<>();
    ArrayList<MealArea> mealAreas = new ArrayList<>();
    ArrayList<ItemInfo> itemInfos = new ArrayList<>();
    public static final String recipeUrl="https://www.themealdb.com/api/json/v1/1/lookup.php?i=";
    int id;

    public MealAdapter(CategoryList context, ArrayList<MealCategory> list1, ArrayList<MealArea> list2, int id) {
        this.ct = context;
        this.meal = list1;
        this.mealAreas = list2;
        this.id = id;
    }

    public MealAdapter(RecipeInfo context, ArrayList<ItemInfo> itemInfos, int id) {
        this.ct = context;
        this.itemInfos = itemInfos;
        this.id=id;

    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v1 = null;
        if (id == 10) {
            v1 = LayoutInflater.from(ct).inflate(R.layout.category_list, parent, false);
        } else if (id == 11) {
            v1 = LayoutInflater.from(ct).inflate(R.layout.category_list, parent, false);
        } else if (id == 12) {

            v1 = LayoutInflater.from(ct).inflate(R.layout.category_list, parent, false);
        }

        return new MyViewHolder(v1);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        if (id == 10) {
            holder.textView.setText(meal.get(position).getStrCategory());
            Picasso.with(ct).load(meal.get(position).getStrCategoryThumb()).error(R.mipmap.ic_launcher).into(holder.images);
        } else if (id == 11) {
            holder.textView.setText(mealAreas.get(position).getStrArea());
            Picasso.with(ct).load(R.mipmap.area).error(R.mipmap.ic_launcher).into(holder.images);
        } else if (id == 12) {

            holder.textView.setText(itemInfos.get(position).getStrMeal());
            Picasso.with(ct).load(itemInfos.get(position).getStrMealThumb()).error(R.mipmap.ic_launcher).into(holder.images);
        }
    }

    @Override
    public int getItemCount() {
        int size = 0;
        if (id == 10) {
            size = (meal == null) ? 0 : meal.size();
        } else if (id == 11) {
            size = (mealAreas == null) ? 0 : mealAreas.size();
        } else if (id == 12) {
            size = (itemInfos == null) ? 0 : itemInfos.size();
        }
        return size;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView images;
        TextView textView;

        public MyViewHolder(View itemView) {
            super(itemView);
            if (id == 10) {
                images = itemView.findViewById(R.id.image1);
                textView = itemView.findViewById(R.id.text);
                images.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String filter;
                        int position = getLayoutPosition();
                        filter = meal.get(position).getStrCategory();
                        Intent intent = new Intent(ct, RecipeInfo.class);
                        intent.putExtra("filter", "c=" + filter);
                        v.getContext().startActivity(intent);
                    }
                });
            } else if (id == 11) {
                images = itemView.findViewById(R.id.image1);
                textView = itemView.findViewById(R.id.text);
                images.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String filter;
                        int position = getLayoutPosition();
                        filter = mealAreas.get(position).getStrArea();
                        Intent intent = new Intent(ct, RecipeInfo.class);
                        intent.putExtra("filter", "a=" + filter);
                        v.getContext().startActivity(intent);
                    }
                });
            } else if (id == 12) {
                images = itemView.findViewById(R.id.image1);
                textView = itemView.findViewById(R.id.text);
                images.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = getLayoutPosition();
                        String id=itemInfos.get(position).getIdMeal();
                        String name=itemInfos.get(position).getStrMeal();
                        String thumb=itemInfos.get(position).getStrMealThumb();
                        String[] item={id,name,thumb};
                        String url=recipeUrl+id;
                        MyAsyncTask myAsyncTask=new MyAsyncTask(ct,4);
                        myAsyncTask.execute(url);
                      }
                });

            }
        }
    }
}
