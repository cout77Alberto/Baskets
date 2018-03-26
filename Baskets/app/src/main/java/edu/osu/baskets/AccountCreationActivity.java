package edu.osu.baskets;

import android.support.v4.app.Fragment;

public class AccountCreationActivity extends SingleFragmentActivity {

    @Override
    public Fragment createFragment() {
        return new AccountCreationFragment();
    }
}
