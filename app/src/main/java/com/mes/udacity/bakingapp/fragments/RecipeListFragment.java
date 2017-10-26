package com.mes.udacity.bakingapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mes.udacity.bakingapp.adapters.RecipeListAdapter;
import com.mes.udacity.bakingapp.R;
import com.mes.udacity.bakingapp.listeners.ListItemClickListener;
import com.mes.udacity.bakingapp.listeners.RecipeListListener;
import com.mes.udacity.bakingapp.models.Recipe;
import com.mes.udacity.bakingapp.viewmodels.RecipeListViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mohamed on 10/21/2017.
 */

public class RecipeListFragment extends Fragment implements RecipeListListener,
        ListItemClickListener{

    public interface RecipeActivityCallBack {
        public void onItemClicked(String recipeStr);
    }

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private RecipeListAdapter recipeListAdapter;
    private RecipeListViewModel recipeListViewModel;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        recipeListViewModel = new RecipeListViewModel(context, this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recipeListViewModel.onCreate();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recipe_list_fragment,container,false);
        progressBar = (ProgressBar) view.findViewById(R.id.recipes_progress_bar);
        recyclerView = (RecyclerView) view.findViewById(R.id.recipes_recyclerview);
        if(recipeListAdapter == null){
            recipeListAdapter = new RecipeListAdapter(new ArrayList<Recipe>(), getContext(),this);
        }
        if (recipeListAdapter.getItemCount() == 0) {
            progressBar.setVisibility(View.VISIBLE);
        }
        int numberOfRows = getResources().getInteger(R.integer.col_num);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), numberOfRows);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(recipeListAdapter);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        recipeListViewModel.requestRecipesList();
    }

    @Override
    public void onResume() {
        super.onResume();
        recipeListViewModel.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        recipeListViewModel.onDestroy();
    }

    @Override
    public void onListReady(List<Recipe> recipes) {
        progressBar.setVisibility(View.GONE);
        recipeListAdapter.updateRecipeList(recipes);
    }

    @Override
    public void onError(String message) {
        progressBar.setVisibility(View.GONE);
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG);
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        Recipe recipe = recipeListAdapter.getRecipeAt(clickedItemIndex);
        Gson gson = new Gson();
        String recipeStr = gson.toJson(recipe);
        ((RecipeActivityCallBack)getActivity()).onItemClicked(recipeStr);
    }
}
