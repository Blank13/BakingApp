package com.mes.udacity.bakingapp.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.google.gson.Gson;
import com.mes.udacity.bakingapp.R;
import com.mes.udacity.bakingapp.fragments.RecipeDetailListFragment;
import com.mes.udacity.bakingapp.fragments.RecipeIngredientFragment;
import com.mes.udacity.bakingapp.fragments.RecipeStepFragment;
import com.mes.udacity.bakingapp.listeners.OnStepChangeListener;
import com.mes.udacity.bakingapp.models.Recipe;
import com.mes.udacity.bakingapp.models.RecipeStep;
import com.mes.udacity.bakingapp.widget.RecipeWidgetService;


/**
 * Created by Mohamed on 10/22/2017.
 */

public class RecipeDetailActivity  extends AppCompatActivity
        implements RecipeDetailListFragment.RecipeDetailActivityCallBack, OnStepChangeListener{

    private FragmentManager fragmentManager;
    private Recipe recipe;
    private boolean mTwoPane;

    public static RecipeDetailActivity instance;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail_list);
        fragmentManager = getSupportFragmentManager();
        attachListDetailActivity(fragmentManager,R.id.recipe_detail_list_container);
        if (findViewById(R.id.recipe_detail_detail_container) != null) {
            mTwoPane = true;
        }
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(getString(R.string.recipe_intent_key))) {
            Gson gson = new Gson();
            String recipeJson = intent.getStringExtra(getString(R.string.recipe_intent_key));
            SharedPreferences.Editor editor = this.getSharedPreferences(
                    this.getString(R.string.shared_preference_name), MODE_PRIVATE).edit();
            editor.putString(this.getString(R.string.shared_preference_recipe), recipeJson);
            editor.apply();
            RecipeWidgetService.startActionUpdateBakingWidgets(this);
            recipe = gson.fromJson(recipeJson,Recipe.class);
        }
    }

    public RecipeStep getDesiredRecipeStep(int position) {
        return recipe.getRecipeSteps().get(position - 1);
    }

    private void attachListDetailActivity(FragmentManager fragmentManager,
                                      int recipe_detail_list_container) {
        String fragmentName = getString(R.string.recipe_fragment);
        Fragment fragment = fragmentManager.findFragmentByTag(fragmentName);
        if (fragment == null) {
            fragment = new RecipeDetailListFragment();
        }
        fragmentManager.beginTransaction()
                .replace(recipe_detail_list_container, fragment, fragmentName)
                .commit();
    }

    @Override
    public void onItemClicked(String dataStr, int position, int count) {
        if (mTwoPane) {
            Bundle args = new Bundle();
            args.putString(getString(R.string.detail_intent_key),dataStr);
            args.putInt(getString(R.string.position_intent_key), position);
            args.putInt(getString(R.string.count_intent_key), count);
            if (position == 0) {
                RecipeIngredientFragment fragment = new RecipeIngredientFragment();
                fragment.setArguments(args);
                fragmentManager.beginTransaction()
                        .replace(R.id.recipe_detail_detail_container,
                        fragment, getString(R.string.ingredient_fragment)).commit();
            }
            else {
                RecipeStepFragment fragment = new RecipeStepFragment();
                fragment.setArguments(args);
                fragmentManager.beginTransaction()
                        .replace(R.id.recipe_detail_detail_container,
                                fragment, getString(R.string.step_fragment)).commit();
            }
        }
        else{
            Intent intent;
            if (position == 0) {
                intent = new Intent(this, RecipeIngredientListActivity.class);
            }
            else {
                intent = new Intent(this, RecipeStepActivity.class);
            }
            intent.putExtra(getString(R.string.detail_intent_key),dataStr);
            intent.putExtra(getString(R.string.position_intent_key),position);
            intent.putExtra(getString(R.string.count_intent_key), count);
            instance = this;
            startActivity(intent);
        }
    }

    @Override
    public void changeStepTo(int position) {
        RecipeStep step = recipe.getRecipeSteps().get(position - 1);
        Gson gson = new Gson();
        int count = recipe.getRecipeSteps().size() + 1;
        String dataStr = gson.toJson(step);
        if (mTwoPane) {
            Bundle args = new Bundle();
            args.putString(getString(R.string.detail_intent_key),dataStr);
            args.putInt(getString(R.string.position_intent_key), position);
            args.putInt(getString(R.string.count_intent_key), count);
            RecipeStepFragment fragment = new RecipeStepFragment();
            fragment.setArguments(args);
            fragmentManager.beginTransaction()
                    .replace(R.id.recipe_detail_detail_container,
                            fragment, getString(R.string.step_fragment)).commit();
        }
    }
}
