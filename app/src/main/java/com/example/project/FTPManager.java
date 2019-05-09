package com.example.project;

import android.os.StrictMode;
import android.util.Log;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import java.io.File;
import java.io.FileInputStream;

public class FTPManager {
    FTPClient ftpClient = null;

    public FTPManager() {
        ftpClient = new FTPClient();
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
    }


    // 連接到ftp服務器
    public synchronized boolean connect() throws Exception {

        boolean bool = false;
        if (ftpClient.isConnected()) {//判斷是否已登陸
            ftpClient.disconnect();
        }

        ftpClient.setDataTimeout(20000);//設置連接超時時間
        ftpClient.setControlEncoding("utf-8");
        ftpClient.connect("134.208.3.152");

        if (FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
            if (ftpClient.login("vsftp", "123")) {
                bool = true;
                ftpClient.enterLocalPassiveMode();
                Log.d("myTag", "ftp connected successfully");
            }
        }
        return bool;
    }

    // 實現上傳文件的功能
    public synchronized boolean uploadFile(File localFile)
            throws Exception {
        // 上傳文件之前，先判斷本地文件是否存在
        if (!localFile.exists()) {
            Log.d("myTag", "file not exist");
            return false;
        }

//        Log.d("myTag", localFile.getPath());
        boolean success = false;
        try {
            FileInputStream input = new FileInputStream(localFile);
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            boolean s = ftpClient.changeWorkingDirectory("test/");
            success = ftpClient.storeFile(localFile.getName(), input);
            input.close();
            return success;
        } catch (Exception e) {
            Log.d("myTag", "upload failed: " + e.getMessage());
        }
        return  success;
    }
    public synchronized boolean findFile(String filename) {
        String remotePath = "/test/" + filename;
        Log.d("myTag", remotePath);
        try{
            FTPFile[] remoteFiles = ftpClient.listFiles(remotePath);
            if (remoteFiles.length > 0)
            {
                return true;
            }
            return false;
        } catch(Exception e){
            Log.d("myTag", "check file failed: " + e.getMessage());
        }
        return false;
    }

    // 如果ftp上傳打開，就關閉掉
    public void closeFTP() throws Exception {
        if (ftpClient.isConnected()) {
            ftpClient.disconnect();
        }
    }
}