import static java.lang.Thread.sleep;

public class MainThreadWaitForThread {

    // TODO Daemon threads in practice: Daemon threads are typically used for background tasks
    //  that should run only as long as the application is running, like garbage collection or handling background I/O.
    //  If the application (or main thread) finishes, there's no reason for these threads to continue, so they are terminated.


    public static void main(String[] args) {

        Runnable r = () -> {
            for (int i = 0; i < 5; i++) {
                try {
                    sleep(1000);
                    System.out.println("Running");
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        };

        Thread t = new Thread(r);
        t.setDaemon(true); // JVM can exit without waiting for t
        t.start();
        // Main thread waits for t
        try {
            t.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
