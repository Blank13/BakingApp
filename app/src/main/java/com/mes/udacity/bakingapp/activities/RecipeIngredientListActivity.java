package com.mes.udacity.bakingapp.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.mes.udacity.bakingapp.R;
import com.mes.udacity.bakingapp.fragments.RecipeIngredientFragment;

/**
 * Created by Mohamed on 10/23/2017.
 */

public class RecipeIngredientListActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredient_detail);
        fragmentManager = getSupportFragmentManager();
        attachDetailFragment(fragmentManager, R.id.recipe_detail_detail_container);
    }

    private void attachDetailFragment(FragmentManager fragmentManager, int fragmentContainer) {
        String fragmentName = getString(R.string.ingredient_fragment);
        Fragment fragment = fragmentManager.findFragmentByTag(fragmentName);
        if (fragment == null) {
            fragment = new RecipeIngredientFragment();
        }
        fragmentManager.beginTransaction()
                .replace(fragmentContainer, fragment, fragmentName)
                .commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }
}
