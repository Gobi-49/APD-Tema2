/* Implement this class. */

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MyDispatcher extends Dispatcher {

    List<Task> tasks;

    public MyDispatcher(SchedulingAlgorithm algorithm, List<Host> hosts) {
        super(algorithm, hosts);
        tasks = Collections.synchronizedList(new ArrayList<>());
    }

    @Override
    public void addTask(Task task) {
        tasks.add(task);

        switch (algorithm) {
            case ROUND_ROBIN -> {
                int i = tasks.indexOf(task);
                hosts.get(i % hosts.size()).addTask(task);
            }
            case SHORTEST_QUEUE -> {
                synchronized (MyDispatcher.class) {
                    hosts.sort(new Comparator<Host>() {
                        @Override
                        public int compare(Host o1, Host o2) {
                            int cmp = Integer.compare(o1.getQueueSize(), o2.getQueueSize());
                            if (cmp == 0) {
                                return Long.compare(o1.getId(), o2.getId());
                            }
                            return cmp;
                        }
                    });
                }
                hosts.get(0).addTask(task);
            }
            case SIZE_INTERVAL_TASK_ASSIGNMENT -> {
                switch (task.getType()) {
                    case SHORT -> {
                        hosts.get(0).addTask(task);
                    }
                    case MEDIUM -> {
                        hosts.get(1).addTask(task);
                    }
                    case LONG -> {
                        hosts.get(2).addTask(task);
                    }
                }
            }
            case LEAST_WORK_LEFT -> {
                Host min = hosts.get(0);
                for(Host i : hosts) {
                    if(i.getWorkLeft() == 0) {
                        min = i;
                        break;
                    }
                    if(i.getWorkLeft() < min.getWorkLeft()) {
                        min = i;
                    }
                }
                min.addTask(task);
            }
        }
    }
}
