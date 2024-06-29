package com.onesandzeros.patima.settings.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.onesandzeros.patima.R;

public class ParametersActivity extends AppCompatActivity {

    final private float RESET_DETECT_THRESHOLD = 0.6F;
    SharedPreferences sharedPreferences;
    ImageButton backBtn;
    private SeekBar thresholdSlider;
    private TextView thresholdValueText;
    private ImageButton saveBtn, resetBtn;
    private float DETECT_THRESHOLD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_parameters);

        sharedPreferences = getSharedPreferences("Startup", MODE_PRIVATE);

        DETECT_THRESHOLD = sharedPreferences.getFloat("CONFIDENCE_THRESHOLD", 0);
        thresholdSlider = findViewById(R.id.thresholdbar);
        thresholdValueText = findViewById(R.id.thresholdval);

        saveBtn = findViewById(R.id.save_btn);
        resetBtn = findViewById(R.id.reset_btn);
        backBtn = findViewById(R.id.return_button);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putFloat("CONFIDENCE_THRESHOLD", DETECT_THRESHOLD);
                editor.apply();
                Toast.makeText(ParametersActivity.this, "Changes Saved", Toast.LENGTH_SHORT).show();

            }
        });

        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DETECT_THRESHOLD = RESET_DETECT_THRESHOLD;
                changeParameters();
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        changeParameters();

    }

    private void changeParameters() {

        float initialThreshold = DETECT_THRESHOLD * 100;
        thresholdSlider.setProgress((int) initialThreshold);
        thresholdValueText.setText(String.valueOf(initialThreshold / 100));

        thresholdSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float currentProgress = (float) progress / seekBar.getMax(); // Convert progress to float
                thresholdValueText.setText(String.valueOf(currentProgress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Optional: handle touch start event (e.g., show a toast)
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                DETECT_THRESHOLD = (float) seekBar.getProgress() / seekBar.getMax();
            }
        });

    }

}