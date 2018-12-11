package com.yixia.videoeditor.utils;

import java.text.DecimalFormat;

public class NumberFormatUtils {
    public static String getDoubleOne(double numbers) {
        return new DecimalFormat("0.0").format(numbers);
    }

    public static String getDoubleTwo(double numbers) {
        return new DecimalFormat("0.00").format(numbers);
    }

    public static String getDoubleSix(double numbers) {
        return new DecimalFormat("0.000000").format(numbers);
    }
}
