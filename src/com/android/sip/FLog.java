/*
 * Copyright (C) 2010, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.sip;

import android.os.SystemClock;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Date;

/**
 * A simple log-to-file utility class. Only meant for development.
 */
class FLog {
    static File file;
    static PrintWriter writer;

    static {
        try {
            file = new File("/sdcard/siplog.txt");
            writer = new PrintWriter(new FileWriter(file, true));
            writer.println("-------------------------------------------------");
            writer.println("FLog started at " + new Date().toString());
            writer.flush();
        } catch (Throwable t) {
            Log.e("FLOG", "init", t);
        }
    }

    private static String getDate() {
        String[] ss = new Date().toString().split(" ");
        return ss[1] + " " + ss[2] + " " + ss[3];
    }

    private static String getTime() {
        long now = SystemClock.elapsedRealtime();
        int ms = (int) (now % 1000);
        int s = (int) (now / 1000);
        int m = s / 60;
        s %= 60;
        return String.format("%d.%d.%d", m, s, ms);
    }

    static synchronized void write(String type, String tag, String msg) {
        if (writer == null) return;
        try {
            writer.println(String.format("%s %s %s| %s| %s", type,
                    getDate(), getTime(), tag, msg));
            writer.flush();
        } catch (Throwable t) {
            Log.e("FLOG", type, t);
        }
    }

    static synchronized void write(String type, String tag, String msg,
            Throwable t) {
        if (writer == null) return;
        try {
            writer.println(String.format("%s %s| %s| %s", type,
                    getDate(), getTime(), tag, msg));
            t.printStackTrace(writer);
            writer.flush();
        } catch (Throwable tt) {
            Log.e("FLOG", type, tt);
        }
    }

    static void v(String tag, String msg) {
        Log.v(tag, msg);
        write("v", tag, msg);
    }

    static void d(String tag, String msg) {
        Log.d(tag, msg);
        write("d", tag, msg);
    }

    static void w(String tag, String msg) {
        Log.w(tag, msg);
        write("w", tag, msg);
    }

    static void w(String tag, String msg, Throwable t) {
        Log.w(tag, msg, t);
        write("w", tag, msg, t);
    }

    static void e(String tag, String msg, Throwable t) {
        Log.e(tag, msg, t);
        write("e", tag, msg, t);
    }
}
