public class PCB {
    int processID;
    String state;
    int burstTime;
    int remainingTime;
    int priority;
    int memory;

    int startTime;
    int terminationTime;
    int waitingTime;
    int turnaroundTime;

    boolean starved;

    public PCB(int processID, int burstTime, int priority, int memory) {
        this.processID = processID;
        this.burstTime = burstTime;
        this.remainingTime = burstTime;
        this.priority = priority;
        this.memory = memory;

        this.state = "New";
        this.startTime = -1;
        this.terminationTime = -1;
        this.waitingTime = 0;
        this.turnaroundTime = 0;

        this.starved = false;
    }
}