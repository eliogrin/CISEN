package com.epam.cisen.core.api.util;

import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

/**
 * Created by Vladislav on 28.11.2014.
 */
public class Log {

    private static final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss:SSS");

    public static void debug(String text, Object... args) {
        print(System.out, text, args);
    }

    public static void info(String text, Object... args) {
        print(System.out, text, args);
    }

    public static void error(String text, Object... args) {
        print(System.err, text, args);
    }

    private static String format(String text, Object... args) {
        String result = String.format(text, args);
        return FORMAT.format(new Date()) + " " + result;
    }

    private static synchronized void print(PrintStream printStream, String text, Object... args) {
        String str = format(text, args);
        printStream.println(str);
    }

}
