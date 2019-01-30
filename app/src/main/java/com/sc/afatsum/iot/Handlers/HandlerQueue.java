package com.sc.afatsum.iot.Handlers;

import java.util.concurrent.LinkedBlockingQueue;

public class HandlerQueue {

    public static LinkedBlockingQueue<int[]> linkedBlockingQueue;

    public static void setLinkedBlockingQueue(LinkedBlockingQueue<int[]> queue) {
        linkedBlockingQueue = queue;
    }

    public static LinkedBlockingQueue<int[]> getLinkedBlockingQueue() {
        return linkedBlockingQueue;
    }

}
