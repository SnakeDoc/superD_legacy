/*******************************************************************************
 *  Copyright 2013 Jason Sipula                                                *
 *                                                                             *
 *  Licensed under the Apache License, Version 2.0 (the "License");            *
 *  you may not use this file except in compliance with the License.           *
 *  You may obtain a copy of the License at                                    *
 *                                                                             *
 *      http://www.apache.org/licenses/LICENSE-2.0                             *
 *                                                                             *
 *  Unless required by applicable law or agreed to in writing, software        *
 *  distributed under the License is distributed on an "AS IS" BASIS,          *
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.   *
 *  See the License for the specific language governing permissions and        *
 *  limitations under the License.                                             *
 *******************************************************************************/

package net.snakedoc.superd.javafx.gui.controller;

import javafx.concurrent.Task;
import net.snakedoc.superd.filescan.Walker;
import net.snakedoc.superd.javafx.gui.ApplicationWindow;
import net.snakedoc.superd.launcher.DedupeR;

public class ThreadDedupe {
    
    private Thread thread;
    
    public ThreadDedupe() {
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
                String str[] = { "-r", ApplicationWindow.getTargetTextField(), "-d", ApplicationWindow.getDelimiterTextField(), "-h", "SHA-512" };
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
