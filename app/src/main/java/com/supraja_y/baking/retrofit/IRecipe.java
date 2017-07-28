

package com.supraja_y.baking.retrofit;

import com.supraja_y.baking.Models.Recipe;

import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by suprajayerranagu on 22/07/17.
 */

public interface IRecipe {
    @GET("baking.json")
    Call<ArrayList<Recipe>> getRecipe();
}