/* Implement this class. */

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class MyHost extends Host {
    private List<Task> queue;
    private final Object l = new Object();
    private final AtomicBoolean on = new AtomicBoolean(true);
    private AtomicInteger ttl = new AtomicInteger();

    MyHost() {
        queue = Collections.synchronizedList(new ArrayList<>());
        ttl.set(0);
    }

    @Override
    public void run() {
        Task working = null;
        while(on.get()) {
            // sort the queue desc for priority, asc for start time
            synchronized (l) {
                queue.sort((o1, o2) -> {
                    int p = Integer.compare(o2.getPriority(), o1.getPriority());
                    if(p == 0) {
                        return Integer.compare(o1.getStart(), o2.getStart());
                    }
                    return p;
                });
            }

            //Check for priority if preemptible
            if(working != null && !queue.isEmpty()) {
                if (queue.get(0).getPriority() > working.getPriority() && working.isPreemptible()) {
                    synchronized (l) {
                        queue.add(working);
                        working = queue.get(0);
                        queue.remove(0);
                    }
                }
            }

            //Get from queue
            if(working == null) {
                if(!queue.isEmpty()) {
                    synchronized (l) {
                        working = queue.get(0);
                        queue.remove(0);
                        ttl.set(working.getLeft().intValue());
                    }
                }
            } else {
                // Finish Task
                if(working.getLeft() == 0) {
                    working.finish();
                    working = null;
                    ttl.set(0);
                    continue;
                }
            }

            //Pass Time
            if(working != null) {
                try {
                    Thread.sleep(1000L);
                    //System.out.println(currentThread().getName() + " Waiting");
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                //Modify remaining time
                working.setLeft(working.getLeft() - 1000L);
                ttl.set(working.getLeft().intValue());
            }
        }
    }

    @Override
    public void addTask(Task task) {
        synchronized (l) {
            queue.add(task);
        }
    }

    @Override
    public int getQueueSize() {
        if (ttl.get() == 0)
            return queue.size();
        else
            return queue.size() + 1;
    }

    @Override
    public long getWorkLeft() {
        synchronized (l) {
            int sum = ttl.get();
            for (Task i : queue) {
                sum += i.getLeft();
            }
            return sum;
        }
    }

    @Override
    public void shutdown() {
        on.set(false);
    }
}
