package com.example.yulialysenko.test1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

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
import java.util.List;

/**
 * Created by yulialysenko on 16.04.2018.
 */

public class SecondActivity extends AppCompatActivity {
    private ListView lstText;
    List<String> headers = new ArrayList<>();
    List<String> texts = new ArrayList<>();

    public List<String> merge(List<String> a, List<String> b) {
        List<String> res = new ArrayList<>();
        for (int i = 0; i < a.size(); i++) {
            res.add(a.get(i));
            res.add(b.get(i));
        }
        return res;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        this.lstText = findViewById(R.id.text_view_id);
        RequestQueue queue = Volley.newRequestQueue(this);
        Intent intent = getIntent();
        final String article = intent.getExtras().getString("article");

        String url = "http://mikonatoruri.win/post.php?article=" + article;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        List<String> news = new ArrayList<>();
                        try {
                            news.add("Команда 1: " + response.getString("team1") +
                                    "\n" + "Kоманда 2: " + response.getString("team2") +
                                    "\n" + "Время игры: " + response.getString("time"));
                            news.add("Турнир: " + response.getString("tournament") +
                                    "\n" + "Место проведения: " + response.getString("place"));
                            JSONArray articles = response.getJSONArray("article");
                            for (int i = 0; i < articles.length(); i++) {
                                headers.add(articles.getJSONObject(i).getString("header"));
                                texts.add(articles.getJSONObject(i).getString("text"));
                            }
                            news.addAll(merge(headers, texts));
                            news.add("Прогноз: " + response.getString("prediction"));
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(SecondActivity.this, R.layout.list_view_row, R.id.listText, news);
                            lstText.setAdapter(adapter);
                        } catch (JSONException e) {
                            Toast.makeText(SecondActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SecondActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }

        });

        queue.add(jsonObjectRequest);
    }

}
