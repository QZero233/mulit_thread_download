/*
 * Author QZero
 * All Rights Reserved
 */

package com.qzero.download;

/**
 * 保存下载进度的对象
 * @author QZero
 */
public class DownloadProgress {
    //远程文件的大小
    private long fileLength;
    //已下载文件的大小
    private long currentLength;
    //上一次调用 getDeltaLength 时的已下载文件大小
    private long lastLength=0;

    public DownloadProgress(long fileLength, long currentLength) {
        this.fileLength = fileLength;
        this.currentLength = currentLength;
    }

    public long getFileLength() {
        return fileLength;
    }

    public void setFileLength(long fileLength) {
        this.fileLength = fileLength;
    }

    public long getCurrentLength() {
        return currentLength;
    }

    public void setCurrentLength(long currentLength) {
        this.currentLength = currentLength;
    }

    /**
     * 返回上一个反馈周期（默认1秒）到当前的下载量
     * @return 下载量
     */
    public synchronized long getDeltaLength(){
        long result=currentLength-lastLength;
        lastLength=currentLength;
        return result;
    }

    /**
     * 获取当前进度百分数
     * @return 进度百分数
     */
    public synchronized double getFinishPercentage(){
        return ((double)currentLength/(double)fileLength)*100;
    }

    /**
     * 添加已下载文件大小
     * @param length 大小
     */
    synchronized void addLength(long length){
        currentLength+=length;
        if(currentLength>fileLength)
            currentLength=fileLength;
    }

}
