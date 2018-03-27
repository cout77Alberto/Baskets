package edu.osu.baskets;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import edu.osu.baskets.foods.IFood;


/**
 * Created by Daniel on 2/20/2018.
 */

public class InventoryFragment extends Fragment {
    private RecyclerView mBasketRecycler, mFridgeRecycler;
    private FoodAdapter mBasketAdapter, mFridgeAdapter;
    private static String TAG  = "InventoryFragment";

    @Override
    @TargetApi(23)
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Toast.makeText(this.getContext(), R.string.strawberries, Toast.LENGTH_LONG).show();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_inventory, container, false);

        mBasketRecycler = v.findViewById(R.id.basket_recycler);
        mFridgeRecycler = v.findViewById(R.id.fridge_recycler);

        mBasketRecycler.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        mFridgeRecycler.setLayoutManager(new GridLayoutManager(getActivity(), 3));

        updateUI();

        return v;
    }

    private void updateUI() {
        Inventory inv = Inventory.get(getActivity());
        List<IFood> basketSlots = inv.GetBasketItems();
        List<IFood> fridgeSlots = inv.GetFridgeItems();

        mBasketRecycler.setAdapter(new FoodAdapter(basketSlots));
        mFridgeRecycler.setAdapter(new FoodAdapter(fridgeSlots));
    }

    private class FoodHolder extends RecyclerView.ViewHolder {
        private ImageView mThumbnailImageView;
        private TextView mStackSizeTextView, mDaysToExpireTextView;
        private ProgressBar mExpirationProgressBar;

        public FoodHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.grid_item_food, parent, false));

            mThumbnailImageView = itemView.findViewById(R.id.iv_food_thumbnail);
            mStackSizeTextView = itemView.findViewById(R.id.tv_stack_size);
            mDaysToExpireTextView = itemView.findViewById(R.id.tv_days_to_expire);
            mExpirationProgressBar = itemView.findViewById(R.id.pb_expiration_bar);
        }

        public void bind(IFood item) {
            if (item != null) {
                mStackSizeTextView.setVisibility(View.VISIBLE);
                mDaysToExpireTextView.setVisibility(View.VISIBLE);
                mExpirationProgressBar.setVisibility(View.VISIBLE);

                if (item.GetImageResId() != 0) {
                    mThumbnailImageView.setImageResource(item.GetImageResId());
                } else {
                    //mThumbnailImageView.setImageResource(android.R.drawable.btn_star_big_on);
                }
                mStackSizeTextView.setText(String.format("%d", item.GetStackSize()));
                mDaysToExpireTextView.setText(String.format("%d", item.GetDaysToExpire()));
                mExpirationProgressBar.setMax(item.GetShelfLife());
                mExpirationProgressBar.setProgress(item.GetDaysToExpire());
            } else {
                mThumbnailImageView.setImageDrawable(null);
                mStackSizeTextView.setVisibility(View.INVISIBLE);
                mDaysToExpireTextView.setVisibility(View.INVISIBLE);
                mExpirationProgressBar.setVisibility(View.INVISIBLE);
            }
        }
    }

    private class FoodAdapter extends RecyclerView.Adapter<FoodHolder> {
        private List<IFood> mSlots;

        public FoodAdapter(List<IFood> slots) {
            mSlots = slots;
        }

        @Override
        public FoodHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            return new FoodHolder(inflater, parent);
        }

        @Override
        public void onBindViewHolder(FoodHolder holder, int position) {
            IFood slot = mSlots.get(position);
            holder.bind(slot);
        }

        @Override
        public int getItemCount() {
            return mSlots.size();
        }
    }


    //Fragment lifecycle
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
