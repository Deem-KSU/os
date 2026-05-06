import java.io.File;
import java.io.FileNotFoundException;
import java.util.Queue;
import java.util.Scanner;

public class JobReaderThread extends Thread {

    private Queue<PCB> jobQueue;
    private String fileName;
    private boolean[] readerDone;

    public JobReaderThread(Queue<PCB> jobQueue, String fileName, boolean[] readerDone) {
        this.jobQueue = jobQueue;
        this.fileName = fileName;
        this.readerDone = readerDone;
    }

    public void run() {
        try {
            File file = new File(fileName);
            Scanner input = new Scanner(file);

            while (input.hasNextLine()) {
                String line = input.nextLine().trim();

                if (!line.equals("")) {
                    String[] mainParts = line.split(";");
                    String[] processParts = mainParts[0].split(":");

                    int processID = Integer.parseInt(processParts[0]);
                    int burstTime = Integer.parseInt(processParts[1]);
                    int priority = Integer.parseInt(processParts[2]);
                    int memory = Integer.parseInt(mainParts[1]);

                    PCB process = new PCB(processID, burstTime, priority, memory);
                    process.state = "Job Queue";

                    synchronized (jobQueue) {
                        jobQueue.add(process);
                    }

                    System.out.println("P" + processID + " added to Job Queue.");
                }
            }

            input.close();

        } catch (FileNotFoundException e) {
            System.out.println("Error: job.txt file not found.");
        } catch (Exception e) {
            System.out.println("Error while reading job file.");
        }

        readerDone[0] = true;
    }
}