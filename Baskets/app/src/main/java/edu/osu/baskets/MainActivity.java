package edu.osu.baskets;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.navbar_container);

        if (fragment == null){
            fragment = new NavbarFragment();
            fm.beginTransaction()
                    .add(R.id.navbar_container, fragment)
                    .commit();
        }
    }
}
