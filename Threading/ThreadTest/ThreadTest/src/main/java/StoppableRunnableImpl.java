public class StoppableRunnableImpl {

    public static class StoppableRunnable implements Runnable {

        private boolean stopped = false;

        public synchronized void stop() {
            this.stopped = true;
        }

        public synchronized boolean isStoppedRequested() {
            return this.stopped;
        }

        private void sleep(long millis) {
            try {
                Thread.sleep(millis);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            System.out.println("StoppableRunnable running");
            while (!stopped) {
                sleep(1000);
                System.out.println("...");
            }
            System.out.println("StoppableRunnable stopped");
        }
    }

    public static void main(String[] args) {
        Thread mainThread = Thread.currentThread();
        System.out.println(mainThread.getPriority());
        System.out.println(mainThread.getThreadGroup());

        StoppableRunnable stoppableRunnable = new StoppableRunnable();
        Thread t1 = new Thread(stoppableRunnable, "Thread");
        System.out.println(t1.getPriority());
        System.out.println(t1.getThreadGroup());
        t1.start();


        // Sleep main thread --> Allow t1 to execute
        sleepMainThread(5000);

        stoppableRunnable.stop();
    }

    private static void sleepMainThread(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
