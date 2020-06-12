package com.example.coderhealth;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class StepCountActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;
    TextView textViewCounter, textViewStep;

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_count);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        textViewCounter = findViewById(R.id.textView);
        textViewStep = findViewById(R.id.textView2);
//        textViewCounter.setText("总步数: " + 0);
//        textViewStep.setText("单次: " + 0);

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if(sensorEvent.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            textViewCounter.setText("总步数: " + sensorEvent.values[0]);
        } else if(sensorEvent.sensor.getType() == Sensor.TYPE_STEP_DETECTOR) {
            textViewStep.setText("单次: " + sensorEvent.values[0]);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER),
                SensorManager.SENSOR_DELAY_UI);//获取数据频率
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR),
                SensorManager.SENSOR_DELAY_UI);//获取数据频率

    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);//解除监听注册
    }
}

