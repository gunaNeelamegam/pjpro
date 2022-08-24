
package com.zilogic.pjproject;


class MyShutdownHook extends Thread {

    Thread thread;

    MyShutdownHook(Thread paramThread) {
        this.thread = paramThread;
    }

    public void run() {
        this.thread.interrupt();
        try {
            this.thread.join();
        } catch (Exception exception) {
        }
    }
}
