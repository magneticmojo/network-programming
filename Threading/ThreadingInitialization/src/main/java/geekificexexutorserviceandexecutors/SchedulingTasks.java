package geekificexexutorserviceandexecutors;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class SchedulingTasks {

    public static void main(String[] args) throws InterruptedException {

        /*
        * SchedulingExecutorService:
        * Schedule tasks to run --> Periodically || after specified amount of time
        * */

        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
        Runnable task = () -> System.out.println("Executing after specified delay: " + Thread.currentThread().getName());

        // Specialized future
        ScheduledFuture<?> future = executorService.schedule(task, 3, TimeUnit.SECONDS);
        Thread.sleep(1_500);
        long remainingDelay = future.getDelay(TimeUnit.MILLISECONDS);
        System.out.printf("Remaining delay: %sms%n", remainingDelay);

        /*
        * Scheduling Tasks Periodically
        *
        * Param 2: Leading delay or wait time
        * Param 3: Rate/period
        *
        * ScheduleAtFixedRate() --> No account for the duration of the execution of the task
        * --> periodical delay shorter than execution ==> thread-pool quickly reach full capacity
        * */

        ScheduledExecutorService executorService2 = Executors.newScheduledThreadPool(1);
        Runnable task2 = () -> System.out.println("Executing periodically at fixed rate: " + Thread.currentThread().getName());
        executorService2.scheduleAtFixedRate(task2, 0, 1, TimeUnit.SECONDS);

        /*
        * Fixed Delay --> Wait time period added between end of task and start of next task
        * CANNOT PREDICT THE DURATION OF THE TASK
        * */

        ScheduledExecutorService executorService3 = Executors.newScheduledThreadPool(1);
        Runnable task3 = () -> System.out.println("Executing periodically with fixed delay: " + Thread.currentThread().getName());
        executorService3.scheduleWithFixedDelay(task3, 0, 1, TimeUnit.SECONDS);

        System.out.println("Awaiting termination for all executorservices");
        executorService.awaitTermination(5, TimeUnit.SECONDS);
        executorService2.awaitTermination(5, TimeUnit.SECONDS);
        executorService3.awaitTermination(5, TimeUnit.SECONDS);

        System.out.println("Shutdown Now!");
        executorService.shutdownNow();
        executorService2.shutdownNow();
        executorService3.shutdownNow();


    }
}
