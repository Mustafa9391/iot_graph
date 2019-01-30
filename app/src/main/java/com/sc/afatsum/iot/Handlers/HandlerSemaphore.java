package com.sc.afatsum.iot.Handlers;

import java.util.concurrent.Semaphore;

/**
 * Created by theo on 15/08/2016.
 */
public class HandlerSemaphore {

    static Semaphore semaphore;
    static Semaphore mutex;

    public static Semaphore getMutex() {
        return mutex;
    }

    public static void setMutex(Semaphore mutex) {
        HandlerSemaphore.mutex = mutex;
    }

    public static Semaphore getSemaphore() {
        return semaphore;
    }

    public static void setSemaphore(Semaphore semaphore) {
        HandlerSemaphore.semaphore = semaphore;
    }

}
