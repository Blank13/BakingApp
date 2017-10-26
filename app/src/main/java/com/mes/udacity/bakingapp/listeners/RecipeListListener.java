package com.mes.udacity.bakingapp.listeners;

import com.mes.udacity.bakingapp.models.Recipe;

import java.util.List;

/**
 * Created by Mohamed on 10/22/2017.
 */

public interface RecipeListListener {

    void onListReady(List<Recipe> recipes);

    void onError(String message);

}
