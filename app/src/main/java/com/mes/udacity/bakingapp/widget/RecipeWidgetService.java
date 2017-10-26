package com.mes.udacity.bakingapp.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.mes.udacity.bakingapp.R;
import com.mes.udacity.bakingapp.models.Recipe;

/**
 * Created by Mohamed on 10/26/2017.
 */

public class RecipeWidgetService extends IntentService {

    public static final String ACTION_UPDATE_BAKING_WIDGET = "com.mes.udacity.bakingapp.action.update_baking_widgets";

    public RecipeWidgetService() {
        super("BakingAppWidgetService");
    }

    public static void startActionUpdateBakingWidgets(Context context) {
        Intent intent = new Intent(context, RecipeWidgetService.class);
        Bundle args = new Bundle();
        SharedPreferences prefs = context.getSharedPreferences(
                context.getString(R.string.shared_preference_name), MODE_PRIVATE);
        String recipeStr = prefs.getString(context.getString(R.string.shared_preference_recipe), "");
        Gson gson = new Gson();
        Recipe recipe = gson.fromJson(recipeStr, Recipe.class);
        args.putString(context.getString(R.string.recipe_intent_key),recipeStr);
        intent.setAction(ACTION_UPDATE_BAKING_WIDGET);
        intent.putExtras(args);
        if (recipe != null) {
            context.startService(intent);
        }
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_UPDATE_BAKING_WIDGET.equals(action)) {
                final String recipeString = intent.getExtras().getString(getString(R.string.recipe_intent_key));
                handleActionUpdateBakingWidgets(recipeString);
            }
        }
    }

    private void handleActionUpdateBakingWidgets(String recipeString) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, RecipeWidgetProvider.class));
        //Trigger data update to handle the ListView widgets and force a data refresh
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_ingredient_list);
        //Now update all widgets
        RecipeWidgetProvider.updateBakingWidgets(this, appWidgetManager, recipeString, appWidgetIds);
    }
}
