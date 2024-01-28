package geekificexexutorserviceandexecutors;


/*
* ExecutorService and Executors:
* Run asynchronous tasks and manage a pool of threads
* Threads belonging to the pool will be reused for new tasks
*
* 1 ExecutorService --> As many concurrent tasks as you want in the application life-cycle
* */

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Java5ThreadPool {


    public static void main(String[] args) {

        /*
        * EXECUTORS ==> EXPLICIT TERMINATION || INDEFINITELY LISTENING FOR NEW TASKS
        * */

        // Factory methods in Executors class provide different ExecutorService instances
        ExecutorService executor = Executors.newSingleThreadExecutor(); // newSingleThreadExecutor CANNOT BE RECONFIGURED

        executor.submit(() -> {
           String threadName = Thread.currentThread().getName();
            System.out.println("Executing: " + threadName + "(newSingleThreadExecutor CANNOT BE RECONFIGURED)");
        });

        // newFixedThreadPool ==> Reconfigured to use more than one thread
        ExecutorService executor2 = Executors.newFixedThreadPool(1);

        executor2.submit(() -> {
            String threadName = Thread.currentThread().getName();
            System.out.println("Executing: " + threadName + "(newFixedThreadPool(1) RECONFIGURABLE!)");
        });

        System.out.println("executor2.shutdownNow() ==> interrupting all currently running tasks");
        executor2.shutdownNow();


        /*
        * A) Tasks accepted but execution has not started (submitted tasks)
        * B) Tasks accepted and currently executing
        *
        * Orderly shutdown ==> NO INTERRUPTION of running tasks || tasks submitted but not yet running
        * The method returns almost immediately after ensuring that no new tasks will be accepted, --> but it does not wait for ongoing tasks to finish.
        *
        * */

        System.out.println("executor.shutdown()");
        executor.shutdown();

        /*
        * Blocks calling thread until:
        * All tasks have completed execution
        * Timeout occurs
        * Current thread is interrupted --> If other some thread interrupts the waiting thread:
        * (main implicit thread in this case) --> main thread will stop waiting and proceed
        * */
        try {
            boolean terminated = executor.awaitTermination(3, TimeUnit.SECONDS);
            System.out.println("This executor terminated if true --> " + terminated);
            System.out.println("Timeout elapsed before termination if false --> " + terminated);

            if (!terminated) {
                // Interrupt all running tasks and shuts the executor down
                executor.shutdownNow();
                System.out.println("Shutdown finished");
            }


        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println("ExecutorService was terminated: " + executor.isTerminated());
        System.out.println("ExecutorService2 was terminated: " + executor2.isTerminated());

    }
}
