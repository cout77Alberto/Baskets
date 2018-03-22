package edu.osu.baskets;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.util.Log;

import edu.osu.baskets.recipes.CookingHistory;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private Inventory mInventory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar mainToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(mainToolbar);

        FoodUtils.PopulateConstructors(this);
        mInventory = Inventory.get(this);
        mInventory.AddItemToBasket(FoodUtils.Spawn("strawberries",7));
        mInventory.AddItemToBasket(FoodUtils.Spawn("water", 10));
        mInventory.AddItemToBasket(FoodUtils.Spawn("strawberries",4));
        mInventory.AddItemToBasket(FoodUtils.Spawn("strawberries",34));
        mInventory.AddItemToBasket(FoodUtils.Spawn("strawberries",35));
        mInventory.AddItemToBasket(FoodUtils.Spawn("strawberries",40));
        mInventory.AddItemToBasket(FoodUtils.Spawn("strawberries",40));
        mInventory.AddItemToBasket(FoodUtils.Spawn("strawberries",40));
        mInventory.AddItemToBasket(FoodUtils.Spawn("strawberries",40));
        mInventory.AddItemToBasket(FoodUtils.Spawn("strawberries",40));
        mInventory.AddItemToBasket(FoodUtils.Spawn("strawberries",35));

        mInventory.MoveToFridge(0);
        mInventory.MoveToFridge(1);

        //mInventory.RemoveItem("water", 1);
        mInventory.RemoveItem("strawberries", 2);
        mInventory.RemoveItem("strawberries", 3);
        mInventory.RemoveItem("strawberries", 35);

        android.app.FragmentManager fm = getFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.navbar_container);

        if (fragment == null){
            fragment = new NavbarFragment();
                fm.beginTransaction()
                        .add(R.id.navbar_container, fragment)
                        .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.account:
                Intent intent = new Intent(MainActivity.this, AccountActivity.class);
                startActivity(intent);
                return true;
            case R.id.leader:
                // start leader activity
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called");
        CookingHistory.get(this).initialize(this);
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
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
        CookingHistory.get(this).saveToFile(this);
    }
}
