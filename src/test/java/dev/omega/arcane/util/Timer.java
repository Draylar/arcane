package dev.omega.arcane.util;

import java.util.concurrent.TimeUnit;

public class Timer {

    private long start;

    public void start() {
        start = System.nanoTime();
    }

    public long end() {
        return TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);
    }

    public static long time(Runnable task) {
        Timer timer = new Timer();
        timer.start();
        task.run();
        return timer.end();
    }
}
