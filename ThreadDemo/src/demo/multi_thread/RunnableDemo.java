package demo.multi_thread;

public class RunnableDemo implements Runnable {
    private Thread t;

    private String threadName;

    public RunnableDemo(String name){
        threadName = name;
        System.out.println("Creating " +  threadName);

    }

    public void run() {
        System.out.println("Running " +  threadName);

        try {
            for(int i = 4; i > 0; i--) {
                System.out.println("Thread: " + threadName + ", " + i);

                // De thread ngung trong choc lat.
                Thread.sleep(50);   //Thread scheduler
            }

        } catch (InterruptedException e) {
            System.out.println("Thread " +  threadName + " interrupted.");
        }

        System.out.println("Thread " +  threadName + " exiting.");

    }

    public void start ()
    {
        System.out.println("Starting " +  threadName);

        // kiem tra luong da khoi tao chua
        if (t == null) {
            t = new Thread (this, threadName);
            t.start (); //goi run
        }
    }
}
