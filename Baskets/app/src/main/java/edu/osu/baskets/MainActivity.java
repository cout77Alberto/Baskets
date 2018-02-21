package edu.osu.baskets;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.FragmentManager;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fm = getFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.navbar_container);

        if (fragment == null){
            fragment = new NavbarFragment();
                fm.beginTransaction()
                        .add(R.id.navbar_container, fragment)
                        .commit();
        }
    }

    public void onClick(View view) {
        Fragment fragment = new TradesFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.trades_container, fragment );
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
