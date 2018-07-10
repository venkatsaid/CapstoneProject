package com.example.user.recipebook;


import android.content.Context;
import android.content.Intent;

import android.os.AsyncTask;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import com.example.user.recipebook.models.ItemDetail;
import com.example.user.recipebook.models.ItemInfo;
import com.example.user.recipebook.models.MealArea;
import com.example.user.recipebook.models.MealCategory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class MyAsyncTask extends AsyncTask<String, Void, Void> {

    private String data="";
    private ArrayList<MealCategory> mealCategories=new ArrayList<>();
    private ArrayList<MealArea> mealAreas=new ArrayList<>();
    private ArrayList<ItemInfo> itemInfos=new ArrayList<>();
    public static final String strCategoryThumb="strCategoryThumb";
    public static final String details="details";
    public static final String youtubeBaseUrl="https://www.youtube.com/watch?v=";
    Context ct;
    int url_check;
    ItemDetail itemDetail=new ItemDetail();
    public MyAsyncTask(Context context, int id) {
        this.ct=context;
        this.url_check=id;
    }

    @Override
    protected Void doInBackground(String... strings) {
        try {
            if (url_check==1){
            data = NetworkUtilities.GetJson(strings);
            JSONObject JO = null;
            JO = new JSONObject(data);
            JSONArray JA = (JO.getJSONArray(ct.getResources().getString(R.string.msg2)));
            for (int i = 0; i < JA.length(); i++) {
                JSONObject category = JA.getJSONObject(i);
                MealCategory mealCategory=new MealCategory();
                mealCategory.setStrCategory(category.getString(ct.getResources().getString(R.string.m2)));
                mealCategory.setStrCategoryThumb(category.getString(strCategoryThumb));
                mealCategories.add(mealCategory);
                }
            }
            else if (url_check==2){
                data = NetworkUtilities.GetJson(strings);
                JSONObject JO = null;
                JO = new JSONObject(data);
                JSONArray JA = (JO.getJSONArray(ct.getResources().getString(R.string.meal)));
                for (int i = 0; i < JA.length(); i++) {
                    JSONObject area = JA.getJSONObject(i);
                    mealAreas.add(new MealArea(area.getString(ct.getResources().getString(R.string.strArea))));
                }
            }
            else if (url_check==3){
                data = NetworkUtilities.GetJson(strings);
                JSONObject JO = null;
                JO = new JSONObject(data);
                JSONArray JA = (JO.getJSONArray(ct.getResources().getString(R.string.meal)));
                for (int i = 0; i < JA.length(); i++) {
                    JSONObject item = JA.getJSONObject(i);
                    ItemInfo itemInfo=new ItemInfo();
                    itemInfo.setStrMeal(item.getString(ct.getResources().getString(R.string.jsonstrMeal)));
                    itemInfo.setStrMealThumb(item.getString(ct.getResources().getString(R.string.jsonstrMealThumb)));
                    itemInfo.setIdMeal(item.getString(ct.getResources().getString(R.string.jsonidMeal)));
                    itemInfos.add(itemInfo);
                }
            }
            else if (url_check==4){
                data = NetworkUtilities.GetJson(strings);
                JSONObject JO = null;
                JO = new JSONObject(data);
                JSONArray JA = (JO.optJSONArray(ct.getResources().getString(R.string.meal)));
                JSONObject item = JA.optJSONObject(0);
                itemDetail.setIdMeal(item.optString(ct.getResources().getString(R.string.jsonidMeal)));
                itemDetail.setStrMeal(item.optString(ct.getResources().getString(R.string.jsonstrMeal)));
                itemDetail.setStrCategory(item.optString(ct.getResources().getString(R.string.m2)));
                itemDetail.setStrArea(item.optString(ct.getResources().getString(R.string.strArea)));
                itemDetail.setStrInstructions(item.optString(ct.getResources().getString(R.string.jsonstrInstructions)));
                itemDetail.setStrMealThumb(item.optString(ct.getResources().getString(R.string.jsonstrMealThumb)));
                itemDetail.setStrYoutube(item.optString(ct.getResources().getString(R.string.jsonstrYoutube)));
                itemDetail.setStrSource(item.optString(ct.getString(R.string.jsonstrSource)));
                }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
    @Override
    protected void onPostExecute(Void v) {
        super.onPostExecute(v);
        if (url_check==1) {
            CategoryList.recyclerView.setLayoutManager(new GridLayoutManager(ct,2));
            MealAdapter adapterObj = new MealAdapter((CategoryList) ct,mealCategories,mealAreas,10);
            CategoryList.recyclerView.setAdapter(adapterObj);

        }
        else if (url_check==2){
            CategoryList.recyclerView.setLayoutManager(new GridLayoutManager(ct,2));
            MealAdapter adapterObj = new MealAdapter((CategoryList) ct, mealCategories,mealAreas,11);
            CategoryList.recyclerView.setAdapter(adapterObj);

        }
        else if (url_check==3){
                RecipeInfo.recyclerView2.setLayoutManager(new LinearLayoutManager(ct));
                MealAdapter mealAdapter=new MealAdapter((RecipeInfo) ct,itemInfos,12);
                RecipeInfo.recyclerView2.setAdapter(mealAdapter);

        }
        else if (url_check==4){
            ArrayList<String> s=new ArrayList<>();
            s.add(itemDetail.getStrMeal());
            s.add(itemDetail.getStrMealThumb());
            s.add(itemDetail.getStrCategory());
            s.add(itemDetail.getStrArea());
            s.add(itemDetail.getStrInstructions());
            s.add(itemDetail.getStrYoutube().replace(youtubeBaseUrl,""));
            s.add(itemDetail.getStrSource());
            s.add(itemDetail.getIdMeal());
            Intent intent1 = new Intent(ct, RecipeDetail.class);
            intent1.putStringArrayListExtra(details,s);
            ct.startActivity(intent1);
        }


    }

}
