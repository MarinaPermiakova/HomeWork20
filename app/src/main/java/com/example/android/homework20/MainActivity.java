package com.example.android.homework20;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences mySharedPreferences;
    private static String TEXT = "note_text";
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mySharedPreferences = getSharedPreferences(getString(R.string.large_text), MODE_PRIVATE);

        SharedPreferences.Editor myEditor = mySharedPreferences.edit();
        String s = String.valueOf(getText(R.string.large_text));
        myEditor.putString(TEXT, s);
        myEditor.apply();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ListView list = findViewById(R.id.list);
        final List<Map<String, String>> values = prepareContent();
        final BaseAdapter listContentAdapter = createAdapter(values);
        list.setAdapter(listContentAdapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                values.remove(position);
                listContentAdapter.notifyDataSetChanged();
            }
        });

        mSwipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                final List<Map<String, String>> values = prepareContent();
                final BaseAdapter listContentAdapter = createAdapter(values);
                list.setAdapter(listContentAdapter);
                mSwipeRefreshLayout.setRefreshing(false);
                listContentAdapter.notifyDataSetChanged();
            }
        });
    }

    @NonNull
    private BaseAdapter createAdapter(List<Map<String, String>> values) {
        return new SimpleAdapter(this, values,
                R.layout.simple_adapter,
                new String[]{"text", "number"},
                new int[]{R.id.textView, R.id.textView2});
    }

    @NonNull
    private List<Map<String, String>> prepareContent() {
        List<Map<String, String>> arrayList = new ArrayList<>();
        Map<String, String> map;

        String[] arrayContent = mySharedPreferences.getString(TEXT, "").split("\n\n");
        for (String s : arrayContent) {
            map = new HashMap<>();
            map.put("text", s);
            map.put("number", String.valueOf(s.length()));
            arrayList.add(map);
        }
        return arrayList;
    }
}