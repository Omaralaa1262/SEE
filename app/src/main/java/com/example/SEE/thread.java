package com.example.SEE;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.MotionEvent;
import android.widget.ImageButton;

//import com.example.graduationproject.logger.Log;
//import com.example.graduationproject.logger.LogWrapper;


public class thread extends Activity implements SensorEventListener {

    private final float TOUCH_SCALE_FACTOR = 180.0f / 320;
    private float previousX;
    private float previousY;
    CountDownTimer time;
    private SensorManager sensorMan;
    private Sensor accelerometer;
    private float[] mGravity;
    private double mAccel;
    private double mAccelCurrent;
    private double mAccelLast;
    // On screen logging via a fragment with a TextView.
    //private LogFragment mLogFragment = (LogFragment) getSupportFragmentManager().findFragmentById(R.id.log_fragment);
    ImageButton btn;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        sensorMan = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorMan.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mAccel = 0.00f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;
    //    btn=findViewById(R.id.button2);


    }


    public void onResume() {
        super.onResume();
        sensorMan.registerListener(this, accelerometer,
                SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorMan.unregisterListener(this);
    }


    @Override
    public void onSensorChanged(SensorEvent event) {

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            mGravity = event.values.clone();
            // Shake detection
            double x = mGravity[0];
            double y = mGravity[1];
            double z = mGravity[2];
            mAccelLast = mAccelCurrent;
            mAccelCurrent = Math.sqrt(x * x + y * y + z * z);
            ;
            double delta = mAccelCurrent - mAccelLast;
            mAccel = mAccel * 0.9f + delta;
            // Make this higher or lower according to how much
            // motion you want to detect

            if (mAccel > 4) {

                Intent intent = new Intent(this,timer.class);

                startActivity(intent);


            }
        }}

        public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);

          //  com.example.graduationproject.logger.Log.i(MainActivity.class.getName(), String.valueOf(event));
            return false;
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // required method
    }

    public void initializeLogging() {
        // Using Log, front-end to the logging chain, emulates
        // android.util.log method signatures.

        // Wraps Android's native log framework
//        LogWrapper logWrapper = new LogWrapper();
//        Log.setLogNode(logWrapper);

        // A filter that strips out everything except the message text.
        //MessageOnlyLogFilter msgFilter = new MessageOnlyLogFilter();
        //logWrapper.setNext(msgFilter);

      //  msgFilter.setNext(mLogFragment.getLogView());
    }


    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
