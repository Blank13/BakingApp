package com.mes.udacity.bakingapp.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.google.gson.Gson;
import com.mes.udacity.bakingapp.R;
import com.mes.udacity.bakingapp.fragments.RecipeStepFragment;
import com.mes.udacity.bakingapp.listeners.OnStepChangeListener;
import com.mes.udacity.bakingapp.models.RecipeStep;

/**
 * Created by Mohamed on 10/23/2017.
 */

public class RecipeStepActivity extends AppCompatActivity implements OnStepChangeListener{

    private FragmentManager fragmentManager;
    private String recipeStepStr;
    private int position = 0;
    private int stepsCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredient_detail);
        Intent intent = getIntent();
        if (savedInstanceState != null) {
            recipeStepStr = savedInstanceState.getString(getString(R.string.detail_intent_key));
            position = savedInstanceState.getInt(getString(R.string.position_intent_key), 0);
            stepsCount = savedInstanceState.getInt(getString(R.string.count_intent_key), 0);
        }
        if (intent != null && intent.hasExtra(getString(R.string.detail_intent_key)) && position == 0) {
            recipeStepStr = intent.getStringExtra(getString(R.string.detail_intent_key));
            position = intent.getIntExtra(getString(R.string.position_intent_key), 0);
            stepsCount = intent.getIntExtra(getString(R.string.count_intent_key), 0);
        }
        Bundle args = new Bundle();
        args.putString(getString(R.string.detail_intent_key),recipeStepStr);
        args.putInt(getString(R.string.position_intent_key), position);
        args.putInt(getString(R.string.count_intent_key), stepsCount);
        fragmentManager = getSupportFragmentManager();
        String fragmentName = getString(R.string.step_fragment);
        Fragment fragment = fragmentManager.findFragmentByTag(fragmentName);
        if (fragment == null) {
            fragment = new RecipeStepFragment();
        }
        fragment.setArguments(args);
        fragmentManager.beginTransaction()
                .replace(R.id.recipe_detail_detail_container, fragment, fragmentName)
                .commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(getString(R.string.detail_intent_key),recipeStepStr);
        outState.putInt(getString(R.string.position_intent_key), position);
        outState.putInt(getString(R.string.count_intent_key), stepsCount);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public void changeStepTo(int position) {
        RecipeDetailActivity parent = RecipeDetailActivity.instance;
        RecipeStep recipeStep = parent.getDesiredRecipeStep(position);
        Gson gson = new Gson();
        String dataStr = gson.toJson(recipeStep);
        this.recipeStepStr = dataStr;
        this.position = position;
        Bundle args = new Bundle();
        args.putString(getString(R.string.detail_intent_key),dataStr);
        args.putInt(getString(R.string.position_intent_key), position);
        args.putInt(getString(R.string.count_intent_key), stepsCount);
        RecipeStepFragment fragment = new RecipeStepFragment();
        fragment.setArguments(args);
        fragmentManager.beginTransaction()
                .replace(R.id.recipe_detail_detail_container,
                        fragment, getString(R.string.step_fragment)).commit();
    }
}
