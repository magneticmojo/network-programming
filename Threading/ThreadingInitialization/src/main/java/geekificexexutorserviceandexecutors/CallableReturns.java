package geekificexexutorserviceandexecutors;

import java.util.concurrent.*;

public class CallableReturns {

    public static void main(String[] args) {


        // Implements functional interface Callable method call()
        Callable<String> callableTask = () -> {
            try {
                TimeUnit.SECONDS.sleep(2);
                return "Returning after sleeping for 2 seconds";
            } catch (InterruptedException e) {
                return "InterruptedException was thrown";
            }
        };

        /*
        * Submit to ExecutorService like Runnable task
        * But asynchronous does not wait for returns --> Future is returned when asked for
        * */

        ExecutorService executorService = Executors.newFixedThreadPool(1);


        /*
        * Future tightly coupled to ExecutorService ==> Non-terminated futures throw exception if executor is shutdown!!
        * Call to future.get() ==> Block and wait until underlying callable has been terminated
        * Worst Case Scenario --> Callable run indefinitely ==> Application unresponsive
        *
        * *********** call overloaded future.get() with Timeout to solve the problem ********************************
        *
        * */
        Future<String> futureReturnAfter2Sec = executorService.submit(callableTask);

        // Future --> Will throw exception
        //executorService.shutdownNow();

        while (!futureReturnAfter2Sec.isDone()) {
            try {
                Thread.sleep(2_00);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Waiting for callable task to be done to get result from future");
        }

        try {
            // get() blocks the currently running thread and wait until callable completes before returning result
            String result = futureReturnAfter2Sec.get();
            System.out.println(result);
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.out.println("Thread was interrupted");
        } catch (ExecutionException e) {
            e.printStackTrace();
            System.out.println("ExecutionException thrown");
        }

        // Shutdown executor service to not have infinite running
        executorService.shutdownNow();
    }
}
