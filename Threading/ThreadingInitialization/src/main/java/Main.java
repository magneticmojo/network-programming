import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {

        Runnable task = () -> {
            String name = Thread.currentThread().getName();
            System.out.println("Executing thread " + name);
        };

        Thread t1 = new Thread(task);
        t1.start();
        Thread t2 = new Thread(task);
        t2.start();

        Runnable task2 = () -> {
            String name = Thread.currentThread().getName();
            System.out.println("Before " + name);
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("After " + name);
        };

        new Thread(task2).start();
        new Thread(task2).start();


    }
}
