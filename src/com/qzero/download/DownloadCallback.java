/*
 * Author QZero
 * All Rights Reserved
 */

package com.qzero.download;

/**
 * 下载回调类
 * @author QZero
 */
public interface DownloadCallback {
    /**
     * 下载成功时的回调
     * @param length 下载了的文件大小
     */
    void onSuccess(long length);

    /**
     * 下载失败时的回调
     * @param code 失败代码
     */
    void onFailure(int code);

    /**
     * 下载进度回调
     * 默认1秒调用一次
     * @param progress 下载进度
     */
    void onProgress(DownloadProgress progress);
}
