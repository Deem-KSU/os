import java.util.*;

public class Scheduler {

    private Queue<PCB> processes;
    private Queue<GanttEntry> ganttChart;

    public Scheduler(Queue<PCB> processes) {
        this.processes = processes;
        this.ganttChart = new LinkedList<>();
    }

    public void runSJF() {
        int currentTime = 0;
        Queue<PCB> readyQueue = new LinkedList<>(processes);

        while (!readyQueue.isEmpty()) {

            PCB shortest = readyQueue.peek();

            for (PCB p : readyQueue) {
                if (p.burstTime < shortest.burstTime) {
                    shortest = p;
                }
            }

            readyQueue.remove(shortest);

            shortest.startTime = currentTime;
            int startBurst = shortest.remainingTime;

            currentTime = currentTime + shortest.remainingTime;
            shortest.remainingTime = 0;

            shortest.terminationTime = currentTime;
            shortest.turnaroundTime = shortest.terminationTime;
            shortest.waitingTime = shortest.turnaroundTime - shortest.burstTime;

            ganttChart.add(new GanttEntry(
                    shortest.processID,
                    shortest.startTime,
                    shortest.terminationTime,
                    startBurst,
                    shortest.remainingTime
            ));
        }

        printResults(false);
    }

    public void runRoundRobin() {
        int currentTime = 0;
        int quantum = 5;

        Queue<PCB> readyQueue = new LinkedList<>(processes);

        while (!readyQueue.isEmpty()) {

            PCB current = readyQueue.remove();

            if (current.startTime == -1) {
                current.startTime = currentTime;
            }

            int startTime = currentTime;
            int startBurst = current.remainingTime;

            if (current.remainingTime > quantum) {
                currentTime = currentTime + quantum;
                current.remainingTime = current.remainingTime - quantum;

                ganttChart.add(new GanttEntry(
                        current.processID,
                        startTime,
                        currentTime,
                        startBurst,
                        current.remainingTime
                ));

                readyQueue.add(current);

            } else {
                currentTime = currentTime + current.remainingTime;
                current.remainingTime = 0;

                current.terminationTime = currentTime;
                current.turnaroundTime = current.terminationTime;
                current.waitingTime = current.turnaroundTime - current.burstTime;

                ganttChart.add(new GanttEntry(
                        current.processID,
                        startTime,
                        currentTime,
                        startBurst,
                        current.remainingTime
                ));
            }
        }

        printResults(false);
    }

    public void runPriority() {
        int currentTime = 0;
        Queue<PCB> readyQueue = new LinkedList<>(processes);

        while (!readyQueue.isEmpty()) {

            checkStarvationAndAging(readyQueue, currentTime);

            PCB highest = readyQueue.peek();

            for (PCB p : readyQueue) {
                if (p.priority < highest.priority) {
                    highest = p;
                }
            }

            readyQueue.remove(highest);

            highest.startTime = currentTime;
            int startBurst = highest.remainingTime;

            currentTime = currentTime + highest.remainingTime;
            highest.remainingTime = 0;

            highest.terminationTime = currentTime;
            highest.turnaroundTime = highest.terminationTime;
            highest.waitingTime = highest.turnaroundTime - highest.burstTime;

            ganttChart.add(new GanttEntry(
                    highest.processID,
                    highest.startTime,
                    highest.terminationTime,
                    startBurst,
                    highest.remainingTime
            ));
        }

        printResults(true);
    }

    private void checkStarvationAndAging(Queue<PCB> readyQueue, int currentTime) {
        int numberOfProcesses = readyQueue.size();
        int starvationLimit = numberOfProcesses * 5;

        if (currentTime > starvationLimit) {
            for (PCB p : readyQueue) {
                p.starved = true;

                if (p.priority > 1) {
                    p.priority = p.priority - 1;
                }
            }
        }
    }

    private void printResults(boolean showStarvation) {

        System.out.println("\n========== Gantt Chart ==========");
        for (GanttEntry g : ganttChart) {
            g.print();
        }

        System.out.println("\n========== Process Table ==========");
        System.out.println("PID\tBurst\tStart\tEnd\tWaiting\tTurnaround");

        double totalWaiting = 0;
        double totalTurnaround = 0;

        for (PCB p : processes) {
            System.out.println(
                    "P" + p.processID + "\t" +
                            p.burstTime + "\t" +
                            p.startTime + "\t" +
                            p.terminationTime + "\t" +
                            p.waitingTime + "\t" +
                            p.turnaroundTime
            );

            totalWaiting = totalWaiting + p.waitingTime;
            totalTurnaround = totalTurnaround + p.turnaroundTime;
        }

        System.out.println("\nAverage Waiting Time: " + (totalWaiting / processes.size()));
        System.out.println("Average Turnaround Time: " + (totalTurnaround / processes.size()));

        if (showStarvation) {
            System.out.println("\n========== Starved Processes ==========");

            boolean found = false;

            for (PCB p : processes) {
                if (p.starved) {
                    System.out.println("P" + p.processID + " suffered from starvation.");
                    found = true;
                }
            }

            if (!found) {
                System.out.println("No process suffered from starvation.");
            }
        }
    }
}