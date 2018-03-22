package edu.osu.baskets.recipes;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import edu.osu.baskets.R;

/**
 * Created by Daniel on 3/21/2018.
 */

public class RecipeInfoActivity extends AppCompatActivity {
    public static BaseRecipe sRecipe;
    private TextView mTitleTextView, mIngredientsTextView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_info);

        mTitleTextView = (TextView)findViewById(R.id.info_title);
        mTitleTextView.setText(sRecipe.getTitle());
        mIngredientsTextView = (TextView) findViewById(R.id.info_ingredients);
        String ingredients="";
        for (int i = 0; i < sRecipe.requiredFoods.size(); i++) {
            ingredients = ingredients.concat(sRecipe.requiredFoods.get(i).GetPrefab()+": "+sRecipe.requiredAmounts.get(i)+"\n");
        }
        mIngredientsTextView.setText(ingredients);
    }
    public static void setRecipe(BaseRecipe recipe){
        sRecipe = recipe;
    }
}
