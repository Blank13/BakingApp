package com.mes.udacity.bakingapp.viewmodels;

import android.content.Context;

import com.mes.udacity.bakingapp.Utils.StaticEvents;
import com.mes.udacity.bakingapp.dataservice.RecipeListDataService;
import com.mes.udacity.bakingapp.listeners.RecipeListListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by Mohamed on 10/22/2017.
 */

public class RecipeListViewModel {

    public Context mContext;
    private RecipeListListener mListener;
    private StaticEvents.RecipeListEvent recipeListEvent;
    private RecipeListDataService recipeListDataService;

    public RecipeListViewModel(Context mContext, RecipeListListener mListener) {
        this.mContext = mContext;
        this.mListener = mListener;
        recipeListDataService = new RecipeListDataService(mContext);
    }

    public void onCreate(){
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    public void onResume() {
        if (recipeListEvent == null) {
            requestRecipesList();
        }
        else {
            notifyRegisteredLisiteners(recipeListEvent);
        }
    }

    public void requestRecipesList() {
        recipeListDataService.getRecipe();
    }

    public void onDestroy() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRecipeListEvent(StaticEvents.RecipeListEvent recipeListEvent){
        this.recipeListEvent = recipeListEvent;
        notifyRegisteredLisiteners(recipeListEvent);
    }

    private void notifyRegisteredLisiteners(StaticEvents.RecipeListEvent recipeListEvent) {
        switch (recipeListEvent.getEventType()){
            case Success:
                mListener.onListReady(recipeListEvent.getRecipeList());
                break;
            case Error:
                mListener.onError(recipeListEvent.getErrorMessage());
                break;
        }
    }
}
