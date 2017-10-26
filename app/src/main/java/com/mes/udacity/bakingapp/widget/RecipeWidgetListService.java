package com.mes.udacity.bakingapp.widget;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.google.gson.Gson;
import com.mes.udacity.bakingapp.R;
import com.mes.udacity.bakingapp.models.Recipe;
import com.mes.udacity.bakingapp.models.RecipeIngredient;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Mohamed on 10/26/2017.
 */

public class RecipeWidgetListService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RecipeListRemoteViewFactory(getApplicationContext());
    }
}

class RecipeListRemoteViewFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context context;
    private Recipe recipe;

    public RecipeListRemoteViewFactory(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        SharedPreferences prefs = context.getSharedPreferences(
                context.getString(R.string.shared_preference_name), MODE_PRIVATE);
        String recipeString = prefs.getString(context.getString(R.string.shared_preference_recipe), "");
        recipe = new Gson().fromJson(recipeString, Recipe.class);
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        if (recipe == null){
            return 0;
        }
        return recipe.getIngredients() == null ? 0 : recipe.getIngredients().size();
    }

    @Override
    public RemoteViews getViewAt(int i) {
        List<RecipeIngredient> ingredients = recipe.getIngredients();
        if (ingredients == null || ingredients.isEmpty()) {
            return null;
        }
        RecipeIngredient ingredient = ingredients.get(i);
        RemoteViews view = new RemoteViews(context.getPackageName(),
                R.layout.recipe_ingredient_widget_item);
        view.setTextViewText(R.id.widget_ingredient_quantity, ingredient.getQuantity() + " " +
                ingredient.getMeasure());
        view.setTextViewText(R.id.widget_ingredient_name, ingredient.getName());
        Gson gson = new Gson();
        Bundle extras = new Bundle();
        extras.putString(context.getString(R.string.recipe_intent_key), gson.toJson(recipe));
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);
        view.setOnClickFillInIntent(R.id.widget_ingredient_item, fillInIntent);
        return view;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}