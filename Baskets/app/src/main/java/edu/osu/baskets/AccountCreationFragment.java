package edu.osu.baskets;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AccountCreationFragment extends Fragment {

    private static final String EXTRA_USER_ID = "user_id";

    private EditText mNameBox;
    private Button mSaveButton;
    private String mName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_account_creation, container, false);

        mNameBox = (EditText) v.findViewById(R.id.account_name);
        mNameBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                mSaveButton.setEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mName = s.toString();
                mSaveButton.setEnabled(true);
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Nothing
            }
        });

        mSaveButton = (Button) v.findViewById(R.id.save_button);
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference mDatabase = FirebaseDatabase.getInstance()
                        .getReference("users");

                // creating new user node, which returns the unique key value
                // new user node would be /users/$userid/
                String userId = mDatabase.push().getKey();

                Account account = new Account(mName, userId);

                // pushing new account to 'users' node using userId
                mDatabase.child(userId).setValue(account);

                // save userId
                Account.saveUserId(getActivity(), userId);

                // save account
                AccountSingleton accountSingleton = AccountSingleton.get();
                accountSingleton.setAccount(account);

                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });

        return v;
    }
}
