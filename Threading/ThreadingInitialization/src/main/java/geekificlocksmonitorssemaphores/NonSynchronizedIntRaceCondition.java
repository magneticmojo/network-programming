package geekificlocksmonitorssemaphores;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

public class NonSynchronizedIntRaceCondition {

    private static int count = 0;

    public static void main(String[] args) throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(4); // 4 threads in pool (can be reconfigured)
        IntStream.range(0, 1000)
                // Terminal op --> apply given lambda to each element in the stream
                .forEach(intStreamElement -> executorService.submit(NonSynchronizedIntRaceCondition::incrementSynchronized)); // submits the increment()

        // Main thread sleep
        Thread.sleep(1_000);
        System.out.println(count);


        /*
        * Ts work in parallel to perform the operations
        * Shared access to mutable state -->
        *
        * 1. Read
        * 2. Increment by 1
        * 3. Write (back)
        *
        * Thread A reads the value (e.g. 22) --> should update to 23
        * Thread B has read the value of 22 and updates the value to 23
        *
        * Thread B updates value to 23 ==> Value has been updated 2 times but only incremented by 1
        *
        * Result == Increments get lost
        *
        * */
    }

    static void increment() {
        count += 1;
    }

    static synchronized void incrementSynchronized() {
        count += 1;
    }

}
