package com.example.user.recipebook;

import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.user.recipebook.Authentication.LoginActivity;
import com.example.user.recipebook.dataBase.Contract;
import com.example.user.recipebook.models.ItemDetail;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;

public class CategoryList extends AppCompatActivity implements LoaderManager.LoaderCallbacks{

    public static final int Loader_id=404;
    SwipeRefreshLayout mySwipeRefreshLayout;
    private FirebaseAuth.AuthStateListener authListner;
    public static final String categories="categories";
    public static final String area="area";
    public static final String favorites="favorites";

    ArrayList<ItemDetail> favList;
    String instanceValue="category";
    static RecyclerView recyclerView;
    final String ONSAVEINSTANCE="instance";
    public static String STATE="instance";
    public static String VALUE="categories";
    public static final String categoryUrl = "https://www.themealdb.com/api/json/v1/1/categories.php";
    public String areaUrl = "https://www.themealdb.com/api/json/v1/1/list.php?a=list";
    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_list);
        //ButterKnife.bind(this);
        recyclerView = findViewById(R.id.recycler);
        mySwipeRefreshLayout=findViewById(R.id.swiperefresh);
        if (isnetconnection()) {
            if (savedInstanceState != null) {
                if (savedInstanceState.containsKey(STATE)) {
                    switch (savedInstanceState.getString(STATE)) {
                        case categories:
                            VALUE = categories;
                            currentstate(VALUE);
                            break;
                        case area:
                            VALUE = area;
                            currentstate(VALUE);
                            break;
                        case favorites:
                            VALUE =favorites;
                            favoritesmethod();
                            break;
                    }
                } else {
                    currentstate(VALUE);
                }
            } else {
                currentstate(VALUE);
            }
        } else {
            if (savedInstanceState != null) {
                if (savedInstanceState.containsKey(STATE)) {
                    switch (savedInstanceState.getString(STATE)) {
                        case favorites:
                            VALUE = favorites;
                            favoritesmethod();
                            break;
                        default:
                            Snackbar.make(mySwipeRefreshLayout, R.string.nonetconnnection, Snackbar.LENGTH_LONG)
                                    .setAction(R.string.action, null).show();
                    }
                } else {
                    Snackbar.make(mySwipeRefreshLayout,R.string.nonetconnnection, Snackbar.LENGTH_LONG)
                            .setAction(R.string.action, null).show();
                }
            } else {
                Snackbar.make(mySwipeRefreshLayout,R.string.nonetconnnection, Snackbar.LENGTH_LONG)
                        .setAction(R.string.action, null).show();
            }
        }
        mySwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        finish();
                        Intent i=new Intent(CategoryList.this,CategoryList.class);
                        startActivity(i);
                    }
                }
        );
        auth=FirebaseAuth.getInstance();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        authListner = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // user auth state is changed - user is null
                    // launch login activity
                    startActivity(new Intent(CategoryList.this, LoginActivity.class));
                    finish();
                }
            }
        };
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sort_menu, menu);
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(STATE,VALUE);

    }
    public void currentstate(String value) {
        switch (value) {
            case categories:
               VALUE =categories;
                MyAsyncTask myAsyncTask=new MyAsyncTask(this,1);
                myAsyncTask.execute(categoryUrl);
                break;
            case area:
                VALUE=area;
                MyAsyncTask myAsyncTask1=new MyAsyncTask(this,2);
                myAsyncTask1.execute(areaUrl);
                break;

            case favorites:
                VALUE = favorites;
                favoritesmethod();
                break;
        }
    }
    public boolean isnetconnection() {
        ConnectivityManager connectivity = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connectivity.getActiveNetworkInfo();
        if (netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()) {
            return false;
        }
        return true;
    }
    public void clickedCategory(MenuItem item) {
        VALUE =categories;
        MyAsyncTask myAsyncTask=new MyAsyncTask(this,1);
        myAsyncTask.execute(categoryUrl);

    }
    public  void favoritesmethod()
    {
        getLoaderManager().restartLoader(Loader_id,null,CategoryList.this);

    }
    public void clickedArea(MenuItem item) {
          VALUE = area;
        MyAsyncTask myAsyncTask=new MyAsyncTask(this,2);
        myAsyncTask.execute(areaUrl);
    }
    public void clickedfavorite(MenuItem item) {
        VALUE = favorites;
        favoritesmethod();
    }
    public void clickedsignout(MenuItem item){
        auth.signOut();
        this.startActivity(new Intent(this,LoginActivity.class));
    }
    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader(this) {
            @Override
            public Object loadInBackground() {
                VALUE=favorites;
                Cursor favcursor=getContentResolver().query(Uri.parse(Contract.FavRecipeData.CONTENT_URI+""),null,null,null,null);
                favList =new ArrayList<>();
                if (favcursor.getCount()>0){
                    if (favcursor.moveToFirst()){
                        do {
                            ItemDetail itemDetail = new ItemDetail();
                            itemDetail.setIdMeal(favcursor.getString(1));
                            itemDetail.setStrMeal(favcursor.getString(2));
                            itemDetail.setStrCategory(favcursor.getString(3));
                            itemDetail.setStrArea(favcursor.getString(4));
                            itemDetail.setStrMealThumb(favcursor.getString(5));
                            itemDetail.setStrInstructions(favcursor.getString(6));
                            itemDetail.setStrYoutube(favcursor.getString(7));
                            itemDetail.setStrSource(favcursor.getString(8));
                            favList.add(itemDetail);
                        }while (favcursor.moveToNext());
                    }
                }
                else {
                    Snackbar.make(mySwipeRefreshLayout,com.example.user.recipebook.R.string.nofavadded, Snackbar.LENGTH_LONG)
                            .setAction(R.string.action, null).show();
                }
                return favList;
            }
            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                forceLoad();
            }
        };
    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        FavAdapter favAdapter=new FavAdapter(this,favList);
        recyclerView.setAdapter(favAdapter);

    }

    @Override
    public void onLoaderReset(Loader loader) {

    }

}
