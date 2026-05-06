public class GanttEntry {
    int processID;
    int startTime;
    int endTime;
    int startBurst;
    int stopBurst;

    public GanttEntry(int processID, int startTime, int endTime, int startBurst, int stopBurst) {
        this.processID = processID;
        this.startTime = startTime;
        this.endTime = endTime;
        this.startBurst = startBurst;
        this.stopBurst = stopBurst;
    }

    public void print() {
        System.out.println("P" + processID +
                " | Time: " + startTime + " -> " + endTime +
                " | Burst: " + startBurst + " -> " + stopBurst);
    }
}