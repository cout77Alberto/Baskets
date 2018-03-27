package edu.osu.baskets;

import android.content.Context;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Comparator;
import java.util.Date;
import java.util.Scanner;

import edu.osu.baskets.recipes.BaseRecipe;

/**
 * Model class for user account.
 */

public class Account {
    private static final String TAG = "Account";

    private String mName;
    private String mId;
    private int mCalories;

    public Account() {}

    public Account(String name, String id) {
        mName = name;
        mId = id;
        mCalories = 0;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public int getCalories() {
        return mCalories;
    }

    public void setCalories(int calories) {
        mCalories = calories;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Account account = (Account) o;

        return mId.equals(account.mId);
    }

    static boolean localUserIdExists(Context context) {
        try {
            context.openFileInput("UserId.txt");
        }catch (Exception e){
            return false;
        }
        return true;
    }

    static void saveUserId(Context context, String userId) {
        FileOutputStream outputStream;
        try{
            outputStream = context.openFileOutput("UserId.txt",Context.MODE_PRIVATE);
            outputStream.write(userId.getBytes());
            outputStream.close();
            Log.d(TAG, "File was written to");
        }catch (Exception e){
            Log.d(TAG, "No file for userId write");
        }
    }

    static String readUserId(Context context) {
        InputStream  inputStream;
        String userId = "";
        try {
            inputStream = context.openFileInput("UserId.txt");
            Scanner scanner = new Scanner(inputStream);
            userId = scanner.nextLine();
        }catch (Exception e){
            Log.d(TAG, "No file for userId read "+e.getMessage());
        }
        return userId;
    }

    class SortByCalories implements Comparator<Account>
    {
        // Used for sorting in descending order of calories
        public int compare(Account a, Account b)
        {
            return a.getCalories() - b.getCalories();
        }
    }
}
