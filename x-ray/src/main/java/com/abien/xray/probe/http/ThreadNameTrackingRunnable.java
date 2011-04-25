package com.abien.xray.probe.http;

/**
 *
 * @author blog.adam-bien.com
 */
public class ThreadNameTrackingRunnable implements Runnable{

    private String actionName;
    private Runnable runnable;

    public ThreadNameTrackingRunnable(Runnable runnable,String actionName) {
        this.actionName = actionName;
        this.runnable = runnable;
    }


    @Override
    public void run() {
        String originName = Thread.currentThread().getName();
        String tracingName = this.actionName + "#" + originName;
        try{
            Thread.currentThread().setName(tracingName);
            this.runnable.run();
        }finally{
            Thread.currentThread().setName(originName);
        }

    }

    @Override
    public String toString() {
        return "CurrentThreadRenameableRunnable{" + "actionName=" + actionName + '}';
    }
    
}
