package com.mes.udacity.bakingapp.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Mohamed on 10/22/2017.
 */

public class RecipeIngredient {

    @SerializedName("ingredient")
    private String name;

    @SerializedName("measure")
    private String measure;

    @SerializedName("quantity")
    private float quantity;

    public RecipeIngredient(String name, String measure, float quantity) {
        this.name = name;
        this.measure = measure;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public float getQuantity() {
        return quantity;
    }

    public void setQuantity(float quantity) {
        this.quantity = quantity;
    }
}
