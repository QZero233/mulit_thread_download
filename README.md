# JAVA多线程下载实现
java多线程下载实现（暂未实现断点续传）

例程为 com.qzero.sample.TestMain

# 如何使用
1.将 com.qzero.download 包下的文件复制到项目中

2.创建 Downloader 对象

```Java
Downloader downloader=new Downloader("远程文件地址",new File("本地文件地址"),线程数);
```

3.根据需要设置反馈周期（即调用callback中onProgress的周期，单位毫秒，默认1000）
```Java
downloader.setT(500);
```

4.创建 DownloadCallback 下载回调对象
```Java
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
```

5.开始下载

```Java
downloader.download(callback);
```
