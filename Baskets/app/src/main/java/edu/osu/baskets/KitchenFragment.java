package edu.osu.baskets;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import edu.osu.baskets.recipes.BaseRecipe;
import edu.osu.baskets.recipes.CookingHistory;
import edu.osu.baskets.recipes.RecipeBook;

/**
 * Created by Alberto on 2/19/2018.
 */

public class KitchenFragment extends Fragment {
    private boolean toggle = true;
    private static final String TAG = "KitchenFragment";
    private Button mRecipesButton, mHistoryButton;
    private RecyclerView mRecyclerView;
    private RecipeAdapter mAdapter;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_kitchen, container, false);
        mRecipesButton = v.findViewById(R.id.button_recipes);
        mHistoryButton = v.findViewById(R.id.button_cooking_history);
        mRecipesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggle = true;
                mRecipesButton.setBackgroundColor(Color.GRAY);
                mHistoryButton.setBackgroundColor(Color.LTGRAY);
                updateUI();
            }
        });

        mHistoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggle = false;
                mRecipesButton.setBackgroundColor(Color.LTGRAY);
                mHistoryButton.setBackgroundColor(Color.GRAY);
                updateUI();
            }
        });
        mRecyclerView = v.findViewById(R.id.recipe_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();
        return v;
    }

    private void updateUI() {
        RecipeBook recipeBook = RecipeBook.get(getActivity());
        CookingHistory cookingHistory = CookingHistory.get(getActivity());
        List<BaseRecipe> recipes = recipeBook.getRecipes();
        List<BaseRecipe> history = cookingHistory.getRecipes();
        if(toggle) {
            mAdapter = new RecipeAdapter(recipes);
            mRecyclerView.setAdapter(mAdapter);
        }else {
            mAdapter = new RecipeAdapter(history);
            mRecyclerView.setAdapter(mAdapter);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }

    private class RecipeHolder extends RecyclerView.ViewHolder {
        private TextView mTitleTextView;
        private BaseRecipe mRecipe;

        public RecipeHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_recipe, parent, false));

            mTitleTextView = (TextView) itemView.findViewById(R.id.recipe_title);
        }

        public void bind(BaseRecipe crime) {
            mRecipe = crime;
            mTitleTextView.setText(mRecipe.getTitle());
        }
    }
    private class RecipeAdapter extends RecyclerView.Adapter<RecipeHolder> {

        private List<BaseRecipe> mRecipes;

        public RecipeAdapter(List<BaseRecipe> recipes) {
            mRecipes = recipes;
        }
        @Override
        public RecipeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());

            return new RecipeHolder(layoutInflater, parent);
        }

        @Override
        public int getItemCount() {
            return mRecipes.size();
        }
        @Override
        public void onBindViewHolder(RecipeHolder holder, int position) {
            BaseRecipe crime = mRecipes.get(position);
            holder.bind(crime);
        }
    }
}
