/*
 * Author QZero
 * All Rights Reserved
 */

package com.qzero.download;

import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 负责执行下载任务的线程
 * @author QZero
 */
public class DownloadThread extends Thread {

    //文件的远程地址
    private String url;

    //开始与结束的位置
    private long startPos;
    private long endPos;

    //本地文件
    private File dstFile;

    //下载对象，用于更新下载进度
    private Downloader parent;

    private DownloadCallback callback;


    DownloadThread(String url, long startPos, long endPos, File dstFile, Downloader parent,DownloadCallback callback) {
        this.url = url;
        this.startPos = startPos;
        this.endPos = endPos;
        this.dstFile = dstFile;
        this.parent = parent;
        this.callback=callback;
    }

    /**
     * 子线程开始工作
     */
    @Override
    public void run() {
        super.run();

        try {
            URL realUrl = new URL(url);
            HttpURLConnection connection= (HttpURLConnection) realUrl.openConnection();
            connection.setRequestProperty("User-Agent", " Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/37.0.2062.120 Safari/537.36");
            connection.setRequestProperty("Accept-Encoding", "identity");
            connection.setRequestProperty("Range", "bytes=" + startPos + "-"+ endPos);//关键步骤 设置请求范围

            //如果响应码不等于206则失败
            if(connection.getResponseCode()!=206){
                callback.onFailure(connection.getResponseCode());
                return;
            }

            RandomAccessFile raf=new RandomAccessFile(dstFile,"rw");
            raf.seek(startPos);

            InputStream is=connection.getInputStream();
            byte[] buf=new byte[20*1024*1024];
            int len;
            while((len=is.read(buf))!=-1){
                raf.write(buf,0,len);
                parent.addLength(len);
            }

            //该线程下载完成

        }catch (Exception e){
            e.printStackTrace();
            callback.onFailure(-1);
        }

    }

}
