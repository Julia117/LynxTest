package com.example.yulialysenko.test1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ListView lstv;
    List<String> titles = new ArrayList<>();
    List<String> articles = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        List<String> categories = Arrays.asList("football", "hockey", "tennis", "basketball", "volleyball", "cybersport");
        lstv = findViewById(R.id.list);
        RequestQueue queue = Volley.newRequestQueue(this);
        for (String category : categories) {
            String url = "http://mikonatoruri.win/list.php?category=" + category;

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                System.out.println(1);
                                JSONArray events = response.getJSONArray("events");
                                for (int i = 0; i < events.length(); i++) {
                                    titles.add(events.getJSONObject(i).getString("title"));
                                    articles.add(events.getJSONObject(i).getString("article"));
                                }
                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, R.layout.list_view_row, R.id.listText, titles);
                                lstv.setAdapter(adapter);
                                lstv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                                        intent.putExtra("article", articles.get(position));
                                        startActivity(intent);
                                    }
                                });
                            } catch (JSONException e) {
                                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

            queue.add(jsonObjectRequest);
        }
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}