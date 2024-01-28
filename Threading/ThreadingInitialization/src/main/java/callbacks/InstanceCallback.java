package callbacks;

public class InstanceCallback {

    String result;

    // Method instead of constructor --> Spawning threads in ctr is unpredictable
    public void startWorkerWithThis() {
        System.out.println("Starting up worker thread");
        WorkerWIthCallbackInstance worker = new WorkerWIthCallbackInstance(this);
        Thread workerThread = new Thread(worker);
        workerThread.start();
    }

    public void receiveWorkerResult(String result) {
        this.result = result;
        System.out.println("Print this result coming from worker: " + result);
    }

    public static void main(String[] args) {
        System.out.println("Executing main thread");
        InstanceCallback mainThread = new InstanceCallback();
        mainThread.startWorkerWithThis();
    }
}

class WorkerWIthCallbackInstance implements Runnable{

    InstanceCallback callback;

    public WorkerWIthCallbackInstance(InstanceCallback callback) {
        this.callback = callback;
    }

    @Override
    public void run() {
        System.out.println("Executing run of worker thread");
        callback.receiveWorkerResult("I am da madda fakkin worker!!!");
    }
}
