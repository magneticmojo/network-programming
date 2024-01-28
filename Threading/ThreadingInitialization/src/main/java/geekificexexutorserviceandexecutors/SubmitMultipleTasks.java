package geekificexexutorserviceandexecutors;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

public class SubmitMultipleTasks {
    public static void main(String[] args) {
        // InvokeAll / InvokeAny
        // Submit multiple callables at once (a collection of callables) --> return list of futures


        /*
         * INVOKEALL ==> BATCH SUBMIT CALLABLES
         * Java 8 --> newWorkStealingPool()
         * --> Created for a given parallelism size == AVAILABLE NUM OF CORES IN THE CPU
         */
        ExecutorService executor = Executors.newWorkStealingPool();
        List<Callable<String>> callables = Arrays.asList(
                () -> "Task 1", () -> "Task 2", () -> "Task 3"
        );

        try {
            for (Future<String> future : executor.invokeAll(callables)) {
                try {
                    System.out.println(future.get());
                } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        executor.shutdownNow();

        /*
        * INVOKEANY (does not return futures) ==> Block (waits) until the first callable terminates and returns the result of that callable
        * */
        ExecutorService executorService2 = Executors.newWorkStealingPool();
        List<Callable<String>> callables2 = Arrays.asList(
                newCallable("Task A", 3),
                newCallable("Task B", 2),
                newCallable("Task C", 1)
        );

        try {
            System.out.println(executorService2.invokeAny(callables2));
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

    }

    static Callable<String> newCallable(String result, long sleepSeconds) {
        return () -> {
            TimeUnit.SECONDS.sleep(sleepSeconds);
            return result;
        };
    }

}
