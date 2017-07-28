
package com.supraja_y.baking.Screens;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.core.deps.guava.annotations.VisibleForTesting;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.supraja_y.baking.Adapters.RecipeAdapter;
import com.supraja_y.baking.IdlingResource.SimpleIdlingResource;
import com.supraja_y.baking.Models.Recipe;
import com.supraja_y.baking.R;

import java.util.ArrayList;

/**
 * Created by suprajayerranagu on 23/07/17.
 */
public class RecipeActivity extends AppCompatActivity implements RecipeAdapter.ListItemClickListener {

    static String ALL_RECIPES = "All_Recipes";
    static String SEL_RECIPE = "Selected_Recipes";
    static String SEL_STEPS = "Selected_Steps";
    static String SEL_POSITION = "Selected_Position";

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

        setContentView(R.layout.activity_recipe);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setHomeButtonEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setTitle("Baking Items");
        // Get the IdlingResource instance
        getIdlingResource();
    }

    @Override
    public void onListItemClick(Recipe selectedItemIndex) {

        Bundle selectedRecipeBundle = new Bundle();
        ArrayList<Recipe> selectedRecipe = new ArrayList<>();
        selectedRecipe.add(selectedItemIndex);
        selectedRecipeBundle.putParcelableArrayList(SEL_RECIPE, selectedRecipe);

        final Intent intent = new Intent(this, RecipeDetailsActivity.class);
        intent.putExtras(selectedRecipeBundle);
        startActivity(intent);

    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }


}
