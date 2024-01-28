import net.jcip.annotations.*;

/**
 * Represents a concurrently executable task, `T2`, that can be managed
 * (started, suspended, resumed, or killed) by a Thread. This task, when active,
 * periodically prints its name to the standard output.
 * It provides controls to suspend, resume, and kill the task.
 */
@ThreadSafe
public class T2 implements Runnable {

    private final String taskName;
    @GuardedBy("this")
    private volatile boolean alive = true;
    @GuardedBy("this")
    private volatile boolean active = true;
    private Thread executingThread;

    /**
     * Constructs a new instance of T2 with the specified taskName.
     * Naming the task can be used to trace and log execution of specific tasks
     * in a complex application.
     *
     * @param taskName the taskName of this task
     */
    public T2(String taskName) {
        this.taskName = taskName;
    }

    /**
     * Returns the name of this task.
     *
     * @return the name of this task
     */
    public String getTaskName() {
        return taskName;
    }

    /**
     * Stops the execution of the task by interrupting the thread if it's running.
     */
    public synchronized void kill() {
        if (executingThread != null) {
            // wake up the thread if it's sleeping for faster termination
            executingThread.interrupt();
            alive = false;
            active = false;
        }
    }

    /**
     * Suspends the execution of the task, causing it to go into an idle state.
     */
    public synchronized void suspendExecution() {
        this.active = false;
    }

    /**
     * Resumes the execution of a previously suspended task.
     */
    public synchronized void resumeExecution() {
        this.active = true;
    }

    /**
     * The main execution method of the task. When active, it periodically prints the name of the executing thread.
     * If suspended, it waits idly.
     */
    @Override
    public void run() {
        executingThread = Thread.currentThread();
        String threadName = executingThread.getName();
        while (alive) {
            while (active) {
                System.out.println(threadName);
                sleep(1000);
            }
            sleep(25); // Avoid busy-waiting
        }
    }

    /**
     * Causes the currently executing thread to sleep (temporarily cease execution)
     * for the specified number of milliseconds. If the thread is interrupted during sleep,
     * the catch-clause logic sets its interrupt status again.
     *
     * @param millis the length of time to sleep in milliseconds
     */
    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            executingThread.interrupt();
        }
    }
}
