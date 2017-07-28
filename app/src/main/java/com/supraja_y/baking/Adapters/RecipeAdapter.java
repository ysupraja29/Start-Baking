


package com.supraja_y.baking.Adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.supraja_y.baking.R;
import com.supraja_y.baking.Models.Recipe;

import java.util.ArrayList;
/**
 * Created by suprajayerranagu on 21/07/17.
 */
public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecyclerViewHolder> {
    ArrayList<Recipe> listRecipes;
    Context mContext;
    final private ListItemClickListener lOnClickListener;

    public interface ListItemClickListener {
        void onListItemClick(Recipe clickedItemIndex);
    }

    public RecipeAdapter(ListItemClickListener listener) {
        lOnClickListener = listener;
    }

    public void setRecipeData(ArrayList<Recipe> recipesIn, Context context) {
        listRecipes = recipesIn;
        mContext = context;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.recipe_card_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForListItem, viewGroup, false);
        RecyclerViewHolder viewHolder = new RecyclerViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        holder.textRecyclerView.setText(listRecipes.get(position).getName());
        String imageUrl = listRecipes.get(position).getImage();
        if (imageUrl != "") {
            Uri builtUri = Uri.parse(imageUrl).buildUpon().build();
            Picasso.with(mContext).load(builtUri).into(holder.imageRecyclerView);
        }

    }

    @Override
    public int getItemCount() {
        return listRecipes != null ? listRecipes.size() : 0;
    }

    class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textRecyclerView;
        ImageView imageRecyclerView;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            textRecyclerView = (TextView) itemView.findViewById(R.id.title);
            imageRecyclerView = (ImageView) itemView.findViewById(R.id.recipeImage);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            lOnClickListener.onListItemClick(listRecipes.get(clickedPosition));
        }

    }
}
