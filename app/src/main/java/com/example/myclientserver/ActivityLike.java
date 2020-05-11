package com.example.myclientserver;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

import okhttp3.HttpUrl;

public class ActivityLike extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private LikesAdapter mExampleAdapter;
    private ArrayList<LikesItem> mExampleList;
    DataBaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_like);


        mRecyclerView = findViewById(R.id.recycler_views);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mExampleList = new ArrayList<>();


        dbHelper = new DataBaseHelper(this);
        final SQLiteDatabase database = dbHelper.getWritableDatabase();
        Cursor cursor = database.query(DataBaseHelper.TABLE_CONTACTS, null, null, null, null, null, null);
        ArrayList<String> BD = new ArrayList();
        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(DataBaseHelper.KEY_ID);
            int nameIndex = cursor.getColumnIndex(DataBaseHelper.URL);
            do {
                BD.add(cursor.getString(nameIndex));
                Log.d("mLog", "ID = " + cursor.getInt(idIndex) +
                        ", URL = " + cursor.getString(nameIndex));
            } while (cursor.moveToNext());
        } else
            Log.d("mLog", "0 rows");
        cursor.close();

        parse(BD);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_select, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.cats:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void parse(ArrayList<String> BD) {
        mExampleList.clear();
        int size = BD.size();
        if (size <= 10) {
            for (int i = 0; i < BD.size(); i++) {
                String catUrl = BD.get(i);
                mExampleList.add(new LikesItem(catUrl));
                mExampleAdapter = new LikesAdapter(ActivityLike.this, mExampleList);
                mRecyclerView.setAdapter(mExampleAdapter);
            }

        } else {
            int length = BD.size() - 10;
            for (int i = length; i < BD.size(); i++) {
                String catUrl = BD.get(i);
                mExampleList.add(new LikesItem(catUrl));
                mExampleAdapter = new LikesAdapter(ActivityLike.this, mExampleList);
                mRecyclerView.setAdapter(mExampleAdapter);
            }
        }

    }

}
