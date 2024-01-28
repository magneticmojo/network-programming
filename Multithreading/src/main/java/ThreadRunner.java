/**
 * Demonstrates the functionality of Thread {@link T1} and Runnable {@link T2} classes.
 * This class sequentially starts, manages, and stops instance of T1 and a thread using instance of T2.
 */
public class ThreadRunner {

    /**
     * Entry point for the demonstration.
     * <p>
     * The method does the following (hence the main thread):
     * 1. Creates and starts an instance of the T1 thread class and sleeps for 5 seconds. (While t1 is running)
     * 2. Awakes, creates and starts an instance of the Thread class which is assigned an instance of T2 runnable task
     *    and then sleeps for 5 seconds. (While t1 and t2 are running)
     * 3. Awakes, and suspends the execution of the t2 and sleeps for 5 seconds. (While t1 is running)
     * 4. Awakes, and resumes the execution of the t2 and sleeps for 5 seconds. (While t1 and t2 are running)
     * 5. Awakes, and interrupts t1 then sleeps for 5 seconds. (While t2 is running)
     * 6. Awakes and stops the t2.
     * </p>
     *
     * @param args command-line arguments (not used)
     * @throws InterruptedException if any thread is interrupted while sleeping
     */
    public static void main(String[] args) throws InterruptedException {

        System.out.println("**** Starting ThreadRunner ****" + "\n");
        System.out.println("**** Starting T1 - In Main****");
        System.out.println("(Main thread sleeps)" + "\n");

        //Create and start T1 instance
        T1 t1 = new T1("Tråd 1");
        t1.start();

        // Wait 5 seconds
        Thread.sleep(5000);

        System.out.println("\n" + "Main thread awake");
        System.out.println("**** Starting T2 - In Main ****");

        //Then Create and start T2 instance
        T2 task = new T2("task 2");
        Thread t2 = new Thread(task, "Tråd 2");
        t2.start();

        System.out.println("(Main thread sleeps)" + "\n");
        System.out.println("**** T1 and T2 Running ****" + "\n");

        // Wait 5 seconds --> ThreadRunner thread sleeps
        Thread.sleep(5000);

        System.out.println("\n" + "Main thread awake");
        System.out.println("**** Suspend T2 - In Main ****");

        // Suspend execution of t2
        task.suspendExecution();

        System.out.println("(Main thread sleeps)" + "\n");

        // Wait 5 seconds --> ThreadRunner thread sleeps
        Thread.sleep(5000);

        System.out.println("\n" + "Main thread awake");
        System.out.println("**** Resume T2 - In Main ****");

        // Resume execution of t2
        task.resumeExecution();

        System.out.println("(Main thread sleeps)" + "\n");
        System.out.println("**** T1 and T2 Running ****" + "\n");

        // Wait 5 seconds --> ThreadRunner thread sleeps
        Thread.sleep(5000);

        System.out.println("\n" + "Main thread awake");
        System.out.println("**** Interrupt T1 - In Main ****" + "\n");

        // Stop T1
        t1.interrupt();

        System.out.println("(Main thread sleeps)" + "\n");

        // Wait 5 seconds
        Thread.sleep(5000);

        System.out.println("\n" + "Main thread awake");
        System.out.println("**** Kill T2 - In Main ****");

        // Stop T2
        task.kill();
    }
}
