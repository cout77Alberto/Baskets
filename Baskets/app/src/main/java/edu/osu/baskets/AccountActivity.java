package edu.osu.baskets;

import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

public class AccountActivity extends AppCompatActivity {

    private TextView mAccountName;
    private TextView mAccountCalories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        Toolbar mainToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(mainToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        AccountSingleton accountSingleton = AccountSingleton.get();

        mAccountName = (TextView) findViewById(R.id.account_name);
        mAccountName.setText("Name: " + accountSingleton.getName());
        mAccountCalories = (TextView) findViewById(R.id.account_calories);
        mAccountCalories.setText("Calories: " + accountSingleton.getCalories());
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
