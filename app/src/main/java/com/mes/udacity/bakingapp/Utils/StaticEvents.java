package com.mes.udacity.bakingapp.Utils;

import com.mes.udacity.bakingapp.models.Recipe;

import java.util.List;

/**
 * Created by Mohamed on 10/22/2017.
 */

public abstract class StaticEvents {

    public static class RecipeListEvent {
        private Enums.EventType eventType;
        private List<Recipe> recipeList;
        private String errorMessage;

        public RecipeListEvent(Enums.EventType eventType, List<Recipe> recipeList, String errorMessage) {
            this.eventType = eventType;
            this.recipeList = recipeList;
            this.errorMessage = errorMessage;
        }

        public Enums.EventType getEventType() {
            return eventType;
        }

        public List<Recipe> getRecipeList() {
            return recipeList;
        }

        public String getErrorMessage() {
            return errorMessage;
        }
    }

}
