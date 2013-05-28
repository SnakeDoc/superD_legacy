package net.snakedoc.superd.javafx.gui.controller;

public class ThreadMonitor {

    private static volatile ThreadMonitor uniqueInstance;
    
    
    public static ThreadMonitor getInstance() {
        if (ThreadMonitor.uniqueInstance == null) {
            synchronized (ThreadMonitor.class) {
                if (ThreadMonitor.uniqueInstance == null) {
                    ThreadMonitor.uniqueInstance = new ThreadMonitor();
                }
            }
        }
        return ThreadMonitor.uniqueInstance;
    }
    
    private ThreadMonitor() {};
}
