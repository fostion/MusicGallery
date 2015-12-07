package cc.fs.musicgallery.utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by fostion on 2015/9/6.
 */
public class FileUtil {
    public static String getCachePath(Context context, String uniqueName) {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return cachePath + File.separator + uniqueName;
    }

    public static String getEnviromentPath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath();
    }

    /**
     * 创建文件夹
     */
    public static File getDir(String path) throws IOException {
        File file = new File(path);
        if (!file.exists()) file.mkdirs();
        return file;
    }

    /**
     * 创建文件
     */
    public static File getFile(String path) throws IOException {
        String dirPath = path.substring(0, path.lastIndexOf(File.separator));
        File dirFile = new File(dirPath);
        File file = new File(path);
        if (!dirFile.exists()) dirFile.mkdirs();
        if (!file.exists()) file.createNewFile();
        return file;
    }

    /**
     * 删除文件
     */
    public static void deleteFile(String path) {
        File file = new File(path);
        if (file.isDirectory()) {//delete dir
            File[] childFile = file.listFiles();
            for (File tempFile : childFile) {
                tempFile.delete();
            }
            file.delete();
        } else {//delete file
            file.delete();
        }
    }

    /**
     * 将文件写进sd卡中
     */
    public static boolean writeFile(File file ,byte[] data) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(data);
            fileOutputStream.flush();
            fileOutputStream.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
