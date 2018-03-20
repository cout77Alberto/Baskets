package edu.osu.baskets;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

/**
 * Created by Alberto on 2/19/2018.
 */

public class NavbarFragment extends Fragment {

    private static final String TAG = "NavbarFragment";
    private ImageButton mTradeButton, mMapButton, mInventoryButton, mKitchenButton;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_navbar, container, false);

        mTradeButton = (ImageButton) v.findViewById(R.id.trades_ib);
        mTradeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment;
                FragmentTransaction transaction = getActivity().getFragmentManager().beginTransaction();
                fragment = new TradesFragment();
                transaction.replace(R.id.fragment_container, fragment);
                transaction.commit();
            }
        });
        mInventoryButton = (ImageButton) v.findViewById(R.id.inventory_ib);
        mInventoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment;
                FragmentTransaction transaction = getActivity().getFragmentManager().beginTransaction();
                fragment = new InventoryFragment();
                transaction.replace(R.id.fragment_container, fragment);
                transaction.commit();
            }
        });
        mKitchenButton = (ImageButton) v.findViewById(R.id.kitchen_ib);
        mKitchenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment;
                FragmentTransaction transaction = getActivity().getFragmentManager().beginTransaction();
                fragment = new KitchenFragment();
                transaction.replace(R.id.fragment_container, fragment);
                transaction.commit();
            }
        });
        mMapButton = (ImageButton) v.findViewById(R.id.map_ib);
        mMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment;
                FragmentTransaction transaction = getActivity().getFragmentManager().beginTransaction();
                fragment = new MapFragment();
                transaction.replace(R.id.fragment_container, fragment);
                transaction.commit();
            }
        });
        return v;
    }
}
