package com.wjb.pdflibrary;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import com.github.barteksc.pdfviewer.PDFView;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;

import java.io.File;

import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE;

/**
 * description ：
 * author : wjb
 * date : 2019/10/30 10:12
 */
public class PDFHelper {
    private static PDFHelper pdfHelper;
    private PDFView pdfView;

    public static void initFileDownload(Context context) {
        FileDownloader.init(context);
    }

    public static PDFHelper getInstance(PDFView pdfView) {
        pdfHelper = new PDFHelper(pdfView);
        return pdfHelper;
    }

    public PDFHelper(PDFView pdfView) {
        this.pdfView = pdfView;
    }

    /**
     * Use a file as the pdf source
     */
    public PDFView.Configurator fromFile(File file) {
        return pdfView.fromFile(file);
    }

    public void downloadPDF(String pdfUrl, String mFilePath, FileDownloadListener fileDownloadListener) {
        FileDownloader.getImpl().create(pdfUrl)
                .setPath(mFilePath)
                .setListener(fileDownloadListener).start();
    }

    public void setProgress(DownCircleProgressBar downCircleProgressBar, int soFarBytes, int totalBytes) {
        long progress = (long) ((soFarBytes * 100) / totalBytes);
        downCircleProgressBar.setProgress(progress);
    }

    public void changePDFOrientation(boolean withAnimation) {
        pdfView.setSwipeVertical(!pdfView.isSwipeVertical());
        pdfView.jumpTo(pdfView.getCurrentPage(), withAnimation);
    }

    public void changeWindowOrientation(Activity activity) {
        //判断当前屏幕方向
        if (activity.getRequestedOrientation() == SCREEN_ORIENTATION_SENSOR_LANDSCAPE) {
            //切换竖屏
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            //切换横屏SCREEN_ORIENTATION_SENSOR_LANDSCAPE
            activity.setRequestedOrientation(SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            pdfView.jumpTo(pdfView.getCurrentPage() + 1);
            Message msg1 = new Message();
            msg1.what = 7788;
            handler.sendMessageDelayed(msg1, delayMillis);
        }
    };
    private Message msg;
    private long delayMillis;
    private boolean isAutoPlay = false;

    public void startAutoPlayPDF(long delayMillis) {
        isAutoPlay = true;
        msg = new Message();
        msg.what = 7788;
        handler.sendMessageDelayed(msg, delayMillis);
    }

    public void stopAutoPlayPDF() {
        isAutoPlay = false;
        handler.removeMessages(7788);
    }

    public boolean isAutoPlay() {
        return isAutoPlay;
    }
}
