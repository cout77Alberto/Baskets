package edu.osu.baskets;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.osu.baskets.recipes.BaseRecipe;
import edu.osu.baskets.recipes.CookingHistory;
import edu.osu.baskets.recipes.RecipeBook;
import edu.osu.baskets.recipes.RecipeInfoActivity;

public class LeaderActivity extends AppCompatActivity {

    private RecyclerView mLeaderView;
    private List<Account> accounts;
    private LeaderAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader);

        Toolbar mainToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(mainToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        AccountSingleton accountSingleton = AccountSingleton.get();
        accounts = new ArrayList<>();

        // firebase stuff to get account from db
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("users");
        ValueEventListener accountsListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                accounts.clear();
                for (DataSnapshot accountSnapshot: dataSnapshot.getChildren()) {
                    Account account = accountSnapshot.getValue(Account.class);
                    accounts.add(account);
                }
                Collections.sort(accounts, new SortByCalories());
                // update view on change
                updateUI();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("Leader", "Error reading from firebase", databaseError.toException());
            }
        };
        mDatabase.addValueEventListener(accountsListener);

        mLeaderView = (RecyclerView) findViewById(R.id.leader_recycler_view);
        mLeaderView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void updateUI() {
        mAdapter = new LeaderAdapter(accounts);
        mLeaderView.setAdapter(mAdapter);
    }

    private class AccountHolder extends RecyclerView.ViewHolder {
        private TextView mRankView, mNameView, mCaloriesView;
        private Account mAccount;
        public AccountHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_account, parent, false));
            mRankView = (TextView) itemView.findViewById(R.id.account_rank);
            mNameView = (TextView) itemView.findViewById(R.id.account_name);
            mCaloriesView = (TextView) itemView.findViewById(R.id.account_calories);
        }

        public void bind(Account account, int position) {
            mAccount = account;
            mRankView.setText("" + (position + 1));
            mNameView.setText("Name: " + account.getName());
            // Note: setText on calories does weird things if string concat is removed
            mCaloriesView.setText("Calories: " + account.getCalories());
        }
    }

    private class LeaderAdapter extends RecyclerView.Adapter<LeaderActivity.AccountHolder> {

        private List<Account> mAccounts;

        public LeaderAdapter(List<Account> accounts) {
            mAccounts = accounts;
        }
        @Override
        public LeaderActivity.AccountHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(LeaderActivity.this);
            return new AccountHolder(layoutInflater, parent);
        }

        @Override
        public int getItemCount() {
            return mAccounts.size();
        }

        @Override
        public void onBindViewHolder(LeaderActivity.AccountHolder holder, int position) {
            Account account = mAccounts.get(position);
            holder.bind(account, position);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.homeAsUp:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}