package com.mes.udacity.bakingapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mes.udacity.bakingapp.R;
import com.mes.udacity.bakingapp.adapters.RecipeIngredientListAdapter;
import com.mes.udacity.bakingapp.models.RecipeIngredient;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by Mohamed on 10/23/2017.
 */

public class RecipeIngredientFragment extends Fragment{

    private ArrayList<RecipeIngredient> recipeIngredients;
    private RecyclerView recyclerView;
    private RecipeIngredientListAdapter ingredientListAdapter;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recipe_ingredient_detail_fragment,container,false);
        Intent intent = getActivity().getIntent();
        Gson gson = new Gson();
        String ingredientsStr;
        if (intent != null && intent.hasExtra(getString(R.string.detail_intent_key))) {
            ingredientsStr = intent.getStringExtra(getString(R.string.detail_intent_key));
        }
        else {
            Bundle args = getArguments();
            ingredientsStr = args.getString(getString(R.string.detail_intent_key));
        }
        Type listType = new TypeToken<ArrayList<RecipeIngredient>>() {
        }.getType();
        recipeIngredients = gson.fromJson(ingredientsStr,listType);
        recyclerView = view.findViewById(R.id.recipe_ingredient_recyclerview);
        ingredientListAdapter = new RecipeIngredientListAdapter(recipeIngredients,getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(ingredientListAdapter);
        return view;
    }
}
