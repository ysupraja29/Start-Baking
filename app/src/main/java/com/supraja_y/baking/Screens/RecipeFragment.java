package com.supraja_y.baking.Screens;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.supraja_y.baking.IdlingResource.SimpleIdlingResource;
import com.supraja_y.baking.R;
import com.supraja_y.baking.Adapters.RecipeAdapter;
import com.supraja_y.baking.Models.Recipe;
import com.supraja_y.baking.retrofit.IRecipe;
import com.supraja_y.baking.retrofit.RetrofitBuilder;
import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.supraja_y.baking.Screens.RecipeActivity.ALL_RECIPES;


/**
 * Created by suprajayerranagu on 24/07/17.
 */
public class RecipeFragment extends Fragment  {



    public RecipeFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RecyclerView recyclerView;
        View rootView = inflater.inflate(R.layout.recipe_fragment_bp, container, false);
        recyclerView=(RecyclerView)  rootView.findViewById(R.id.recipe_recycler);
        final RecipeAdapter recipesAdapter =new RecipeAdapter((RecipeActivity)getActivity());
        recyclerView.setAdapter(recipesAdapter);
        if (rootView.getTag()!=null && rootView.getTag().equals("phone-land")){
            GridLayoutManager mLayoutManager = new GridLayoutManager(getContext(),4);
            recyclerView.setLayoutManager(mLayoutManager);
        }
        else {
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(mLayoutManager);
        }

        IRecipe iRecipe = RetrofitBuilder.Retrieve();
        Call<ArrayList<Recipe>> recipe_call = iRecipe.getRecipe();
        SimpleIdlingResource idlingResource = (SimpleIdlingResource)((RecipeActivity)getActivity()).getIdlingResource();

        if (idlingResource != null) {
            idlingResource.setIdleState(false);
        }

        recipe_call.enqueue(new Callback<ArrayList<Recipe>>() {
            @Override
            public void onResponse(Call<ArrayList<Recipe>> call, Response<ArrayList<Recipe>> response) {
                Integer statusCode = response.code();
                Log.v("status code: ", statusCode.toString());

                ArrayList<Recipe> recipes = response.body();

                Bundle recipesBundle = new Bundle();
                recipesBundle.putParcelableArrayList(ALL_RECIPES, recipes);

                recipesAdapter.setRecipeData(recipes,getContext());
                if (idlingResource != null) {
                    idlingResource.setIdleState(true);
                }
            }
            @Override
            public void onFailure(Call<ArrayList<Recipe>> call, Throwable t) {
                Log.v("http fail: ", t.getMessage());
                Toast.makeText(getContext(), "Something went wrong.", Toast.LENGTH_SHORT).show();

            }
        });
        return rootView;
    }


}
