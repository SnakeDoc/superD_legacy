package net.snakedoc.superd.javafx.gui.controller;

import javafx.concurrent.Task;
import net.snakedoc.superd.launcher.DedupeR;

public class ThreadMonitor {

    private static volatile ThreadMonitor uniqueInstance;
    
    private Task<Void> taskDedupe = new Task<Void>() {
        @Override
        public void run() {
            DedupeR deduper = new DedupeR();
            String str[] = { "-d" };
            deduper.driver(str);
        }        
        
        @Override
        protected Void call() throws Exception {
            // TODO Auto-generated method stub
            return null;
        }
    };
    
    private Thread threadDedupe = new Thread(this.taskDedupe);
    
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
    
    public void startDedupeTask() {
        this.threadDedupe.start();
    }
    
    public void stopDedupeTask() {
        this.threadDedupe.interrupt();
    }
    
    private ThreadMonitor() {};
}
