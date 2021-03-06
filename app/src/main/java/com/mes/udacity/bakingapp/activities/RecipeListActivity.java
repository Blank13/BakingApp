package com.mes.udacity.bakingapp.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.mes.udacity.bakingapp.fragments.RecipeListFragment;
import com.mes.udacity.bakingapp.R;
import com.mes.udacity.bakingapp.idlingresource.SimpleIdlingResource;

public class RecipeListActivity extends AppCompatActivity implements RecipeListFragment.RecipeActivityCallBack {

    private static final String TAG = RecipeListActivity.class.getSimpleName();
    private FragmentManager fragmentManager;

    @Nullable
    private SimpleIdlingResource mIdlingResource;

    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);
        fragmentManager = getSupportFragmentManager();
        attachRecipeFragment(fragmentManager,R.id.recipe_fragment_container);
    }

    private void attachRecipeFragment(FragmentManager fragmentManager,
                                      int recipe_fragment_container) {
        String fragmentName = getString(R.string.recipe_fragment);
        Fragment fragment = fragmentManager.findFragmentByTag(fragmentName);
        if (fragment == null) {
            fragment = new RecipeListFragment();
        }
        fragmentManager.beginTransaction()
                .replace(recipe_fragment_container, fragment, fragmentName)
                .commit();
    }

    @Override
    public void onItemClicked(String recipeStr) {
        Intent intent = new Intent(this, RecipeDetailActivity.class)
                .putExtra(getString(R.string.recipe_intent_key),recipeStr);
        startActivity(intent);
    }

}
