package com.mes.udacity.bakingapp.dataservice;

import android.app.DownloadManager;
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mes.udacity.bakingapp.Utils.Enums;
import com.mes.udacity.bakingapp.Utils.MyRequestQueue;
import com.mes.udacity.bakingapp.Utils.StaticEvents;
import com.mes.udacity.bakingapp.models.Recipe;

import org.greenrobot.eventbus.EventBus;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by Mohamed on 10/22/2017.
 */

public class RecipeListDataService {

    private final String baseUrl = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";
//    private RequestQueue mRequestQueue;
    private Context context;
//
    public RecipeListDataService(Context context) {
       this.context = context;
    }

    public void getRecipe(){
        try {
            StringRequest recipeRequest = new StringRequest(Request.Method.GET, baseUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Gson gson =new Gson();
                            Type listType = new TypeToken<ArrayList<Recipe>>() {
                            }.getType();
                            ArrayList<Recipe> recipeArrayList =
                                    gson.fromJson(response.replace("\"\""," null ").trim(),
                                    listType);
                            StaticEvents.RecipeListEvent recipeListEvent =
                                    new StaticEvents.RecipeListEvent(Enums.EventType.Success,
                                            recipeArrayList,
                                            null);
                            EventBus.getDefault().post(recipeListEvent);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                            StaticEvents.RecipeListEvent recipeListEvent =
                                    new StaticEvents.RecipeListEvent(Enums.EventType.Error,
                                            null,
                                            error.getMessage());
                            EventBus.getDefault().post(recipeListEvent);
                        }
                    });
            MyRequestQueue.getInstance(context).addToRequestQueue(recipeRequest);
        } catch (Exception e) {
            StaticEvents.RecipeListEvent recipeListEvent =
                    new StaticEvents.RecipeListEvent(Enums.EventType.Error,
                            null,
                            e.getMessage());
            EventBus.getDefault().post(recipeListEvent);
        }
    }
}