package net.snakedoc.superd.javafx.gui.controller;

import javafx.concurrent.Task;
import net.snakedoc.superd.filescan.Walker;
import net.snakedoc.superd.launcher.DedupeR;

public class ThreadDedupe {
    
    private Thread thread;
    
    public ThreadDedupe() {
        //ThreadObj threadObj = new ThreadObj();
        //this.thread = threadObj.getThread();
    }
    
    private Thread getNewThread() {
        ThreadObj threadObj = new ThreadObj();
        return threadObj.getThread();
    }
    
    public void setNewThread() {
        this.thread = getNewThread();
    }
    
    public void startDedupeTask() {
        Walker.setTerminate(false);
        this.thread.start();
    }
    
    public void stopDedupeTask() {
        this.thread.interrupt();
    }
}

class ThreadObj {
    
    private Task<Void> taskDedupe = null;
    
    private Thread threadDedupe = null;
    
    public ThreadObj() {
        
        this.taskDedupe = new Task<Void>() {
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
        
        this.threadDedupe = new Thread(this.taskDedupe);
        
    }
    
    public Thread getThread() {
        
        return this.threadDedupe;
        
    }
    
}
