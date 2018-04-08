package edu.osu.baskets;

import android.app.Fragment;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Random;


/**
 * Created by Daniel on 2/20/2018.
 */

public class MapFragment extends Fragment implements SensorEventListener{

    private static String TAG  = "MapFragment";
    private SensorManager mSensorManager;
    private Sensor mSensor;
    private TextView mTextView;
    private int mStepCount;
    private int nextStepMilestone= 300;
    private ProgressBar mProgressBar;
    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do something here if sensor accuracy changes.
    }

    @Override
    public final void onSensorChanged(SensorEvent event) {
        mStepCount = (int) event.values[0];
        update();
    }

    private void update(){
        if(mTextView!=null) {
            mTextView.setText(mStepCount+"Steps");
            mProgressBar.setMax(nextStepMilestone);
            mProgressBar.setProgress(mStepCount);
            if(mStepCount>nextStepMilestone){
                //do some stuff
                addItem();
                nextStepMilestone += mStepCount;
                update();
            }
        }
    }

    private void addItem() {
        Inventory inv = Inventory.get(getActivity());
        String[] foods = getActivity().getResources().getStringArray(R.array.prefablist);
        Random rand = new Random();
        int foodIndex = rand.nextInt(foods.length);
        inv.AddItemToBasket(FoodUtils.Spawn(foods[foodIndex], 5));
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_map, container, false);
        mTextView = (TextView) v.findViewById(R.id.step_count);
        mProgressBar = (ProgressBar) v.findViewById(R.id.step_progress);
        mSensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        mSensor = (Sensor) mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        mProgressBar.setProgress(mStepCount);
        return v;
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
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called");
        mSensorManager.unregisterListener(this);

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
