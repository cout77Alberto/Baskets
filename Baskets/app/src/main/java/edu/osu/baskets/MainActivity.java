package edu.osu.baskets;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.FragmentManager;
import android.view.View;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        android.app.FragmentManager fm = getFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.navbar_container);

        if (fragment == null){
            fragment = new NavbarFragment();
                fm.beginTransaction()
                        .add(R.id.navbar_container, fragment)
                        .commit();
        }
    }

    public void onClick(View view) {
        Fragment fragment;
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        switch (view.getId()) {
            case R.id.trades_ib:
                fragment = new TradesFragment();
                transaction.replace(R.id.fragment_container, fragment);
                break;
            case R.id.kitchen_ib:
                fragment = new KitchenFragment();
                transaction.replace(R.id.fragment_container, fragment);
                break;
            case R.id.inventory_ib:
                fragment = new InventoryFragment();
                transaction.replace(R.id.fragment_container, fragment);
                break;
            case R.id.map_ib:
                fragment = new MapFragment();
                transaction.replace(R.id.fragment_container, fragment);
                break;
        }
        transaction.addToBackStack(null);
        transaction.commit();
    }
    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called");
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
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }
}
