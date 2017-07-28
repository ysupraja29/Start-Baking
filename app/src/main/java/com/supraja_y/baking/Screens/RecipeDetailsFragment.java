
package com.supraja_y.baking.Screens;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.supraja_y.baking.Adapters.RecipeDetailAdapter;
import com.supraja_y.baking.Models.Ingredient;
import com.supraja_y.baking.Models.Recipe;
import com.supraja_y.baking.R;
import com.supraja_y.baking.Widget.UpdateingBakingService;

import java.util.ArrayList;
import java.util.List;

import static com.supraja_y.baking.Screens.RecipeDetailsActivity.REP_SCROLL_POSITION;
import static com.supraja_y.baking.Screens.RecipeDetailsActivity.SEL_POSITION;
import static com.supraja_y.baking.Screens.RecipeDetailsActivity.SEL_RECIPES;

/**
 * Created by suprajayerranagu on 24/07/17.
 */

public class RecipeDetailsFragment extends Fragment {

    ArrayList<Recipe> recipe_list;
    String recipeName;
    RecyclerView recyclerView;
    TextView textView;
    private int mScrollPosition;
    private int lastFirstVisiblePosition=0;
    private ScrollView scrollview_recipe_detail_text;

    public RecipeDetailsFragment() {

    }
//    @Override
//    protected Parcelable onSaveInstanceState() {
//        Parcelable superState = super.onSaveInstanceState();
//        RecyclerView.LayoutManager layoutManager = getLayoutManager();
//        if(layoutManager != null && layoutManager instanceof LinearLayoutManager){
//            mScrollPosition = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
//        }
//        SavedState newState = new SavedState(superState);
//        newState.mScrollPosition = mScrollPosition;
//        return newState;
//    }
//
//    @Override
//    protected void onRestoreInstanceState(Parcelable state) {
//        super.onRestoreInstanceState(state);
//        if(state != null && state instanceof SavedState){
//            mScrollPosition = ((SavedState) state).mScrollPosition;
//            RecyclerView.LayoutManager layoutManager = getLayoutManager();
//            if(layoutManager != null){
//                int count = layoutManager.getChildCount();
//                if(mScrollPosition != RecyclerView.NO_POSITION && mScrollPosition < count){
//                    layoutManager.scrollToPosition(mScrollPosition);
//                }
//            }
//        }
//    }
//
//    static class SavedState extends android.view.View.BaseSavedState {
//        public int mScrollPosition;
//        SavedState(Parcel in) {
//            super(in);
//            mScrollPosition = in.readInt();
//        }
//        SavedState(Parcelable superState) {
//            super(superState);
//        }
//
//        @Override
//        public void writeToParcel(Parcel dest, int flags) {
//            super.writeToParcel(dest, flags);
//            dest.writeInt(mScrollPosition);
//        }
//        public static final Parcelable.Creator<SavedState> CREATOR
//                = new Parcelable.Creator<SavedState>() {
//            @Override
//            public SavedState createFromParcel(Parcel in) {
//                return new SavedState(in);
//            }
//
//            @Override
//            public SavedState[] newArray(int size) {
//                return new SavedState[size];
//            }
//        };
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.recipe_detail_fragment_bp, container, false);
        textView = (TextView) rootView.findViewById(R.id.recipe_detail_text);
        scrollview_recipe_detail_text = (ScrollView) rootView.findViewById(R.id.scrollview_recipe_detail_text);
        recipe_list = new ArrayList<>();
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recipe_detail_recycler);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        if (savedInstanceState != null) {
            recipe_list = savedInstanceState.getParcelableArrayList(SEL_RECIPES);
            lastFirstVisiblePosition = savedInstanceState.getInt(SEL_POSITION);

            final int[] position = savedInstanceState.getIntArray(REP_SCROLL_POSITION);
            if (position != null)
                scrollview_recipe_detail_text.post(new Runnable() {
                    public void run() {
                        scrollview_recipe_detail_text.scrollTo(position[0], position[1]);
                    }
                });

        } else {
            recipe_list = getArguments().getParcelableArrayList(SEL_RECIPES);
        }
        List<Ingredient> ingredients = recipe_list.get(0).getIngredients();
        recipeName = recipe_list.get(0).getName();
        ArrayList<String> recipeIngredientsForWidgets = new ArrayList<>();
        ingredients.forEach((a) ->
        {
            textView.append("\u2022 " + a.getIngredient() + "\n");
            textView.append("\t\t\t Quantity: " + a.getQuantity().toString() + "\n");
            textView.append("\t\t\t Measure: " + a.getMeasure() + "\n\n");

            recipeIngredientsForWidgets.add(a.getIngredient() + "\n" +
                    "Quantity: " + a.getQuantity().toString() + "\n" +
                    "Measure: " + a.getMeasure() + "\n");
        });



        RecipeDetailAdapter mRecipeDetailAdapter = new RecipeDetailAdapter((RecipeDetailsActivity) getActivity());
        recyclerView.setAdapter(mRecipeDetailAdapter);
        mRecipeDetailAdapter.setMasterRecipeData(recipe_list, getContext());
        UpdateingBakingService.startBakingService(getContext(), recipeIngredientsForWidgets);
        recyclerView.getLayoutManager().scrollToPosition(lastFirstVisiblePosition);

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle currentState) {
        super.onSaveInstanceState(currentState);
        currentState.putParcelableArrayList(SEL_RECIPES, recipe_list);
        currentState.putString("Title", recipeName);
        lastFirstVisiblePosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
        currentState.putIntArray(REP_SCROLL_POSITION,
                new int[]{scrollview_recipe_detail_text.getScrollX(), scrollview_recipe_detail_text.getScrollY()});
        currentState.putInt(SEL_POSITION, lastFirstVisiblePosition);

    }



}


