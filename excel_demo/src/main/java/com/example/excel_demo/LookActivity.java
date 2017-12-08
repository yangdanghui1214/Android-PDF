package com.example.excel_demo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;

/**
 * @author task
 */
public class LookActivity extends AppCompatActivity {

    SuperFileView2 mSuperFileView;
    String filePath;
    private String TAG = "FileDisplayActivity";
    private String Path="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_look);
        init();
    }

    private void init() {
        mSuperFileView = (SuperFileView2) findViewById(R.id.mSuperFileView);
        mSuperFileView.setOnGetFilePathListener(new SuperFileView2.OnGetFilePathListener() {
            @Override
            public void onGetFilePath(SuperFileView2 mSuperFileView2) {
//                getFilePathAndShowFile(mSuperFileView2);
            }
        });

        String path = (String) getIntent().getSerializableExtra("path");

        if (!TextUtils.isEmpty(path)) {
            Log.d(TAG, "文件path:" + path);
//            setFilePath(path);
        }
        mSuperFileView.show();
        mSuperFileView.displayFile(new File("/storage/emulated/0/look/scorelist?classID=852&examID=57.xls"));
    }

    public static void show(Context context, String url) {
        Intent intent = new Intent(context, LookActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("path", url);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"FileDisplayActivity-->onDestroy");
        if (mSuperFileView != null) {
            mSuperFileView.onStopDisplay();
        }
    }

    private void getFilePathAndShowFile(SuperFileView2 mSuperFileView2) {
        if (getFilePath().contains("http")) {//网络地址要先下载
            downLoadFromNet(getFilePath(),mSuperFileView2);
        } else {
        }
    }

    private void downLoadFromNet(String filePath, final SuperFileView2 mSuperFileView2) {

        DownloadUtil.get().download(filePath,"look",mSuperFileView2, new DownloadUtil.OnDownloadListener() {
            @Override
            public void onDownloadSuccess() {

            }

            @Override
            public void onDownloading(int progress, String path) {
                if (progress>=100) {
                    Log.d("look", "   " + path);
                }
            }

            @Override
            public void onDownloadFailed() {

            }
        });
    }

    public void setFilePath(String fileUrl) {
        this.filePath = fileUrl;
    }

    private String getFilePath() {
        return filePath;
    }


}
