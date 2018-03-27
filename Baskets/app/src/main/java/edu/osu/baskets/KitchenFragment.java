package edu.osu.baskets;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TimeUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import edu.osu.baskets.recipes.BaseRecipe;
import edu.osu.baskets.recipes.CookingHistory;
import edu.osu.baskets.recipes.RecipeBook;
import edu.osu.baskets.recipes.RecipeInfoActivity;

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
        List<BaseRecipe> history = cookingHistory.reverse();
        if (toggle) {
            mAdapter = new RecipeAdapter(recipes);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter = new RecipeAdapter(history);
            mRecyclerView.setAdapter(mAdapter);
        }
    }

    private class RecipeHolder extends RecyclerView.ViewHolder {
        private TextView mTitleTextView, mCreatedTextView;
        private Button mMakeButton;
        private BaseRecipe mRecipe;
        public RecipeHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_recipe, parent, false));
            mCreatedTextView = (TextView) itemView.findViewById(R.id.created_text);
            mTitleTextView = (TextView) itemView.findViewById(R.id.recipe_title);
            mTitleTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri theUri = Uri.parse(mRecipe.url);
                    Intent LaunchBrowserIntent =
                            new Intent(Intent.ACTION_VIEW,
                                    theUri);
                    startActivity(
                            LaunchBrowserIntent);
                }
            });
            mMakeButton = (Button) itemView.findViewById(R.id.make_button);
            mMakeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mRecipe!=null) {
                        mRecipe.make(getActivity());
                        updateUI();
                        Toast.makeText(getActivity(),"Calories Increased by "+mRecipe.getCalories(),Toast.LENGTH_LONG).show();
                    }
                }
            });
            if(!toggle){
                mMakeButton.setVisibility(View.INVISIBLE);
            }else{
                mCreatedTextView.setVisibility(View.INVISIBLE);
            }
        }

        public void bind(BaseRecipe recipe) {
            mRecipe = recipe;
            mTitleTextView.setText(mRecipe.getTitle());
            if(mRecipe.getLastCreated()!=null) {
                mCreatedTextView.setText(mRecipe.getLastCreated().toString());
            }
            if(!mRecipe.isCookable(getActivity())){
                mMakeButton.setEnabled(false);
                mMakeButton.setBackgroundColor(Color.RED);
            }else{
                mMakeButton.setEnabled(true);
                mMakeButton.setBackgroundColor(Color.GREEN);
            }
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
            BaseRecipe recipe = mRecipes.get(position);
            holder.bind(recipe);
        }
    }
}
