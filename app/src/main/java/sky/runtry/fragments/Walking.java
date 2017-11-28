package sky.runtry.fragments;

/**
 * Created by pruthvishpatel on 11/9/17.
 */

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.TextView;

import sky.runtry.R;
import sky.runtry.sensors.GPSHelper;
import sky.runtry.sensors.PedometerHelper;

public class Walking extends Fragment{

    View rootView;
    GPSHelper gps;
    PedometerHelper pedo;

    Chronometer chrono;

    boolean active = false;
    int active_time = 0;
    int steps = 0;

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);

        gps = new GPSHelper(activity, 1, 16);
        gps.setActivityName("Walking");
        pedo = new PedometerHelper(activity, 11.91);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.rootView = inflater.inflate(R.layout.walking, container, false);

        rootView.findViewById(R.id.btn_start).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                onBtnStart();
            }
        });
        rootView.findViewById(R.id.btn_stop).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                onBtnStop();
            }
        });

        chrono = (Chronometer) rootView.findViewById(R.id.chronometer);
        chrono.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                setActive(gps.isActive(),pedo.getStepCount());
                if (active) {
                    if (active_time < getTimeMs())
                        active_time++;

                    gps.setTime(getTimeMs());
                    gps.setTime_active(getTimeActiveMs());
                    gps.setStepCount(steps);

                    steps = pedo.getStepCount();

                    updateUI();
                }
            }
        });

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    public void onBtnStart() {
        gps.requestUpdates();
        gps.setStepCounting(true);

        pedo.registerSensor();

        chrono = (Chronometer) rootView.findViewById(R.id.chronometer);
        chrono.setBase(SystemClock.elapsedRealtime());
        chrono.start();

        rootView.findViewById(R.id.btn_start).setVisibility(View.GONE);
        rootView.findViewById(R.id.btn_stop).setVisibility(View.VISIBLE);
    }

    public void onBtnStop() {
        if (gps != null) {
            gps.stopPeriodicUpdates();
            gps.setStepCounting(false);
        }

        if(pedo != null)
            pedo.unregisterSensor();

        chrono = (Chronometer) rootView.findViewById(R.id.chronometer);
        chrono.stop();

        rootView.findViewById(R.id.btn_start).setVisibility(View.VISIBLE);
        rootView.findViewById(R.id.btn_stop).setVisibility(View.GONE);
    }

    public String getTime(){
        long elapsedMillis = SystemClock.elapsedRealtime() - chrono.getBase();
        short hours = (short) (elapsedMillis / 3600);
        short minutes = (short) ((elapsedMillis % 3600) / 60);
        short seconds = (short) (elapsedMillis % 60);

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    public String getTimeActive(){
        short hours = (short) (active_time / 3600);
        short minutes = (short) ((active_time % 3600) / 60);
        short seconds = (short) (active_time % 60);

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    public long getTimeMs(){
        return SystemClock.elapsedRealtime() - chrono.getBase();
    }

    public long getTimeActiveMs(){
        return active_time;
    }

    public void setActive(boolean active, int steps) {
        if(active)
            this.active = active;
        else if(this.steps < steps)
            this.active = true;
    }



	/* UI values setting */

    private void updateUI(){
        updateTimeActive();
        updateSteps();
        updateSpeed();
        updateSpeedMax();
        updateSpeedAVG();
        updateTempo();
        updateTempoMin();
        updateTempoAVG();
        updateDistance();
        updateAltitude();
    }

    public void updateTimeActive() {
        TextView tv = (TextView) rootView.findViewById(R.id.txt_time_active);
        tv.setText(getTimeActive());
    }

    public void updateSteps() {
        TextView tv = (TextView) rootView.findViewById(R.id.txt_steps);
        tv.setText(String.valueOf(steps));
    }

    public void updateSpeed() {
        TextView tv = (TextView) rootView.findViewById(R.id.txt_speed);
        tv.setText(String.valueOf(gps.getSpeed()));
    }

    public void updateSpeedMax() {
        TextView tv = (TextView) rootView.findViewById(R.id.txt_speed_max);
        tv.setText(String.valueOf(gps.getSpeed_max()));
    }

    public void updateSpeedAVG() {
        TextView tv = (TextView) rootView.findViewById(R.id.txt_avg_speed);
        tv.setText(String.valueOf(gps.getSpeed_avg()));
    }

    public void updateTempo() {
        TextView tv = (TextView) rootView.findViewById(R.id.txt_tempo);
        tv.setText(String.valueOf(gps.getTempo()));
    }

    public void updateTempoMin() {
        TextView tv = (TextView) rootView.findViewById(R.id.txt_tempo_min);
        tv.setText(String.valueOf(gps.getTempo_min()));
    }

    public void updateTempoAVG() {
        TextView tv = (TextView) rootView.findViewById(R.id.txt_avg_tempo);
        tv.setText(String.valueOf(gps.getTempo_avg()));
    }

    public void updateDistance() {
        TextView tv = (TextView) rootView.findViewById(R.id.txt_distance);
        tv.setText(String.valueOf(gps.getTotal_distance()));
    }

    public void updateGoal() {
        TextView tv = (TextView) rootView.findViewById(R.id.txt_goal);
        tv.setText(String.valueOf(""));
    }

    public void updateAltitude(){
        TextView txt_alt = (TextView) rootView.findViewById(R.id.txt_altitude);
        txt_alt.setText(String.valueOf(gps.getAltitude()));

        TextView txt_min = (TextView) rootView.findViewById(R.id.txt_altitude_min);
        txt_min.setText(String.valueOf(gps.getAltitude_min()));

        TextView txt_max = (TextView) rootView.findViewById(R.id.txt_altitude_max);
        txt_max.setText(String.valueOf(gps.getAltitude_max()));

        TextView txt_diff= (TextView) rootView.findViewById(R.id.txt_altitude_diff);
        txt_diff.setText(String.valueOf(gps.getAltitude_diff()));

        TextView txt_upward= (TextView) rootView.findViewById(R.id.txt_altitude_upward);
        txt_upward.setText(String.valueOf(gps.getUpward()));

        TextView txt_downward= (TextView) rootView.findViewById(R.id.txt_altitude_downward);
        txt_downward.setText(String.valueOf(gps.getDownward()));
    }

}