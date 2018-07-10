package com.example.user.recipebook;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.recipebook.models.ItemDetail;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FavAdapter extends RecyclerView.Adapter<FavAdapter.ViewHolder>{
    Context ct;
    ArrayList<ItemDetail> itemDetail=new ArrayList<>();
    public static final String youtubeBaseUrl="https://www.youtube.com/watch?v=";
    public FavAdapter(CategoryList context, ArrayList<ItemDetail> itemDetails) {
        this.ct = context;
        this.itemDetail= itemDetails;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View v1 = LayoutInflater.from(ct).inflate(R.layout.category_list, parent, false);
        return new ViewHolder(v1);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textView.setText(itemDetail.get(position).getStrMeal());
        Picasso.with(ct).load(itemDetail.get(position).getStrMealThumb()).error(R.mipmap.ic_launcher).into(holder.images);
    }

    @Override
    public int getItemCount() {
        return itemDetail.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView images;
        TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            images = itemView.findViewById(R.id.image1);
            textView = itemView.findViewById(R.id.text);
            images.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = getLayoutPosition();
                        ArrayList<String> s=new ArrayList<>();
                        s.add(itemDetail.get(position).getStrMeal());
                        s.add(itemDetail.get(position).getStrMealThumb());
                        s.add(itemDetail.get(position).getStrCategory());
                        s.add(itemDetail.get(position).getStrArea());
                        s.add(itemDetail.get(position).getStrInstructions());
                        s.add(itemDetail.get(position).getStrYoutube().replace(youtubeBaseUrl,""));
                        s.add(itemDetail.get(position).getStrSource());
                        s.add(itemDetail.get(position).getIdMeal());

                        Intent intent1 = new Intent(ct, RecipeDetail.class);
                        intent1.putStringArrayListExtra(ct.getResources().getString(R.string.msg1),s);
                        ct.startActivity(intent1);
                    }
                });
            }
        }
    }