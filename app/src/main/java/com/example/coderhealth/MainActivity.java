package com.example.coderhealth;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button mRunButton1, mRunButton2, mRunButton3, mRunButton4;
    public static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;
        mRunButton1 =  findViewById(R.id.run_button);
        mRunButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(MainActivity.this,HeartRateActivity.class);
                startActivity(i);
            }
        });


        mRunButton2 =  findViewById(R.id.run_button2);
        mRunButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(MainActivity.this, SitPostureActivity.class);
                startActivity(i);
            }
        });


        mRunButton3 =  findViewById(R.id.run_button3);
        mRunButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(MainActivity.this,StepCountActivity.class);
                startActivity(i);
            }
        });


//        mRunButton4 =  findViewById(R.id.run_button4);
//        mRunButton4.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i=new Intent(MainActivity.this,AugmentedFacesActivity.class);
//                startActivity(i);
//            }
//        });

    }
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
