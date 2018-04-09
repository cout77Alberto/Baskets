package edu.osu.baskets;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AccountSingleton {

    private static AccountSingleton sAccountSingleton;
    private Account mAccount;

    public static AccountSingleton get() {
        if (sAccountSingleton == null) {
            sAccountSingleton = new AccountSingleton();
        }
        return sAccountSingleton;
    }

    private AccountSingleton() {
        mAccount = new Account();
    }

    public Account getAccount() {
        return mAccount;
    }

    public void setAccount(Account account) {
        mAccount = account;
    }

    public String getName() {
        return mAccount.getName();
    }

    public void setName(String name) {
        mAccount.setName(name);
    }

    public String getId() {
        return mAccount.getId();
    }

    public void setId(String id) {
        mAccount.setId(id);
    }

    public int getCalories() {
        return mAccount.getCalories();
    }

    public void setCalories(int calories) {
        mAccount.setCalories(calories);
    }

    public void addCalories(int newCalories) {
        int currentCalories = mAccount.getCalories();
        mAccount.setCalories(currentCalories + newCalories);
    }

    public void pushAccount() {
        // get reference to account firebase entry using id
        if(mAccount.getId()!=null) {
            DatabaseReference mDatabase = FirebaseDatabase.getInstance()
                    .getReference("users").child(mAccount.getId());
            // push account to 'users' node, overwriting old user
            mDatabase.setValue(mAccount);
        }
    }
}
