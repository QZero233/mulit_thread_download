/*
 * Author QZero
 * All Rights Reserved
 */

package com.qzero.download;

import java.io.File;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 下载类
 * @author QZero
 */
public class Downloader {

    //下载文件的URL
    private String url;
    //本地文件的对象
    private File dstFile;
    //线程数
    private int threadCount;
    //远程文件的大小
    private long fileLength;
    //下载进度
    private DownloadProgress progress;
    //反馈周期 单位毫秒
    private long t=1000;

    /**
     * 普通构造函数
     * @param url 下载文件的URL
     * @param dstFile 本地文件
     * @param threadCount 线程数
     */
    public Downloader(String url, File dstFile, int threadCount) {
        this.url = url;
        this.dstFile = dstFile;
        this.threadCount = threadCount;
    }

    /**
     * 文件大小单位字节转为GB
     * @param size 大小 单位字节
     * @return 大小，单位GB
     */
    public static double sizeToGb(long size){
        return (double) size/(1024*1024*1024);
    }

    /**
     * 获取下载速度
     * @param deltaLength 文件长度变化量
     * @param t 周期
     * @return 速度，单位Mb/s
     */
    public static double getSpeed(long deltaLength,long t){
        double deltaLengthInMb=(double)deltaLength/(1024*1024);
        return deltaLengthInMb/((double)t/1000);
    }

    /**
     * 修改反馈周期
     * @param t 反馈周期 单位毫秒
     */
    public void setT(long t){
        this.t=t;
    }

    /**
     * 开始下载
     * @param callback 下载回调对象
     */
    public void download(DownloadCallback callback){
        if(!doInit())
            return;

        //计算出每个线程的任务量
        long average=fileLength/threadCount;

        //开始分配任务（除最后一个线程外的）
        for(int i=0;i<threadCount-1;i++){
            DownloadThread  thread=new DownloadThread(url,i*average,(i+1)*average,dstFile,this,callback);
            thread.start();
        }

        //单独分配最后一个线程的任务，让它下载全部剩下的资源
        DownloadThread thread=new DownloadThread(url,(threadCount-1)*average,fileLength,dstFile,this,callback);
        thread.start();

        //任务分配完成，开始循环汇报下载进度
        while(true){
            double percentage=progress.getFinishPercentage();
            if(percentage>=100)
                //下载完成则退出循环，结束程序
                break;

            //反馈进度
            callback.onProgress(progress);
            try{
                Thread.sleep(t);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        //下载成功
        callback.onSuccess(progress.getCurrentLength());
    }

    /**
     * 供下载线程更改当前进度
     * @param length 新下载了的长度
     */
    synchronized void addLength(long length){
        progress.addLength(length);
    }

    /**
     * 初始化一些对象
     * @return 是否成功
     */
    private boolean doInit(){
        try {
            //打开连接并设置参数
            URL realUrl = new URL(url);
            HttpURLConnection connection= (HttpURLConnection) realUrl.openConnection();
            connection.setRequestProperty("User-Agent", " Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/37.0.2062.120 Safari/537.36");
            connection.setRequestProperty("Accept-Encoding", "identity");

            //如果响应码不等于200则失败
            if(connection.getResponseCode()!=200)
                return false;

            //初始化进度对象
            fileLength=connection.getContentLengthLong();
            progress=new DownloadProgress(fileLength,0);

            //初始化本地文件
            RandomAccessFile raf=new RandomAccessFile(dstFile,"rw");
            raf.setLength(fileLength);
            raf.close();

            return true;
        }catch (Exception e){
            return false;
        }
    }

}
