package com.mes.udacity.bakingapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.google.gson.Gson;
import com.mes.udacity.bakingapp.R;
import com.mes.udacity.bakingapp.activities.RecipeDetailActivity;
import com.mes.udacity.bakingapp.models.Recipe;

/**
 * Created by Mohamed on 10/26/2017.
 */

public class RecipeWidgetProvider extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                String recipeString, int appWidgetId) {

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_ingredient_widget);
        views.setTextViewText(R.id.widget_recipe_name, new Gson().fromJson(recipeString, Recipe.class).getName());
        Intent intent = new Intent(context, RecipeWidgetListService.class);
        views.setRemoteAdapter(R.id.widget_ingredient_list, intent);
        Intent appIntent = new Intent(context, RecipeDetailActivity.class);
        PendingIntent appPendingIntent = PendingIntent.getActivity(context, 0, appIntent
                , PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.widget_ingredient_list, appPendingIntent);
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        //Start the intent service update widget action, the service takes care of updating the widgets UI
        RecipeWidgetService.startActionUpdateBakingWidgets(context);
    }

    public static void updateBakingWidgets(Context context,
                                           AppWidgetManager appWidgetManager,
                                           String recipeString, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, recipeString, appWidgetId);
        }
    }
}
