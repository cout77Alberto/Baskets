package edu.osu.baskets.recipes;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

import edu.osu.baskets.MainActivity;

/**
 * Created by Daniel on 3/9/2018.
 */

public class CookingHistory {
    private static final String TAG = "CookingHistory";
    private static CookingHistory sCookingHistory;
    private ArrayList<BaseRecipe> mRecipes = new ArrayList<BaseRecipe>();
    public static CookingHistory get(Context context) {
        if (sCookingHistory == null) {
            sCookingHistory = new CookingHistory(context);
        }
        return sCookingHistory;
    }

    private CookingHistory(Context context) {
        mRecipes = new ArrayList<>();
    }

    public ArrayList<BaseRecipe> reverse(){
        ArrayList<BaseRecipe> reverseList = new ArrayList<>();
        for(int i=mRecipes.size()-1;i>-1;i--){
            reverseList.add(mRecipes.get(i));
        }
        return reverseList;
    }
    public void add(BaseRecipe recipe){
        mRecipes.add(recipe);
    }
    public boolean hasRecipe(BaseRecipe r){
        return mRecipes.contains(r);
    }
    public ArrayList<BaseRecipe> getRecipes(){
        return  mRecipes;
    }
    public BaseRecipe getRecipe(String title) {
        for (BaseRecipe recipe : mRecipes) {
            if (recipe.getTitle().equals(title)) {
                return recipe;
            }
        }
        return null;
    }

    public void initialize(Context context) {
        InputStream  inputStream = null;
        try {
            inputStream = context.openFileInput("CookingHistory.txt");
            Scanner scanner = new Scanner(inputStream);
            mRecipes.clear();
            while(scanner.hasNext()) {
                String recipeData = scanner.nextLine();
                int i = recipeData.indexOf(':');
                Date date = new Date();
                date.setTime(new Long(recipeData.substring(i + 1, recipeData.length()-1)));
                BaseRecipe recipe = new BaseRecipe();
                recipe.setTitle(recipeData.substring(1, i));
                recipe.setLastCreated(date);
                mRecipes.add(recipe);
            }
        }catch (Exception e){
            Log.d(TAG, "no file for cooking history read "+e.getMessage());
        }

    }

    public void saveToFile(Context context) {
        FileOutputStream outputStream;
        String FileContents="";
        for (BaseRecipe recipe:mRecipes
             ) {
            FileContents = FileContents.concat("<" +recipe.getTitle()+":"+recipe.getLastCreated().getTime()+">\n");
        }
        try{
            outputStream = context.openFileOutput("CookingHistory.txt",Context.MODE_PRIVATE);
            outputStream.write(FileContents.getBytes());
            outputStream.close();
            Log.d(TAG, "File was written to");
        }catch (Exception e){
            Log.d(TAG, "no file for cooking history write");
        }
    }
}
