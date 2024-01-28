public class ThreadSyntax {


    public static void main(String[] args) {

        Runnable runnable = () -> {
            String threadName = Thread.currentThread().getName();
            System.out.println("Thread from Runnable lambda is running with name " + threadName);
        };

        Thread t1 = new Thread(runnable, "thread one");
        t1.start();

        Runnable runnable2 = new Runnable() {
            @Override
            public void run() {
                System.out.println("Thread created by anonymous class");
            }
        };

        Thread t2 = new Thread(runnable2);
        t2.start();

        printFinalAndEffectivelyFinalLocalVariable();

    }

    public static void printFinalAndEffectivelyFinalLocalVariable() {
        final int finalLocalVar = 1;
        int effectivelyFinalLocalVar = 2;

        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println(finalLocalVar);
                System.out.println(effectivelyFinalLocalVar);
            }
        });
        t1.start();

        Runnable runnable;
        Thread t2 = new Thread(runnable = () -> {
            System.out.println("Very compact thread syntax");
        });
        t2.start();
    }
}
