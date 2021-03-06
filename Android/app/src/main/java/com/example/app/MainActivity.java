package com.example.app;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.hardware.*;
import android.content.Context;
import android.widget.TextView;
import android.widget.ImageView;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;


public class MainActivity extends ActionBarActivity implements SensorEventListener {

    float[] accelerometerValues;
    float[] magnetometerValues;
    ImageView compassImg;
    // record the compass picture angle turned
    private float currentDegree = 0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
        // set up the compass's image
        compassImg = (ImageView) findViewById(R.id.compass_img);
    }

    @Override
    public void onAccuracyChanged(Sensor s, int i){
    }
    @Override
    public void onSensorChanged(SensorEvent s){
    }

    @Override
    protected void onStart () {
       super.onStart();

        // First, get an instance of the SensorManager
        SensorManager sMan = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        // Second, get the sensors we're interested in
        Sensor magnetField = sMan.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        Sensor accelerometer = sMan.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        // Third, implement a SensorEventListener class
       SensorEventListener magnetListener = new SensorEventListener(){
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
                // do things if you're interested in accuracy changes
            }
            public void onSensorChanged(SensorEvent event) {

               //Setting up TextViews.
               String magVals = "";
               String accelVals = "";
               TextView view = (TextView)findViewById(R.id.magnetometerTextView);
               TextView view2 = (TextView)findViewById(R.id.accelerometerTextView);
               TextView dirHeaded = (TextView)findViewById(R.id.dirTextView);

               if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
                   accelerometerValues = event.values;
               else if (event.sensor.getType() ==  Sensor.TYPE_MAGNETIC_FIELD)
                   magnetometerValues = event.values;

               // Getting Values[] for Accelerometer, setting in TextView
               if(accelerometerValues != null) {
                   accelVals += String.format("Accelerometer values(%d)\n", accelerometerValues.length);
                   for (int i=0;i<accelerometerValues.length;i++)
                       accelVals += String.format("Accelerometer Values[%d]:%f\n", i, accelerometerValues[i]);
               }
               // Getting Values[] for Magnetometer, setting in TextView
               if(magnetometerValues != null) {
                   magVals += String.format("Magnetometer values(%d)\n", magnetometerValues.length);
                   for (int i=0;i<magnetometerValues.length;i++)
                       magVals+= String.format("Magnetometer Values[%d]:%f\n", i, magnetometerValues[i]);
               }
                view.setText(magVals);
                view2.setText(accelVals);

                //Set up an Animation for the compass picture! :-)
                //get the angle around the z-axis rotated
                float degree = Math.round(event.values[0]);

                dirHeaded.setText("Heading: " + Float.toString(degree) + " degrees");

                // create a rotation animation (reverse turn degree degrees)
               RotateAnimation ra = new RotateAnimation(
                        currentDegree,
                        -degree,
                        Animation.RELATIVE_TO_SELF, 0.5f,
                        Animation.RELATIVE_TO_SELF,
                        0.5f);

                // how long the animation will take place
                ra.setDuration(210);

                // set the animation after the end of the reservation status
                ra.setFillAfter(true);

                // Start the animation
                compassImg.startAnimation(ra);
                currentDegree = -degree;
            }
        };
        //Registering listeners to the Sensor Manager, sMan !
        sMan.registerListener(magnetListener, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        sMan.registerListener(magnetListener, magnetField, SensorManager.SENSOR_DELAY_NORMAL);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }



}
