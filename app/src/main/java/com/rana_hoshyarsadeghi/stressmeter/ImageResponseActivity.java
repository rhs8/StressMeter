package com.rana_hoshyarsadeghi.stressmeter;

import static com.rana_hoshyarsadeghi.stressmeter.ImageAdapter.STRESS_SCORES;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ImageResponseActivity extends AppCompatActivity {

    private static final String CSV_NAME = "stress_timestamp.csv";

    private ImageView ivLarge;
    private Button btnSelect, btnCancel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_response);

        ivLarge = findViewById(R.id.iv_large);
        btnSelect = findViewById(R.id.btn_select);
        btnCancel = findViewById(R.id.btn_cancel);

        Intent i = getIntent();
        int resId = i.getIntExtra("resId", -1);

        if (resId != -1) {
            ivLarge.setImageResource(resId);
        }
        btnCancel.setOnClickListener(v -> finish());
        btnSelect.setOnClickListener(v -> {
            int score = STRESS_SCORES.containsKey(resId) ? STRESS_SCORES.get(resId) : 8;
            String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)
                    .format(new Date());

            try {
                File csv = new File(getFilesDir(), CSV_NAME);
                boolean isNew = !csv.exists();

                try (FileWriter writer = new FileWriter(csv, true)) {
                    if (isNew) {
                        writer.append("timestamp,score\n"); // header once
                    }
                    writer.append(timestamp)
                            .append(",")
                            .append(String.valueOf(score))
                            .append("\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            finishAffinity();
        });
    }
}
