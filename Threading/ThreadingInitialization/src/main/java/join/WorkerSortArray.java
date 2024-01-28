package join;

import java.util.Arrays;

// TODO --> Join easier than callback --> Executor + Future easier than join

public class WorkerSortArray {

    public static void main(String[] args) {

        double[] array = new double[10000];
        for (int i = 0; i < array.length; i++) {
            array[i] = Math.random();
        }

        SortingWorker task = new SortingWorker(array);
        Thread sortingWorker = new Thread(task);
        sortingWorker.start();

        // Här skulle man kunna göra andra saker tills dess att worker är klar!

        try {
            Thread.sleep(1_000);


            // Joining the worker thread from main thread -->
            // Main thread awaits the run() of worker to be finished before continuing

            sortingWorker.join();
            System.out.println("Joining with worker thread -> awaiting sorting");

            System.out.println("Min: " + array[0]);
            System.out.println("Median: " + array[array.length / 2]);
            System.out.println("Max: " + array[array.length - 1]);

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

class SortingWorker implements Runnable {

    double[] array;

    public SortingWorker(double[] array) {
        this.array = array;
    }

    @Override
    public void run() {
        System.out.println("Worker is sorting the array");
        Arrays.sort(array);
    }
}
