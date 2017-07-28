package com.supraja_y.baking.Screens;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.supraja_y.baking.R;
import com.supraja_y.baking.Adapters.RecipeDetailAdapter;
import com.supraja_y.baking.Models.Recipe;
import com.supraja_y.baking.Models.Step;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by suprajayerranagu on 23/07/17.
 */

public class RecipeDetailsActivity extends AppCompatActivity implements RecipeDetailAdapter.ListItemClickListener, RecipeStepFragment.ListItemClickListener {

    static String ALL_RECIPES = "All_Recipes";
    static String SEL_RECIPES = "Selected_Recipes";
    static String SEL_STEPS = "Selected_Steps";
    static String SEL_POSITION = "Selected_Position";
    static String STACK_DETAIL = "STACK_RECIPE_DETAIL";
    static String STACK_STEP_DETAIL = "STACK_RECIPE_STEP_DETAIL";
    static String REP_SCROLL_POSITION = "REP_SCROLL_POSITION";



    private ArrayList<Recipe> recipe;
    String recipeName;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_recipe_detail);

        if (savedInstanceState == null) {

            Bundle selectedRecipeBundle = getIntent().getExtras();

            recipe = new ArrayList<>();
            recipe = selectedRecipeBundle.getParcelableArrayList(SEL_RECIPES);
            recipeName = recipe.get(0).getName();

            final RecipeDetailsFragment fragment = new RecipeDetailsFragment();
            fragment.setArguments(selectedRecipeBundle);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, fragment).addToBackStack(STACK_DETAIL)
                    .commit();

            if (findViewById(R.id.recipe_linear_layout).getTag() != null && findViewById(R.id.recipe_linear_layout).getTag().equals("tablet-land")) {

                final RecipeStepFragment fragment2 = new RecipeStepFragment();
                fragment2.setArguments(selectedRecipeBundle);
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container2, fragment2).addToBackStack(STACK_STEP_DETAIL)
                        .commit();

            }


        } else {
            recipeName = savedInstanceState.getString("Title");
        }


        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(recipeName);

        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                if (findViewById(R.id.fragment_container2) == null) {
                    if (fm.getBackStackEntryCount() > 1) {
                        //go back to "Recipe Detail" screen
                        fm.popBackStack(STACK_DETAIL, 0);
                    } else if (fm.getBackStackEntryCount() > 0) {
                        finish();

                    }


                } else {
                    finish();

                }

            }
        });
    }


    @Override
    public void onListItemClick(List<Step> stepsOut, int selectedItemIndex, String recipeName) {


        final RecipeStepFragment fragment = new RecipeStepFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();

        getSupportActionBar().setTitle(recipeName);
        Bundle stepBundle = new Bundle();
        stepBundle.putParcelableArrayList(SEL_STEPS, (ArrayList<Step>) stepsOut);
        stepBundle.putInt(SEL_POSITION, selectedItemIndex);
        stepBundle.putString("Title", recipeName);
        fragment.setArguments(stepBundle);
        if (findViewById(R.id.recipe_linear_layout).getTag() != null && findViewById(R.id.recipe_linear_layout).getTag().equals("tablet-land")) {
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container2, fragment).addToBackStack(STACK_STEP_DETAIL)
                    .commit();

        } else {
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, fragment).addToBackStack(STACK_STEP_DETAIL)
                    .commit();
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("Title", recipeName);
    }


}
