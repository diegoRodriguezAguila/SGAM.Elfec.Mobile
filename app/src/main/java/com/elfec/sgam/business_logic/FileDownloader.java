package com.elfec.sgam.business_logic;

import android.support.annotation.Nullable;
import android.util.Log;

import com.elfec.sgam.settings.AppPreferences;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.ResponseBody;

/**
 * Created by drodriguez on 07/06/2016.
 * Downloads files
 */
public class FileDownloader {

    private static final FileDownloadListener sDummyListener = new FileDownloadListener() {
        //region Dummy Methods
        @Override
        public void onStarted(long fileSize) {
        }

        @Override
        public void onProgress(long fileSizeDownloaded, long totalFileSize) {
        }

        @Override
        public void onCompleted(File d) {
        }
        //endregion
    };

    /**
     * Downloads a file stream to disk
     *
     * @param body response body of the stream
     * @param path path to save the file
     * @return file saved
     */
    public @Nullable File downloadFileToDisk(ResponseBody body, String path) {
        return downloadFileToDisk(body, path, null);
    }

    /**
     * Downloads a file stream to disk
     *
     * @param body response body of the stream
     * @param path path to save the file
     * @param listener download process listener
     * @return file saved
     */
    public @Nullable File downloadFileToDisk(ResponseBody body, String path,
                            FileDownloadListener listener) {
        try {
            if (listener == null)
                listener = sDummyListener;
            File downloadFile = new File(AppPreferences.getApplicationContext()
                    .getExternalFilesDir(null) + File.separator + path);
            createDirIfNecessary(downloadFile);
            InputStream inputStream = null;
            OutputStream outputStream = null;
            try {
                byte[] fileBuffer = new byte[4096];
                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;
                inputStream = body.byteStream();
                outputStream = new FileOutputStream(downloadFile);
                listener.onStarted(fileSize);
                while (true) {
                    int read = inputStream.read(fileBuffer);
                    if (read == -1)
                        break;
                    outputStream.write(fileBuffer, 0, read);
                    fileSizeDownloaded += read;
                    listener.onProgress(fileSizeDownloaded, fileSize);
                    Log.d("FILE DOWNLOAD ", "file download: " + fileSizeDownloaded + " of " +
                            fileSize);
                }
                outputStream.flush();
                listener.onCompleted(downloadFile);
                return downloadFile;
            } catch (IOException e) {
                return null;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Creates the dir for the file if needed
     * @param file file path
     * @return true if success
     */
    private boolean createDirIfNecessary(File file){
        if (!file.exists()) {
            try {
                if (!file.createNewFile()) {
                    return false;
                }
            } catch (IOException e) {
                return false;
            }
        }
        return true;
    }

    /**
     * Listener for a download progress
     */
    public interface FileDownloadListener {
        void onStarted(long fileSize);

        void onProgress(long fileSizeDownloaded, long totalFileSize);

        void onCompleted(File downloadedFile);
    }
}
