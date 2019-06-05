/*
 * Author QZero
 * All Rights Reserved
 */

package com.qzero.sample;

import com.qzero.download.DownloadCallback;
import com.qzero.download.DownloadProgress;
import com.qzero.download.Downloader;

import java.io.File;

public class TestMain {

    private static final long T=1000;

    public static void main(String[] args) {

        DownloadCallback callback=new DownloadCallback() {
            @Override
            public void onSuccess(long length) {
                System.out.println("下载成功，大小 "+ Downloader.sizeToGb(length)+" Gb");
            }

            @Override
            public void onFailure(int code) {
                System.err.println("错误，错误码 "+code);
            }

            @Override
            public void onProgress(DownloadProgress progress) {
                System.out.println("下载进度 "+progress.getFinishPercentage()+"%"+" "+Downloader.sizeToGb(progress.getCurrentLength())+
                        "Gb / "+Downloader.sizeToGb(progress.getFileLength())+" Gb 速度 "+
                        Downloader.getSpeed(progress.getDeltaLength(),T)+" Mb/s");
            }
        };

        Downloader downloader=new Downloader("http://res06.bignox.com/full/20190401/d123a694c9f747818432ee6da2342f57.exe?filename=nox_setup_v6.2.8.0_full.exe",new File("H:\\1.exe"),5);
        downloader.setT(T);
        downloader.download(callback);

    }

}
