
package com.supraja_y.baking.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.supraja_y.baking.R;
import com.supraja_y.baking.Models.Recipe;
import com.supraja_y.baking.Models.Step;

import java.util.List;
/**
 * Created by suprajayerranagu on 21/07/17.
 */

public class RecipeDetailAdapter extends RecyclerView.Adapter<RecipeDetailAdapter.RecyclerViewHolder> {
    List<Step> listSteps;
    private String recipeName;
    final private ListItemClickListener listOnClickListener;

    public interface ListItemClickListener {
        void onListItemClick(List<Step> stepsOut, int clickedItemIndex, String recipeName);
    }

    public RecipeDetailAdapter(ListItemClickListener listener) {
        listOnClickListener = listener;
    }

    public void setMasterRecipeData(List<Recipe> recipesIn, Context context) {
        //lSteps = recipesIn;
        listSteps = recipesIn.get(0).getSteps();
        recipeName = recipesIn.get(0).getName();
        notifyDataSetChanged();
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();

        int layoutIdForListItem = R.layout.detail_card_items;

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForListItem, viewGroup, false);

        RecyclerViewHolder viewHolder = new RecyclerViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        holder.textRecyclerView.setText(listSteps.get(position).getId() + ". " + listSteps.get(position).getShortDescription());

    }

    @Override
    public int getItemCount() {
        return listSteps != null ? listSteps.size() : 0;
    }

    class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textRecyclerView;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            textRecyclerView = (TextView) itemView.findViewById(R.id.shortDescription);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            listOnClickListener.onListItemClick(listSteps, clickedPosition, recipeName);
        }

    }
}
