package com.example.task.android_pdf;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnDrawListener;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;

import java.io.File;

public class MainActivity extends AppCompatActivity implements OnPageChangeListener,OnLoadCompleteListener,OnDrawListener {

    private ProgressBar progressBar;
    private String url= "http://www.51capinfo.cn/static/showing/zanbaby.pdf";
    private PDFView pdfView;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    //进度条的值
                    int i = msg.arg1;
                    progressBar.setProgress(i);
            }
            if (msg.arg1==100){
                progressBar.setVisibility(View.GONE);
                pdfView.setVisibility(View.VISIBLE);
                displayFromFile(new File(Path));
            }
        }
    };
    private String Path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        pdfView = (PDFView) findViewById(R.id.pdfview);

        SharedPreferences sharedPreferences = MainActivity.this.getSharedPreferences("test", MODE_PRIVATE);

        Path=sharedPreferences.getString("name",null);
        if (isNetworkAvailable(this)) {


            if (Path == null) {
                DownloadUtil.get().download(url, "assets", new DownloadUtil.OnDownloadListener() {
                    @Override
                    public void onDownloadSuccess() {
                        Log.d("h_bl", "文件下载成功");
                    }

                    @Override
                    public void onDownloading(int progress, String path) {
                        //获取一个文件名为test、权限为private的xml文件的SharedPreferences对象
                        SharedPreferences sharedPreferences = MainActivity.this.getSharedPreferences("test", MODE_PRIVATE);

                        //得到SharedPreferences.Editor对象，并保存数据到该对象中
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("path", path);

                        //保存key-value对到文件中
                        editor.commit();

                        Path = path;
                        Message msg = handler.obtainMessage();
                        msg.what = 1;
                        msg.arg1 = progress;
                        handler.sendMessage(msg);

                    }

                    @Override
                    public void onDownloadFailed() {
                        Log.d("h_bl", "文件下载失败");
                    }
                });
            } else {
                progressBar.setVisibility(View.GONE);
                pdfView.setVisibility(View.VISIBLE);
                displayFromFile(new File(Path));
            }
        }else {
            progressBar.setVisibility(View.GONE);
            pdfView.setVisibility(View.VISIBLE);
            displayFromAsset();
        }

    }
    //读取网路上的PDF文本
    private void displayFromFile( File file ) {
        pdfView.fromFile(file)   //设置pdf文件地址
                .defaultPage(1)         //设置默认显示第1页
                .onPageChange(this)     //设置翻页监听
                .onLoad(this)           //设置加载监听
                .onDraw(this)            //绘图监听
                .showMinimap(false)     //pdf放大的时候，是否在屏幕的右上角生成小地图
                .swipeVertical( true )  //pdf文档翻页是否是垂直翻页，默认是左右滑动翻页
                .enableSwipe(true)   //是否允许翻页，默认是允许翻
                // .pages( 2 ，5  )  //把2  5 过滤掉
                .load();
    }
    //读取本地的 PDF 格式的文本
    private void displayFromAsset() {

        pdfView.fromAsset("zanbaby.pdf")
                .defaultPage(1)
                .onPageChange(this)
                .enableAnnotationRendering(true)
                .swipeVertical( true )  //pdf文档翻页是否是垂直翻页，默认是左右滑动翻页
                .onLoad(this)
                .load();
    }

    @Override
    public void onPageChanged(int page, int pageCount) {
        Toast.makeText( this , " " + page +
                " / " + pageCount , Toast.LENGTH_SHORT).show();

    }

    /**
     * 加载完成回调
     * @param nbPages  总共的页数
     */
    @Override
    public void loadComplete(int nbPages) {
        Toast.makeText( this ,  "加载完成" + nbPages  , Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLayerDrawn(Canvas canvas, float pageWidth, float pageHeight, int displayedPage) {
        // Toast.makeText( MainActivity.this ,  "pageWidth= " + pageWidth + "
        // pageHeight= " + pageHeight + " displayedPage="  + displayedPage , Toast.LENGTH_SHORT).show();
    }

    // 检查是否连接到网络
    public static boolean isNetworkAvailable(Context context) {
            ConnectivityManager cm = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (cm == null) {
            } else {
                //如果仅仅是用来判断网络连接
                // 则可以使用 cm.getActiveNetworkInfo().isAvailable();
                NetworkInfo[] info = cm.getAllNetworkInfo();
                if (info != null) {
                    for (int i = 0; i < info.length; i++) {
                        if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                            return true;
                        }
                    }
                }
            }
            return false;
    }
}
