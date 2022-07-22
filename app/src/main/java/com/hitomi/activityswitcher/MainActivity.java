package com.hitomi.activityswitcher;

import android.Manifest;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hitomi.aslibrary.ActivitySwitcher;

import pub.devrel.easypermissions.EasyPermissions;
import pub.devrel.easypermissions.PermissionRequest;

public class MainActivity extends AppCompatActivity {

    static int index;
    static int totalCount = 8;
    static int[] bgColors = new int[] {
            Color.parseColor("#92c8d0"),
            Color.parseColor("#c4dcce"),
            Color.parseColor("#cd7b91"),
            Color.parseColor("#e5c5dc"),
            Color.parseColor("#742a8d"),
            Color.parseColor("#2eb2d8"),
            Color.parseColor("#b9d84e"),
            Color.parseColor("#35fe62")
    };

    private RelativeLayout relativeLayout;
    private Button btnNext;
    private TextView tvPage;

    private ActivitySwitcher activitySwitcher;
    private int tag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activitySwitcher = ActivitySwitcher.getInstance();

        relativeLayout = (RelativeLayout) findViewById(R.id.relayout);
        btnNext = findViewById(R.id.btn_next);
        tvPage = findViewById(R.id.tv_page);

        findViewById(R.id.btnShowSwitch).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activitySwitcher.showSwitch();
            }
        });


        relativeLayout.setBackgroundColor(bgColors[index]);
        tag = index;
        tvPage.setText("当前第" + (tag + 1) + "页" );

        if (index == totalCount - 1) {
            btnNext.setText("退出程序");
        }
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (++index > totalCount - 1) {
                    activitySwitcher.exit();
                } else {
                    startActivity(new Intent(MainActivity.this, MainActivity.class));
                }
            }
        });

        activitySwitcher.setOnActivitySwitchListener(new ActivitySwitcher.OnActivitySwitchListener() {
            @Override
            public void onSwitchStarted() {}

            @Override
            public void onSwitchFinished(Activity activity) {
                if (activity instanceof MainActivity) {
                    MainActivity mainActivity = (MainActivity) activity;
                    MainActivity.index = mainActivity.getTag();
                }
            }
        });

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        activitySwitcher.processTouchEvent(ev);
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onBackPressed() {
        MainActivity.index = tag - 1 <= 0 ? 0 : tag - 1;
        activitySwitcher.finishSwitch(this);
    }

    public int getTag() {
        return tag;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    final int RC_CAMERA_AND_LOCATION = 101;
    @Override
    protected void onStart() {
        super.onStart();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            EasyPermissions.requestPermissions(
                    new PermissionRequest.Builder(this, RC_CAMERA_AND_LOCATION, Manifest.permission.READ_EXTERNAL_STORAGE)
                            //.setRationale(R.string.camera_and_location_rationale)
                            //.setPositiveButtonText(R.string.rationale_ask_ok)
                            //.setNegativeButtonText(R.string.rationale_ask_cancel)
                            //.setTheme(R.style.my_fancy_style)
                            .build());
        }
    }
}
