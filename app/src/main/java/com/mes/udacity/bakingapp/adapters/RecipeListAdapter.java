package com.mes.udacity.bakingapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mes.udacity.bakingapp.R;
import com.mes.udacity.bakingapp.listeners.ListItemClickListener;
import com.mes.udacity.bakingapp.models.Recipe;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Mohamed on 10/21/2017.
 */

public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.RecipeViewHolder> {

    private List<Recipe> recipeList;
    private Context context;
    private ListItemClickListener itemClickListener;

    public RecipeListAdapter(List<Recipe> recipeList, Context context, ListItemClickListener itemClickListener) {
        this.recipeList = recipeList;
        this.context = context;
        this.itemClickListener = itemClickListener;
    }

    @Override
    public RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recipe_item, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipeViewHolder holder, int position) {
        Recipe recipe = recipeList.get(position);
        holder.recipeNameTextView.setText(recipe.getName());
        holder.recipeServingTextView.setText(String.valueOf(recipe.getServings()));
        if (recipe.getImage() != null && !recipe.getImage().isEmpty()) {
            Picasso.with(context).load(recipe.getImage()).into(holder.imageView);
        }
    }

    @Override
    public int getItemCount() {
        return recipeList != null ? recipeList.size() : 0;
    }

    public void updateRecipeList(List<Recipe> recipes) {
        recipeList = recipes;
        notifyDataSetChanged();
    }

    public Recipe getRecipeAt(int position) {
        return recipeList.get(position);
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView imageView;
        private TextView recipeNameTextView;
        private TextView recipeServingTextView;

        public RecipeViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.recipe_image);
            recipeNameTextView = itemView.findViewById(R.id.recipe_name);
            recipeServingTextView = itemView.findViewById(R.id.recipe_servings);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int clickedItemIndex = getAdapterPosition();
            itemClickListener.onListItemClick(clickedItemIndex);
        }

    }
}
