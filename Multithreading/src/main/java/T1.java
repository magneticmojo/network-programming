import net.jcip.annotations.ThreadSafe;

/**
 * Represents a concurrently executable task, `T1`, extending the Thread class.
 * When the thread runs, it periodically prints its name to the standard output until it's interrupted.
 */
@ThreadSafe
public class T1 extends Thread {

    /**
     * Constructs a new instance of T1 with the specified thread name.
     *
     * @param threadName the name of this thread
     */
    public T1(String threadName) {
        super(threadName);  // Set the thread's name using the parent constructor
    }


    @Override
    public void run() {
        while (!isInterrupted()) {  // Check the interruption status
            try {
                System.out.println(this.getName());
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // If the thread is interrupted during sleep, exit the loop.
                break;
            }
        }
    }
}
