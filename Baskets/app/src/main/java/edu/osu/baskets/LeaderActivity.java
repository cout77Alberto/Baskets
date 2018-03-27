package edu.osu.baskets;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Collections;
import java.util.List;

public class LeaderActivity extends AppCompatActivity {

    private RecyclerView mLeaderView;
    private List<Account> accounts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader);

        Toolbar mainToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(mainToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        AccountSingleton accountSingleton = AccountSingleton.get();

        // firebase stuff to get account from db
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("users");
        ValueEventListener accountsListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                accounts = (List<Account>) dataSnapshot.getValue();


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("Leader", "Error reading from firebase", databaseError.toException());
            }
        };
        mDatabase.addListenerForSingleValueEvent(accountsListener);

        mLeaderView = (RecyclerView) findViewById(R.id.leader_recycler_view);
        mLeaderView.setLayoutManager(new LinearLayoutManager(this));

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