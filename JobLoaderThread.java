import java.util.Queue;

public class JobLoaderThread extends Thread {

    private Queue<PCB> jobQueue;
    private Queue<PCB> readyQueue;
    private int[] availableMemory;
    private boolean[] readerDone;

    public JobLoaderThread(Queue<PCB> jobQueue, Queue<PCB> readyQueue, int[] availableMemory, boolean[] readerDone) {
        this.jobQueue = jobQueue;
        this.readyQueue = readyQueue;
        this.availableMemory = availableMemory;
        this.readerDone = readerDone;
    }

    public void run() {
        while (!readerDone[0] || !jobQueue.isEmpty()) {

            synchronized (jobQueue) {
                if (!jobQueue.isEmpty()) {
                    PCB process = jobQueue.peek();

                    if (process.memory <= availableMemory[0]) {
                        jobQueue.remove();

                        availableMemory[0] = availableMemory[0] - process.memory;
                        process.state = "Ready";

                        synchronized (readyQueue) {
                            readyQueue.add(process);
                        }

                        System.out.println("P" + process.processID +
                                " loaded to Ready Queue. Available Memory: " +
                                availableMemory[0]);
                    }
                }
            }

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                System.out.println("Loader interrupted.");
            }
        }
    }
}