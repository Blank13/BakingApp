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
import com.mes.udacity.bakingapp.R;
import com.mes.udacity.bakingapp.adapters.RecipeDetailListAdapter;
import com.mes.udacity.bakingapp.listeners.ListItemClickListener;
import com.mes.udacity.bakingapp.models.Recipe;
import com.mes.udacity.bakingapp.models.RecipeStep;

/**
 * Created by Mohamed on 10/22/2017.
 */

public class RecipeDetailListFragment extends Fragment implements ListItemClickListener{

    public interface RecipeDetailActivityCallBack {
        void onItemClicked(String dataStr, int position, int size);
    }

    private RecyclerView recyclerView;
    private RecipeDetailListAdapter recipeDetailListAdapter;
    private Recipe recipe;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recipe_detail_list_fragment,container,false);
        Intent intent = getActivity().getIntent();
        if (intent != null && intent.hasExtra(getString(R.string.recipe_intent_key))) {
            Gson gson = new Gson();
            String recipeJson = intent.getStringExtra(getString(R.string.recipe_intent_key));
            recipe = gson.fromJson(recipeJson,Recipe.class);
        }
        recyclerView = view.findViewById(R.id.recipe_detail_recyclerview);
        if (recipeDetailListAdapter == null) {
            recipeDetailListAdapter = new RecipeDetailListAdapter(recipe.getRecipeSteps(),
                    getContext(),this);
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(recipeDetailListAdapter);
        return view;
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        Gson gson = new Gson();
        String data;
        if (clickedItemIndex == 0) {
            data = gson.toJson(recipe.getIngredients());
        }
        else {
            RecipeStep recipeStep = recipeDetailListAdapter.getRecipeStepAt(clickedItemIndex);
            data = gson.toJson(recipeStep);
        }
        ((RecipeDetailActivityCallBack)getActivity()).onItemClicked(data, clickedItemIndex,
                recipeDetailListAdapter.getItemCount() - 1);
    }

}
