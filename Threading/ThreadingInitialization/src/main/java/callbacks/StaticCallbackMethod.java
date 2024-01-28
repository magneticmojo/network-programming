package callbacks;

public class StaticCallbackMethod {

    public static void callerCallback(String secondPart) {
        System.out.println("Callback method reached!");
        System.out.println(secondPart + "ThirdPart");
    }

    public static void main(String[] args) {

        System.out.println("In main thread -> creating worker thread");
        Thread worker = new Thread(new CalleeCallback("firstPart"));
        worker.start();


    }
}

class CalleeCallback implements Runnable {

    String firstPart;

    public CalleeCallback(String firstPart) {
        this.firstPart = firstPart;
    }


    @Override
    public void run() {

        try {
            System.out.println("In worker thread -> Going to sleep");
            Thread.sleep(2_000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Awake -> Doing operations -> Calling callback method of creator");
        String concat = firstPart + "secondPart";
        StaticCallbackMethod.callerCallback(concat);
    }
}


