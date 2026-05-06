import java.util.*;

public class main {

    public static void main(String[] args) {

        Queue<PCB> jobQueue = new LinkedList<>();
        Queue<PCB> readyQueue = new LinkedList<>();

        int[] availableMemory = {2048};
        boolean[] readerDone = {false};

        JobReaderThread reader = new JobReaderThread(jobQueue, "job.txt", readerDone);
        JobLoaderThread loader = new JobLoaderThread(jobQueue, readyQueue, availableMemory, readerDone);

        reader.start();
        loader.start();

        try {
            reader.join();
            loader.join();
        } catch (InterruptedException e) {
            System.out.println("Main interrupted.");
        }

        if (readyQueue.isEmpty()) {
            System.out.println("No processes loaded.");
            return;
        }

        Scanner input = new Scanner(System.in);

        System.out.println("\nChoose Scheduling Algorithm:");
        System.out.println("1. Shortest Job First");
        System.out.println("2. Round Robin");
        System.out.println("3. Priority Scheduling");
        System.out.print("Enter choice: ");

        int choice = input.nextInt();

        Scheduler scheduler = new Scheduler(readyQueue);

        if (choice == 1) {
            scheduler.runSJF();
        } else if (choice == 2) {
            scheduler.runRoundRobin();
        } else if (choice == 3) {
            scheduler.runPriority();
        } else {
            System.out.println("Invalid choice.");
        }

        input.close();
    }
}