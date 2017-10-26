package com.mes.udacity.bakingapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mes.udacity.bakingapp.R;
import com.mes.udacity.bakingapp.listeners.ListItemClickListener;
import com.mes.udacity.bakingapp.models.RecipeStep;

import java.util.List;

/**
 * Created by Mohamed on 10/22/2017.
 */

public class RecipeDetailListAdapter extends RecyclerView.Adapter<RecipeDetailListAdapter.RecipeDetailViewHolder>{

    private List<RecipeStep> steps;
    private Context context;
    private ListItemClickListener itemClickListener;

    public RecipeDetailListAdapter(List<RecipeStep> steps, Context context, ListItemClickListener itemClickListener) {
        this.steps = steps;
        this.context = context;
        this.itemClickListener = itemClickListener;
    }

    @Override
    public RecipeDetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recipe_detail_item, parent, false);
        return new RecipeDetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipeDetailViewHolder holder, int position) {
        if (position == 0) {
            holder.detailTextView.setText(context.getText(R.string.ingredient_detail_text));
        }
        else {
            holder.detailTextView.setText(steps.get(position - 1).getShortDescription());
        }
    }

    @Override
    public int getItemCount() {
        return steps != null ? steps.size() + 1 : 0;
    }

    public RecipeStep getRecipeStepAt(int position) {
        return steps.get(position - 1);
    }

    public class RecipeDetailViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView detailTextView;

        public RecipeDetailViewHolder(View itemView) {
            super(itemView);
            detailTextView = itemView.findViewById(R.id.recipe_detail_text);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int clickedItemIndex = getAdapterPosition();
            itemClickListener.onListItemClick(clickedItemIndex);
        }
    }

}
