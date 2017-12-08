package com.example.excel_demo;

import android.Manifest;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import pub.devrel.easypermissions.EasyPermissions;

/**
 * @author task
 */
public class MainActivity extends AppCompatActivity {


    private String filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] perms = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE};
                filePath = "http://121.43.227.225:8080/brandroomInfos/admin/exam/scorelist?classID=852&examID=57";
                if (!EasyPermissions.hasPermissions(MainActivity.this, perms)) {
                    EasyPermissions.requestPermissions(MainActivity.this, "需要访问手机存储权限！", 10086, perms);
                } else {
                    LookActivity.show(MainActivity.this, filePath);
                }
            }
        });
        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
