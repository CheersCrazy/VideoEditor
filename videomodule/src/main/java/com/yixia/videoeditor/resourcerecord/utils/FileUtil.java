package com.yixia.videoeditor.resourcerecord.utils;

import android.os.Environment;

import java.io.File;
import java.text.DecimalFormat;
import java.util.Locale;

public class FileUtil {


    static String RECORD_FILE_NAME = Environment.getExternalStorageDirectory().getAbsolutePath() + "/com.gsafety.naturegas/";

    public static String createFile(String path) {
        File file = new File(RECORD_FILE_NAME);
        File recordFile = null;
        String stringPath;
        try {
            if (!file.exists()) {
                file.mkdirs();
            }
            recordFile = new File(file, path);
            if (!recordFile.exists()) {
                recordFile.createNewFile();
            }
            stringPath = recordFile.getAbsolutePath();
            return stringPath;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String fileSize(long size) {
        if (size <= 0) return "0";
        final String[] units = new String[]{"B", "kB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

    private static final long MB = 1024 * 1024;
    public String getSizeByUnit(double size) {
        if (size == 0) {
            return "0K";
        }
        if (size >= MB) {
            double sizeInM = size / MB;
            return String.format(Locale.getDefault(), "%.1f", sizeInM) + "M";
        }
        double sizeInK = size / 1024;
        return String.format(Locale.getDefault(), "%.1f", sizeInK) + "K";
    }

}
