package edu.osu.baskets;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Splash screen fragment.
 */
public class SplashFragment extends Fragment implements View.OnTouchListener{
    protected boolean mIsActive = true;
    protected int mSplashTime = 1000;
    protected int mTimeIncrement = 100;
    protected int mSleepTime = 100;
    private String TAG = "SplashFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_splash, container, false);
        v.setOnTouchListener(this);
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        // Thread for displaying the Splash Screen
        Thread splashThread = new Thread() {
            @Override
            public void run() {
                try {
                    int elapsedTime = 0;
                    while (mIsActive && (elapsedTime < mSplashTime)) {
                        sleep(mSleepTime);
                        if (mIsActive) elapsedTime = elapsedTime + mTimeIncrement;
                    }
                } catch (InterruptedException e) {
                    // do nothing
                } finally {
                    getActivity().finish();
                    // Check if user has account, if not, send to account creation activity
                    if (Account.localUserIdExists(getActivity())) {
                        // get userId from storage
                        String userId = Account.readUserId(getActivity());

                        // initialize food constructors and get inventory from storage
                        FoodUtils.PopulateConstructors(getActivity());
                        Inventory.get(getActivity()).LoadFromFile(getString(R.string.inventory_save_file));

                        // enable firebase local persistence
                        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

                        // firebase stuff to get account from db
                        DatabaseReference mDatabase = FirebaseDatabase.getInstance()
                                .getReference("users").child(userId);
                        ValueEventListener accountListener = new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Account account = dataSnapshot.getValue(Account.class);
                                AccountSingleton accountSingleton = AccountSingleton.get();
                                accountSingleton.setAccount(account);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Log.w("Splash", "Error reading from firebase"
                                        , databaseError.toException());
                            }
                        };
                        mDatabase.addListenerForSingleValueEvent(accountListener);

                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(getActivity(), AccountCreationActivity.class);
                        startActivity(intent);
                    }
                }
            }
        };
        splashThread.start();
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            mIsActive = false;
            return true;
        }
        return false;
    }
}
