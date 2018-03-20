package edu.osu.baskets;

import java.util.UUID;

/**
 * Model class for user account.
 */

public class Account {
    private String mName;
    private UUID mID;

    public Account(String name) {
        mName = name;
        mID = UUID.randomUUID();
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public UUID getID() {
        return mID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Account account = (Account) o;

        return mID.equals(account.mID);
    }
}
