package edu.osu.baskets;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import edu.osu.baskets.foods.IFood;
import edu.osu.baskets.recipes.BaseRecipe;
import edu.osu.baskets.recipes.CookingHistory;
import edu.osu.baskets.recipes.RecipeBook;
import edu.osu.baskets.recipes.RecipeInfoActivity;
import edu.osu.baskets.recipes.strawberry_shake;

/**
 * Created by Alberto on 2/19/2018.
 */

public class KitchenFragment extends Fragment {
    private boolean toggle = true;
    private static final String TAG = "KitchenFragment";
    private Button mRecipesButton, mHistoryButton;
    private RecyclerView mRecyclerView;
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
            mRecyclerView.setAdapter(new RecipeAdapter(recipes));
        } else {
            mRecyclerView.setAdapter(new RecipeAdapter(history));
        }
    }

    private class RecipeHolder extends RecyclerView.ViewHolder {
        private TextView mTitleTextView, mCreatedTextView;
        private Button mMakeButton;
        private BaseRecipe mRecipe;
        private RecyclerView mIngredientRecycler;
        public RecipeHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_recipe, parent, false));
            mCreatedTextView = (TextView) itemView.findViewById(R.id.created_text);
            mTitleTextView = (TextView) itemView.findViewById(R.id.recipe_title);
            mIngredientRecycler = (RecyclerView) itemView.findViewById(R.id.ingredient_recycler);

            mTitleTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(toggle) {
                        Uri theUri = Uri.parse(mRecipe.url);
                        Intent LaunchBrowserIntent =
                                new Intent(Intent.ACTION_VIEW,
                                        theUri);
                        startActivity(
                                LaunchBrowserIntent);
                    }else{
                        RecipeInfoActivity.sRecipe = new strawberry_shake(getActivity());
                        Intent intent = new Intent(getActivity(),RecipeInfoActivity.class);
                        startActivity(intent);
                    }
                }
            });
            mMakeButton = (Button) itemView.findViewById(R.id.make_button);
            mMakeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mRecipe!=null) {
                        new MakeRecipeTask().execute(mRecipe);
                    }
                }
            });

            mIngredientRecycler.setLayoutManager(new GridLayoutManager(getActivity(), 5));

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
            mIngredientRecycler.setAdapter(new IngredientAdapter(recipe.getIngredients()));
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
            holder.bind(mRecipes.get(position));
        }
    }

    private class IngredientHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView mThumbnailImageView;
        private TextView mAmountTextView;
        private IFood mIngred;
        public IngredientHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.grid_item_food, parent, false));
            itemView.setOnClickListener(this);

            mThumbnailImageView = itemView.findViewById(R.id.iv_food_thumbnail);
            mAmountTextView = itemView.findViewById(R.id.tv_stack_size);
            itemView.findViewById(R.id.tv_days_to_expire).setVisibility(View.INVISIBLE);
            itemView.findViewById(R.id.pb_expiration_bar).setVisibility(View.INVISIBLE);
        }

        public void bind(IFood item) {
            mIngred = item;
            if (item.GetImageResId() != 0) {
                mThumbnailImageView.setBackgroundResource(item.GetImageResId());
            } else {
                mThumbnailImageView.setBackgroundResource(android.R.drawable.btn_star_big_on);
            }
            mAmountTextView.setText(String.format("%d", item.GetStackSize()));
        }

        @Override
        public void onClick(View v) {
            new ShowIngredDetailSnackbarTask().execute(mIngred);
        }
    }
    private class IngredientAdapter extends RecyclerView.Adapter<IngredientHolder> {
        private List<IFood> mIngredients;

        public IngredientAdapter(List<IFood> ingreds) {
            mIngredients = ingreds;
        }

        @Override
        public IngredientHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            return new IngredientHolder(inflater, parent);
        }

        @Override
        public void onBindViewHolder(IngredientHolder holder, int position) {
            holder.bind(mIngredients.get(position));
        }

        @Override
        public int getItemCount() {
            return mIngredients.size();
        }
    }

    private class MakeRecipeTask extends AsyncTask<BaseRecipe, Void, BaseRecipe> {
        @Override
        protected BaseRecipe doInBackground(BaseRecipe... iRecipes) {
            iRecipes[0].make(getActivity());
            return iRecipes[0];
        }

        @Override
        protected void onPostExecute(BaseRecipe recipe) {
            updateUI();
            Toast.makeText(getActivity(),"Calories Increased by " + recipe.getCalories(),Toast.LENGTH_LONG).show();
        }
    }

    private class ShowIngredDetailSnackbarTask extends AsyncTask<IFood, Void, Void> {
        @Override
        protected Void doInBackground(IFood... iFoods) {
            Snackbar.make(getActivity().findViewById(R.id.kitchen_coordinator), String.format("%s  [%d cal]", iFoods[0].GetName(), iFoods[0].GetCalories()), Snackbar.LENGTH_SHORT).show();
            return null;
        }
    }
}
