package com.mes.udacity.bakingapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mes.udacity.bakingapp.R;
import com.mes.udacity.bakingapp.models.RecipeIngredient;

import java.util.List;

/**
 * Created by Mohamed on 10/23/2017.
 */

public class RecipeIngredientListAdapter extends RecyclerView.Adapter<RecipeIngredientListAdapter.IngredientViewHolder> {

    private List<RecipeIngredient> ingredientsList;
    private Context context;

    public RecipeIngredientListAdapter(List<RecipeIngredient> ingredientsList, Context context) {
        this.ingredientsList = ingredientsList;
        this.context = context;
    }

    @Override
    public IngredientViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.ingredient_item, parent, false);
        return new IngredientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(IngredientViewHolder holder, int position) {
        RecipeIngredient ingredient = ingredientsList.get(position);
        holder.ingredientName.setText(ingredient.getName());
        holder.ingredientMeasure.setText(ingredient.getMeasure());
        holder.ingredientQuantity.setText(String.valueOf(ingredient.getQuantity()));
    }

    @Override
    public int getItemCount() {
        return ingredientsList != null ? ingredientsList.size() : 0;
    }

    public class IngredientViewHolder extends RecyclerView.ViewHolder {

        private TextView ingredientName;
        private TextView ingredientMeasure;
        private TextView ingredientQuantity;

        public IngredientViewHolder(View itemView) {
            super(itemView);
            ingredientName = itemView.findViewById(R.id.ingredient_name);
            ingredientMeasure = itemView.findViewById(R.id.ingredient_measure);
            ingredientQuantity = itemView.findViewById(R.id.ingredient_quantity);
        }
    }
}
