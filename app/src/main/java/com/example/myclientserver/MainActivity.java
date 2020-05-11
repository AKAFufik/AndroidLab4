package com.example.myclientserver;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.HttpUrl;

public class MainActivity extends AppCompatActivity  implements ExampleAdapter.OnItemClickListener {
    private boolean loading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;


    private RecyclerView mRecyclerView;
    private ExampleAdapter mExampleAdapter;
    private ArrayList<ExampleItem> mExampleList;
    private RequestQueue mRequestQueue;
    private JsonArrayRequest requests;
    DataBaseHelper dataBaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dataBaseHelper = new DataBaseHelper(this);


        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        final LinearLayoutManager mLayoutManager;
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mExampleList = new ArrayList<>();
        mRequestQueue = Volley.newRequestQueue(this);


        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) { //check for scroll down
                    visibleItemCount = mLayoutManager.getChildCount();
                    totalItemCount = mLayoutManager.getItemCount();
                    pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();

                    if (loading) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            loading = false;
                            Log.v("...", "Last Item Wow !");
                            HttpUrl.Builder urlBuilder = HttpUrl.parse("https://api.thecatapi.com/v1/images/search").newBuilder();
                            urlBuilder.addQueryParameter("limit", "100");
                            urlBuilder.addQueryParameter("page", "10");
                            urlBuilder.addQueryParameter("order", "Desc");
                            urlBuilder.addQueryParameter("mime_types", "gif,jpg,png");
                            String url = urlBuilder.build().toString();
                            parseJSON(url);
                        }
                    }
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String breed = "";
        switch (item.getItemId()) {
            case R.id.LiKE:
                Intent intent = new Intent(this,ActivityLike.class);
                startActivity(intent);
                finish();
            return true;
            case R.id.all:
                HttpUrl.Builder urlBuilder = HttpUrl.parse("https://api.thecatapi.com/v1/images/search").newBuilder();
                urlBuilder.addQueryParameter("limit", "100");
                urlBuilder.addQueryParameter("page", "10");
                urlBuilder.addQueryParameter("order", "Desc");
                urlBuilder.addQueryParameter("mime_types", "gif,jpg,png");
                String url = urlBuilder.build().toString();
                parseJSON(url);
                return true;
            case R.id.abyssianian:
                breed = "abys";
                breedUrl(breed);
                return true;
            case R.id.aegean:
                breed = "aege";
                breedUrl(breed);
                return true;
            case R.id.balinese:
                breed = "bali";
                breedUrl(breed);
                return true;
            case R.id.bengal:
                breed = "beng";
                breedUrl(breed);
                return true;
            case R.id.burmese:
                breed = "burm";
                breedUrl(breed);
                return true;
            case R.id.chartreux:
                breed = "char";
                breedUrl(breed);
                return true;
            case R.id.cymric:
                breed = "cymr";
                breedUrl(breed);
                return true;
            case R.id.persian:
                breed = "pers";
                breedUrl(breed);
                return true;
            case R.id.ragdoll:
                breed = "ragd";
                breedUrl(breed);
                return true;
            case R.id.savannah:
                breed = "sava";
                breedUrl(breed);
                return true;
            case R.id.sphynx:
                breed = "sphy";
                breedUrl(breed);
                return true;
            case R.id.toyger:
                breed = "toyg";
                breedUrl(breed);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void breedUrl(String breed) {
        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://api.thecatapi.com/v1/images/search").newBuilder();
        urlBuilder.addQueryParameter("breed_id", breed);
        urlBuilder.addQueryParameter("limit", "100");
        urlBuilder.addQueryParameter("page", "10");
        urlBuilder.addQueryParameter("order", "Desc");
        urlBuilder.addQueryParameter("mime_types", "gif,jpg,png");
        String url = urlBuilder.build().toString();
        parseJSON(url);

    }

    public void parseJSON(String url) {
        mExampleList.clear();
        requests = new JsonArrayRequest(url, new com.android.volley.Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject hit = null;
                for (int i = 1; i < response.length(); i++) {

                    try {
                        hit = response.getJSONObject(i);
                        String imageUrl = hit.getString("url");
                        mExampleList.add(new ExampleItem(imageUrl));
                        mExampleAdapter = new ExampleAdapter(MainActivity.this, mExampleList);
                        mRecyclerView.setAdapter(mExampleAdapter);
                        mExampleAdapter.setOnItemClickListener(MainActivity.this);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        mRequestQueue.add(requests);
    }
    public void buttonDislike(View view) {
        Toast.makeText(this, "Вам не понравилось", Toast.LENGTH_SHORT).show();

    }

    public void buttonLike(View view) {
        Toast.makeText(this, "Вам понравилось", Toast.LENGTH_SHORT).show();

    }
    @Override
    public void onItemClick(int position) {

        ExampleItem clickedItem = mExampleList.get(position);
        String url =clickedItem.getImageUrl();
        final SQLiteDatabase database = dataBaseHelper.getWritableDatabase();
        final ContentValues contentValues = new ContentValues();
        contentValues.put(DataBaseHelper.URL,(url));
        database.insert(DataBaseHelper.TABLE_CONTACTS, null, contentValues);
    }

}