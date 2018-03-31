package edu.osu.baskets;

import android.content.Context;

import java.util.ArrayList;

import edu.osu.baskets.foods.IFood;

/**
 * Created by Alberto on 3/19/2018.
 */

public class Container {
    private Context mContext;
    private ArrayList<IFood> mSlots;

    public Container(Context context, int capacity) {
        mContext = context;
        mSlots = new ArrayList<>(capacity);
        for (int i = 0; i < capacity; i++) {
            mSlots.add(null);
        }
    }

    public int GetSize() {
        return mSlots.size();
    }
    public ArrayList<IFood> GetSlots() {
        return mSlots;
    }
    public boolean AllSlotsFilled() {
        for (IFood slot : mSlots) {
            if (slot == null) { return false; }
        }
        return true;
    }

    public int CountItem(String prefab) {
        int sum = 0;
        for (IFood slot : find(prefab, mSlots)) {
            sum += slot.GetStackSize();
        }
        return sum;
    }

    // can check amounts > maxStackSize
    // returns amount of items that don't fit
    public int CountOverflowIfAdd(String prefab, int amount) {
        int maxStack = FoodUtils.Spawn(prefab).GetMaxStackSize();
        for (IFood slot : mSlots) {
            if (slot == null) { amount -= maxStack; }
        }
        amount -= CountEmptySpaceInNonFullStacks(prefab);
        if (amount > 0) {
            return amount;
        }
        return 0;
    }

    // counts how many more items non-full stacks can hold before reaching their maxStackSize
    private int CountEmptySpaceInNonFullStacks(String prefab) {
        int sum = 0;
        for (IFood slot : find(prefab, mSlots)) {
            if (!slot.IsFull()) {
                sum += (slot.GetMaxStackSize() - slot.GetStackSize());
            }
        }
        return sum;
    }

    // returns sum of all stack sizes of any non-full prefab stack, 0 otherwise
    private int CountAllSemiFullStacks(String prefab) {
        int sum = 0;
        for (IFood slot : find(prefab, mSlots)) {
            if (!slot.IsFull()) {
                sum += slot.GetStackSize();
            }
        }
        return sum;
    }

    // updates item
    // returns clone of item but with stackSize == amountToSeparate
    private IFood Split(IFood item, int amountToSeparate) {
        item.RemoveFromStackSize(amountToSeparate);
        IFood splitItem = item.Clone();
        splitItem.SetStackSize(amountToSeparate);
        return splitItem;
    }

    /**
     * Puts as many items from itemB into itemA.
     *
     * @requires itemA != null, itemB != null
     * @updates itemA
     * @clears itemB
     * @return what doesn't fit in itemA (non-null)
     **/
    private IFood CombineItems(IFood itemA, IFood itemB) {
        //assert
        if (!itemA.GetPrefab().equals(itemB.GetPrefab())) { throw new AssertionError(); }

        int stackA = itemA.GetStackSize();
        int stackB = itemB.GetStackSize();
        int maxStackSize = itemA.GetMaxStackSize();
        if (stackA + stackB <= maxStackSize) {
            //all items in B fit into A
            itemA.AddToStackSize(stackB);
            itemB.ClearStackSize();
            itemA.SetDaysToExpire(CombineSpoilage(itemA.GetDaysToExpire(), itemB.GetDaysToExpire(), stackA, stackB));
            return itemB;
        } else {
            //some items in B CANNOT fit into A
            IFood overflow = Split(itemB, stackA + stackB - maxStackSize);
            itemA.AddToStackSize(itemB.GetStackSize());
            itemA.SetDaysToExpire(CombineSpoilage(itemA.GetDaysToExpire(), itemB.GetDaysToExpire(), stackA, itemB.GetStackSize()));
            itemB.ClearStackSize();
            return overflow;
        }
    }

    private int CombineSpoilage(int expA, int expB, int stackA, int stackB) {
        float spoilage_round_up_thresh = Float.parseFloat(mContext.getResources().getString(R.string.spoilage_combination_thresh));
        float totalStack = stackA + stackB;
        float daysToExp = (expA * stackA/totalStack) + (expB * stackB/totalStack);
        float daysToExpTrunc = (float) Math.floor(daysToExp);

        if ((daysToExp - daysToExpTrunc) >= spoilage_round_up_thresh - 0.00001 ) {
            return (int) Math.ceil(daysToExp);
        } else {
            return (int) daysToExpTrunc;
        }
    }

    /**
     * Adds an item to the container, in a new slot if possible. If container has no empty slots,
     *      will combine with other stacks of same prefab and return any extra.
     * NOTE: could exit loop as soon as both overflow and item reach stackSize = 0
     *
     * @param
     *      item: item to add to container
     * @updates item
     * @return item stack that could not fit (non-null)
     */
    public IFood AddItem(IFood item) {
        int existingStackSpaceCanUse = 0;
        if (AllSlotsFilled()) { existingStackSpaceCanUse = CountEmptySpaceInNonFullStacks(item.GetPrefab()); }
        //overflow contains stack of items that need to be placed in an empty slot
        int overflowCount = item.GetStackSize() - existingStackSpaceCanUse;
        if (overflowCount < 0) { overflowCount = 0; }
        IFood overflow = Split(item, overflowCount);

        for (int slot = 0; slot < mSlots.size(); slot++) {
            if (mSlots.get(slot) == null && !overflow.IsEmpty()) {
                //add overflow
                mSlots.set(slot, overflow.Clone());
                overflow.ClearStackSize();
            } else if (mSlots.get(slot) != null) {
                //add to existing stack of prefab
                if (mSlots.get(slot).GetPrefab().equals(item.GetPrefab()) && !mSlots.get(slot).IsFull() && !item.IsEmpty()) {
                    item = CombineItems(mSlots.get(slot), item);
                }
            }
        }
        return overflow;
    }

    // if items same, add as many as can
    // if items different, replace item in position "slot"
    // returns overflow or item replaced (could be null)
    public IFood AddItemToSlot(IFood item, int slot) {
        IFood slotItem = mSlots.get(slot);
        if (slotItem == null) {
            //no item, use slot
            return mSlots.set(slot, item);
        } else if (slotItem.GetPrefab().equals(item.GetPrefab())) {
            //same items, combine
            return CombineItems(slotItem, item);
        } else {
            //different items, replace
            return mSlots.set(slot, item);
        }
    }

    // returns item replaced (could be null)
    public IFood ForceItemInSlot(IFood item, int slot) {
        return mSlots.set(slot, item);
    }

    /**
     * Removes [amount] number of [prefab] items from container. If one stack is not enough,
     *      will look thru all other stacks and remove from those that have same prefab.
     *
     * @requires amount <= total number of prefab items in container
     *           amount <= max stack size of prefab
     *           amount > 0
     * @param
     *      prefab: name of the item to remove
     * @param
     *      amount: how much of item to remove
     * @return item stack removed, w/ stackSize == amount (non-null)
     */
    public IFood RemoveItem(String prefab, int amount) {
        //asserts
        if (amount > CountItem(prefab)) { throw new AssertionError(); }
        if (amount > FoodUtils.Spawn(prefab).GetMaxStackSize()) { throw new AssertionError(); }
        if (amount <= 0) { throw new AssertionError(); }

        //borrow contains items that need to be taken from a full slot
        int borrowCount = amount - CountAllSemiFullStacks(prefab);
        if (borrowCount < 0) { borrowCount = 0; }
        int fromSemiFullsCount = amount - borrowCount;

        IFood removed = FoodUtils.Spawn(prefab,0);
        for (int slot : findIndices(prefab, mSlots)) {
            if (mSlots.get(slot).IsFull() && borrowCount > 0) {
                //remove all needed items from the full stack
                int splitAmount = borrowCount;
                if (splitAmount > mSlots.get(slot).GetStackSize()) { splitAmount = mSlots.get(slot).GetStackSize(); }
                IFood splitItems = Split(mSlots.get(slot), splitAmount);
                CombineItems(removed, splitItems);
                borrowCount -= splitAmount;
            } else if (!mSlots.get(slot).IsFull() && fromSemiFullsCount > 0) {
                //remove at most the number of items in the slot, decrement counter
                int splitAmount = fromSemiFullsCount;
                if (splitAmount > mSlots.get(slot).GetStackSize()) { splitAmount = mSlots.get(slot).GetStackSize(); }
                IFood splitItems = Split(mSlots.get(slot), splitAmount);
                CombineItems(removed, splitItems);
                fromSemiFullsCount -= splitAmount;
            }
            if (mSlots.get(slot).IsEmpty()) {
                //set slot to null if stack == 0
                mSlots.set(slot, null);
            }
        }
        return removed;
    }

    // returns item in position "slot"
    public IFood RemoveItemFromSlot(int slot) {
        return mSlots.set(slot, null);
    }

    public void AgeItemsByDays(int numDays) {
        for (int slot = 0; slot < mSlots.size(); slot++) {
            mSlots.get(slot).AgeByDays(numDays);
            if (mSlots.get(slot).IsExpired()) { mSlots.set(slot, null); }
        }
    }

    @Override
    public String toString() {
        //data format: [slot, slot-data]
        //             [slot, prefab, stacksize, days-to-expire]
        String data = "";
        boolean first = true;
        for (int slot : nonNullIndices(mSlots)) {
            if (!first) { data = data.concat(","); }
            //store position & slot info
            data = data.concat(Integer.toString(slot) + ",");
            data = data.concat(mSlots.get(slot).toString());
            first = false;
        }
        return data;
    }
    public void FillFromString(String stringData) {
        String[] data = stringData.split(",");
        int index = 0;
        while (index < data.length) {
            String[] args = { data[index+1], data[index+2], data[index+3] };
            IFood item = FoodUtils.SpawnFromArgs(args);

            ForceItemInSlot(item, Integer.parseInt(data[index]));
            index += 4;
        }
    }


    // slot filters
    private ArrayList<Integer> nonNullIndices(ArrayList<IFood> slots) {
        ArrayList<Integer> filteredList = new ArrayList<>(slots.size());
        for (int slot = 0; slot < slots.size(); slot++) {
            if (slots.get(slot) != null) { filteredList.add(slot); }
        }
        return filteredList;
    }
    private ArrayList<Integer> findIndices(String prefab, ArrayList<IFood> slots) {
        ArrayList<Integer> filteredList = new ArrayList<>(slots.size());
        for (int slot : nonNullIndices(slots)) {
            if (slots.get(slot).GetPrefab().equals(prefab)) { filteredList.add(slot); }
        }
        return filteredList;
    }
    private ArrayList<IFood> find(String prefab, ArrayList<IFood> slots) {
        ArrayList<IFood> filteredList = new ArrayList<>(slots.size());
        for (int slot : nonNullIndices(slots)) {
            if (slots.get(slot).GetPrefab().equals(prefab)) { filteredList.add(slots.get(slot)); }
        }
        return filteredList;
    }
}
